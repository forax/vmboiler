package com.github.forax.vmboiler.sample.script;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.github.forax.vmboiler.sample.script.Expr.*;

public class TypeInferer {
  static class Env {
    final HashMap<String,Binding> scope;
    final HashMap<Expr, Binding> bindingMap;
    Type expectedType = Type.VOID; 
    
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
    
    // warning stupid side effect, *must* be called when calling VISITOR.call(...)
    Env expectedType(Type type) {
      expectedType = type;
      return this;
    }
  }
  
  public static Type inferType(Fn fn, Type returnType, Type[] parameterTypes, HashMap<Expr, Binding> bindingMap) {
    Env env = new Env(bindingMap);
    for(int i = 0; i < parameterTypes.length; i++) {
      Parameter parameter = fn.parameters().get(i);
      Binding binding = new Binding(parameterTypes[i]);
      env.scope.put(parameter.name(), binding);
      bindingMap.put(parameter, binding);
    }
    return VISITOR.call(fn.block(), env.expectedType(returnType));
  }
  
  private static final Visitor<Type, Env> VISITOR = new Visitor<Type, Env>()
      .when(Literal.class, (literal, env) -> {
        Object constant = literal.constant();
        return (constant instanceof Integer)? Type.INT: Type.OBJECT;
      })
      .when(Block.class, (block, env) -> {
        List<Expr> exprs = block.exprs();
        int size = exprs.size();
        if (size == 0) {
          return Type.OBJECT;
        }
        Type expectedType = env.expectedType;
        for(int i = 0; i < size - 1; i++) {
          TypeInferer.VISITOR.call(exprs.get(i), env.expectedType(Type.VOID));
        }
        return TypeInferer.VISITOR.call(exprs.get(size - 1), env.expectedType(expectedType));
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
        String name = varAssignment.name();
        Binding binding = env.scope.get(name);
        Type expectedType = env.expectedType;
        expectedType = (binding != null && binding.type() != null)? binding.type(): (expectedType != Type.VOID)? expectedType: null;
        Type type = TypeInferer.VISITOR.call(varAssignment.expr(), env.expectedType(expectedType));
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
        Type expectedType = env.expectedType;
        List<Type> types = call.exprs().stream().map(expr -> TypeInferer.VISITOR.call(expr, env.expectedType(null))).collect(Collectors.toList());
        Type returnType = (expectedType != null)? expectedType.mix(true):
          call.optionalOp().map(op -> op.returnTypeOp().apply(types.get(0), types.get(1))).orElse(Type.MIXED_INT); //TODO improve heuristic ?
        env.bindingMap.put(call, new Binding(returnType));
        return returnType;
      })
      .when(If.class, (if_, env) -> {
        Type expectedType = env.expectedType;
        TypeInferer.VISITOR.call(if_.condition(), env.newEnv().expectedType(Type.BOOL));
        Type type1 = TypeInferer.VISITOR.call(if_.truePart(), env.newEnv().expectedType(expectedType));
        Type type2 = TypeInferer.VISITOR.call(if_.falsePart(), env.newEnv().expectedType(expectedType));
        Type type = Type.merge(type1, type2);
        env.bindingMap.put(if_, new Binding(type));
        return type;
      })
      .when(While.class, (while_, env) -> {
        Type expectedType = env.expectedType;
        TypeInferer.VISITOR.call(while_.condition(), env.newEnv().expectedType(Type.BOOL));
        TypeInferer.VISITOR.call(while_.body(), env.newEnv().expectedType(Type.VOID));
        Type type =  (expectedType == Type.VOID)? Type.VOID: Type.OBJECT;
        env.bindingMap.put(while_, new Binding(type));
        return type;
      })
      ;
}
