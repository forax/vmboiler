package com.github.forax.vmboiler;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;

import org.objectweb.asm.MethodVisitor;

public class Var extends Value {
  private final String name;  // may be null
  private final int slot;

  protected Var(Type type, String name, int slot) {
    super(type);
    if (slot < 0) {
      throw new IllegalArgumentException("slot must be a positive number");
    }
    this.name = name;
    this.slot = slot;
  }

  public String name() {
    return name;
  }
  public int slot() {
    return slot;
  }
  
  @Override
  public String toString() {
    return "var(" + type() + " " + name + " " + slot + ')';
  }
  
  @Override
  void loadPrimitive(MethodVisitor mv) {
    Type type = type();
    int slot = this.slot + (type.isMixed()? 1: 0); 
    mv.visitVarInsn(loadOpcode(type.vmType()), slot);
  }
  
  @Override
  void loadAll(MethodVisitor mv) {
    Type type = type();
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
    int slot = this.slot + (type.isMixed()? 1: 0); 
    mv.visitVarInsn(storeOpcode(type.vmType()), slot);
  }
  
  void storeAll(MethodVisitor mv) {
    Type type = type();
    int slot = this.slot;
    if (type.isMixed()) {
      mv.visitVarInsn(ASTORE, slot);
      mv.visitVarInsn(storeOpcode(type.vmType()), slot + 1);
      return;
    }
    mv.visitVarInsn(storeOpcode(type.vmType()), slot);
  }
}