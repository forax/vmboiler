package com.github.forax.vmboiler.rt;

public final class OptimisticError extends Error {
  private static final long serialVersionUID = 9149565412323824897L;
  
  private final Object value;

  private OptimisticError(Object value) {
    super(null, null, false, false);
    this.value = value;
  }

  // called by generated code
  public Object value() {
    return value;
  }
  
  // called by generated code
  public static OptimisticError newOptimisticError(Object value) {
    return new OptimisticError(value);
  }
}