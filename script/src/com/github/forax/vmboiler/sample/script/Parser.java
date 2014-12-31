package com.github.forax.vmboiler.sample.script;

import java.io.Reader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.forax.vmboiler.sample.script.Expr.*;
import com.github.forax.vmboiler.sample.script.tools.Analyzers;
import com.github.forax.vmboiler.sample.script.tools.GrammarEvaluator;
import com.github.forax.vmboiler.sample.script.tools.TerminalEvaluator;

public class Parser {
  static final class FunGrammarEvaluator implements GrammarEvaluator {
    @Override
    public void acceptScript() {
      // do nothing
    }

    private Script script;
    
    public Script getScript() {
      return script;
    }
    
    @Override
    public void script(List<Fn> fun_star) {
      script = new Script(fun_star);
    }

    @Override
    public Fn fun(String id, List<String> id_star, List<Expr> expr_star) {
      return new Fn(id, id_star.stream().map(Parameter::new).collect(Collectors.toList()), new Block(expr_star));
    }
    
    @Override
    public Expr expr_integer(Object integer) {
      return new Literal(integer);
    }
    @Override
    public Expr expr_text(String text) {
      return new Literal(text);
    }

    @Override
    public Expr expr_block(List<Expr> expr_star) {
      return new Block(expr_star);
    }

    @Override
    public Expr expr_var_access(String id) {
      return new VarAccess(id);
    }
    @Override
    public Expr expr_var_assignment(String id, Expr expr) {
      return new VarAssignment(id, expr);
    }

    @Override
    public Expr expr_call(String id, List<Expr> expr_star) {
      return new Call(null, id, expr_star);
    }

    @Override
    public Expr expr_if(Expr expr, Expr expr2, Expr expr3) {
      return new If(expr, expr2, expr3);
    }
    @Override
    public Expr expr_while(Expr expr, List<Expr> expr_star) {
      return new While(expr, new Block(expr_star));
    }

    

    @Override
    public Expr expr_add(Expr expr, Expr expr2) {
      return binOp(Op.add, expr, expr2);
    }
    @Override
    public Expr expr_sub(Expr expr, Expr expr2) {
      return binOp(Op.sub, expr, expr2);
    }
    @Override
    public Expr expr_mul(Expr expr, Expr expr2) {
      return binOp(Op.mul, expr, expr2);
    }
    @Override
    public Expr expr_div(Expr expr, Expr expr2) {
      return binOp(Op.div, expr, expr2);
    }
    @Override
    public Expr expr_rem(Expr expr, Expr expr2) {
      return binOp(Op.rem, expr, expr2);
    }
    
    @Override
    public Expr expr_eq(Expr expr, Expr expr2) {
      return binOp(Op.eq, expr, expr2);
    }
    @Override
    public Expr expr_ne(Expr expr, Expr expr2) {
      return binOp(Op.ne, expr, expr2);
    }
    @Override
    public Expr expr_lt(Expr expr, Expr expr2) {
      return binOp(Op.lt, expr, expr2);
    }
    @Override
    public Expr expr_le(Expr expr, Expr expr2) {
      return binOp(Op.le, expr, expr2);
    }
    @Override
    public Expr expr_gt(Expr expr, Expr expr2) {
      return binOp(Op.gt, expr, expr2);
    }
    @Override
    public Expr expr_ge(Expr expr, Expr expr2) {
      return binOp(Op.ge, expr, expr2);
    }

    private static Expr binOp(Op op, Expr expr, Expr expr2) {
      return new Call(op, op.name(), Arrays.asList(expr, expr2));
    }
  }

  public static Script parse(Reader reader) {
    TerminalEvaluator<CharSequence> terminalEvaluator = new TerminalEvaluator<CharSequence>() {
      @Override
      public String text(CharSequence data) {
        return data.subSequence(1, data.length() - 1).toString();
      }
      @Override
      public Object integer(CharSequence data) {
        try {
          return Integer.parseInt(data.toString());
        } catch(IllegalArgumentException e) {
          return new BigInteger(data.toString());
        }
      }
      @Override
      public String id(CharSequence data) {
        return data.toString();
      }
      
      @Override
      public void comment(CharSequence data) {
        // ignore
      }
    };

    FunGrammarEvaluator grammarEvaluator = new FunGrammarEvaluator();
    Analyzers.run(reader, terminalEvaluator, grammarEvaluator, null, null);
    return grammarEvaluator.getScript();
  }
}
