package com.github.forax.vmboiler.rt;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.Arrays;

/**
 * The runtime support that handle the dynamic part of the deoptimization.
 * This call should never be referenced directly.
 */
public final class RT {
  /**
   * Bootstrap method called called in a deoptimisation path when at least one argument
   * doesn't fit in its parameter type.
   * 
   * @param lookup lookup object
   * @param name name of the virtual method
   * @param methodType descriptor of the virtual method
   * @param array parameters that begins with the 4 objects followed by two arrays
   *    <pre>
   *        a string encoding if an argument is mixed ('M') or not ('.')
   *        a bootstrap method as method handle with signature (Lookup,String,MethodType, ...)CallSite,
   *        the number of constant arguments of the bootstrap method
   *        a deopt callback as a method handle with signature (Object[], ...)boolean
   *        the bootstrap constant arguments of the bsm as an array of objects flattened
   *        the deopt callback constant arguments as an array of objects flattened
   *    </pre>
   * @return a callsite
   * @throws Throwable if an exception occurs
   */
  //called by generated code
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType, Object... array) throws Throwable {
    // decode all arguments
    String mixed = (String)array[0];
    MethodHandle bsm = (MethodHandle)array[1];
    int bsmConstantCount = (int)array[2];
    MethodHandle deoptArgs = (MethodHandle)array[3];     // boolean mh(Lookup, Object[], ...)
    
    //System.out.println("bootstrap: " + name + '(' + mixed + ") with " + bsm + "/" + bsmConstantCount + " " + deoptArgs);
    
    // call the bsm
    Object[] arguments = new Object[3 + bsmConstantCount];
    arguments[0] = lookup;
    arguments[1] = name;
    arguments[2] = decodeMixedMethodType(mixed, methodType);
    if (bsmConstantCount != 0) {
      System.arraycopy(array, 4, arguments, 3, bsmConstantCount);
    }
    CallSite callSite = (CallSite)bsm.invokeWithArguments(arguments);
    
    // prepend lookup/name/methodType
    deoptArgs = MethodHandles.insertArguments(deoptArgs, 0, lookup, name, methodType);
    
    // bundle deoptArgs constant arguments with deoptArgs if necessary
    if (array.length != 4 + bsmConstantCount) {
      Object[] deoptArgsCsts = Arrays.copyOfRange(array, 4  + bsmConstantCount, array.length);
      
      //FIXME check that deoptArgs as the right number of arguments
      //FIXME should we support varargs ?
      
      //System.out.println("deopt cst args " + deoptArgs + " " + Arrays.toString(deoptArgsCsts));
      
      deoptArgs = MethodHandles.insertArguments(deoptArgs, 1, deoptArgsCsts);
    }
    
    MethodHandle target = callSite.dynamicInvoker();
    target = target.asSpreader(Object[].class, mixed.length());
    target = deoptCallback(target, deoptArgs);
    target = MethodHandles.filterReturnValue(DECODE_MIXED_VALUES.bindTo(mixed), target);
    target = target.asCollector(Object[].class, methodType.parameterCount());
    target = target.asType(methodType);
    
    return new ConstantCallSite(target);
  }
  
  /**
   * Bootstrap method called called in a deoptimisation path when a return value
   * doesn't fit in the return type.
   *  
   * @param lookup the lookup object.
   * @param name the name of the method
   * @param methodType always (Object)OptimisiticError
   * @param deoptRet the callback to cause to indicate a deopt error.
   * @param deoptRetCsts the constant arguments of deoptRet
   * @return a call site
   * @throws Throwable if an error occurs
   */
  // called by generated code
  public static CallSite bsm_optimistic_failure(Lookup lookup, String name, MethodType methodType, MethodHandle deoptRet, Object... deoptRetCsts) throws Throwable {
    //System.out.println("bsm_optimistic_failure " +  name + " with " + deoptRet + "/" + deoptRetCsts.length);
    
    // do some checks
    //if (!deoptRet.type().equals(MethodType.methodType(boolean.class, Object.class))) {
    //  throw new WrongMethodTypeException("invalid deop callback signature ! " + deoptRet.type());
    //}
    
    // prepend lookup/name/methodType
    deoptRet = MethodHandles.insertArguments(deoptRet, 0, lookup, name, methodType);
    
    // bundle deoptRet constant arguments with deoptRet if necessary
    if (deoptRetCsts.length != 0) {
      deoptRet = MethodHandles.insertArguments(deoptRet, 1, deoptRetCsts);
    }
    
    return new ConstantCallSite(
        MethodHandles.filterReturnValue(OPTIMISTIC_ERROR_VALUE,
            deoptCallback(MethodHandles.identity(Object.class), deoptRet)));
  }
  
  
  private static MethodHandle deoptCallback(MethodHandle target, MethodHandle deoptCallback) {
    MutableCallSite callSite = new MutableCallSite(target);
    Class<?> parameterType = target.type().parameterType(0);
    MethodHandle unit = MethodHandles.identity(parameterType).asType(MethodType.methodType(void.class, parameterType));
    MethodHandle fallback = MethodHandles.dropArguments(SET_TARGET.bindTo(callSite).bindTo(target), 0, parameterType);
    callSite.setTarget(MethodHandles.foldArguments(target, MethodHandles.guardWithTest(deoptCallback, unit, fallback)));
    return callSite.dynamicInvoker();
  }

  private static final MethodHandle SET_TARGET;
  static {
    try {
      SET_TARGET = MethodHandles.publicLookup().findVirtual(MutableCallSite.class, "setTarget",
          MethodType.methodType(void.class, MethodHandle.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
 
  
  private static MethodType decodeMixedMethodType(String mixed, MethodType methodType) {
    int length = mixed.length();
    Class<?>[] parameterTypes = new Class<?>[length];
    int a = 0;
    for(int i = 0; i < length; i++) {
      if (mixed.charAt(i) == 'M') {
        parameterTypes[i] = Object.class;
        a += 2;
      } else {
        parameterTypes[i] = methodType.parameterType(a++);
      }
    }
    return MethodType.methodType(methodType.returnType(), parameterTypes);
  }
  
  @SuppressWarnings("unused")  // called by a method handle
  private static Object[] decodeMixedValues(String mixed, Object[] args) {
    int length = mixed.length();
    Object[] newArgs = new Object[length];
    int a = 0;
    for(int i = 0; i < length; i++) {
      if (mixed.charAt(i) == 'M') {
        newArgs[i] = unmix(args[a], args[a + 1]);
        a += 2;
      } else {
        newArgs[i] = args[a++];
      }
    }
    return newArgs;
  }
  
  private static Object unmix(Object prim, Object ref) {
    return (ref == NONE)? prim: ref;
  }
  
  /**
   * Well know constant indicating if a mixed type store its value
   * in its primitive slot or in its object slot.
   */
  // used by generated code
  public static final Object NONE = new Object();
  
  private static final MethodHandle DECODE_MIXED_VALUES, OPTIMISTIC_ERROR_VALUE;
  static {
    Lookup lookup = MethodHandles.lookup();
    try {
      DECODE_MIXED_VALUES = lookup.findStatic(RT.class, "decodeMixedValues",
          MethodType.methodType(Object[].class, String.class, Object[].class));
      OPTIMISTIC_ERROR_VALUE = lookup.findVirtual(OptimisticError.class, "value",
          MethodType.methodType(Object.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
}
