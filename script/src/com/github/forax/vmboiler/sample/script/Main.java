package com.github.forax.vmboiler.sample.script;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class Main {
  public static void main(String[] args) throws Throwable {
    Reader reader;
    if (args.length>0) {
      reader = new FileReader(args[0]);
    } else {
      reader = new InputStreamReader(System.in);
    }
    Script script = Parser.parse(reader);
    Linker linker = new Linker(script);
    MethodHandle main = linker.getCallSite("main", MethodType.methodType(Object.class)).getTarget();
    main.invoke();
  }
}
