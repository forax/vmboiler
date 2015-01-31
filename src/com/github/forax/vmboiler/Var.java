package com.github.forax.vmboiler;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;

import org.objectweb.asm.MethodVisitor;

/**
 * A variable with an optional name and a type.
 * A variable has also a slot, i.e. an index into the local variable table
 * but this slot is intended to be used by {@link CodeGen} only. 
 * 
 * This class can be inherited.
 * 
 * @see CodeGen#createVar(Type, java.util.function.Function)
 */
public class Var extends Value {
  private /*almost final*/ int slot;

  /**
   * Marker value to indicate that the variable is stack allocated.
   */
  public static final int STACK_ALLOCATED = -1;
  
  /**
   * Initialize a variable
   * @param type the type of the variable.
   */
  protected Var(Type type) {
    super(type);
    this.slot = STACK_ALLOCATED;
  }
  
  void injectSlot(int slot) {
    if (this.slot != STACK_ALLOCATED) {
      throw new IllegalStateException("the variable already have a slot ");
    }
    this.slot = slot;
  }
  
  /**
   * Returns the slot of the current variable.
   * This value is intended to be used by {@link CodeGen} only.
   * @return the slot of the current variable or STACK_ALLOCATED
   *         if the variable is stack allocated.
   */
  public int slot() {
    return slot;
  }
  
  @Override
  public String toString() {
    return "var(" + type() + " " + ((slot == STACK_ALLOCATED)? "stack allocated": slot) + ')';
  }
  
  @Override
  void loadPrimitive(MethodVisitor mv) {
    Type type = type();
    if (type.vmType() == Type.VM_VOID) {
      return;
    }
    int slot = this.slot;
    if (this.slot == STACK_ALLOCATED) {
      // do nothing, the result is already on stack
      return;
    }
    mv.visitVarInsn(loadOpcode(type.vmType()), slot + (type.isMixed()? 1: 0));
  }
  
  @Override
  void loadAll(MethodVisitor mv) {
    Type type = type();
    if (type.vmType() == Type.VM_VOID) {
      return;
    }
    int slot = this.slot;
    if (type.isMixed()) {
      mv.visitVarInsn(loadOpcode(type.vmType()), slot + 1);
      mv.visitVarInsn(ALOAD, slot);
      return;
    }
    mv.visitVarInsn(loadOpcode(type.vmType()), slot);
  }
  
  void storePrimitive(MethodVisitor mv) {
    Type type = type();
    if (type.vmType() == Type.VM_VOID) {
      return;
    }
    int slot = this.slot;
    if (slot == STACK_ALLOCATED) {
      // do nothing, the result should stay on stack
      return;
    }
    mv.visitVarInsn(storeOpcode(type.vmType()), slot + (type.isMixed()? 1: 0));
  }
  
  void storeAll(MethodVisitor mv) {
    Type type = type();
    if (type.vmType() == Type.VM_VOID) {
      return;
    }
    int slot = this.slot;
    if (type.isMixed()) {
      mv.visitVarInsn(ASTORE, slot);
      mv.visitVarInsn(storeOpcode(type.vmType()), slot + 1);
      return;
    }
    mv.visitVarInsn(storeOpcode(type.vmType()), slot);
  }
}