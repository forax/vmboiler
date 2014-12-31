package com.github.forax.vmboiler.sample.script;

import java.util.Arrays;

import org.objectweb.asm.ClassWriter;

public class ConstantPoolPatch {
  private final ClassWriter writer;
  private int[] indexes = new int[32];
  private Object[] values = new Object[32];
  private int size;
  
  public ConstantPoolPatch(ClassWriter writer) {
    this.writer = writer;
  }
  
  public String encode(Object o) {
    if (size == indexes.length) {
      indexes = Arrays.copyOf(indexes, size << 1);
      values = Arrays.copyOf(values, size << 1);
    }
    String mangled = "<<PATCH" + size + ">>";
    int index = writer.newConst(mangled);
    indexes[size] = index;
    values[size] = o;
    size++;
    return mangled;
  }
  
  public Object[] createPatchArray() {
    int constantPoolSize = writer.newConst("<<SENTINEL>>");
    Object[] patches = new Object[constantPoolSize];
    for(int i = 0; i < indexes.length; i++) {
      patches[indexes[i]] = values[i];
    }
    return patches;
  }
}
