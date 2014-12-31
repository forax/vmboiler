package com.github.forax.vmboiler.sample.script;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface Expr {
  public class Literal implements Expr {
    private final Object constant;
    
    public Literal(Object constant) {
      this.constant = Objects.requireNonNull(constant);
    }

    public Object constant() {
      return constant;
    }
  }
  
  public class Block implements Expr {
    private final List<Expr> exprs;

    public Block(List<Expr> exprs) {
      this.exprs = Objects.requireNonNull(exprs);
    }

    public List<Expr> exprs() {
      return exprs;
    }
  }
  
  public class Parameter implements Expr {
    private final String name;

    public Parameter(String name) {
      this.name = name;
    }
    
    public String name() {
      return name;
    }
  }
  
  public class VarAccess implements Expr {
    private final String name;

    public VarAccess(String name) {
      this.name = Objects.requireNonNull(name);
    }
    
    public String name() {
      return name;
    }
  }
  
  public class VarAssignment implements Expr {
    private final String name;
    private final Expr expr;

    public VarAssignment(String name, Expr expr) {
      this.name = Objects.requireNonNull(name);
      this.expr = Objects.requireNonNull(expr);
    }
    
    public String name() {
      return name;
    }
    public Expr expr() {
      return expr;
    }
  }
  
  public class Call implements Expr {
    private final Op op;
    private final String name;
    private final List<Expr> exprs;
    
    public Call(Op op, String name, List<Expr> exprs) {
      this.op = op;
      this.name = Objects.requireNonNull(name);
      this.exprs = Objects.requireNonNull(exprs);
    }
    
    public Optional<Op> optionalOp() {
      return Optional.ofNullable(op);
    }
    public String name() {
      return name;
    }
    public List<Expr> exprs() {
      return exprs;
    }
  }
  
  public class If implements Expr {
    private final Expr condition;
    private final Expr truePart;
    private final Expr falsePart;
    
    public If(Expr condition, Expr truePart, Expr falsePart) {
      this.condition = Objects.requireNonNull(condition);
      this.truePart = Objects.requireNonNull(truePart);
      this.falsePart = Objects.requireNonNull(falsePart);
    }
    
    public Expr condition() {
      return condition;
    }
    public Expr truePart() {
      return truePart;
    }
    public Expr falsePart() {
      return falsePart;
    }
  }
  
  public class While implements Expr {
    private final Expr condition;
    private final Block body;
    
    public While(Expr condition, Block body) {
      this.condition = Objects.requireNonNull(condition);
      this.body = Objects.requireNonNull(body);
    }
    
    public Expr condition() {
      return condition;
    }
    public Block body() {
      return body;
    }
  }
}
