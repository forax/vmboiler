package com.github.forax.vmboiler.sample.script;

import java.util.List;
import java.util.Objects;

import com.github.forax.vmboiler.sample.script.Expr.Block;
import com.github.forax.vmboiler.sample.script.Expr.Parameter;

public class Fn {
  private final String name;
  private final List<Parameter> parameters;
  private final Block block;

  public Fn(String name, List<Parameter> parameters, Block block) {
    this.name = Objects.requireNonNull(name);
    this.parameters = Objects.requireNonNull(parameters);
    this.block = Objects.requireNonNull(block);
  }

  public String name() {
    return name;
  }
  public List<Parameter> parameters() {
    return parameters;
  }
  public Block block() {
    return block;
  }
}
