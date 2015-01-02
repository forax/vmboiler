package com.github.forax.vmboiler.sample.script;

import java.io.PrintStream;
import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Linker {
  static class Function {
    final Fn fn;
    private final HashMap<List<Class<?>>, HashMap<Expr, Binding>> bindingMapMap = new HashMap<>();
    final HashMap<MethodType, InvalidableCallSite> callSiteMap = new HashMap<>();
    
    Function(Fn fn) {
      this.fn = fn;
    }
    
    CallSite createCallSite(Linker linker, String name, MethodType methodType) {
      return callSiteMap.computeIfAbsent(methodType, key -> {
        Type returnType = Type.getTypeFromClass(methodType.returnType());
        Type[] parameterTypes = methodType.parameterList().stream().map(Type::getTypeFromClass).toArray(Type[]::new);
        HashMap<Expr, Binding> bindingMap = bindingMapMap.computeIfAbsent(methodType.parameterList(), key2 -> {
          HashMap<Expr, Binding> newBindingMap = new HashMap<>();
          TypeInferer.inferType(fn, returnType, parameterTypes, newBindingMap);
          return newBindingMap;
        });
        return new InvalidableCallSite(methodType, () ->
            Generator.generate(fn, linker, bindingMap, returnType.mix(true) /*FIXME ?*/, name, parameterTypes));
      });
    }
  }
  
  static class InvalidableCallSite extends MutableCallSite {
    private final Supplier<MethodHandle> supplier;
    private final MethodHandle fallback;
    
    InvalidableCallSite(MethodType methodType, Supplier<MethodHandle> supplier) {
      super(methodType);
      this.supplier = supplier;
      MethodHandle fallback = MethodHandles.foldArguments(
          MethodHandles.exactInvoker(methodType), FALLBACK.bindTo(this));
      this.setTarget(supplier.get());
      this.fallback = fallback;
    }
    
    @SuppressWarnings("unused")  // called by a method handle
    private MethodHandle fallback() {
      MethodHandle target = supplier.get();
      setTarget(target);
      return target;
    }
    
    void invalidate() {
      setTarget(fallback);
    }
    
    private static final MethodHandle FALLBACK;
    static {
      try {
        FALLBACK = MethodHandles.lookup().findVirtual(InvalidableCallSite.class, "fallback",
            MethodType.methodType(MethodHandle.class));
      } catch (NoSuchMethodException | IllegalAccessException e) {
        throw new AssertionError(e);
      }
    }
  }
  
  private final Map<String, Function> functionMap;
  
  public Linker(Script script) {
    functionMap = script.funs().stream().collect(Collectors.toMap(Fn::name, Function::new));
  }
  
  public void invalidate(String nameAndType) {
    int index = nameAndType.indexOf('(');
    String name = nameAndType.substring(0, index);
    MethodType methodType = MethodType.fromMethodDescriptorString(nameAndType.substring(index), null);

    System.out.println("linker invalidate: " + name + ':' + methodType);    
    functionMap.get(name).callSiteMap.get(methodType).invalidate();
  }
  
  public CallSite getCallSite(String name, MethodType methodType) {
    // try built-ins first
    if (name.equals("print")) {
      MethodHandle mh;
      try {
        mh = MethodHandles.publicLookup().findVirtual(PrintStream.class, "println",
            MethodType.methodType(void.class, methodType.parameterType(0)));
      } catch (NoSuchMethodException | IllegalAccessException e) {
        throw new AssertionError(e);
      }
      mh = mh.bindTo(System.out);
      return new ConstantCallSite(mh.asType(mh.type().changeReturnType(methodType.returnType())));
    }
    
    Function function = functionMap.get(name);
    if (function == null || function.fn.parameters().size() != methodType.parameterCount()) {
      throw new IllegalStateException("no function matching " + name + methodType + " found");
    }
    
    return function.createCallSite(this, name, methodType);
  }
}
