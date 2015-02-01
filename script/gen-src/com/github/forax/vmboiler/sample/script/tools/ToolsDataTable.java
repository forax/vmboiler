package com.github.forax.vmboiler.sample.script.tools;

import fr.umlv.tatoo.runtime.tools.ToolsTable;

import java.util.EnumMap;
import java.util.EnumSet;

import com.github.forax.vmboiler.sample.script.lexer.RuleEnum;
import com.github.forax.vmboiler.sample.script.parser.TerminalEnum;

public class ToolsDataTable {
  public static ToolsTable<RuleEnum,TerminalEnum> createToolsTable() {
      EnumSet<RuleEnum> spawns = EnumSet.of(RuleEnum.assign,RuleEnum.mul,RuleEnum.ne,RuleEnum.if_,RuleEnum.gt,RuleEnum.fn,RuleEnum.lpar,RuleEnum.add,RuleEnum.lt,RuleEnum.colon,RuleEnum.div,RuleEnum.ge,RuleEnum.integer,RuleEnum.rpar,RuleEnum.rem,RuleEnum.le,RuleEnum.id,RuleEnum.comment,RuleEnum.eol,RuleEnum.text,RuleEnum.eq,RuleEnum.sub,RuleEnum.while_);
      EnumSet<RuleEnum> discards = EnumSet.allOf(RuleEnum.class);
      EnumMap<RuleEnum,TerminalEnum> terminal = new EnumMap<RuleEnum,TerminalEnum>(RuleEnum.class);
              terminal.put(RuleEnum.assign,TerminalEnum.assign);
              terminal.put(RuleEnum.mul,TerminalEnum.mul);
              terminal.put(RuleEnum.ne,TerminalEnum.ne);
              terminal.put(RuleEnum.if_,TerminalEnum.if_);
              terminal.put(RuleEnum.gt,TerminalEnum.gt);
              terminal.put(RuleEnum.fn,TerminalEnum.fn);
              terminal.put(RuleEnum.lpar,TerminalEnum.lpar);
              terminal.put(RuleEnum.add,TerminalEnum.add);
              terminal.put(RuleEnum.lt,TerminalEnum.lt);
              terminal.put(RuleEnum.colon,TerminalEnum.colon);
              terminal.put(RuleEnum.div,TerminalEnum.div);
              terminal.put(RuleEnum.ge,TerminalEnum.ge);
              terminal.put(RuleEnum.integer,TerminalEnum.integer);
              terminal.put(RuleEnum.rpar,TerminalEnum.rpar);
              terminal.put(RuleEnum.rem,TerminalEnum.rem);
              terminal.put(RuleEnum.le,TerminalEnum.le);
              terminal.put(RuleEnum.id,TerminalEnum.id);
              terminal.put(RuleEnum.eol,TerminalEnum.eol);
              terminal.put(RuleEnum.text,TerminalEnum.text);
              terminal.put(RuleEnum.eq,TerminalEnum.eq);
              terminal.put(RuleEnum.sub,TerminalEnum.sub);
              terminal.put(RuleEnum.while_,TerminalEnum.while_);
            EnumSet<RuleEnum> unconditionals = EnumSet.of(RuleEnum.comment,RuleEnum.space);
      return new ToolsTable<RuleEnum,TerminalEnum>(spawns,discards,unconditionals,terminal);
  }
}