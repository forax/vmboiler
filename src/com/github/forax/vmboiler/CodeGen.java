package com.github.forax.vmboiler;

import static com.github.forax.vmboiler.Type.VM_BOOLEAN;
import static com.github.forax.vmboiler.Type.VM_BYTE;
import static com.github.forax.vmboiler.Type.VM_CHAR;
import static com.github.forax.vmboiler.Type.VM_DOUBLE;
import static com.github.forax.vmboiler.Type.VM_FLOAT;
import static com.github.forax.vmboiler.Type.VM_INT;
import static com.github.forax.vmboiler.Type.VM_LONG;
import static com.github.forax.vmboiler.Type.VM_SHORT;
import static com.github.forax.vmboiler.Value.loadOpcode;
import static com.github.forax.vmboiler.Value.returnOpcode;
import static com.github.forax.vmboiler.Value.size;
import static org.objectweb.asm.Opcodes.*;

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

/**
 * Wrapper on top of the ASM MethodVisitor allowing to generate code that is optimistically typed.
 * 
 * Type
 * {@link Type}
 * 
 * Values
 * {@link Value}
 * {@link Constant}
 * {@link #createVar(Type, String)}
 * 
 * Operations
 * {@link #ret(Value)}
 * {@link #move(Var, Value)}
 * {@link #call(Handle, Object[], Object, Object, Var, String, Value...)}
 * 
 * {@link #label(Label)}
 * {@link #jump(Label)}
 * {@link #jumpIfTrue(Value, Label)}
 * {@link #jumpIfFalse(Value, Label)}
 * 
 * End
 * The method {@link #end()} must be called after all instructions are generated.
 * By default CodeGen delay the generation of bytecodes that handles deoptimization at the end of the method.
 * If you do not want this behavior, you can call {@link #end()} after any operations.
 */
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
    slotCount += size(type.vmType()) + (type.isMixed()? 1: 0);
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
    if (!varType.isMixed() || valueType.isMixed() || varType.vmType() != valueType.vmType()) {
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
      desc.append(v.type().vmType());
    }
    desc.append(')').append(result.type().vmType());
    
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
        desc2.append(type.vmType());
        if (type.isMixed()) {
          desc2.append("Ljava/lang/Object;");
          mixedDesc.append('M');
        } else {
          mixedDesc.append('.');
        }
      }
      desc2.append(')').append(result.type().vmType());
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
    MethodVisitor mv = this.mv;
    Type valueType = value.type();
    if (returnType != valueType && (!returnType.isMixed() || valueType.isMixed() || returnType.vmType() != valueType.vmType())) {
      throw new IllegalArgumentException("invalid convertion " + returnType + " " + valueType);
    }
    
    if (!valueType.isMixed()) {
      value.loadPrimitive(mv);
      mv.visitInsn(returnOpcode(valueType.vmType()));
      return;
    }
    Var var = (Var)value;
    int slot = var.slot();
    mv.visitVarInsn(ALOAD, slot);
    CodeGen.loadNone(mv);
    Label endLabel = new Label();
    mv.visitJumpInsn(IF_ACMPNE, endLabel);
    mv.visitVarInsn(loadOpcode(valueType.vmType()), slot + 1);
    mv.visitInsn(returnOpcode(valueType.vmType()));
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
    if (value.type().vmType() != VM_BOOLEAN) {
      throw new IllegalArgumentException("value.type must be a boolean");
    }
    MethodVisitor mv = this.mv;
    value.loadPrimitive(mv);
    mv.visitJumpInsn(IFNE, label);
  }
  public void jumpIfFalse(Value value, Label label) {
    if (value.type().vmType() != VM_BOOLEAN) {
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
    sideExit = () -> { /* empty */};
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
    switch(type.vmType()) {
    case VM_BOOLEAN:
    case VM_BYTE:
    case VM_CHAR:
    case VM_SHORT:
    case VM_INT:
      mv.visitInsn(ICONST_0);
      return;
    case VM_LONG:
      mv.visitInsn(LCONST_0);
      return;
    case VM_FLOAT:
      mv.visitInsn(FCONST_0);
      return;
    case VM_DOUBLE:
      mv.visitInsn(DCONST_0);
      return;
    default:  //OBJECT, ARRAY
      mv.visitInsn(ACONST_NULL);
    }
  }
}
