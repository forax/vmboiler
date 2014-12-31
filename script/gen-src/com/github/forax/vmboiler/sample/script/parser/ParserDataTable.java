package com.github.forax.vmboiler.sample.script.parser;

import com.github.forax.vmboiler.sample.script.parser.NonTerminalEnum;
import com.github.forax.vmboiler.sample.script.parser.ProductionEnum;
import com.github.forax.vmboiler.sample.script.parser.TerminalEnum;
import fr.umlv.tatoo.runtime.parser.AcceptAction;
import fr.umlv.tatoo.runtime.parser.Action;
import fr.umlv.tatoo.runtime.parser.BranchAction;
import fr.umlv.tatoo.runtime.parser.ErrorAction;
import fr.umlv.tatoo.runtime.parser.ExitAction;
import fr.umlv.tatoo.runtime.parser.ParserTable;
import fr.umlv.tatoo.runtime.parser.ReduceAction;
import fr.umlv.tatoo.runtime.parser.ShiftAction;
import fr.umlv.tatoo.runtime.parser.StateMetadata;
import java.util.EnumMap;

/** 
 *  This class is generated - please do not edit it 
 */
public class ParserDataTable {
  private ParserDataTable() {
   accept = AcceptAction.<TerminalEnum,ProductionEnum,VersionEnum>getInstance();
   exit = ExitAction.<TerminalEnum,ProductionEnum,VersionEnum>getInstance();
    initexprGotoes();
    initid_star_1Gotoes();
    initfunGotoes();
    initexpr_star_5Gotoes();
    initexpr_star_4Gotoes();
    initexpr_star_2Gotoes();
    initexpr_star_3Gotoes();
    initfun_star_0Gotoes();
    initscriptGotoes();
    reduceexpr_star_5_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_5_rec,2,expr_star_5Gotoes);
    reducescript = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.script,1,scriptGotoes);
    reduceexpr_var_assignment = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_var_assignment,3,exprGotoes);
    reduceexpr_div = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_div,3,exprGotoes);
    reduceexpr_integer = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_integer,1,exprGotoes);
    reduceexpr_call = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_call,4,exprGotoes);
    reduceexpr_star_4_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_4_rec,2,expr_star_4Gotoes);
    reduceexpr_if = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_if,6,exprGotoes);
    reducefun_star_0_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.fun_star_0_rec,2,fun_star_0Gotoes);
    reduceexpr_le = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_le,3,exprGotoes);
    reduceexpr_text = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_text,1,exprGotoes);
    reducefun = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.fun,7,funGotoes);
    reduceexpr_star_2_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_2_empty,0,expr_star_2Gotoes);
    reduceexpr_rem = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_rem,3,exprGotoes);
    reduceexpr_add = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_add,3,exprGotoes);
    reduceid_star_1_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.id_star_1_empty,0,id_star_1Gotoes);
    reduceexpr_star_5_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_5_empty,0,expr_star_5Gotoes);
    reduceexpr_ne = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_ne,3,exprGotoes);
    reduceexpr_sub = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_sub,3,exprGotoes);
    reduceexpr_star_3_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_3_rec,2,expr_star_3Gotoes);
    reduceexpr_gt = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_gt,3,exprGotoes);
    reducefun_star_0_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.fun_star_0_empty,0,fun_star_0Gotoes);
    reduceexpr_var_access = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_var_access,1,exprGotoes);
    reduceid_star_1_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.id_star_1_rec,2,id_star_1Gotoes);
    reduceexpr_mul = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_mul,3,exprGotoes);
    reduceexpr_lt = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_lt,3,exprGotoes);
    reduceexpr_star_4_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_4_empty,0,expr_star_4Gotoes);
    reduceexpr_star_3_empty = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_3_empty,0,expr_star_3Gotoes);
    reduceexpr_block = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_block,3,exprGotoes);
    reduceexpr_eq = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_eq,3,exprGotoes);
    reduceexpr_while = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_while,5,exprGotoes);
    reduceexpr_ge = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_ge,3,exprGotoes);
    reduceexpr_star_2_rec = new ReduceAction<TerminalEnum,ProductionEnum,VersionEnum>(ProductionEnum.expr_star_2_rec,2,expr_star_2Gotoes);
    shift29 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(29);
    shift9 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(9);
    shift47 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(47);
    shift17 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(17);
    shift45 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(45);
    shift21 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(21);
    shift13 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(13);
    shift2 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(2);
    shift27 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(27);
    shift23 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(23);
    shift6 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(6);
    shift12 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(12);
    shift35 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(35);
    shift16 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(16);
    shift19 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(19);
    shift41 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(41);
    shift3 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(3);
    shift31 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(31);
    shift4 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(4);
    shift15 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(15);
    shift57 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(57);
    shift37 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(37);
    shift54 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(54);
    shift18 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(18);
    shift39 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(39);
    shift25 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(25);
    shift8 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(8);
    shift49 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(49);
    shift10 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(10);
    shift11 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(11);
    shift33 = new ShiftAction<TerminalEnum,ProductionEnum,VersionEnum>(33);
    error0 = new ErrorAction<TerminalEnum,ProductionEnum,VersionEnum>("parse error");
    branch0 = new BranchAction<TerminalEnum,ProductionEnum,VersionEnum>("parse error");
    inittextArray();
    initmulArray();
    initrparArray();
    initcolonArray();
    initwhile_Array();
    initaddArray();
    initleArray();
    initlparArray();
    init__eof__Array();
    initidArray();
    initif_Array();
    initeqArray();
    initsubArray();
    initremArray();
    initneArray();
    initltArray();
    initgtArray();
    initintegerArray();
    initassignArray();
    initfnArray();
    initgeArray();
    initdivArray();
    EnumMap<TerminalEnum,Action<TerminalEnum,ProductionEnum,VersionEnum>[]> tableMap =
      new EnumMap<TerminalEnum,Action<TerminalEnum,ProductionEnum,VersionEnum>[]>(TerminalEnum.class);
      
    tableMap.put(TerminalEnum.text,textArray);
    tableMap.put(TerminalEnum.mul,mulArray);
    tableMap.put(TerminalEnum.rpar,rparArray);
    tableMap.put(TerminalEnum.colon,colonArray);
    tableMap.put(TerminalEnum.while_,while_Array);
    tableMap.put(TerminalEnum.add,addArray);
    tableMap.put(TerminalEnum.le,leArray);
    tableMap.put(TerminalEnum.lpar,lparArray);
    tableMap.put(TerminalEnum.__eof__,__eof__Array);
    tableMap.put(TerminalEnum.id,idArray);
    tableMap.put(TerminalEnum.if_,if_Array);
    tableMap.put(TerminalEnum.eq,eqArray);
    tableMap.put(TerminalEnum.sub,subArray);
    tableMap.put(TerminalEnum.rem,remArray);
    tableMap.put(TerminalEnum.ne,neArray);
    tableMap.put(TerminalEnum.lt,ltArray);
    tableMap.put(TerminalEnum.gt,gtArray);
    tableMap.put(TerminalEnum.integer,integerArray);
    tableMap.put(TerminalEnum.assign,assignArray);
    tableMap.put(TerminalEnum.fn,fnArray);
    tableMap.put(TerminalEnum.ge,geArray);
    tableMap.put(TerminalEnum.div,divArray);
    initBranchArrayTable();
    
    StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>[] tableMetadata = createStateMetadataTable();
    
    EnumMap<NonTerminalEnum,Integer> tableStarts =
      new EnumMap<NonTerminalEnum,Integer>(NonTerminalEnum.class);
    tableStarts.put(NonTerminalEnum.script,0);
    table = new ParserTable<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>(tableMap,branchArrayTable,tableMetadata,tableStarts,VersionEnum.values(),61,TerminalEnum.__eof__,null);
  } 

  // metadata aren't stored in local vars because it freak-out the register allocator of android
  @SuppressWarnings("unchecked")
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>[] createStateMetadataTable() {
        metadata0eq_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.eq,null);
    metadata0lpar_metadata0reduceexpr_star_4_empty = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.lpar,reduceexpr_star_4_empty);
    metadata0id_star_1_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.id_star_1,null);
    metadata0rpar_metadata0reduceexpr_call = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rpar,reduceexpr_call);
    metadata0id_metadata0reduceid_star_1_empty = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.id,reduceid_star_1_empty);
    metadata0lpar_metadata0reduceexpr_star_3_empty = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.lpar,reduceexpr_star_3_empty);
    metadata0fn_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.fn,null);
    metadata0colon_metadata0reduceexpr_star_2_empty = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.colon,reduceexpr_star_2_empty);
    metadata0lpar_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.lpar,null);
    metadata0assign_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.assign,null);
    metadata0expr_star_5_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr_star_5,null);
    metadata0while__metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.while_,null);
    metadata0mul_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.mul,null);
    metadata0sub_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.sub,null);
    metadata0lt_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.lt,null);
    metadata0add_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.add,null);
    metadata0fun_star_0_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.fun_star_0,null);
    metadata0expr_star_2_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr_star_2,null);
    metadata0ge_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.ge,null);
    metadata0div_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.div,null);
    metadata0script_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.script,null);
    metadata0rpar_metadata0reduceexpr_while = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rpar,reduceexpr_while);
    metadata0fun_metadata0reducefun_star_0_rec = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.fun,reducefun_star_0_rec);
    metadata0expr_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr,null);
    metadata0id_metadata0reduceid_star_1_rec = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.id,reduceid_star_1_rec);
    metadata0rpar_metadata0reducefun = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rpar,reducefun);
    metadata0ne_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.ne,null);
    metadata0rpar_metadata0reduceexpr_block = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rpar,reduceexpr_block);
    metadata0expr_metadata0reduceexpr_mul = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr,reduceexpr_mul);
    metadata0__eof___metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.__eof__,null);
    metadata0expr_star_3_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr_star_3,null);
    metadata0rem_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rem,null);
    metadata0le_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.le,null);
    metadata0id_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.id,null);
    metadata0gt_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.gt,null);
    metadata0expr_metadata0reduceexpr_rem = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr,reduceexpr_rem);
    metadata0null_metadata0reducefun_star_0_empty = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(null,reducefun_star_0_empty);
    metadata0integer_metadata0reduceexpr_integer = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.integer,reduceexpr_integer);
    metadata0if__metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.if_,null);
    metadata0text_metadata0reduceexpr_text = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.text,reduceexpr_text);
    metadata0expr_metadata0reduceexpr_div = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr,reduceexpr_div);
    metadata0expr_star_4_metadata0null = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithNonTerminal(NonTerminalEnum.expr_star_4,null);
    metadata0rpar_metadata0reduceexpr_if = StateMetadata.<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>createAllVersionWithTerminal(TerminalEnum.rpar,reduceexpr_if);

    return (StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum>[])new StateMetadata<?,?,?,?>[]{metadata0null_metadata0reducefun_star_0_empty,metadata0fun_star_0_metadata0null,metadata0fn_metadata0null,metadata0lpar_metadata0null,metadata0id_metadata0reduceid_star_1_empty,metadata0id_star_1_metadata0null,metadata0colon_metadata0reduceexpr_star_2_empty,metadata0expr_star_2_metadata0null,metadata0text_metadata0reduceexpr_text,metadata0integer_metadata0reduceexpr_integer,metadata0rpar_metadata0reducefun,metadata0while__metadata0null,metadata0lpar_metadata0null,metadata0lpar_metadata0reduceexpr_star_3_empty,metadata0expr_star_3_metadata0null,metadata0rpar_metadata0reduceexpr_block,metadata0id_metadata0null,metadata0assign_metadata0null,metadata0if__metadata0null,metadata0lpar_metadata0null,metadata0expr_metadata0null,metadata0mul_metadata0null,metadata0expr_metadata0reduceexpr_mul,metadata0gt_metadata0null,metadata0expr_metadata0null,metadata0add_metadata0null,metadata0expr_metadata0null,metadata0le_metadata0null,metadata0expr_metadata0null,metadata0eq_metadata0null,metadata0expr_metadata0null,metadata0ge_metadata0null,metadata0expr_metadata0null,metadata0div_metadata0null,metadata0expr_metadata0reduceexpr_div,metadata0sub_metadata0null,metadata0expr_metadata0null,metadata0lt_metadata0null,metadata0expr_metadata0null,metadata0rem_metadata0null,metadata0expr_metadata0reduceexpr_rem,metadata0ne_metadata0null,metadata0expr_metadata0null,metadata0expr_metadata0null,metadata0expr_metadata0null,metadata0rpar_metadata0reduceexpr_if,metadata0expr_metadata0null,metadata0lpar_metadata0reduceexpr_star_4_empty,metadata0expr_star_4_metadata0null,metadata0rpar_metadata0reduceexpr_call,metadata0expr_metadata0null,metadata0expr_metadata0null,metadata0expr_metadata0null,metadata0expr_star_5_metadata0null,metadata0rpar_metadata0reduceexpr_while,metadata0expr_metadata0null,metadata0expr_metadata0null,metadata0id_metadata0reduceid_star_1_rec,metadata0fun_metadata0reducefun_star_0_rec,metadata0script_metadata0null,metadata0__eof___metadata0null};
  }

  
  private int[] exprGotoes;

  private void initexprGotoes() {
    exprGotoes = 
      new int[]{-1,-1,-1,-1,-1,-1,-1,56,-1,-1,-1,-1,52,-1,51,-1,-1,46,-1,20,43,22,-1,24,-1,26,-1,28,-1,30,-1,32,-1,34,-1,36,-1,38,-1,40,-1,42,-1,44,-1,-1,-1,-1,50,-1,-1,-1,-1,55,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] id_star_1Gotoes;

  private void initid_star_1Gotoes() {
    id_star_1Gotoes = 
      new int[]{-1,-1,-1,-1,5,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] funGotoes;

  private void initfunGotoes() {
    funGotoes = 
      new int[]{-1,58,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] expr_star_5Gotoes;

  private void initexpr_star_5Gotoes() {
    expr_star_5Gotoes = 
      new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,53,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] expr_star_4Gotoes;

  private void initexpr_star_4Gotoes() {
    expr_star_4Gotoes = 
      new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,48,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] expr_star_2Gotoes;

  private void initexpr_star_2Gotoes() {
    expr_star_2Gotoes = 
      new int[]{-1,-1,-1,-1,-1,-1,7,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] expr_star_3Gotoes;

  private void initexpr_star_3Gotoes() {
    expr_star_3Gotoes = 
      new int[]{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,14,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] fun_star_0Gotoes;

  private void initfun_star_0Gotoes() {
    fun_star_0Gotoes = 
      new int[]{1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }
  
  private int[] scriptGotoes;

  private void initscriptGotoes() {
    scriptGotoes = 
      new int[]{59,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
  }

  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] textArray;
  @SuppressWarnings("unchecked")
  private void inittextArray() {
    textArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_star_2_empty,shift8,reduceexpr_text,reduceexpr_integer,branch0,branch0,shift8,reduceexpr_star_3_empty,shift8,reduceexpr_block,reduceexpr_var_access,shift8,branch0,shift8,shift8,shift8,reduceexpr_mul,shift8,reduceexpr_gt,shift8,reduceexpr_add,shift8,reduceexpr_le,shift8,reduceexpr_eq,shift8,reduceexpr_ge,shift8,reduceexpr_div,shift8,reduceexpr_sub,shift8,reduceexpr_lt,shift8,reduceexpr_rem,shift8,reduceexpr_ne,shift8,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift8,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift8,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] mulArray;
  @SuppressWarnings("unchecked")
  private void initmulArray() {
    mulArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift21,branch0,reduceexpr_mul,branch0,shift21,branch0,shift21,branch0,shift21,branch0,shift21,branch0,shift21,branch0,reduceexpr_div,branch0,shift21,branch0,shift21,branch0,reduceexpr_rem,branch0,shift21,shift21,shift21,reduceexpr_if,shift21,branch0,branch0,reduceexpr_call,shift21,shift21,shift21,branch0,reduceexpr_while,shift21,shift21,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] rparArray;
  @SuppressWarnings("unchecked")
  private void initrparArray() {
    rparArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_star_2_empty,shift10,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,reduceexpr_star_3_empty,shift15,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,branch0,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,branch0,shift45,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift49,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift54,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] colonArray;
  @SuppressWarnings("unchecked")
  private void initcolonArray() {
    colonArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,reduceid_star_1_empty,shift6,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceid_star_1_rec,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] while_Array;
  @SuppressWarnings("unchecked")
  private void initwhile_Array() {
    while_Array=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_star_2_empty,shift11,reduceexpr_text,reduceexpr_integer,branch0,branch0,shift11,reduceexpr_star_3_empty,shift11,reduceexpr_block,reduceexpr_var_access,shift11,branch0,shift11,shift11,shift11,reduceexpr_mul,shift11,reduceexpr_gt,shift11,reduceexpr_add,shift11,reduceexpr_le,shift11,reduceexpr_eq,shift11,reduceexpr_ge,shift11,reduceexpr_div,shift11,reduceexpr_sub,shift11,reduceexpr_lt,shift11,reduceexpr_rem,shift11,reduceexpr_ne,shift11,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift11,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift11,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] addArray;
  @SuppressWarnings("unchecked")
  private void initaddArray() {
    addArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift25,branch0,reduceexpr_mul,branch0,shift25,branch0,reduceexpr_add,branch0,shift25,branch0,shift25,branch0,shift25,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,shift25,branch0,reduceexpr_rem,branch0,shift25,shift25,shift25,reduceexpr_if,shift25,branch0,branch0,reduceexpr_call,shift25,shift25,shift25,branch0,reduceexpr_while,shift25,shift25,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] leArray;
  @SuppressWarnings("unchecked")
  private void initleArray() {
    leArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift27,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift27,shift27,reduceexpr_if,shift27,branch0,branch0,reduceexpr_call,shift27,shift27,shift27,branch0,reduceexpr_while,shift27,shift27,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] lparArray;
  @SuppressWarnings("unchecked")
  private void initlparArray() {
    lparArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,shift3,branch0,branch0,branch0,reduceexpr_star_2_empty,shift13,reduceexpr_text,reduceexpr_integer,branch0,shift12,shift13,reduceexpr_star_3_empty,shift13,reduceexpr_block,shift47,shift13,shift19,shift13,shift13,shift13,reduceexpr_mul,shift13,reduceexpr_gt,shift13,reduceexpr_add,shift13,reduceexpr_le,shift13,reduceexpr_eq,shift13,reduceexpr_ge,shift13,reduceexpr_div,shift13,reduceexpr_sub,shift13,reduceexpr_lt,shift13,reduceexpr_rem,shift13,reduceexpr_ne,shift13,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift13,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift13,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] __eof__Array;
  @SuppressWarnings("unchecked")
  private void init__eof__Array() {
    __eof__Array=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{reducefun_star_0_empty,reducescript,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reducefun,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reducefun_star_0_rec,accept,accept};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] idArray;
  @SuppressWarnings("unchecked")
  private void initidArray() {
    idArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,shift4,reduceid_star_1_empty,shift57,reduceexpr_star_2_empty,shift16,reduceexpr_text,reduceexpr_integer,branch0,branch0,shift16,reduceexpr_star_3_empty,shift16,reduceexpr_block,reduceexpr_var_access,shift16,branch0,shift16,shift16,shift16,reduceexpr_mul,shift16,reduceexpr_gt,shift16,reduceexpr_add,shift16,reduceexpr_le,shift16,reduceexpr_eq,shift16,reduceexpr_ge,shift16,reduceexpr_div,shift16,reduceexpr_sub,shift16,reduceexpr_lt,shift16,reduceexpr_rem,shift16,reduceexpr_ne,shift16,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift16,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift16,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,reduceid_star_1_rec,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] if_Array;
  @SuppressWarnings("unchecked")
  private void initif_Array() {
    if_Array=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_star_2_empty,shift18,reduceexpr_text,reduceexpr_integer,branch0,branch0,shift18,reduceexpr_star_3_empty,shift18,reduceexpr_block,reduceexpr_var_access,shift18,branch0,shift18,shift18,shift18,reduceexpr_mul,shift18,reduceexpr_gt,shift18,reduceexpr_add,shift18,reduceexpr_le,shift18,reduceexpr_eq,shift18,reduceexpr_ge,shift18,reduceexpr_div,shift18,reduceexpr_sub,shift18,reduceexpr_lt,shift18,reduceexpr_rem,shift18,reduceexpr_ne,shift18,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift18,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift18,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] eqArray;
  @SuppressWarnings("unchecked")
  private void initeqArray() {
    eqArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift29,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift29,shift29,reduceexpr_if,shift29,branch0,branch0,reduceexpr_call,shift29,shift29,shift29,branch0,reduceexpr_while,shift29,shift29,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] subArray;
  @SuppressWarnings("unchecked")
  private void initsubArray() {
    subArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift35,branch0,reduceexpr_mul,branch0,shift35,branch0,reduceexpr_add,branch0,shift35,branch0,shift35,branch0,shift35,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,shift35,branch0,reduceexpr_rem,branch0,shift35,shift35,shift35,reduceexpr_if,shift35,branch0,branch0,reduceexpr_call,shift35,shift35,shift35,branch0,reduceexpr_while,shift35,shift35,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] remArray;
  @SuppressWarnings("unchecked")
  private void initremArray() {
    remArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift39,branch0,reduceexpr_mul,branch0,shift39,branch0,shift39,branch0,shift39,branch0,shift39,branch0,shift39,branch0,reduceexpr_div,branch0,shift39,branch0,shift39,branch0,reduceexpr_rem,branch0,shift39,shift39,shift39,reduceexpr_if,shift39,branch0,branch0,reduceexpr_call,shift39,shift39,shift39,branch0,reduceexpr_while,shift39,shift39,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] neArray;
  @SuppressWarnings("unchecked")
  private void initneArray() {
    neArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift41,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift41,shift41,reduceexpr_if,shift41,branch0,branch0,reduceexpr_call,shift41,shift41,shift41,branch0,reduceexpr_while,shift41,shift41,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] ltArray;
  @SuppressWarnings("unchecked")
  private void initltArray() {
    ltArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift37,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift37,shift37,reduceexpr_if,shift37,branch0,branch0,reduceexpr_call,shift37,shift37,shift37,branch0,reduceexpr_while,shift37,shift37,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] gtArray;
  @SuppressWarnings("unchecked")
  private void initgtArray() {
    gtArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift23,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift23,shift23,reduceexpr_if,shift23,branch0,branch0,reduceexpr_call,shift23,shift23,shift23,branch0,reduceexpr_while,shift23,shift23,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] integerArray;
  @SuppressWarnings("unchecked")
  private void initintegerArray() {
    integerArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_star_2_empty,shift9,reduceexpr_text,reduceexpr_integer,branch0,branch0,shift9,reduceexpr_star_3_empty,shift9,reduceexpr_block,reduceexpr_var_access,shift9,branch0,shift9,shift9,shift9,reduceexpr_mul,shift9,reduceexpr_gt,shift9,reduceexpr_add,shift9,reduceexpr_le,shift9,reduceexpr_eq,shift9,reduceexpr_ge,shift9,reduceexpr_div,shift9,reduceexpr_sub,shift9,reduceexpr_lt,shift9,reduceexpr_rem,shift9,reduceexpr_ne,shift9,branch0,reduceexpr_if,reduceexpr_var_assignment,reduceexpr_star_4_empty,shift9,reduceexpr_call,reduceexpr_star_4_rec,reduceexpr_star_3_rec,reduceexpr_star_5_empty,shift9,reduceexpr_while,reduceexpr_star_5_rec,reduceexpr_star_2_rec,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] assignArray;
  @SuppressWarnings("unchecked")
  private void initassignArray() {
    assignArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,shift17,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] fnArray;
  @SuppressWarnings("unchecked")
  private void initfnArray() {
    fnArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{reducefun_star_0_empty,shift2,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reducefun,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reducefun_star_0_rec,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] geArray;
  @SuppressWarnings("unchecked")
  private void initgeArray() {
    geArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift31,branch0,reduceexpr_mul,branch0,reduceexpr_gt,branch0,reduceexpr_add,branch0,reduceexpr_le,branch0,reduceexpr_eq,branch0,reduceexpr_ge,branch0,reduceexpr_div,branch0,reduceexpr_sub,branch0,reduceexpr_lt,branch0,reduceexpr_rem,branch0,reduceexpr_ne,shift31,shift31,reduceexpr_if,shift31,branch0,branch0,reduceexpr_call,shift31,shift31,shift31,branch0,reduceexpr_while,shift31,shift31,branch0,branch0,branch0,branch0};
  }
  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] divArray;
  @SuppressWarnings("unchecked")
  private void initdivArray() {
    divArray=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{branch0,branch0,branch0,branch0,branch0,branch0,branch0,branch0,reduceexpr_text,reduceexpr_integer,branch0,branch0,branch0,branch0,branch0,reduceexpr_block,reduceexpr_var_access,branch0,branch0,branch0,shift33,branch0,reduceexpr_mul,branch0,shift33,branch0,shift33,branch0,shift33,branch0,shift33,branch0,shift33,branch0,reduceexpr_div,branch0,shift33,branch0,shift33,branch0,reduceexpr_rem,branch0,shift33,shift33,shift33,reduceexpr_if,shift33,branch0,branch0,reduceexpr_call,shift33,shift33,shift33,branch0,reduceexpr_while,shift33,shift33,branch0,branch0,branch0,branch0};
  }

  private Action<TerminalEnum,ProductionEnum,VersionEnum>[] branchArrayTable;
  @SuppressWarnings("unchecked")
  private void initBranchArrayTable() {
    branchArrayTable=(Action<TerminalEnum,ProductionEnum,VersionEnum>[])new Action<?,?,?>[]{reducefun_star_0_empty,reducescript,error0,error0,error0,error0,error0,error0,error0,error0,reducefun,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,error0,reducefun_star_0_rec,exit,exit};
  }

  private final ParserTable<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> table;
  
  public static final ParserTable<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> createTable() {
    return new ParserDataTable().table;
  }

  private final AcceptAction<TerminalEnum,ProductionEnum,VersionEnum> accept;
  private final ExitAction<TerminalEnum,ProductionEnum,VersionEnum> exit;

  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_5_rec;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reducescript;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_var_assignment;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_div;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_integer;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_call;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_4_rec;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_if;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reducefun_star_0_rec;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_le;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_text;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reducefun;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_2_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_rem;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_add;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceid_star_1_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_5_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_ne;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_sub;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_3_rec;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_gt;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reducefun_star_0_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_var_access;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceid_star_1_rec;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_mul;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_lt;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_4_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_3_empty;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_block;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_eq;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_while;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_ge;
  private final ReduceAction<TerminalEnum,ProductionEnum,VersionEnum> reduceexpr_star_2_rec;

  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift29;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift9;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift47;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift17;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift45;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift21;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift13;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift2;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift27;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift23;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift6;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift12;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift35;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift16;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift19;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift41;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift3;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift31;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift4;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift15;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift57;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift37;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift54;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift18;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift39;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift25;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift8;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift49;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift10;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift11;
  private final ShiftAction<TerminalEnum,ProductionEnum,VersionEnum> shift33;


  private final ErrorAction<TerminalEnum,ProductionEnum,VersionEnum> error0;

  private final BranchAction<TerminalEnum,ProductionEnum,VersionEnum> branch0;


  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0eq_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0lpar_metadata0reduceexpr_star_4_empty;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0id_star_1_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rpar_metadata0reduceexpr_call;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0id_metadata0reduceid_star_1_empty;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0lpar_metadata0reduceexpr_star_3_empty;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0fn_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0colon_metadata0reduceexpr_star_2_empty;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0lpar_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0assign_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_star_5_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0while__metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0mul_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0sub_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0lt_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0add_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0fun_star_0_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_star_2_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0ge_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0div_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0script_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rpar_metadata0reduceexpr_while;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0fun_metadata0reducefun_star_0_rec;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0id_metadata0reduceid_star_1_rec;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rpar_metadata0reducefun;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0ne_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rpar_metadata0reduceexpr_block;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_metadata0reduceexpr_mul;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0__eof___metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_star_3_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rem_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0le_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0id_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0gt_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_metadata0reduceexpr_rem;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0null_metadata0reducefun_star_0_empty;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0integer_metadata0reduceexpr_integer;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0if__metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0text_metadata0reduceexpr_text;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_metadata0reduceexpr_div;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0expr_star_4_metadata0null;
  private StateMetadata<TerminalEnum,NonTerminalEnum,ProductionEnum,VersionEnum> metadata0rpar_metadata0reduceexpr_if;
}
