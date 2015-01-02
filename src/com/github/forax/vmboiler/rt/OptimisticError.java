package com.github.forax.vmboiler.rt;

/**
 * An exception that must be thrown is the return type
 * doesn't allow to store the return value.
 */
public final class OptimisticError extends Error {
  private static final long serialVersionUID = 9149565412323824897L;
  
  private final Object value;

  private OptimisticError(Object value) {
    super(null, null, false, false);
    this.value = value;
  }

  /**
   * Returns the return value of the method call.
   * @return the return value of the method call.
   */
  // called by generated code
  public Object value() {
    return value;
  }
  
  /**
   * Create a new {@link OptimisticError}.
   * @param value the return value of the method call.
   * @return a newly craeted {@link OptimisticError}.
   */
  // called by generated code
  public static OptimisticError newOptimisticError(Object value) {
    return new OptimisticError(value);
  }
}