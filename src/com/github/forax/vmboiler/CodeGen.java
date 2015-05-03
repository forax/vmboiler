package com.github.forax.vmboiler;

import static com.github.forax.vmboiler.Type.VM_BOOLEAN;
import static com.github.forax.vmboiler.Type.VM_BYTE;
import static com.github.forax.vmboiler.Type.VM_CHAR;
import static com.github.forax.vmboiler.Type.VM_DOUBLE;
import static com.github.forax.vmboiler.Type.VM_FLOAT;
import static com.github.forax.vmboiler.Type.VM_INT;
import static com.github.forax.vmboiler.Type.VM_LONG;
import static com.github.forax.vmboiler.Type.VM_SHORT;
import static com.github.forax.vmboiler.Type.VM_VOID;
import static com.github.forax.vmboiler.Value.loadOpcode;
import static com.github.forax.vmboiler.Value.returnOpcode;
import static com.github.forax.vmboiler.Value.size;
import static org.objectweb.asm.Opcodes.*;

import java.util.function.Function;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import com.github.forax.vmboiler.rt.OptimisticError;
import com.github.forax.vmboiler.rt.RT;

/**
 * Wrapper on top of the ASM MethodVisitor allowing to generate code that is optimistically typed.
 * 
 * <p>Type
 * <p>A {@link Type} is either a classical VM type (int, double, etc) or a mixed type,
 * i.e. a type composed by an object type and a primitive type. A mixed type allow to optimistically
 * use the primitive type and fallback to the object type if the value can not be stored in the
 * primitive type. 
 * 
 * <p>Values
 * <p>All {@link Value}s are represented either by a {@link Constant} (a side
 * effect free constant storable in the class constant pool) or by a variable {@link Var}
 * that act as a register. {@link Var Variable} unlike {@link Constant} are 
 * {@link #createVar(Type, Function) created} on the current {@link CodeGen}
 * and should never be used with another {@link CodeGen}.
 * 
 * <p>Operations
 * <p>There are only 8 operations (instructions) to define the semantics of your language,
 * <p>{@link #call(Handle, Object[], Object, Object, Object[], Var, String, Value...)} emit an invokedynamic
 * with the some values as arguments and an variable as register for the return type.
 * The first two arguments are the bootstrap method and the bootstrap arguments,
 * the 3rd and the 4th arguments are two method handles, the first one will be called
 * if at least one argument doesn't match its declared type and the second will be called
 * if the return type doesn't match the result type.
 * <p>{@link #move(Var, Value)} that copy the value into a variable.
 * <p>{@link #ret(Value)} that returns from the current function with the value. 
 * <p>{@link #label(Label)} that set a label that can be use as a target of a jump.
 * <p>{@link #jump(Label)} that jump unconditionally to the specified label.
 * <p>{@link #jumpIfTrue(Value, Label)} that jump if the value is true.  
 * <p>{@link #jumpIfFalse(Value, Label)} that jump if the value is false.
 * <p>{@link #lineNumber(int)} that indicate a start of a new line in the source code.
 * 
 * <p>End
 * <p>The method {@link #end()} must be called after all instructions are generated.
 * By default CodeGen delay the generation of bytecodes that handles deoptimization at the end of the method.
 * If you do not want this behavior, you can call {@link #end()} after any operations.
 */
public final class CodeGen {
  private final MethodVisitor mv;
  private final Type returnType;
  private int slotCount;
  private Runnable sideExit = () -> { /* empty */ };
  
  /**
  * Create a new CodeGen to generate code of a method.
  * @param mv ASM method visitor.
  * @param returnType the return type of a method.
  */
  public CodeGen(MethodVisitor mv, Type returnType) {
    this.mv = mv;
    this.returnType = returnType;
  }
  
  /**
   * Returns the underlying ASM MethodVisitor.
   * This method should be used rarely because the modification
   * of method visitor state will not be tracked by the current CodeGen.
   * @return the underlying ASM MethodVisitor.
   */
  public MethodVisitor methodVisitor() {
    return mv;
  }
  
  /**
   * Returns type of the current method.
   * @return type of the current method.
   */
  public Type returnType() {
    return returnType;
  }
  
  /**
   * Create a variable attached to the current CodeGen.
   * This is a convenient method for
   * <pre>
   *  {@code createVar(type, Var::new)}
   * </pre>
   * 
   * @param type the type of the variable.
   * @return a newly created variable
   * 
   * @see #createVar(Type, Function)
   */
  public Var createVar(Type type) {
    return createVar(type, Var::new);
  }
  
  /**
   * Create a variable attached to the current CodeGen.
   * This method first call the factory to create a variable and
   * then assign a slot to it.
   * 
   * @param type type of the variable.
   * @param varFactory a factory which create a variable from a type.
   * @return a newly created variable.
   */
  public <T extends Type, V extends Var> V createVar(T type, Function<? super T, ? extends V> varFactory) {
    V var = varFactory.apply(type);
    var.injectSlot(slotCount);
    slotCount += size(type.vmType()) + (type.isMixed()? 1: 0);
    return var;
  }
  
  /**
   * Copy the content of the value into the variable.
   * The type of the value must be either the same as the type of the variable
   * or if the type of the variable is mixed, the same as the primitive value of
   * the type of the variable.
   * 
   * @param var the variable that will store the value.
   * @param value the value.
   */
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
    value.loadPrimitive(mv);
    loadNone(mv);
    var.storeAll(mv);
  }
  
  /**
   * Call a 'virtual method' using invokedynamic. Call {@code deoptArgsCallback} if
   * of one the argument value is declared with a mixed type and the primitive part
   * can not store the value of the register.
   * 
   * @param bsm the bootstrap method used to resolved the call.
   * @param bsmCsts the bootstrap constant arguments passed to the bootstrap method.
   * @param deoptArgs a method handle as a String or a {@link Handle}
   *        that will be called if at least one argument doesn't match its declared type. 
   * @param deoptRet a method handle as a String or a {@link Handle}
   *        that will be called if return value doesn't match its declared type
   * @param deoptCsts constants arguments sent as arguments of deopt methods
   * @param result the variable that will contains the result value
   *        (this variable can have a {@link Type#vmType()} equals to {@link Type#VM_VOID}).
   * @param name the name of the 'virtual method'.
   * @param values the arguments of the call.
   */
  public void call(Handle bsm, Object[] bsmCsts,
      Object deoptArgs, Object deoptRet, Object[] deoptCsts,
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
    mv.visitInvokeDynamicInsn(name, desc.toString(), bsm, bsmCsts);
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
            BSM_OPTIMISTIC_FAILURE, concat(deoptRet, deoptCsts));
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
      callDeopt(label, sideExitBackLabel, handler, bsm, bsmCsts, deoptArgs, deoptCsts, result, name, values);
    }
  }
  
  private void callDeopt(Label sideExitStart, Label sideExitBackLabel, Label handler,
      Handle bsm, Object[] bsmCsts, Object deoptArgs, Object[] deoptCsts,
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
          concat(mixedDesc.toString(), bsm, bsmCsts.length, deoptArgs, bsmCsts, deoptCsts));
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

  private static Object[] concat(Object o, Object[] array) {
    int length = array.length;
    Object[] newArray = new Object[length + 1];
    newArray[0] = o;
    System.arraycopy(array, 0, newArray, 1, length);
    return newArray;
  }
  
  private static Object[] concat(Object o1, Object o2, Object o3, Object o4, Object[] array1, Object[] array2) {
    Object[] newArray = new Object[array1.length + array2.length + 4];
    newArray[0] = o1;
    newArray[1] = o2;
    newArray[2] = o3;
    newArray[3] = o4;
    System.arraycopy(array1, 0, newArray, 4, array1.length);
    System.arraycopy(array2, 0, newArray, 4 + array1.length, array2.length);
    return newArray;
  }
  
  
  /**
   * Return the value as return value of the current method.
   * The type of the value must be either the same as the {@link #returnType()}
   * or if the return type is mixed, the same as the primitive value of the return type.
   * @param value the value used as return value.
   */
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

  
  /**
   * Set a label that can be used as target of a jump.
   * @param label an ASM label.
   * 
   * @see #jump(Label)
   * @see #jumpIfTrue(Value, Label)
   * @see #jumpIfFalse(Value, Label)
   */
  public void label(Label label) {
    mv.visitLabel(label);
  }
  
  /**
   * Unconditionally jump to the label pass as argument.
   * @param label an ASM Label
   */
  public void jump(Label label) {
    mv.visitJumpInsn(GOTO, label);
  }
  
  /**
   * Jump to the label if the value is true.
   * @param value a value.
   * @param label an ASM label.
   */
  public void jumpIfTrue(Value value, Label label) {
    String vmType = value.type().vmType();
    if (vmType != VM_BOOLEAN && vmType != VM_INT) {
      throw new IllegalArgumentException("value.type must be an int or a boolean");
    }
    MethodVisitor mv = this.mv;
    value.loadPrimitive(mv);
    mv.visitJumpInsn(IFNE, label);
  }
  
  /**
   * Jump to the label if the value is false.
   * @param value a value.
   * @param label an ASM label.
   */
  public void jumpIfFalse(Value value, Label label) {
    String vmType = value.type().vmType();
    if (vmType != VM_BOOLEAN && vmType != VM_INT) {
      throw new IllegalArgumentException("value.type must be an int or a boolean");
    }
    MethodVisitor mv = this.mv;
    value.loadPrimitive(mv);
    mv.visitJumpInsn(IFEQ, label);
  }
  
  //public void jumpIfNull(Value value, Label label);
  //public void jumpIfNonNull(Value value, Label label);
  
  /**
   * Generate a line number for the next instruction.
   * @param line a line number.
   */
  public void lineNumber(int line) {
    Label label = new Label();
    mv.visitLabel(label);
    mv.visitLineNumber(line, label);
  }
  
  /**
   * End by generating all the code that handle deoptimization paths.
   */
  public void end() {
    sideExit.run();
    sideExit = () -> { /* empty */ };
  }
  
  private static final String RT = RT.class.getName().replace('.', '/');
  private static final String OPTIMISTIC_ERROR = OptimisticError.class.getName().replace('.', '/');
  private static final Handle BSM = new Handle(H_INVOKESTATIC, RT, "bsm",
      "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;");
  private static final Handle BSM_OPTIMISTIC_FAILURE = new Handle(H_INVOKESTATIC, RT, "bsm_optimistic_failure",
      "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;");
  
  private static void newOptimisticError(MethodVisitor mv) {
    mv.visitMethodInsn(INVOKESTATIC, OPTIMISTIC_ERROR, "newOptimisticError",
        "(Ljava/lang/Object;)L" + OPTIMISTIC_ERROR + ';', false);
  }
  
  private static void loadNone(MethodVisitor mv) {
    mv.visitFieldInsn(GETSTATIC, RT, "NONE", "Ljava/lang/Object;");
  }
  
  private static void loadZero(MethodVisitor mv, Type type) {
    switch(type.vmType()) {
    case VM_VOID:
      throw new IllegalArgumentException("type void not allowed here");
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
