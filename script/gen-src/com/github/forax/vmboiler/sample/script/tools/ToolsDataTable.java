package com.github.forax.vmboiler.sample.script.tools;

import fr.umlv.tatoo.runtime.tools.ToolsTable;

import java.util.EnumMap;
import java.util.EnumSet;

import com.github.forax.vmboiler.sample.script.lexer.RuleEnum;
import com.github.forax.vmboiler.sample.script.parser.TerminalEnum;

public class ToolsDataTable {
  public static ToolsTable<RuleEnum,TerminalEnum> createToolsTable() {
      EnumSet<RuleEnum> spawns = EnumSet.of(RuleEnum.if_,RuleEnum.lt,RuleEnum.div,RuleEnum.add,RuleEnum.text,RuleEnum.ge,RuleEnum.while_,RuleEnum.id,RuleEnum.ne,RuleEnum.fn,RuleEnum.lpar,RuleEnum.rem,RuleEnum.eq,RuleEnum.mul,RuleEnum.rpar,RuleEnum.comment,RuleEnum.le,RuleEnum.sub,RuleEnum.colon,RuleEnum.gt,RuleEnum.eol,RuleEnum.integer,RuleEnum.assign);
      EnumSet<RuleEnum> discards = EnumSet.allOf(RuleEnum.class);
      EnumMap<RuleEnum,TerminalEnum> terminal = new EnumMap<RuleEnum,TerminalEnum>(RuleEnum.class);
              terminal.put(RuleEnum.if_,TerminalEnum.if_);
              terminal.put(RuleEnum.lt,TerminalEnum.lt);
              terminal.put(RuleEnum.div,TerminalEnum.div);
              terminal.put(RuleEnum.add,TerminalEnum.add);
              terminal.put(RuleEnum.text,TerminalEnum.text);
              terminal.put(RuleEnum.ge,TerminalEnum.ge);
              terminal.put(RuleEnum.while_,TerminalEnum.while_);
              terminal.put(RuleEnum.id,TerminalEnum.id);
              terminal.put(RuleEnum.ne,TerminalEnum.ne);
              terminal.put(RuleEnum.fn,TerminalEnum.fn);
              terminal.put(RuleEnum.lpar,TerminalEnum.lpar);
              terminal.put(RuleEnum.rem,TerminalEnum.rem);
              terminal.put(RuleEnum.eq,TerminalEnum.eq);
              terminal.put(RuleEnum.mul,TerminalEnum.mul);
              terminal.put(RuleEnum.rpar,TerminalEnum.rpar);
              terminal.put(RuleEnum.le,TerminalEnum.le);
              terminal.put(RuleEnum.sub,TerminalEnum.sub);
              terminal.put(RuleEnum.colon,TerminalEnum.colon);
              terminal.put(RuleEnum.gt,TerminalEnum.gt);
              terminal.put(RuleEnum.eol,TerminalEnum.eol);
              terminal.put(RuleEnum.integer,TerminalEnum.integer);
              terminal.put(RuleEnum.assign,TerminalEnum.assign);
            EnumSet<RuleEnum> unconditionals = EnumSet.of(RuleEnum.space,RuleEnum.comment);
      return new ToolsTable<RuleEnum,TerminalEnum>(spawns,discards,unconditionals,terminal);
  }
}