package com.github.forax.vmboiler.sample.script.tools;

  import com.github.forax.vmboiler.sample.script.Expr;
    import com.github.forax.vmboiler.sample.script.Fn;
    import java.util.List;
  
import com.github.forax.vmboiler.sample.script.lexer.RuleEnum;
import com.github.forax.vmboiler.sample.script.parser.TerminalEnum;
import com.github.forax.vmboiler.sample.script.parser.NonTerminalEnum;
import com.github.forax.vmboiler.sample.script.parser.ProductionEnum;
import com.github.forax.vmboiler.sample.script.tools.TerminalEvaluator;
import com.github.forax.vmboiler.sample.script.tools.GrammarEvaluator;

import fr.umlv.tatoo.runtime.buffer.LexerBuffer;
import fr.umlv.tatoo.runtime.tools.AnalyzerListener;
import fr.umlv.tatoo.runtime.tools.DataViewer;
import fr.umlv.tatoo.runtime.tools.SemanticStack;

/**  This class is called by the parser when
 *  <ol>
 *    <li>a terminal is shifted</li>
 *    <li>a non terminal is reduced</li>
 *    <li>a non terminal is accepted</li>
 *   </ol>
 *   In that case, depending on the information of the .xtls, terminal and non-terminal
 *   values are pushed/pop from a semantic stack.
 *   
 *   Furthermore, in case of error recovery, values of the stack can be pop out
 *   depending if the last recognized element is a terminal or a non-terminal.
 * 
 *  This class is generated - please do not edit it 
 */
public class AnalyzerProcessor<B extends LexerBuffer,D>
  implements AnalyzerListener<RuleEnum,B,TerminalEnum,NonTerminalEnum,ProductionEnum> {
          
  private final GrammarEvaluator grammarEvaluator;
  private final TerminalEvaluator<? super D> terminalEvaluator;
  private final DataViewer<? super B,? extends D> dataViewer;
  private final SemanticStack stack;
  
  protected AnalyzerProcessor(TerminalEvaluator<? super D> terminalEvaluator, GrammarEvaluator grammarEvaluator, DataViewer<? super B,? extends D> dataViewer, SemanticStack stack) {
    this.terminalEvaluator=terminalEvaluator;
    this.grammarEvaluator=grammarEvaluator;
    this.dataViewer=dataViewer;
    this.stack=stack;
  }
  
  /** Creates a tools listener that redirect accept/shift/reduce and comment to the terminal Evaluator
      and the grammar evaluator..
      This constructor allows to share the same stack between more
      than one parser processor.
      @param terminalEvaluator the terminal evaluator.
      @param grammarEvaluator the grammar evaluator.
      @param stack the stack used by the processor
   */
  public static <B extends LexerBuffer,D> AnalyzerProcessor<B,D>
    createAnalyzerProcessor(TerminalEvaluator<? super D> terminalEvaluator, GrammarEvaluator grammarEvaluator, DataViewer<? super B,? extends D> dataViewer, SemanticStack stack) {
    
    return new AnalyzerProcessor<B,D>(terminalEvaluator,grammarEvaluator,dataViewer,stack);
  }
  
  public void comment(RuleEnum rule, B buffer) {
    D data;
    switch(rule) {           case comment:
            data=dataViewer.view(buffer);
            terminalEvaluator.comment(data);
            return;
              default:
    }
    throw new AssertionError("unknown rule "+rule);
  }
 
   public void shift(TerminalEnum terminal, RuleEnum rule, B buffer) {
     D data;
     switch(terminal) {      case assign: {
                       return;
           }
                 case colon: {
                       return;
           }
                 case eol: {
                       return;
           }
                 case lpar: {
                       return;
           }
                 case rpar: {
                       return;
           }
                 case add: {
                       return;
           }
                 case sub: {
                       return;
           }
                 case mul: {
                       return;
           }
                 case div: {
                       return;
           }
                 case rem: {
                       return;
           }
                 case eq: {
                       return;
           }
                 case ne: {
                       return;
           }
                 case lt: {
                       return;
           }
                 case le: {
                       return;
           }
                 case gt: {
                       return;
           }
                 case ge: {
                       return;
           }
                 case fn: {
                       return;
           }
                 case if_: {
                       return;
           }
                 case while_: {
                       return;
           }
                 case text: {
         data=dataViewer.view(buffer);
                                  String text=terminalEvaluator.text(data);
                                      stack.push_Object(text);
                                 return;
           }
                 case integer: {
         data=dataViewer.view(buffer);
                                  Object integer=terminalEvaluator.integer(data);
                                      stack.push_Object(integer);
                                 return;
           }
                 case id: {
         data=dataViewer.view(buffer);
                                  String id=terminalEvaluator.id(data);
                                      stack.push_Object(id);
                                 return;
           }
                 case __eof__: {
                       return;
           }
                 }
     throw new AssertionError("unknown terminal "+terminal);
   }
    
    
    @SuppressWarnings("unchecked")
    public void reduce(ProductionEnum production) {
      switch(production) {           case fun_star_0_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case fun_star_0_rec: { // STAR_RECURSIVE_LEFT
                            
                    Fn fun=(Fn)stack.pop_Object();
                    List<Fn> fun_star_0=(List<Fn>)stack.pop_Object();
                     fun_star_0.add(fun);
                     stack.push_Object(fun_star_0);
                       
          }
          return;
                    case script: { // not synthetic
                                 List<Fn> fun_star_0=(List<Fn>)stack.pop_Object();
                                           grammarEvaluator.script(fun_star_0);
                      
          }
          return;
                    case id_star_1_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case id_star_1_rec: { // STAR_RECURSIVE_LEFT
                            
                    String id=(String)stack.pop_Object();
                    List<String> id_star_1=(List<String>)stack.pop_Object();
                     id_star_1.add(id);
                     stack.push_Object(id_star_1);
                       
          }
          return;
                    case expr_star_2_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case expr_star_2_rec: { // STAR_RECURSIVE_LEFT
                            
                    Expr expr=(Expr)stack.pop_Object();
                    List<Expr> expr_star_2=(List<Expr>)stack.pop_Object();
                     expr_star_2.add(expr);
                     stack.push_Object(expr_star_2);
                       
          }
          return;
                    case fun: { // not synthetic
                                 List<Expr> expr_star_2=(List<Expr>)stack.pop_Object();
                                          List<String> id_star_1=(List<String>)stack.pop_Object();
                                          String id=(String)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.fun(id,id_star_1,expr_star_2));
                      
          }
          return;
                    case expr_integer: { // not synthetic
                                 Object integer=(Object)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_integer(integer));
                      
          }
          return;
                    case expr_text: { // not synthetic
                                 String text=(String)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_text(text));
                      
          }
          return;
                    case expr_star_3_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case expr_star_3_rec: { // STAR_RECURSIVE_LEFT
                            
                    Expr expr=(Expr)stack.pop_Object();
                    List<Expr> expr_star_3=(List<Expr>)stack.pop_Object();
                     expr_star_3.add(expr);
                     stack.push_Object(expr_star_3);
                       
          }
          return;
                    case expr_block: { // not synthetic
                                 List<Expr> expr_star_3=(List<Expr>)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_block(expr_star_3));
                      
          }
          return;
                    case expr_var_access: { // not synthetic
                                 String id=(String)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_var_access(id));
                      
          }
          return;
                    case expr_var_assignment: { // not synthetic
                                 Expr expr=(Expr)stack.pop_Object();
                                          String id=(String)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_var_assignment(id,expr));
                      
          }
          return;
                    case expr_star_4_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case expr_star_4_rec: { // STAR_RECURSIVE_LEFT
                            
                    Expr expr=(Expr)stack.pop_Object();
                    List<Expr> expr_star_4=(List<Expr>)stack.pop_Object();
                     expr_star_4.add(expr);
                     stack.push_Object(expr_star_4);
                       
          }
          return;
                    case expr_call: { // not synthetic
                                 List<Expr> expr_star_4=(List<Expr>)stack.pop_Object();
                                          String id=(String)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_call(id,expr_star_4));
                      
          }
          return;
                    case expr_if: { // not synthetic
                                 Expr expr3=(Expr)stack.pop_Object();
                                          Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_if(expr,expr2,expr3));
                      
          }
          return;
                    case expr_star_5_empty: { // STAR_EMPTY
                            stack.push_Object(new java.util.ArrayList<Object>());
                  
          }
          return;
                    case expr_star_5_rec: { // STAR_RECURSIVE_LEFT
                            
                    Expr expr=(Expr)stack.pop_Object();
                    List<Expr> expr_star_5=(List<Expr>)stack.pop_Object();
                     expr_star_5.add(expr);
                     stack.push_Object(expr_star_5);
                       
          }
          return;
                    case expr_while: { // not synthetic
                                 List<Expr> expr_star_5=(List<Expr>)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_while(expr,expr_star_5));
                      
          }
          return;
                    case expr_mul: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_mul(expr,expr2));
                      
          }
          return;
                    case expr_div: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_div(expr,expr2));
                      
          }
          return;
                    case expr_rem: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_rem(expr,expr2));
                      
          }
          return;
                    case expr_add: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_add(expr,expr2));
                      
          }
          return;
                    case expr_sub: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_sub(expr,expr2));
                      
          }
          return;
                    case expr_eq: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_eq(expr,expr2));
                      
          }
          return;
                    case expr_ne: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_ne(expr,expr2));
                      
          }
          return;
                    case expr_lt: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_lt(expr,expr2));
                      
          }
          return;
                    case expr_le: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_le(expr,expr2));
                      
          }
          return;
                    case expr_gt: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_gt(expr,expr2));
                      
          }
          return;
                    case expr_ge: { // not synthetic
                                 Expr expr2=(Expr)stack.pop_Object();
                                          Expr expr=(Expr)stack.pop_Object();
                                                stack.push_Object(grammarEvaluator.expr_ge(expr,expr2));
                      
          }
          return;
                    default:
             throw new AssertionError("unknown production "+production);
       }
    }

     public void accept(NonTerminalEnum nonterminal) {
       switch(nonterminal) {            case script:
             grammarEvaluator.acceptScript();
             return;
                     default:
       }
        throw new AssertionError("unknown start nonterminal "+nonterminal);
     }

      public void popTerminalOnError(TerminalEnum terminal) {
        switch(terminal) {             case assign:
              
              return;
                         case colon:
              
              return;
                         case eol:
              
              return;
                         case lpar:
              
              return;
                         case rpar:
              
              return;
                         case add:
              
              return;
                         case sub:
              
              return;
                         case mul:
              
              return;
                         case div:
              
              return;
                         case rem:
              
              return;
                         case eq:
              
              return;
                         case ne:
              
              return;
                         case lt:
              
              return;
                         case le:
              
              return;
                         case gt:
              
              return;
                         case ge:
              
              return;
                         case fn:
              
              return;
                         case if_:
              
              return;
                         case while_:
              
              return;
                         case text:
              stack.pop_Object();
              return;
                         case integer:
              stack.pop_Object();
              return;
                         case id:
              stack.pop_Object();
              return;
                         case __eof__:
              
              return;
                     }
        throw new AssertionError("unknown terminal "+terminal);
      }
 
      public void popNonTerminalOnError(NonTerminalEnum nonTerminal) {
        switch(nonTerminal) {             case script:
              
              return;
                         case fun:
              stack.pop_Object();
              return;
                         case expr:
              stack.pop_Object();
              return;
                         case fun_star_0:
              stack.pop_Object();
              return;
                         case id_star_1:
              stack.pop_Object();
              return;
                         case expr_star_2:
              stack.pop_Object();
              return;
                         case expr_star_3:
              stack.pop_Object();
              return;
                         case expr_star_4:
              stack.pop_Object();
              return;
                         case expr_star_5:
              stack.pop_Object();
              return;
                     }
        throw new AssertionError("unknown nonterminal "+nonTerminal);
      }
}