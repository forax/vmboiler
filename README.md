vmboiler
========

A small library on top of ASM that generates optimistically typed bytecodes that aim
to ease the implementation of fast dynamically typed language runtime on top of the JVM.


FAQ
===

How it works ?
---
The boiler provides only 8 operations and ask the runtime developer
to map the language semantics to these operations.
There is only 8 operations because all method call, field access, conversion, etc,
are done using one operation called call that leverage invokedynamic to
specify the exact semantics.
Then the boiler ask that runtime developer to provide type annotations as hints
for the 8 operations. These type annotations can be optimistic indicating that a type
can be either a primitive type or an object type (we call it a mixed type). 
At runtime, if a value doesn't fit its primitive type anymore, the boiler
has inserted code that will handle that and called two special methods
indicating if an arguments or a return value of a called as the .
Calling these methods allows a runtime to, by example, trap the value that changed
and change the corresponding type profile and then invalidate the code and re-generate
a new one using the updated profile.

Why it's not awfully slow ?
---
The idea is to generate more code than the equivalent code in Java but
in a way that JITs can easily optimized in order to generate an assembly
code that is really close to the code generated for the equivalent code in Java.

Why the API is register based an not stack based like the Java bytecode ?
---
De-optimization is triggered by throwing an exception (OptimisticError)
and inside an exception handler, the stack is lost so instead of trying to
reconstruct the stack, it's easier to consider that there is no stack.

Can you compare it to the way Nashorn do deoptimization ?
---
Nashorn do deoptimization by continuation, i.e. for every points where
a deoptimization can occur it stores all the variables that will be
necessary to restart and the AST node then when a deopt occur,
the runtime generates a new code from the AST node and transfer all the values
to the corresponding variables. This implies that you need to keep the AST in memory,
that the saved AST node is written in a specific way and that a specific analysis
is implemented to know all the variables that are needed to restart a code.
While one can try to make the AST and the analysis independent from a language semantics,
it's harder than just let the VM do the deoptimization.

Can you compare it to the way Graal/Truffle do deoptimization ?
---
Truffle use a modified Hotspot runtime and its own JIT (Graal).
The idea is that the runtime deveoper write an interpreter with hints
that helps the partial evaluation done by Graal to generate an
heavily optimized assembly code with the same semantics than the interpreter.
The boiler is a generator, not an interpreter so the code specialization
can be done when generating instead of as a separate pass at compile time
like Truffle-SOM does. The boiler uses invokedynamic to get the values
of the stack frame instead of asking Hotspot these values so it works
great all Java VM that have a decent JIT but will not provide agressive
optimisations as Truffle can do because it doesn't seat on top of Graal.   

