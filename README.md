# Zealot

Zealot is a Dynamically typed Tree-Walking Interpreter designed for simplicity, efficiency, and ease of use.

## Why

Zealot is designed to provide a clean and concise syntax while being powerful and easy to learn.

## Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html)
- Make (Optional)

## Installation

1. Clone `Zealot` official repository:

```bash
git clone https://github.com/kmr-ankitt/Zealot.git
cd Zealot
```

2. Install Zealot:

```bash
source .bashrc  # For Linux/macOS
```

3. Run `Zealot` REPL:

```bash
zealot
```
You can also run Zealot using Makefile:

```bash
make compile
make repl
```

4. Run `.zlt` file:

```bash
zealot FILE_DIRECTORY/FILE_NAME.zlt
```
Or using Makefile:

```bash
make run INPUT="FILE_DIRECTORY/FILE_NAME.zlt"
```

## Zealot Documentation

### File extension

Zealot supports only `.zlt` file extension.
example: `main.zlt`

### Hello, world!
A simple hello world program in Zealot:
```python
print "Hello, World!";
```
**Semi-colons at the end of every line are mandatory in Zealot.**

### Datatypes
Zealot has the following datatypes:

#### Numbers
Number literals can be both integers and floating-point numbers.

examples: `1`, `2.5`, `9`

#### Strings
String literals are defined inside `"`.

examples: `"Zealot"`, `"Strings are easy"`

### Booleans
Boolean literals can be either `true` or `false`.

examples: `true`, `false`

### Null Type
Zealot has a null datatype. It can be defined using the `nill` keyword. All uninitialized variables are given the value of `nill`.

examples: `nill`

### Operators

Zealot has the following operators:

#### Assignment
`=` - equals

#### Unary operators
`-` - Unary negation

### Logical operators
`and` - logical AND

`or`  - logical OR

`!`   - logical NOT

#### Arithmetic operators
`+` - sum

`-` - difference

`*` - product

`/` - division

`%` - mod

#### Comparison operators
`==` - is equals

`!=` - is not equals

`<`  - is less than

`<=` - is less than or equals

`>`  - is greater than

`>=` - is greater than or equals

### Comments
Zealot supports both single-line and multi-line comments.

- Single-line comment
```c
// This is a single-line comment.
```

- Multi-line comment
```c
/*This is a multi-line comment in Zealot.*/
```

### Variables

```javascript
var num = 15; // number
var name = "radiohead"; // string
var truth = true; // boolean
var lie = false; // boolean
var nullable = nill; // null type
```

### Logical operators
```javascript
!true;  // false.
!false; // true.
true and false; // false.
true and true;  // true.
false or false; // false.
true or false;  // true.
```

### Control flow
```javascript
var num = 15;

if(num > 0){
    print "num is positive";
}else {
    print "num is negative";
}
```

### Loops
```javascript
for(var i = 0; i < 10; i = i + 1){
    print i;
}

var num = 1;
while(num > 0){
    print num;
    num = num - 1;
}
```

### Functions
```javascript
fun add(a, b){
    return a + b;
}

print add(5, 10);
```

### Closures
```javascript
fun addPair(a, b) {
    return a + b;
}

fun identity(a) {
    return a;
}

print identity(addPair)(1, 2); // Prints "3".

fun makeCounter(){
    var c = 0;
    fun counter(){
        c = c + 1;
        print c;
    }
    return counter;
}

var counter1 = makeCounter();
var counter2 = makeCounter();

counter1(); // 1
counter2(); // 1
```

## Classes
Zealot supports object-oriented programming with classes.

- Keywords: `class` for defining a class, `this` for the current context.

```javascript
class Bacon {
  fun eat() {
    print "Crunch crunch crunch!";
  }
}

Bacon().eat(); // Prints "Crunch crunch crunch!".

class Thing {
  fun getCallback() {
    fun localFunction() {
      print this;
    }
    return localFunction;
  }
}

var callback = Thing().getCallback();
callback();
```

## Cleaning Up Compiled Files

After running Zealot, you may want to clean up compiled files. Use the following commands:

**For Linux and macOS:**

```bash
make clean
```

**For Windows:**

```powershell
Get-ChildItem -Path .\com\piscan\zealot -Filter *.class -Recurse | Remove-Item -Force
```

These commands will remove all compiled `.class` files, ensuring a clean environment.


