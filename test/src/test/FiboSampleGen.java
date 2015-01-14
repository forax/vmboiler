package test;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASTORE;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_7;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.CheckClassAdapter;

import com.github.forax.vmboiler.CodeGen;
import com.github.forax.vmboiler.Constant;
import com.github.forax.vmboiler.Type;
import com.github.forax.vmboiler.Var;

public class FiboSampleGen {
  public enum Types implements Type {
    INT, INT_MIXED, BOOL
    ;
    @Override
    public boolean isMixed() {
      return this == INT_MIXED;
    }
    @Override
    public String vmType() {
      return (this == BOOL)? Type.VM_BOOLEAN: Type.VM_INT;
    }
  }
  
  private static final Object[] EMPTY_ARRAY = new Object[0];
  
  private static final String FIBO_SAMPLE_RT = FiboSampleRT.class.getName().replace('.', '/');
  private static final Handle BSM = new Handle(H_INVOKESTATIC,
      FIBO_SAMPLE_RT, "bsm",
      MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
  private static final Handle DEOPT_ARGS = new Handle(H_INVOKESTATIC,
      FIBO_SAMPLE_RT, "deopt_args",
      MethodType.methodType(boolean.class, Object[].class).toMethodDescriptorString());
  private static final Handle DEOPT_RET = new Handle(H_INVOKESTATIC,
      FIBO_SAMPLE_RT, "deopt_return",
      MethodType.methodType(boolean.class, Object.class).toMethodDescriptorString());
  
  
  public static void main(String[] args) throws IOException {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_8, ACC_PUBLIC|ACC_SUPER, "FiboSample", null, "java/lang/Object", null);
    MethodVisitor mv = writer.visitMethod(ACC_PUBLIC|ACC_STATIC, "fibo", "(I)I", null, null);
    mv.visitCode();
    CodeGen codeGen = new CodeGen(mv, Types.INT_MIXED);
    Constant one = new Constant(Types.INT, 1);
    Constant two = new Constant(Types.INT, 2);
    Var r1 = codeGen.createVar(Types.INT);
    Var r2 = codeGen.createVar(Types.BOOL);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r2, "lt", r1, two);
    Label nextLabel = new Label();
    codeGen.jumpIfFalse(r2, nextLabel);
    codeGen.ret(one);
    codeGen.label(nextLabel);
    Var r3 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r3, "sub", r1, one);
    Var r4 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r4, "fibo", r3);
    Var r5 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r5, "sub", r1, two);
    Var r6 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r6, "fibo", r5);
    Var r7 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, EMPTY_ARRAY,
        r7, "add", r4, r6);
    codeGen.ret(r7);
    codeGen.end();
    mv.visitMaxs(-1, -1);
    mv.visitEnd();
    
    MethodVisitor main = writer.visitMethod(ACC_PUBLIC|ACC_STATIC, "main",
        "([Ljava/lang/String;)V", null, null);
    
    main.visitCode();
    Label start = new Label();
    Label end = new Label();
    Label handler = new Label();
    
    main.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
        "Ljava/io/PrintStream;");
    main.visitLdcInsn(7);
    
    main.visitTryCatchBlock(start, end, handler, "com/github/forax/vmboiler/rt/OptimisticError");
    main.visitLabel(start);
    main.visitMethodInsn(INVOKESTATIC, "FiboSample", "fibo", "(I)I", false);
    main.visitLabel(end);
    main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(I)V", false);
    main.visitInsn(RETURN);
    
    main.visitLabel(handler);
    main.visitVarInsn(ASTORE, 0);
    main.visitFieldInsn(GETSTATIC, "java/lang/System", "out",
        "Ljava/io/PrintStream;");
    main.visitVarInsn(ALOAD, 0);
    main.visitMethodInsn(INVOKEVIRTUAL, "com/github/forax/vmboiler/rt/OptimisticError",
        "value", "()Ljava/lang/Object;", false);
    main.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    main.visitInsn(RETURN);
    
    main.visitMaxs(-1, -1);
    main.visitEnd();
    
    writer.visitEnd();
    byte[] array = writer.toByteArray();
    
    ClassReader reader = new ClassReader(array);
    CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
    
    Files.write(Paths.get("FiboSample.class"), array);
  }
}
