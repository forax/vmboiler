package com.github.forax.vmboiler.rt;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.lang.invoke.WrongMethodTypeException;

/**
 * The runtime support that handle the dynamic part of the deoptimization.
 * This call should never be referenced directly.
 */
public final class RT {
  /**
   * ootstrap method called called in a deoptimisation path when at least one argument
   * doesn't fit in its parameter type.
   * 
   * @param lookup lookup object
   * @param name name of the virtual method
   * @param methodType descriptor of the virtual method
   * @param array parameters that begins with the 3 following objects
   *        a bootstrap method as method handle,
   *        the deopt callback for arguments as a method handle,
   *        an array encoding if arguments are mixed ('M') or not ('.')
   *        and ends with the boostrap arguments. 
   * @return a callsite
   * @throws Throwable if an exception occurs
   */
  //called by generated code
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType, Object... array) throws Throwable {
    MethodHandle bsm = (MethodHandle)array[0];
    MethodHandle deoptArgCallback = (MethodHandle)array[1];     // boolean mh(Object[] values)
    String mixed = (String)array[2];
    
    //System.out.println("bsm " + name + " with " + bsm + " " + deoptArgCallback  + " " + mixed);
    
    // do some checks
    if (!deoptArgCallback.type().equals(MethodType.methodType(boolean.class, Object[].class))) {
      throw new WrongMethodTypeException("invalid deop callback signature ! " + deoptArgCallback.type());
    }
    
    // call the bsm
    array[0] = lookup;
    array[1] = name;
    array[2] = decodeMixedMethodType(mixed, methodType);
    CallSite callSite = (CallSite)bsm.invokeWithArguments(array);
    
    MethodHandle target = callSite.dynamicInvoker();
    target = target.asSpreader(Object[].class, mixed.length());
    target = deoptCallback(target, deoptArgCallback);
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
   * @param deoptReturnCallback the callback to cause to indicate a deopt error.
   * @return a call site
   * @throws Throwable if an error occurs
   */
  // called by generated code
  public static CallSite bsm_optimistic_failure(Lookup lookup, String name, MethodType methodType, MethodHandle deoptReturnCallback) throws Throwable {
    //System.out.println("bsm_optimistic_failure " +  name + " with " + deoptReturnCallback);
    
    // do some checks
    if (!deoptReturnCallback.type().equals(MethodType.methodType(boolean.class, Object.class))) {
      throw new WrongMethodTypeException("invalid deop callback signature ! " + deoptReturnCallback.type());
    }
    
    return new ConstantCallSite(
        MethodHandles.filterReturnValue(OPTIMISTIC_ERROR_VALUE,
            deoptCallback(MethodHandles.identity(Object.class), deoptReturnCallback)));
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
