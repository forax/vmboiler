package test;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.math.BigInteger;
import java.util.Arrays;

import com.github.forax.vmboiler.rt.OptimisticError;

public class ExampleRT {
  @SuppressWarnings("unused")
  private static int add(int a, int b) {
    try {
      return Math.addExact(a, b);
    } catch(ArithmeticException e) {
      throw OptimisticError.newOptimisticError(add((Object)a, b));
    }
  }
  @SuppressWarnings("unused")
  private static Object add(Object a, Object b) {
    return asBigInt(a).add(asBigInt(b));
  }
  private static BigInteger asBigInt(Object o) {
    if (o instanceof Integer) {
      return BigInteger.valueOf((Integer)o);
    }
    return (BigInteger)o;
  }
  
  private static int convert(Object result) {
    if (result instanceof BigInteger) {
      throw OptimisticError.newOptimisticError(result);
    }
    return (Integer)result;
  }
  
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType) throws Throwable {
    System.out.println("Example.bsm " + lookup + " " + name + methodType);
    boolean exactMatch = 
        methodType.returnType() == int.class &&
        methodType.parameterType(0) == int.class &&
        methodType.parameterType(1) == int.class;
    MethodType lookupType;
    MethodHandle target = MethodHandles.lookup()
        .findStatic(ExampleRT.class, name, exactMatch?methodType: methodType.generic());
    if (!exactMatch) {
      target = MethodHandles.filterReturnValue(target, CONVERT).asType(methodType);
    }
    return new ConstantCallSite(target);
  }
  
  public static boolean deopt_args(Object[] values, String parameterNames) throws Throwable {
    System.out.println("deopt args " + parameterNames + " " + Arrays.toString(values));
    return false;
  }
  
  public static boolean deopt_ret(Object value, String parameterNames) throws Throwable {
    System.out.println("deopt return " + value);
    return false;
  }
  
  private static final MethodHandle CONVERT;
  static {
    try {
      CONVERT = MethodHandles.lookup().findStatic(ExampleRT.class, "convert", MethodType.methodType(int.class, Object.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
}
