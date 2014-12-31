package com.github.forax.vmboiler;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DCONST_0;
import static org.objectweb.asm.Opcodes.FCONST_0;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ACMPNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LCONST_0;
import static org.objectweb.asm.Type.BOOLEAN;
import static org.objectweb.asm.Type.BYTE;
import static org.objectweb.asm.Type.CHAR;
import static org.objectweb.asm.Type.DOUBLE;
import static org.objectweb.asm.Type.FLOAT;
import static org.objectweb.asm.Type.INT;
import static org.objectweb.asm.Type.LONG;
import static org.objectweb.asm.Type.SHORT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.github.forax.vmboiler.rt.OptimisticError;
import com.github.forax.vmboiler.rt.RT;

public final class CodeGen {
  private final MethodVisitor mv;
  private final VarFactory varFactory;
  private final Type returnType;
  private final ArrayList<Var> parameters;
  private int slotCount;
  private Runnable sideExit = () -> { /* empty */ };
  
  public interface VarFactory {
    public Var create(Type type, String name, int slot);
  }
  
  public CodeGen(MethodVisitor mv, Type returnType, Type[] parameterTypes, String[] parameterNames, VarFactory varFactory) {
    this.mv = mv;
    this.varFactory = varFactory;
    this.returnType = returnType;
    this.parameters = gatherParameters(parameterTypes, parameterNames);
  }
  
  public CodeGen(MethodVisitor mv, Type returnType, Type[] parameterTypes, String[] parameterNames) {
    this(mv, returnType, parameterTypes, parameterNames, Var::new);
  }
  
  private ArrayList<Var> gatherParameters(Type[] parameterTypes, String[] parameterNames) {
    return IntStream.range(0, parameterTypes.length)
        .mapToObj(i -> createVar(parameterTypes[i], parameterNames[i]))
        .peek(var -> {
          if (var.type().isMixed()) {
            throw new IllegalArgumentException("parameter type can not be a mixed type " + var);
          }
        })
        .collect(Collectors.toCollection(ArrayList::new));
  }
  
  public MethodVisitor methodVisitor() {
    return mv;
  }
  
  public Type returnType() {
    return returnType;
  }
  public int parameterCount() {
    return parameters.size();
  }
  public Var parameterVar(int index) {
    return parameters.get(index);
  }
  public List<Var> parameterVars() {
    return Collections.unmodifiableList(parameters);
  }
  
  public Var createVar(Type type, String name) {
    Var var = varFactory.create(type, name, slotCount);
    slotCount += type.asmType().getSize() + (type.isMixed()? 1: 0);
    return var;
  }
  
  public void move(Var var, Value value) {
    MethodVisitor mv = this.mv;
    Type varType = var.type();
    Type valueType = value.type();
    if (varType == valueType) {
      value.loadAll(mv);
      var.storeAll(mv);
      return;
    }
    if (!varType.isMixed() || valueType.isMixed() || !varType.asmType().equals(valueType.asmType())) {
      throw new IllegalArgumentException("invalid convertion " + varType + " " + valueType);
    }
    value.loadAll(mv);
    loadNone(mv);
    var.storeAll(mv);
  }
  
  public void call(Handle bsm, Object[] bsmArgs, Object deoptArgsCallback, Object deoptReturnCallback,
      Var result, String name, Value... values) {
    MethodVisitor mv = this.mv;
    Label label = null;
    for(Value v: values) {
      if (v.type().isMixed()) {
        if (label == null) {
          label = new Label();
        }
        loadNone(mv);
        mv.visitVarInsn(ALOAD, ((Var)v).slot());
        mv.visitJumpInsn(IF_ACMPNE, label);
      }
    }
    
    StringBuilder desc = new StringBuilder().append('(');
    for(Value v: values) {
      v.loadPrimitive(mv);
      desc.append(v.type().asmType().getDescriptor());
    }
    desc.append(')').append(result.type().asmType());
    
    Label start = new Label();
    Label end = new Label();
    Label handler = new Label();
    Label sideExitBackLabel = new Label();
    boolean mixed = result.type().isMixed();
    if (mixed) {
      mv.visitTryCatchBlock(start, end, handler, OPTIMISTIC_ERROR);
      mv.visitLabel(start);
    }
    mv.visitInvokeDynamicInsn(name, desc.toString(), bsm, bsmArgs);
    if (mixed) {
      mv.visitLabel(end);
      result.storePrimitive(mv);
      loadNone(mv);
      mv.visitVarInsn(ASTORE, result.slot());
      Runnable previousSideExit = sideExit;
      sideExit = () -> {
        previousSideExit.run();
        mv.visitLabel(handler);
        mv.visitInvokeDynamicInsn(name, "(L" + OPTIMISTIC_ERROR + ";)Ljava/lang/Object;",
            BSM_OPTIMISTIC_FAILURE, deoptReturnCallback);
        mv.visitVarInsn(ASTORE, result.slot());
        loadZero(mv, result.type());
        result.storePrimitive(mv);
        mv.visitJumpInsn(GOTO, sideExitBackLabel);
      };
    } else {
      result.storePrimitive(mv);
    }
    
    mv.visitLabel(sideExitBackLabel);
    
    if (label != null) {
      callDeopt(label, sideExitBackLabel, handler, bsm, bsmArgs, deoptArgsCallback, result, name, values);
    }
  }
  
  private void callDeopt(Label sideExitStart, Label sideExitBackLabel, Label handler,
      Handle bsm, Object[] bsmArgs, Object deoptArgsCallback,
      Var result, String name, Value[] values) {
    MethodVisitor mv = this.mv;
    Runnable previousSideExit = sideExit;
    sideExit = () -> {
      previousSideExit.run();
      mv.visitLabel(sideExitStart);
      StringBuilder desc2 = new StringBuilder().append('(');
      StringBuilder mixedDesc = new StringBuilder();
      for(Value v: values) {
        v.loadAll(mv);
        Type type = v.type();
        desc2.append(type.asmType());
        if (type.isMixed()) {
          desc2.append("Ljava/lang/Object;");
          mixedDesc.append('M');
        } else {
          mixedDesc.append('.');
        }
      }
      desc2.append(')').append(result.type().asmType());
      Label start = new Label();
      Label end = new Label();
      boolean mixed = result.type().isMixed();
      if (mixed) {
        mv.visitTryCatchBlock(start, end, handler, OPTIMISTIC_ERROR);
        mv.visitLabel(start);
      }
      mv.visitInvokeDynamicInsn(name, desc2.toString(), BSM,
          prepend(bsmArgs, bsm, deoptArgsCallback, mixedDesc.toString()));
      if (mixed) {
        mv.visitLabel(end);
        result.storePrimitive(mv);
        loadNone(mv);
        mv.visitVarInsn(ASTORE, result.slot());
      } else {
        result.storePrimitive(mv);
      }
      mv.visitJumpInsn(GOTO, sideExitBackLabel);
    };
  }

  private static Object[] prepend(Object[] array, Object o1, Object o2, Object o3) {
    int length = array.length;
    Object[] newArray = new Object[length + 3];
    newArray[0] = o1;
    newArray[1] = o2;
    newArray[2] = o3;
    System.arraycopy(array, 0, newArray, 3, length);
    return newArray;
  }
  
  
  
  public void ret(Value value) {
    Type valueType = value.type();
    if (returnType != valueType && (!returnType.isMixed() || valueType.isMixed() || !returnType.asmType().equals(valueType.asmType()))) {
      throw new IllegalArgumentException("invalid convertion " + returnType + " " + valueType);
    }
    
    MethodVisitor mv = this.mv;
    Type type = value.type();
    if (!valueType.isMixed()) {
      value.loadPrimitive(mv);
      mv.visitInsn(type.asmType().getOpcode(IRETURN));
      return;
    }
    Var var = (Var)value;
    int slot = var.slot();
    mv.visitVarInsn(ALOAD, slot);
    CodeGen.loadNone(mv);
    Label endLabel = new Label();
    mv.visitJumpInsn(IF_ACMPNE, endLabel);
    mv.visitVarInsn(type.asmType().getOpcode(ILOAD), slot + 1);
    mv.visitInsn(type.asmType().getOpcode(IRETURN));
    mv.visitLabel(endLabel);
    mv.visitVarInsn(ALOAD, slot);
    CodeGen.newOptimisticError(mv);
    mv.visitInsn(ATHROW);
  }

  
  public void label(Label label) {
    mv.visitLabel(label);
  }
  public void jump(Label label) {
    mv.visitJumpInsn(GOTO, label);
  }
  public void jumpIfTrue(Value value, Label label) {
    if (value.type().asmType().getSort() != BOOLEAN) {
      throw new IllegalArgumentException("value.type must be a boolean");
    }
    MethodVisitor mv = this.mv;
    value.loadPrimitive(mv);
    mv.visitJumpInsn(IFNE, label);
  }
  public void jumpIfFalse(Value value, Label label) {
    if (value.type().asmType().getSort() != BOOLEAN) {
      throw new IllegalArgumentException("value.type must be a boolean");
    }
    MethodVisitor mv = this.mv;
    value.loadPrimitive(mv);
    mv.visitJumpInsn(IFEQ, label);
  }
  
  //public void jumpIfNull(Value value, Label label);
  //public void jumpIfNonNull(Value value, Label label);
  
  
  public void end() {
    sideExit.run();
  }
  
  private static final String RT = RT.class.getName().replace('.', '/');
  private static final String OPTIMISTIC_ERROR = OptimisticError.class.getName().replace('.', '/');
  private static final Handle BSM = new Handle(H_INVOKESTATIC, RT, "bsm",
      "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;");
  private static final Handle BSM_OPTIMISTIC_FAILURE = new Handle(H_INVOKESTATIC, RT, "bsm_optimistic_failure",
      "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;)Ljava/lang/invoke/CallSite;");
  
  private static void newOptimisticError(MethodVisitor mv) {
    mv.visitMethodInsn(INVOKESTATIC, OPTIMISTIC_ERROR, "newOptimisticError",
        "(Ljava/lang/Object;)L" + OPTIMISTIC_ERROR + ';', false);
  }
  
  private static void loadNone(MethodVisitor mv) {
    mv.visitFieldInsn(GETSTATIC, RT, "NONE", "Ljava/lang/Object;");
  }
  
  private static void loadZero(MethodVisitor mv, Type type) {
    switch(type.asmType().getSort()) {
    case BOOLEAN:
    case BYTE:
    case CHAR:
    case SHORT:
    case INT:
      mv.visitInsn(ICONST_0);
      return;
    case LONG:
      mv.visitInsn(LCONST_0);
      return;
    case FLOAT:
      mv.visitInsn(FCONST_0);
      return;
    case DOUBLE:
      mv.visitInsn(DCONST_0);
      return;
    default:
      throw new AssertionError("type " + type + " is mixed with a non primitive");
    }
  }
}
