package com.github.forax.vmboiler.sample.script;

import java.util.function.BinaryOperator;

public enum Op {
  add(NUMERIC()), sub(NUMERIC()), mul(NUMERIC()), div(NUMERIC()), rem(NUMERIC()),
  eq(TEST()), ne(TEST()), lt(TEST()), le(TEST()), gt(TEST()), ge(TEST())
  ;
  
  private final BinaryOperator<Type> returnType;
  
  private Op(BinaryOperator<Type> returnType) {
    this.returnType = returnType;
  }
  
  public BinaryOperator<Type> returnTypeOp() {
    return returnType;
  }
  
  private static final BinaryOperator<Type> TEST()  {
    return (t1, t2) -> Type.BOOL;
  }
  
  private static final BinaryOperator<Type> NUMERIC()  {
    return (t1, t2) -> {
      Type erased1 = t1.erase();
      Type erased2 = t1.erase();
      if (erased1 == Type.INT && erased2 == Type.INT) {
        return Type.MIXED_INT;
      }
      if (erased1 == Type.NUM || erased2 == Type.NUM) {
        return Type.NUM.mix(t1.isMixed() || t2.isMixed());
      }
      return Type.OBJECT;
    };
  }
}