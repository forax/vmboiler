package com.github.forax.vmboiler;

/**
 * Type of the {@link Value values} used by {@link CodeGen}.
 */
public interface Type {
  /**
   * Returns true is the current type both an object and a primitive type
   * @return true is the current type represent either a primitive or an object.
   */
  boolean isMixed();
  
  /**
   * Returns the descriptor of the type using the class file format.
   * If the type is mixed, the VM type is the type of the primitive part of the
   * mixed type.
   * It can be a {@link #VM_VOID}, {@link #VM_BOOLEAN}, a {@link #VM_BYTE},
   * a {@link #VM_CHAR}, a {@link #VM_SHORT}, a {@link #VM_INT},
   * a {@link #VM_LONG}, a {@link #VM_FLOAT}, a {@link #VM_DOUBLE},
   * a {@link #VM_OBJECT} or a user defined type if it's an object type.
   * @return the descriptor of the type using the class file format.
   */
  String vmType();
  
  /** The descriptor of void. */
  public static final String VM_VOID     = "V";
  /** The descriptor of a boolean. */
  public static final String VM_BOOLEAN  = "Z";
  /** The descriptor of a byte. */
  public static final String VM_BYTE     = "B";
  /** The descriptor of a char. */
  public static final String VM_CHAR     = "C";
  /** The descriptor of a short. */
  public static final String VM_SHORT    = "S";
  /** The descriptor of an integer. */
  public static final String VM_INT      = "I";
  /** The descriptor of a long. */
  public static final String VM_LONG     = "J";
  /** The descriptor of a float. */
  public static final String VM_FLOAT    = "F";
  /** The descriptor of a double. */
  public static final String VM_DOUBLE   = "D";
  /** The descriptor of an object. */
  public static final String VM_OBJECT   = "Ljava/lang/Object;";
}