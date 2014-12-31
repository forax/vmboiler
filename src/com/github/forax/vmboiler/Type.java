package com.github.forax.vmboiler;

public interface Type {
  boolean isMixed();
  org.objectweb.asm.Type asmType();
}