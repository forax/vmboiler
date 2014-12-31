package com.github.forax.vmboiler;

/**
 * Type of the {@link Value values} used by {@link CodeGen}.
 */
public interface Type {
  /**
   * Returns true is the current type represent either a primitive or an object.
   * @return true is the current type represent either a primitive or an object.
   */
  boolean isMixed();
  
  /**
   * Returns the Java VM view of a type i.e. its descriptor for the class file format.
   * @return the Java VM view of a type i.e. its descriptor for the class file format.
   */
  String vmType();
  
  public static final String VM_BOOLEAN  = "Z";
  public static final String VM_BYTE     = "B";
  public static final String VM_CHAR     = "C";
  public static final String VM_SHORT    = "S";
  public static final String VM_INT      = "I";
  public static final String VM_LONG     = "J";
  public static final String VM_FLOAT    = "F";
  public static final String VM_DOUBLE   = "D";
  public static final String VM_OBJECT   = "Ljava/lang/Object;";
}