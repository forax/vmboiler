package test;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_STATIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.function.Supplier;

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
import com.github.forax.vmboiler.rt.OptimisticError;

public class Example {
  enum Types implements Type {
    INT, INT_MIXED, ANY
    ;
    @Override
    public boolean isMixed() {
      return this == INT_MIXED;
    }
    @Override
    public String vmType() {
      return (this == ANY)? Type.VM_OBJECT: Type.VM_INT;
    }
  }
  
  private static final Object[] EMPTY_ARRAY = new Object[0];
  
  private static final String EXAMPLE_RT = ExampleRT.class.getName().replace('.', '/');
  private static final Handle BSM = new Handle(H_INVOKESTATIC,
      EXAMPLE_RT, "bsm",
      MethodType.methodType(CallSite.class, Lookup.class, String.class, MethodType.class).toMethodDescriptorString());
  private static final Handle DEOPT_ARGS = new Handle(H_INVOKESTATIC,
      EXAMPLE_RT, "deopt_args",
      MethodType.methodType(boolean.class, Lookup.class, String.class, MethodType.class, Object[].class, String.class).toMethodDescriptorString());
  private static final Handle DEOPT_RET = new Handle(H_INVOKESTATIC,
      EXAMPLE_RT, "deopt_ret",
      MethodType.methodType(boolean.class, Lookup.class, String.class, MethodType.class, Object.class, String.class).toMethodDescriptorString());
  
  private static byte[] generateAdd1Any() {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_8, ACC_PUBLIC|ACC_SUPER, "Foo", null, "java/lang/Object", null);
    MethodVisitor mv = writer.visitMethod(ACC_PUBLIC|ACC_STATIC, "foo", "(I)Ljava/lang/Object;", null, null);
    mv.visitCode();
    CodeGen codeGen = new CodeGen(mv, Types.ANY);
    Var x = codeGen.createVar(Types.INT);
    Constant one = new Constant(Types.INT, 1);
    Var result = codeGen.createVar(Types.ANY);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, new Object[] { "x" },
        result, "add", x, one);
    codeGen.ret(result);
    codeGen.end();
    mv.visitMaxs(-1, -1);
    mv.visitEnd();
    writer.visitEnd();
    return writer.toByteArray();
  }
  
  private static byte[] generateAdd1Int() {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_8, ACC_PUBLIC|ACC_SUPER, "Foo", null, "java/lang/Object", null);
    MethodVisitor mv = writer.visitMethod(ACC_PUBLIC|ACC_STATIC, "foo", "(I)I", null, null);
    mv.visitCode();
    CodeGen codeGen = new CodeGen(mv, Types.INT_MIXED);
    Var x = codeGen.createVar(Types.INT);
    Constant one = new Constant(Types.INT, 1);
    Var result = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, new Object[] { "x" },
        result, "add", x, one);
    codeGen.ret(result);
    codeGen.end();
    mv.visitMaxs(-1, -1);
    mv.visitEnd();
    writer.visitEnd();
    return writer.toByteArray();
  }
  
  private static byte[] generateAdd2Int() {
    ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
    writer.visit(V1_8, ACC_PUBLIC|ACC_SUPER, "Foo", null, "java/lang/Object", null);
    MethodVisitor mv = writer.visitMethod(ACC_PUBLIC|ACC_STATIC, "foo", "(I)I", null, null);
    mv.visitCode();
    CodeGen codeGen = new CodeGen(mv, Types.INT_MIXED);
    Var x = codeGen.createVar(Types.INT);
    Constant one = new Constant(Types.INT, 1);
    Var result1 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, new Object[] { "x" },
        result1, "add", x, one);
    Var result2 = codeGen.createVar(Types.INT_MIXED);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, new Object[] { "result1" },
        result2, "add", result1, one);
    codeGen.ret(result2);
    codeGen.end();
    mv.visitMaxs(-1, -1);
    mv.visitEnd();
    writer.visitEnd();
    return writer.toByteArray();
  }
  
  public static MethodHandle createFunction(Supplier<byte[]> generator, MethodType methodType) {
    byte[] array = generator.get();
    ClassReader reader = new ClassReader(array);
    CheckClassAdapter.verify(reader, true, new PrintWriter(System.out));
    
    Class<?> type = new ClassLoader() {
      Class<?> createClass() {
        return defineClass("Foo", array, 0, array.length);
      }
    }.createClass();
    try {
      return MethodHandles.publicLookup().findStatic(type, "foo", methodType);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
  
  public static void main(String[] args) throws Throwable {
    //MethodHandle mh = createFunction(Example::generateAdd1Any, MethodType.methodType(Object.class, int.class));
    //MethodHandle mh = createFunction(Example::generateAdd1Int, MethodType.methodType(int.class, int.class));
    MethodHandle mh = createFunction(Example::generateAdd2Int, MethodType.methodType(int.class, int.class));
    try {
      int value = (int)mh.invoke(Integer.MAX_VALUE);
      //int value = (int)mh.invokeExact(Integer.MAX_VALUE);
      System.out.println(value);
    } catch(OptimisticError e) {
      System.out.println(e.value());
    }
  }
}
