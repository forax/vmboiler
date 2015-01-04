package com.github.forax.vmboiler.sample.script;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.HashMap;

import com.github.forax.vmboiler.rt.OptimisticError;

public class RT {
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType, Linker linker) {
    //System.out.println("link " + name + methodType);
    return linker.getCallSite(name, methodType);
  }
  
  @SuppressWarnings("unused") // used by a method handle
  private static boolean deopt_args(Linker linker, String nameAndType, Binding[] bindings, Object[] values) {
    //System.out.println("deopt args " + Arrays.toString(values) + " " + Arrays.toString(bindings));
    
    boolean invalidation = false;
    for(int i = 0; i < bindings.length; i++) {
      Binding binding = bindings[i];
      if (binding == null) {
        continue;
      }
      Type type = binding.type();
      Type mergedType = Type.merge(type, Type.getTypeFromValue(values[i]));
      if (type != mergedType) {
        binding.type(mergedType);
        invalidation = true;
      }
    }
    if (invalidation) {
      linker.invalidate(nameAndType);
    }
    return false;
  }
  
  @SuppressWarnings("unused") // used by a method handle
  private static boolean deopt_return(Linker linker, String nameAndType, Binding binding, Object value) {
    System.out.println("deopt return " + value + " " + binding);
    if (binding != null) {
      binding.type(Type.merge(Type.getTypeFromValue(value), binding.type()));
      linker.invalidate(nameAndType);
    }
    return false;
  }
  
  @SuppressWarnings("unused") // used by a method handle
  private static void throwOptimisticError(Object value) {
    throw OptimisticError.newOptimisticError(value);
  }
  
  private static final MethodHandle THROW_OPTIMISTIC_ERROR, IS_INSTANCE;
  static final MethodHandle DEOPT_ARGS, DEOPT_RETURN;
  static {
    Lookup lookup = MethodHandles.lookup();
    try {
      THROW_OPTIMISTIC_ERROR = lookup.findStatic(RT.class, "throwOptimisticError",
          MethodType.methodType(void.class, Object.class));
      IS_INSTANCE = MethodHandles.publicLookup().findVirtual(Class.class, "isInstance",
          MethodType.methodType(boolean.class, Object.class));
      DEOPT_ARGS = lookup.findStatic(RT.class, "deopt_args",
          MethodType.methodType(boolean.class, Linker.class, String.class, Binding[].class, Object[].class));
      DEOPT_RETURN = lookup.findStatic(RT.class, "deopt_return",
          MethodType.methodType(boolean.class, Linker.class, String.class, Binding.class, Object.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
  
  private static MethodHandle checkTypeAndConvert(Class<?> returnType) {
    return MethodHandles.guardWithTest(IS_INSTANCE.bindTo(boxed(returnType)),
        MethodHandles.identity(Object.class).asType(MethodType.methodType(returnType, Object.class)),
        THROW_OPTIMISTIC_ERROR.asType(MethodType.methodType(returnType, Object.class)));
  }
  
  private static Class<?> boxed(Class<?> type) {
    if (!type.isPrimitive()) {
      return type;
    }
    switch(type.getName()) {
    case "int":
      return Integer.class;
    case "double":
      return Double.class;
    default:
      throw new AssertionError("unknown type " + type);
    }
  }
  
  public static CallSite bsm_convert(Lookup lookup, String name, MethodType methodType) {
    //System.out.println("convert " + methodType);
    
    MethodHandle target;
    Class<?> returnType = methodType.returnType();
    Class<?> parameterType = methodType.parameterType(0);
    if (returnType == parameterType || returnType == Object.class || returnType == void.class) {
      target = MethodHandles.identity(parameterType).asType(methodType);
    } else {
      target = checkTypeAndConvert(returnType).asType(methodType);
    }
    return new ConstantCallSite(target);
  }
  
  static class Ops {
    private static BigInteger toBig(Object o) {
      if (o instanceof Integer) {
        return BigInteger.valueOf((Integer)o);
      }
      return (BigInteger)o;
    }
    
    public static int add(int a, int b) {
      //return a + b;
      try {
        return Math.addExact(a, b);
      } catch(ArithmeticException e) {
        throw OptimisticError.newOptimisticError(BigInteger.valueOf(a).add(BigInteger.valueOf(b)));
      }
      //throw OptimisticError.newOptimisticError(BigInteger.valueOf(a).add(BigInteger.valueOf(b)));
    }
    public static Object add(Object a, Object b) {
      return toBig(a).add(toBig(b));
    }
    
    public static int sub(int a, int b) {
      //return a - b;
      try {
        return Math.subtractExact(a, b);
      } catch(ArithmeticException e) {
        throw OptimisticError.newOptimisticError(BigInteger.valueOf(a).subtract(BigInteger.valueOf(b)));
      }
    }
    public static Object sub(Object a, Object b) {
      return toBig(a).subtract(toBig(b));
    }
    
    public static int mul(int a, int b) {
      try {
        return Math.multiplyExact(a, b);
      } catch(ArithmeticException e) {
        throw OptimisticError.newOptimisticError(BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)));
      }
    }
    public static Object mul(Object a, Object b) {
      return toBig(a).multiply(toBig(b));
    }
    
    public static int div(int a, int b) {
      return a / b;
    }
    public static Object div(Object a, Object b) {
      return toBig(a).divide(toBig(b));
    }
    
    public static boolean lt(int a, int b) {
      return a < b;
    }
    public static boolean lt(Object a, Object b) {
      return toBig(a).compareTo(toBig(b)) < 0;
    }
    public static boolean le(int a, int b) {
      return a <= b;
    }
    public static boolean le(Object a, Object b) {
      return toBig(a).compareTo(toBig(b)) <= 0;
    }
    
    public static boolean gt(int a, int b) {
      return a > b;
    }
    public static boolean gt(Object a, Object b) {
      return toBig(a).compareTo(toBig(b)) > 0;
    }
    public static boolean ge(int a, int b) {
      return a >= b;
    }
    public static boolean ge(Object a, Object b) {
      return toBig(a).compareTo(toBig(b)) >= 0;
    }
    
    public static boolean eq(int a, int b) {
      return a == b;
    }
    public static boolean eq(Object a, Object b) {
      return toBig(a).equals(toBig(b));
    }
    public static boolean ne(int a, int b) {
      return a != b;
    }
    public static boolean ne(Object a, Object b) {
      return !toBig(a).equals(toBig(b));
    }
    
    static final HashMap<String, MethodHandle[]> OP_MAP;
    static {
      Lookup lookup = MethodHandles.lookup();
      HashMap<String, MethodHandle[]> opMap = new HashMap<>();
      for(Method method: Ops.class.getMethods()) {
        if (method.getParameterCount() != 2) {
          continue;
        }
        int index = (method.getParameterTypes()[0] == int.class)? 0: 1;
        MethodHandle target;
        try {
          target = lookup.unreflect(method);
        } catch (IllegalAccessException e) {
          throw new AssertionError(e);
        }
        opMap.computeIfAbsent(method.getName(), name -> new MethodHandle[2])[index] = target;
      }
      OP_MAP = opMap;
    }
  }
  
  public static CallSite bsm_op(Lookup lookup, String name, MethodType methodType, Linker linker) {
    //System.out.println("link op " + name + methodType);
    
    MethodHandle[] mhs = Ops.OP_MAP.get(name);
    if (mhs == null) {
      throw new UnsupportedOperationException(name + methodType);
    }
    MethodHandle target;
    if (mhs[0].type() == methodType) {
      target = mhs[0];
    } else {
      target = mhs[1];
      if (target.type().returnType() != methodType.returnType()) {
        target = MethodHandles.filterReturnValue(target, checkTypeAndConvert(methodType.returnType()));
      }
      target = target.asType(methodType);
    }
    return new ConstantCallSite(target);
  }
}
