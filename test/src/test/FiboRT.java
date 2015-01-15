package test;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.Arrays;

import com.github.forax.vmboiler.rt.OptimisticError;

@SuppressWarnings("unused")
public class FiboRT {
  private static int add(int a, int b) {
    throw OptimisticError.newOptimisticError(a + b);
    //return a + b;
  }
  private static int add(Object a, Object b) {
    throw OptimisticError.newOptimisticError(((Integer)a) + (Integer)b);
  }
  private static int sub(int a, int b) {
    return a - b;
  }
  private static boolean lt(int a, int b) {
    return a < b;
  }
  
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType) throws Throwable {
    //System.out.println("FiboRT.bsm " + lookup + " " + name + methodType);
    Lookup l = (name.equals("fibo"))? lookup: MethodHandles.lookup();
    MethodHandle target = l.findStatic(l.lookupClass(), name, methodType);
    return new ConstantCallSite(target);
  }
  
  public static boolean deopt_args(Object[] values) throws Throwable {
    System.out.println("deopt args " + Arrays.toString(values));
    return false;
  }
  
  public static boolean deopt_return(Object value) throws Throwable {
    System.out.println("deopt return " + value);
    return false;
  }
}
