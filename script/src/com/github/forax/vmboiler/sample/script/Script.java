package com.github.forax.vmboiler.sample.script;

import java.util.List;
import java.util.Objects;

public class Script {
  private final List<Fn> funs;

  public Script(List<Fn> funs) {
    this.funs = Objects.requireNonNull(funs);
  }

  public List<Fn> funs() {
    return funs;
  }
}
