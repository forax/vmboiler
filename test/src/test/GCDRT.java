package test;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

@SuppressWarnings("unused")
public class GCDRT {
  private static int sub(int a, int b) {
    return a - b;
  }
  private static boolean ne(int a, int b) {
    return a != b;
  }
  private static boolean gt(int a, int b) {
    return a > b;
  }
  
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType) throws Throwable {
    System.out.println("GCDRT.bsm " + lookup + " " + name + methodType);
    MethodHandle target = MethodHandles.lookup().findStatic(GCDRT.class, name, methodType);
    return new ConstantCallSite(target);
  }
  
  public static boolean deopt_args(Lookup lookup, String name, MethodType methodType, Object[] values) throws Throwable {
    System.out.println("deopt args " + Arrays.toString(values));
    return false;
  }
  
  public static boolean deopt_ret(Lookup lookup, String name, MethodType methodType, Object value) throws Throwable {
    System.out.println("deopt return " + value);
    return false;
  }
}
