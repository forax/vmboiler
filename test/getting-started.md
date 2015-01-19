vmboiler
========

A small library on top of ASM that generates optimistically typed bytecodes that aim
to ease the implementation of fast dynamically typed language runtime on top of the JVM.


Getting Started
===

Intro
---

Let say i want to implement a function that add 1 to a value,
By example, if I want to implement it in Ruby, I will write something like this
```ruby
def addOne(x)
  return x + 1
end
```

Ruby (like Python) handles overflow by transforming integers that can not be stored
on 32/64 bits (Fixnum) into an infinite big number (Bignum) automatically.

```ruby
result = addOne(1)
puts result.is_a? Fixnum             # true

FIXNUM_MAX = (2 ** (0.size * 8 - 2) - 1)
puts FIXNUM_MAX.is_a? Fixnum         # true

result2 = addOne(FIXNUM_MAX)
puts result2.is_a? Fixnum            # false
```

Now say that i want to implement Ruby on the JVM,
the equivalent Java code will be something like this

```java
static Object addOne(int x) {
  try {
    return Math.addExact(x , 1);
  } catch(ArithmeticException e) {  // overflow !
    return BigInteger.valueOf(x).add(BiInteger.ONE);
  }
}
```

The return type of addOne is an Object because if there is an overflow,
the function can return an object of type BigInteger.
The problem is that it means that the result of Math.addExact also need
to be transformed to an Object meaning that we are asking the Java VM
to box a primitive 32bits into an object because it may overflow.

The idea of the vmboiler is to avoid that boxing to only pay the price
of using objects when there is an overflow.
So instead the equivalent Java code for the generated code should be
something like this

```java
static int addOne(int x) {
  try {
    return Math.addExact(x , 1);
  } catch(ArithmeticException e) {  // overflow !
    throw OptimisiticError.newOptimisticError(
      BigInteger.valueOf(x).add(BigInteger.ONE));
  }
}
```

addOne() returns an int so in the general case, there is no boxing,
and if there is an overflow, we use an exception (named OptimisticError)
to indicate that the returned value can not be stored in the return type.

Things get a little more complex when trying to call the method addOne,
let suppose i want a method addTwo defined like this

```ruby
def addTwo(x)
  return addOne(addOne(x))
end
```

The equivalent Java code is something like that

```java
static int addTwo(int x) {
  int result2;
  try {
    int result1;
    try {
      result1 = addOne(x);
    } catch(OptimisiticError e) {
      // e.value() is an Object not an int
      // how to initialize result1 ?
    }
    result2 = addOne(result1);
  } catch(OptimisiticError e) {
    // e.value() is an Object not an int
    // how to initialize result2 ?
  }
  return result2;
}
```

As you can see we have a problem because the result of the first call
to addOne() need to be used as argument of the second call to addOne.
But addOne() can either return an int or return an Object and
we still want to avoid boxing.

To solve that, the idea is to consider that the type of result1
(and result2) is a kind of mixed type that can be either an int
or an object stored into two different local variables
(on storing an int, one storing an object) with the convention
that if the object part is a known constant NONE, then the value
is stored in the int prt, otherwise the value is in the object part.

So the corresponding Java code is in fact something more like that
```java
static int addTwo(int x) {
  Object result2_o
  int result2_i;
  try {
    Object result1_o;
    int result1_i;
    try {
      result1_i = addOne(x);   // take an int
      result1_o = NONE;
    } catch(OptimisiticError e) {
      result1_o = e.value();
      result1_i = 0;
    }
    if (result1_o == NONE) {
      result2_i = addOne(result1_i);  // take an int
      result2_o = NONE;
    } else {
      result2_o = addOne(result1_o);  // take an object
      result2_i = 0;
    }
  } catch(OptimisiticError e) {
    result2_o = e.value();
    result2_i = 0;
  }
  if (result2_o == NONE) {
    return result2_i;
  }
  throw OptimisiticError.newOptimisiticError(result2_o);
}
```

You may think that this code will never be fast, given
the size of the bytecode required but you will be wrong because
this code is highly optimizable because even a simple JIT do
constant propagation and catch/branch profiling.

So for the JIT, given that for the first catch block will
be never executed if there is no overflow,
result1_o will be equal to NONE, so the code of the else branch
will never be executed so result2_o will be equal to NONE.

The equivalent code in Java to what the JIT will generate is
```java
static int addTwo(int x) {
  int result2_i;
  try {
    int result1_i;
    try {
      result1_i = addOne(x);   // take an int
    }
    result2_i = addOne(result1_i);  // take an int
  }
  return result2_i;
}
```

which is really close to the code someone will write in Java
if there is no overflow.

The idea of the vmboiler is to just use the vmboiler API
to generate the classical code and to let the vmboiler code
generator to handle the code that deal with
the exception OptimisticError.


How to use the vm boiler
---

First, you need to declare the types of your languages and
how they are mapped to the Java VM type.

The boiler ask you to implement an interface Type
with two methods isMixed and vmType.
isMixed indicated if a compound type composed of a primitive type
and an object type.

Here is the declaration of the types for our example 
```java
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
```

After that, the CodeGen object allow you to generate code
using constants and variables in a register based fashion.
First, the CodeGen takes a ASM MethodVisitor as parameter
and the function return type. Here, the return type is
INT_MIXED because it can be either an 32bits int or
something that should be boxed into an object.
Then the code declare a variable 'x' of type INT which is
the parameter of the function. It create a constant '1'.
Constants are constant for the JVM thus the value must be
mapped to a JVM type exactly, the value can not be changed. 
The code then declare another variable 'result' that will
contains the result value of the operation '+'.
Note that the result value is also a MIXED_INT because
'+' can overflow. And at the end, the result value is 
returned as return value of the function.

```java
  private static byte[] generateAdd1Int() {
    ...
    CodeGen codeGen = new CodeGen(mv, Types.INT_MIXED);
    Var x = codeGen.createVar(Types.INT);
    Constant one = new Constant(Types.INT, 1);
    Var result = codeGen.createVar(Types.ANY);
    // call + here
    codeGen.ret(result);
    codeGen.end();
    ...
  }
```

Now, we need to add the call to the operator '+',
because the semantics of '+' depends on the semantics
of the dynamic language, the boiler rely on the JVM instruction
invokedynamic to do the linking at runtime between the call of
the operation and the code of the operation.
So codeGen.call that emit a call takes 5 parameters that explain
how to find the code of '+' at runtime and 4 regular parameters,
the result variable (result), the name of the operator (add)
and the two arguments (x and one). 

```java
  private static byte[] generateAdd1Any() {
    ...
    CodeGen codeGen = new CodeGen(mv, Types.ANY);
    Var x = codeGen.createVar(Types.INT);
    Constant one = new Constant(Types.INT, 1);
    Var result = codeGen.createVar(Types.ANY);
    codeGen.call(BSM, EMPTY_ARRAY, DEOPT_ARGS, DEOPT_RET, new Object[] { "x" },
        result, "add", x, one);
    codeGen.ret(result);
    codeGen.end();
    ...
  }
```

The first two parameters, BSM and EMPTY_ARRAY, define the bootstrap method
of the invokedynamic instructions and the constant arguments that can be
sent to the bootstrap method. For our example, we will not sent any constant
arguments that why we use an empty array.

The bootstrap method is a classical bootstrap method of invokedynamic
```java
  public static CallSite bsm(Lookup lookup, String name, MethodType methodType) throws Throwable {
    ...
  }
```

The third and the forth parameters defined the deoptimization methods,
i.e. methods that are called if either the arguments of the call or
the return value of the call doesn't fit in the declared type.
The fifth argument is an array of constant arguments that will be passed
to the two methods like the constant arguments of a bootstrap method.
A deoptimization method will be called with as first arguments either
the value of the call or the return value.
The return boolean value indicate if the method must be called for every
deoptimization (if true) or only once (if false).

```java
  public static boolean deopt_args(Object[] values, String parameterNames) throws Throwable {
    ...
    return false;
  }
  
  public static boolean deopt_ret(Object value, String parameterNames) throws Throwable {
    ...
    return false;
  }
``` 

The last step,, is to implement the bootstrap method and the two deoptimization
methods depending on the semantics of your language.

By example, for our small Ruby example, we need to implement
how to do the addition of two primitive ints, of two objects that can be
either boxed ints or bigints. 
We need in the bootstrap method to check if the requested implementation
is the addition of primitive ints or not.
Get a method pointer (a method handle) to the implementation
and if the implementation may return an object and requested return type
is an int, check at runtime if the return value if a small int or a big int.

In this example, the deoptimisation methods will just print a debug message.

```java
  private static int add(int a, int b) {
    try {
      return Math.addExact(a, b);
    } catch(ArithmeticException e) {
      throw OptimisticError.newOptimisticError(add((Object)a, b));
    }
  }
  private static Object add(Object a, Object b) {
    return asBigInt(a).add(asBigInt(b));
  }

  public static CallSite bsm(Lookup lookup, String name, MethodType methodType) throws Throwable {
    System.out.println("Example.bsm " + lookup + " " + name + methodType);
    boolean exactMatch = methodType.returnType() == int.class &&
                         methodType.parameterType(0) == int.class &&
                         methodType.parameterType(1) == int.class;
    MethodType lookupType;
    MethodHandle target = MethodHandles.lookup().findStatic(ExampleRT.class, name, exactMatch?methodType: methodType.generic());
    if (!exactMatch && methodType.returnType() == int.class) {
      target = MethodHandles.filterReturnValue(target, CONVERT).asType(methodType);
    }
    return new ConstantCallSite(target.asType(methodType));
  }

  
  private static BigInteger asBigInt(Object o) {
    if (o instanceof Integer) {
      return BigInteger.valueOf((Integer)o);
    }
    return (BigInteger)o;
  }
  
  private static int convert(Object result) {
    if (result instanceof BigInteger) {
      throw OptimisticError.newOptimisticError(result);
    }
    return (Integer)result;
  }
  
  public static boolean deopt_args(Object[] values, String parameterNames) throws Throwable {
    System.out.println("deopt args " + parameterNames + " " + Arrays.toString(values));
    return false;
  }
  
  public static boolean deopt_ret(Object value, String parameterNames) throws Throwable {
    System.out.println("deopt return " + value);
    return false;
  }
  
  private static final MethodHandle CONVERT;
  static {
    try {
      CONVERT = MethodHandles.lookup().findStatic(ExampleRT.class, "convert", MethodType.methodType(int.class, Object.class));
    } catch (NoSuchMethodException | IllegalAccessException e) {
      throw new AssertionError(e);
    }
  }
```

The full example is available here
  
you can play with it by commenting or not the line in the main.
