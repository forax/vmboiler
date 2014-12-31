package com.github.forax.vmboiler.sample.script.lexer;

import com.github.forax.vmboiler.sample.script.lexer.RuleEnum;
import fr.umlv.tatoo.runtime.lexer.LexerTable;
import fr.umlv.tatoo.runtime.lexer.rules.RuleData;
import fr.umlv.tatoo.runtime.regex.CharRegexTable;
import java.util.EnumMap;
/** 
 *  This class is generated - please do not edit it 
 */
public class LexerDataTable {

  public static LexerTable<RuleEnum> createTable() {
    return new LexerDataTable().table;
  }

  private LexerDataTable() {
    initassignMainAccepts();
    initassignMainTransitions();
    CharRegexTable assignMain = new CharRegexTable(1, assignMainTransitions, assignMainAccepts);
    RuleData assign = new RuleData(assignMain, null, 0, false);
    initcolonMainAccepts();
    initcolonMainTransitions();
    CharRegexTable colonMain = new CharRegexTable(1, colonMainTransitions, colonMainAccepts);
    RuleData colon = new RuleData(colonMain, null, 1, false);
    initeolMainAccepts();
    initeolMainTransitions();
    CharRegexTable eolMain = new CharRegexTable(1, eolMainTransitions, eolMainAccepts);
    RuleData eol = new RuleData(eolMain, null, 2, false);
    initlparMainAccepts();
    initlparMainTransitions();
    CharRegexTable lparMain = new CharRegexTable(1, lparMainTransitions, lparMainAccepts);
    RuleData lpar = new RuleData(lparMain, null, 3, false);
    initrparMainAccepts();
    initrparMainTransitions();
    CharRegexTable rparMain = new CharRegexTable(1, rparMainTransitions, rparMainAccepts);
    RuleData rpar = new RuleData(rparMain, null, 4, false);
    initaddMainAccepts();
    initaddMainTransitions();
    CharRegexTable addMain = new CharRegexTable(1, addMainTransitions, addMainAccepts);
    RuleData add = new RuleData(addMain, null, 5, false);
    initsubMainAccepts();
    initsubMainTransitions();
    CharRegexTable subMain = new CharRegexTable(1, subMainTransitions, subMainAccepts);
    RuleData sub = new RuleData(subMain, null, 6, false);
    initmulMainAccepts();
    initmulMainTransitions();
    CharRegexTable mulMain = new CharRegexTable(1, mulMainTransitions, mulMainAccepts);
    RuleData mul = new RuleData(mulMain, null, 7, false);
    initdivMainAccepts();
    initdivMainTransitions();
    CharRegexTable divMain = new CharRegexTable(1, divMainTransitions, divMainAccepts);
    RuleData div = new RuleData(divMain, null, 8, false);
    initremMainAccepts();
    initremMainTransitions();
    CharRegexTable remMain = new CharRegexTable(1, remMainTransitions, remMainAccepts);
    RuleData rem = new RuleData(remMain, null, 9, false);
    initeqMainAccepts();
    initeqMainTransitions();
    CharRegexTable eqMain = new CharRegexTable(1, eqMainTransitions, eqMainAccepts);
    RuleData eq = new RuleData(eqMain, null, 10, false);
    initneMainAccepts();
    initneMainTransitions();
    CharRegexTable neMain = new CharRegexTable(1, neMainTransitions, neMainAccepts);
    RuleData ne = new RuleData(neMain, null, 11, false);
    initltMainAccepts();
    initltMainTransitions();
    CharRegexTable ltMain = new CharRegexTable(1, ltMainTransitions, ltMainAccepts);
    RuleData lt = new RuleData(ltMain, null, 12, false);
    initleMainAccepts();
    initleMainTransitions();
    CharRegexTable leMain = new CharRegexTable(2, leMainTransitions, leMainAccepts);
    RuleData le = new RuleData(leMain, null, 13, false);
    initgtMainAccepts();
    initgtMainTransitions();
    CharRegexTable gtMain = new CharRegexTable(1, gtMainTransitions, gtMainAccepts);
    RuleData gt = new RuleData(gtMain, null, 14, false);
    initgeMainAccepts();
    initgeMainTransitions();
    CharRegexTable geMain = new CharRegexTable(2, geMainTransitions, geMainAccepts);
    RuleData ge = new RuleData(geMain, null, 15, false);
    initfnMainAccepts();
    initfnMainTransitions();
    CharRegexTable fnMain = new CharRegexTable(1, fnMainTransitions, fnMainAccepts);
    RuleData fn = new RuleData(fnMain, null, 16, false);
    initif_MainAccepts();
    initif_MainTransitions();
    CharRegexTable if_Main = new CharRegexTable(1, if_MainTransitions, if_MainAccepts);
    RuleData if_ = new RuleData(if_Main, null, 17, false);
    initwhile_MainAccepts();
    initwhile_MainTransitions();
    CharRegexTable while_Main = new CharRegexTable(2, while_MainTransitions, while_MainAccepts);
    RuleData while_ = new RuleData(while_Main, null, 18, false);
    inittextMainAccepts();
    inittextMainTransitions();
    CharRegexTable textMain = new CharRegexTable(1, textMainTransitions, textMainAccepts);
    RuleData text = new RuleData(textMain, null, 19, false);
    initintegerMainAccepts();
    initintegerMainTransitions();
    CharRegexTable integerMain = new CharRegexTable(1, integerMainTransitions, integerMainAccepts);
    RuleData integer = new RuleData(integerMain, null, 20, false);
    initidMainAccepts();
    initidMainTransitions();
    CharRegexTable idMain = new CharRegexTable(1, idMainTransitions, idMainAccepts);
    RuleData id = new RuleData(idMain, null, 21, false);
    initspaceMainAccepts();
    initspaceMainTransitions();
    CharRegexTable spaceMain = new CharRegexTable(1, spaceMainTransitions, spaceMainAccepts);
    RuleData space = new RuleData(spaceMain, null, 22, false);
    initcommentMainAccepts();
    initcommentMainTransitions();
    CharRegexTable commentMain = new CharRegexTable(1, commentMainTransitions, commentMainAccepts);
    RuleData comment = new RuleData(commentMain, null, 23, false);

    EnumMap<RuleEnum,RuleData> datas = new EnumMap<RuleEnum,RuleData>(RuleEnum.class);
    datas.put(RuleEnum.assign, assign);
    datas.put(RuleEnum.colon, colon);
    datas.put(RuleEnum.eol, eol);
    datas.put(RuleEnum.lpar, lpar);
    datas.put(RuleEnum.rpar, rpar);
    datas.put(RuleEnum.add, add);
    datas.put(RuleEnum.sub, sub);
    datas.put(RuleEnum.mul, mul);
    datas.put(RuleEnum.div, div);
    datas.put(RuleEnum.rem, rem);
    datas.put(RuleEnum.eq, eq);
    datas.put(RuleEnum.ne, ne);
    datas.put(RuleEnum.lt, lt);
    datas.put(RuleEnum.le, le);
    datas.put(RuleEnum.gt, gt);
    datas.put(RuleEnum.ge, ge);
    datas.put(RuleEnum.fn, fn);
    datas.put(RuleEnum.if_, if_);
    datas.put(RuleEnum.while_, while_);
    datas.put(RuleEnum.text, text);
    datas.put(RuleEnum.integer, integer);
    datas.put(RuleEnum.id, id);
    datas.put(RuleEnum.space, space);
    datas.put(RuleEnum.comment, comment);
    table = new LexerTable<RuleEnum>(datas);
  }

  
  private boolean[] assignMainAccepts;
  private void initassignMainAccepts() {
    assignMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] assignMainTransitions;
  private void initassignMainTransitions() {
    assignMainTransitions = new int[][] {{0,-1},{0,-1,61,0,62,-1}};
  }
  
  private boolean[] colonMainAccepts;
  private void initcolonMainAccepts() {
    colonMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] colonMainTransitions;
  private void initcolonMainTransitions() {
    colonMainTransitions = new int[][] {{0,-1},{0,-1,58,0,59,-1}};
  }
  
  private boolean[] eolMainAccepts;
  private void initeolMainAccepts() {
    eolMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] eolMainTransitions;
  private void initeolMainTransitions() {
    eolMainTransitions = new int[][] {{0,-1},{0,-1,10,0,11,-1}};
  }
  
  private boolean[] lparMainAccepts;
  private void initlparMainAccepts() {
    lparMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] lparMainTransitions;
  private void initlparMainTransitions() {
    lparMainTransitions = new int[][] {{0,-1},{0,-1,40,0,41,-1}};
  }
  
  private boolean[] rparMainAccepts;
  private void initrparMainAccepts() {
    rparMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] rparMainTransitions;
  private void initrparMainTransitions() {
    rparMainTransitions = new int[][] {{0,-1},{0,-1,41,0,42,-1}};
  }
  
  private boolean[] addMainAccepts;
  private void initaddMainAccepts() {
    addMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] addMainTransitions;
  private void initaddMainTransitions() {
    addMainTransitions = new int[][] {{0,-1},{0,-1,43,0,44,-1}};
  }
  
  private boolean[] subMainAccepts;
  private void initsubMainAccepts() {
    subMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] subMainTransitions;
  private void initsubMainTransitions() {
    subMainTransitions = new int[][] {{0,-1},{0,-1,45,0,46,-1}};
  }
  
  private boolean[] mulMainAccepts;
  private void initmulMainAccepts() {
    mulMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] mulMainTransitions;
  private void initmulMainTransitions() {
    mulMainTransitions = new int[][] {{0,-1},{0,-1,42,0,43,-1}};
  }
  
  private boolean[] divMainAccepts;
  private void initdivMainAccepts() {
    divMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] divMainTransitions;
  private void initdivMainTransitions() {
    divMainTransitions = new int[][] {{0,-1},{0,-1,47,0,48,-1}};
  }
  
  private boolean[] remMainAccepts;
  private void initremMainAccepts() {
    remMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] remMainTransitions;
  private void initremMainTransitions() {
    remMainTransitions = new int[][] {{0,-1},{0,-1,37,0,38,-1}};
  }
  
  private boolean[] eqMainAccepts;
  private void initeqMainAccepts() {
    eqMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] eqMainTransitions;
  private void initeqMainTransitions() {
    eqMainTransitions = new int[][] {{0,-1},{0,-1,61,2,62,-1},{0,-1,61,0,62,-1}};
  }
  
  private boolean[] neMainAccepts;
  private void initneMainAccepts() {
    neMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] neMainTransitions;
  private void initneMainTransitions() {
    neMainTransitions = new int[][] {{0,-1},{0,-1,33,2,34,-1},{0,-1,61,0,62,-1}};
  }
  
  private boolean[] ltMainAccepts;
  private void initltMainAccepts() {
    ltMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] ltMainTransitions;
  private void initltMainTransitions() {
    ltMainTransitions = new int[][] {{0,-1},{0,-1,60,0,61,-1}};
  }
  
  private boolean[] leMainAccepts;
  private void initleMainAccepts() {
    leMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] leMainTransitions;
  private void initleMainTransitions() {
    leMainTransitions = new int[][] {{0,-1},{0,-1,61,0,62,-1},{0,-1,60,1,61,-1}};
  }
  
  private boolean[] gtMainAccepts;
  private void initgtMainAccepts() {
    gtMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] gtMainTransitions;
  private void initgtMainTransitions() {
    gtMainTransitions = new int[][] {{0,-1},{0,-1,62,0,63,-1}};
  }
  
  private boolean[] geMainAccepts;
  private void initgeMainAccepts() {
    geMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] geMainTransitions;
  private void initgeMainTransitions() {
    geMainTransitions = new int[][] {{0,-1},{0,-1,61,0,62,-1},{0,-1,62,1,63,-1}};
  }
  
  private boolean[] fnMainAccepts;
  private void initfnMainAccepts() {
    fnMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] fnMainTransitions;
  private void initfnMainTransitions() {
    fnMainTransitions = new int[][] {{0,-1},{0,-1,102,2,103,-1},{0,-1,110,0,111,-1}};
  }
  
  private boolean[] if_MainAccepts;
  private void initif_MainAccepts() {
    if_MainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] if_MainTransitions;
  private void initif_MainTransitions() {
    if_MainTransitions = new int[][] {{0,-1},{0,-1,105,2,106,-1},{0,-1,102,0,103,-1}};
  }
  
  private boolean[] while_MainAccepts;
  private void initwhile_MainAccepts() {
    while_MainAccepts = new boolean[] {true,false,false,false,false,false};
  }
    
  private int[][] while_MainTransitions;
  private void initwhile_MainTransitions() {
    while_MainTransitions = new int[][] {{0,-1},{0,-1,105,4,106,-1},{0,-1,119,5,120,-1},{0,-1,101,0,102,-1},{0,-1,108,3,109,-1},{0,-1,104,1,105,-1}};
  }
  
  private boolean[] textMainAccepts;
  private void inittextMainAccepts() {
    textMainAccepts = new boolean[] {true,false,false};
  }
    
  private int[][] textMainTransitions;
  private void inittextMainTransitions() {
    textMainTransitions = new int[][] {{0,-1},{0,-1,39,2,40,-1},{0,2,39,0,40,2}};
  }
  
  private boolean[] integerMainAccepts;
  private void initintegerMainAccepts() {
    integerMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] integerMainTransitions;
  private void initintegerMainTransitions() {
    integerMainTransitions = new int[][] {{0,-1,48,0,58,-1},{0,-1,48,0,58,-1}};
  }
  
  private boolean[] idMainAccepts;
  private void initidMainAccepts() {
    idMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] idMainTransitions;
  private void initidMainTransitions() {
    idMainTransitions = new int[][] {{0,0,9,-1,11,0,13,-1,14,0,32,-1,33,0,40,-1,42,0,44,-1,45,0,58,-1,60,0,61,-1,62,0},{0,0,9,-1,11,0,13,-1,14,0,32,-1,33,0,40,-1,42,0,44,-1,45,0,58,-1,60,0,61,-1,62,0}};
  }
  
  private boolean[] spaceMainAccepts;
  private void initspaceMainAccepts() {
    spaceMainAccepts = new boolean[] {true,false};
  }
    
  private int[][] spaceMainTransitions;
  private void initspaceMainTransitions() {
    spaceMainTransitions = new int[][] {{0,-1,9,0,11,-1,13,0,14,-1,32,0,33,-1,44,0,45,-1,59,0,60,-1},{0,-1,9,0,11,-1,13,0,14,-1,32,0,33,-1,44,0,45,-1,59,0,60,-1}};
  }
  
  private boolean[] commentMainAccepts;
  private void initcommentMainAccepts() {
    commentMainAccepts = new boolean[] {true,false,false,false};
  }
    
  private int[][] commentMainTransitions;
  private void initcommentMainTransitions() {
    commentMainTransitions = new int[][] {{0,-1},{0,-1,35,3,36,-1},{0,-1,10,0,11,-1},{0,3,10,0,11,3,13,2,14,3}};
  }
  
  private final LexerTable<RuleEnum> table;
}
