package com.github.forax.vmboiler.sample.script;

import java.util.HashMap;
import java.util.function.BiFunction;

public class Visitor<R,P> {
  private final HashMap<Class<?>, BiFunction<Object, ? super P, ? extends R>> map = new HashMap<>();
  
  public <T> Visitor<R, P> when(Class<? extends T> type, BiFunction<? super T, ? super P, ? extends R> function) {
    map.put(type, (value, param) -> function.apply(type.cast(value), param));
    return this;
  }
  
  public R call(Object value, P param) {
    return map.getOrDefault(value.getClass(),
        (v, p) -> { throw new IllegalStateException("no function for " + v.getClass().getName()); })
            .apply(value, param);
  }
}
