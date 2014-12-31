package com.github.forax.vmboiler.sample.script.tools;

import com.github.forax.vmboiler.sample.script.Expr;
import com.github.forax.vmboiler.sample.script.Fn;
import java.util.List;

/** 
 *  This class is generated - please do not edit it 
 */
public interface GrammarEvaluator {
  /** This methods is called after the reduction of the non terminal script
   *  by the grammar production script.
   *  <code>script ::= fun_star_0</code>
   */
  public void script(List<Fn> fun_star);
  /** This methods is called after the reduction of the non terminal fun
   *  by the grammar production fun.
   *  <code>fun ::= fn lpar id id_star_1 colon expr_star_2 rpar</code>
   */
  public Fn fun(String id,List<String> id_star,List<Expr> expr_star);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_integer.
   *  <code>expr ::= integer</code>
   */
  public Expr expr_integer(Object integer);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_text.
   *  <code>expr ::= text</code>
   */
  public Expr expr_text(String text);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_block.
   *  <code>expr ::= lpar expr_star_3 rpar</code>
   */
  public Expr expr_block(List<Expr> expr_star);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_var_access.
   *  <code>expr ::= id</code>
   */
  public Expr expr_var_access(String id);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_var_assignment.
   *  <code>expr ::= id assign expr</code>
   */
  public Expr expr_var_assignment(String id,Expr expr);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_call.
   *  <code>expr ::= id lpar expr_star_4 rpar</code>
   */
  public Expr expr_call(String id,List<Expr> expr_star);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_if.
   *  <code>expr ::= if_ lpar expr expr expr rpar</code>
   */
  public Expr expr_if(Expr expr,Expr expr2,Expr expr3);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_while.
   *  <code>expr ::= while_ lpar expr expr_star_5 rpar</code>
   */
  public Expr expr_while(Expr expr,List<Expr> expr_star);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_mul.
   *  <code>expr ::= expr mul expr</code>
   */
  public Expr expr_mul(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_div.
   *  <code>expr ::= expr div expr</code>
   */
  public Expr expr_div(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_rem.
   *  <code>expr ::= expr rem expr</code>
   */
  public Expr expr_rem(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_add.
   *  <code>expr ::= expr add expr</code>
   */
  public Expr expr_add(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_sub.
   *  <code>expr ::= expr sub expr</code>
   */
  public Expr expr_sub(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_eq.
   *  <code>expr ::= expr eq expr</code>
   */
  public Expr expr_eq(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_ne.
   *  <code>expr ::= expr ne expr</code>
   */
  public Expr expr_ne(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_lt.
   *  <code>expr ::= expr lt expr</code>
   */
  public Expr expr_lt(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_le.
   *  <code>expr ::= expr le expr</code>
   */
  public Expr expr_le(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_gt.
   *  <code>expr ::= expr gt expr</code>
   */
  public Expr expr_gt(Expr expr,Expr expr2);
  /** This methods is called after the reduction of the non terminal expr
   *  by the grammar production expr_ge.
   *  <code>expr ::= expr ge expr</code>
   */
  public Expr expr_ge(Expr expr,Expr expr2);

  public void acceptScript();
}
