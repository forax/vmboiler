package com.github.forax.vmboiler;

import static com.github.forax.vmboiler.Type.VM_DOUBLE;
import static com.github.forax.vmboiler.Type.VM_LONG;
import static com.github.forax.vmboiler.Type.VM_VOID;
import static org.objectweb.asm.Opcodes.*;

import java.util.Objects;

import org.objectweb.asm.MethodVisitor;

/**
 * Base class that represents either {@link Var a virtual register} or
 * a {@link Constant a side effect free constant}.
 * 
 * Values are used by the {@link CodeGen} operations.
 */
public abstract class Value {
  private final Type type;

  Value(Type type) {
    this.type = Objects.requireNonNull(type);
  }
  
  /**
   * Returns the type of the current value.
   * @return the type of the current value.
   */
  public Type type() {
    return type;
  }
  
  abstract void loadAll(MethodVisitor mv);

  abstract void loadPrimitive(MethodVisitor mv);
  
  static int loadOpcode(String vmType) {
    return LOAD_OPS[vmType.charAt(0) - 'B'];
  }
  static int storeOpcode(String vmType) {
    return STORE_OPS[vmType.charAt(0) - 'B'];
  }
  static int returnOpcode(String vmType) {
    return RETURN_OPS[vmType.charAt(0) - 'B'];
  }
  static int size(String vmType) {
    return (vmType == VM_LONG || vmType == VM_DOUBLE)?2: (vmType == VM_VOID)? 0: 1;
  }
  
  private static final int[] LOAD_OPS =   { ILOAD  , ILOAD  , DLOAD  , 0, FLOAD  , 0, 0, ILOAD  , LLOAD  , 0, ALOAD  , 0, 0, 0, 0, 0, 0, ILOAD  , 0, 0, 0     , 0, 0, 0, ILOAD };
  private static final int[] STORE_OPS =  { ISTORE , ISTORE , DSTORE , 0, FSTORE , 0, 0, ISTORE , LSTORE , 0, ASTORE , 0, 0, 0, 0, 0, 0, ISTORE , 0, 0, 0     , 0, 0, 0, ISTORE };
  private static final int[] RETURN_OPS = { IRETURN, IRETURN, DRETURN, 0, FRETURN, 0, 0, IRETURN, LRETURN, 0, ARETURN, 0, 0, 0, 0, 0, 0, IRETURN, 0, 0, RETURN, 0, 0, 0, IRETURN };
}