package com.github.forax.vmboiler.sample.script;

import java.util.Objects;

public class Binding {
  private Type type;

  public Binding(Type type) {
    this.type = Objects.requireNonNull(type);
  }

  public Type type() {
    return type;
  }

  public void type(Type type) {
    this.type = type;
  }
  
  @Override
  public String toString() {
    return "binding(" + type + ')';
  }
}