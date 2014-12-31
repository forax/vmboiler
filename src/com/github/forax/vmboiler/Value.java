package com.github.forax.vmboiler;

import java.util.Objects;

import org.objectweb.asm.MethodVisitor;

public abstract class Value {
  private final Type type;

  Value(Type type) {
    this.type = Objects.requireNonNull(type);
  }
  
  public Type type() {
    return type;
  }
  
  abstract void loadAll(MethodVisitor mv);

  abstract void loadPrimitive(MethodVisitor mv);
}