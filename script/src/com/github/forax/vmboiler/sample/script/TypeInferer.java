package com.github.forax.vmboiler.sample.script;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.forax.vmboiler.sample.script.Expr.*;

public class TypeInferer {
  static class Env {
    final HashMap<String,Binding> scope;
    final HashMap<Expr, Binding> bindingMap;
    
    private Env(HashMap<String, Binding> scope, HashMap<Expr, Binding> bindingMap) {
      this.scope = scope;
      this.bindingMap = bindingMap;
    }
    
    Env(HashMap<Expr, Binding> bindingMap) {
      this(new HashMap<>(), bindingMap);
    }
    
    Env newEnv() {
      return new Env(new HashMap<>(scope), bindingMap);
    }
  }
  
  public static Type inferType(Fn fn, Type[] parameterTypes, HashMap<Expr, Binding> bindingMap) {
    Env env = new Env(bindingMap);
    for(int i = 0; i < parameterTypes.length; i++) {
      Parameter parameter = fn.parameters().get(i);
      Binding binding = new Binding(parameterTypes[i]);
      env.scope.put(parameter.name(), binding);
      bindingMap.put(parameter, binding);
    }
    return VISITOR.call(fn.block(), env);
  }
  
  private static final Visitor<Type, Env> VISITOR = new Visitor<Type, Env>()
      .when(Literal.class, (literal, env) -> {
        Object constant = literal.constant();
        return (constant instanceof Integer)? Type.INT: Type.OBJECT;
      })
      .when(Block.class, (block, env) -> {
        Type type = Type.OBJECT;
        for(Expr expr: block.exprs()) {
          type = TypeInferer.VISITOR.call(expr, env);
        }
        return type;
      })
      .when(VarAccess.class, (varAccess, env) -> {
        String name = varAccess.name();
        Binding binding = env.scope.get(name);
        if (binding == null) {
          throw new IllegalStateException("no variable " + name + " defined in scope");
        }
        env.bindingMap.put(varAccess, binding);
        return binding.type();
      })
      .when(VarAssignment.class, (varAssignment, env) -> {
        Type type = TypeInferer.VISITOR.call(varAssignment.expr(), env);
        String name = varAssignment.name();
        Binding binding = env.scope.get(name);
        if (binding != null) {
          binding.type(Type.merge(binding.type(), type));
        } else {
          binding = new Binding(type);
          env.scope.put(name, binding);
        }
        env.bindingMap.put(varAssignment, binding);
        return type;
      })
      .when(Call.class, (call, env) -> {
        List<Type> types = call.exprs().stream().map(expr -> TypeInferer.VISITOR.call(expr, env)).collect(Collectors.toList());
        Type returnType = call.optionalOp().map(op -> op.returnTypeOp().apply(types.get(0), types.get(1))).orElse(Type.MIXED_INT); //TODO better heuristic ?
        env.bindingMap.put(call, new Binding(returnType));  // to track return type profile
        return returnType;
      })
      .when(If.class, (if_, env) -> {
        TypeInferer.VISITOR.call(if_.condition(), env);
        Type type1 = TypeInferer.VISITOR.call(if_.truePart(), env.newEnv());
        Type type2 = TypeInferer.VISITOR.call(if_.falsePart(), env.newEnv());
        Type type = Type.merge(type1, type2);
        env.bindingMap.put(if_, new Binding(type));
        return type;
      })
      .when(While.class, (while_, env) -> {
        TypeInferer.VISITOR.call(while_.condition(), env);
        TypeInferer.VISITOR.call(while_.body(), env.newEnv());
        return Type.OBJECT;
      })
      ;
}
