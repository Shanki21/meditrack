# JVM Report — MediTrack

This report covers the core internals of the Java Virtual Machine (JVM) relevant to understanding how MediTrack (and any Java application) actually runs, from source code to execution.

## 1. Class Loader

The Class Loader is responsible for loading compiled `.class` bytecode files into memory at runtime. It does this **lazily** — a class is only loaded the first time it's actually referenced in the running program, not all at once when the application starts. For example, in MediTrack, the `Bill` class isn't loaded into memory until the first time `generateBill()` actually creates a `new Bill(...)`.

Java's class loading is split across a few loaders working together:
- **Bootstrap Class Loader** — loads core JDK classes (`java.lang.*`, `java.util.*`)
- **Platform/Extension Class Loader** — loads platform-level libraries
- **Application Class Loader** — loads the classes we actually write, such as everything under `com.airtribe.meditrack`

This layered loading is also what enforces a form of security and organization — application code can't accidentally override core Java classes.

## 2. Runtime Data Areas

Once a class is loaded, the JVM needs memory regions to actually run the program. Each serves a distinct purpose:

- **Heap** — This is where every object created with `new` actually lives — every `Doctor`, `Patient`, `Appointment`, and `Bill` object in MediTrack is stored here. It's shared across the entire application (and across all threads), which is why it's the region managed by the Garbage Collector — objects that are no longer referenced (e.g., a `Patient` object with no variable pointing to it anymore) are eventually reclaimed.

- **Stack** — Each thread gets its own stack. Every time a method is called (e.g., `registerDoctor()` calling `Validator.validateAge()`), a new "stack frame" is pushed, holding that method's local variables and parameters. When the method returns, its frame is popped off. This explains why deep, uncontrolled recursion causes a `StackOverflowError` — too many frames get pushed without being popped.

- **Method Area** — Stores class-level information: method bytecode, the structure of each class, and `static` fields. In MediTrack, the `static` counters inside `IdGenerator` (like `doctorCounter`) live conceptually in this area, since they belong to the class itself, not to any individual object instance.

- **PC (Program Counter) Register** — Each thread has its own PC Register, which keeps track of the exact bytecode instruction currently being executed by that thread. This is necessary because multiple threads can be executing different methods simultaneously, and each needs to remember its own position.

## 3. Execution Engine

The Execution Engine is what actually runs the bytecode loaded by the Class Loader. It doesn't run raw machine code directly — it works with the platform-independent bytecode, and converts it to something the actual CPU can execute.

## 4. JIT Compiler vs. Interpreter

The Execution Engine uses two complementary strategies:

- **Interpreter** — Reads and executes bytecode line-by-line, immediately. This gives fast startup time, since there's no upfront compilation delay, but it's slower for code that runs repeatedly, since the same lines get re-interpreted every time.

- **JIT (Just-In-Time) Compiler** — Monitors the running program for "hot" methods — code that gets called frequently (for example, if MediTrack were processing thousands of `Validator.validateAge()` calls in a loop). Once a method is identified as hot, the JIT compiles it directly into native machine code, so subsequent calls run at near-native speed instead of being re-interpreted each time.

This is why long-running Java applications often get progressively faster the longer they run — the JIT has more opportunity to identify and optimize hot code paths.

## 5. "Write Once, Run Anywhere"

This principle is a direct consequence of how the JVM works. When you compile MediTrack with `javac`, the output isn't machine code specific to Windows, macOS, or Linux — it's platform-independent **bytecode** (`.class` files). Any machine with a JVM installed can load and run that exact same bytecode, because the JVM itself — not the application code — handles the platform-specific translation to native instructions.

This means the same compiled MediTrack `.class` files could run unmodified on a Windows development machine and a Linux production server, as long as both have a compatible JVM installed (in this project's case, JDK 21).