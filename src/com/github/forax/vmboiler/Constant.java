package com.github.forax.vmboiler;

import static com.github.forax.vmboiler.Type.VM_BYTE;
import static com.github.forax.vmboiler.Type.VM_CHAR;
import static com.github.forax.vmboiler.Type.VM_DOUBLE;
import static com.github.forax.vmboiler.Type.VM_FLOAT;
import static com.github.forax.vmboiler.Type.VM_INT;
import static com.github.forax.vmboiler.Type.VM_LONG;
import static com.github.forax.vmboiler.Type.VM_SHORT;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.DCONST_1;
import static org.objectweb.asm.Opcodes.FCONST_0;
import static org.objectweb.asm.Opcodes.FCONST_1;
import static org.objectweb.asm.Opcodes.FCONST_2;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Opcodes.SIPUSH;

import org.objectweb.asm.MethodVisitor;

/**
 * A side effect free constant.
 */
public final class Constant extends Value {
  private final Object constant;  // may be null

  /**
   * Creates a constant with a type and a value.
   * The value must be a type representable as a constant of
   * the constant pool.
   * @param type the type of the constant.
   * @param constant the value of the constant.
   */
  public Constant(Type type, Object constant) {
    super(type);
    if (type.isMixed()) {
      throw new IllegalArgumentException("constant can not have mixed type");
    }
    this.constant = constant;
  }

  /**
   * Returns the value of the constant.
   * @return the value of the constant.
   */
  public Object constant() {
    return constant;
  }
  
  @Override
  public String toString() {
    return "constant(" + type() + " " + constant + ')';
  }
  
  @Override
  void loadPrimitive(MethodVisitor mv) {
    load(mv, type(), constant);
  }
  @Override
  void loadAll(MethodVisitor mv) {
    load(mv, type(), constant);
  }
  
  private static void load(MethodVisitor mv, Type type, Object constant) {
    switch(type.vmType()) {
    case VM_BYTE:
    case VM_CHAR:
    case VM_SHORT:
    case VM_INT:
      loadInt(mv, constant);
      return;
    case VM_LONG:
      loadLong(mv, constant);
      return;
    case VM_FLOAT:
      loadFloat(mv, constant);
      return;
    case VM_DOUBLE:
      loadDouble(mv, constant);
      return;
    default:  // string or null !
    }
    if (constant == null) {
      mv.visitInsn(ACONST_NULL);
      return;
    }
    mv.visitLdcInsn(constant);
  }

  private static void loadInt(MethodVisitor mv, Object constant) {
    int value = (Integer)constant;
    switch(value) {
    case -1:
    case 0:
    case 1:
    case 2:
    case 3:
    case 4:
    case 5:
      mv.visitInsn(ICONST_0 + value);
      return;
    default:
    }
    if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
      mv.visitIntInsn(BIPUSH, value);
      return;
    }
    if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
      mv.visitIntInsn(SIPUSH, value);
      return;
    }
    mv.visitLdcInsn(constant);
  }
  
  private static void loadLong(MethodVisitor mv, Object constant) {
    long value = (Long)constant;
    if (value == 0 || value == 1) {
      mv.visitInsn(LCONST_0 + (int)value);
      return;
    }
    mv.visitLdcInsn(constant);
  }
  
  private static void loadFloat(MethodVisitor mv, Object constant) {
    float value = (Float)constant;
    if (value == 0.0f) {
      mv.visitInsn(FCONST_0);
      return;
    }
    if (value == 1.0f) {
      mv.visitInsn(FCONST_1);
      return;
    }
    if (value == 2.0f) {
      mv.visitInsn(FCONST_2);
      return;
    }
    mv.visitLdcInsn(constant);
  }
  
  private static void loadDouble(MethodVisitor mv, Object constant) {
    double value = (Double)constant;
    if (value == 0.0) {
      mv.visitInsn(DCONST_0);
      return;
    }
    if (value == 1.0) {
      mv.visitInsn(DCONST_1);
      return;
    }
    mv.visitLdcInsn(constant);
  }
}