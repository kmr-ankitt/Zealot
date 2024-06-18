# Zealot

Zealot is a Tree-Walking Interpreter designed for simplicity, efficiency, and ease of use which I developed while learning. It aims to provide a clean and concise syntax while offering powerful features for developing a wide range of applications.

## Prerequisites

Before running Zealot, ensure that you have the following installed on your system:

- **Java Development Kit (JDK):** You can download and install JDK from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html).

## Zealot Documentation

### File Extension
Zealot supports text files with `zlt` file extension.
example: `main.zlt`

### Hello, world!
A simple hello world program in Zealot:
```python
print "Hello, World!";
```
Semi-colons at the end of every line is mandatory in Zealot.

### Datatypes
Zealot has following datatypes

#### Numbers
These can number literals which can be both integers and floating point numbers.

examples: `1`, `2.5`, `9`

#### Strings
These are string literals defined inside `"`

examples: `"Zealot"`, `"Strings are easy"`

### Booleans
These are boolean literals which can be either `true` or `false`.

examples: `true`, `false`

### Null
Zealot has nulls, the trillion dollar mistale. It can be defined using the `nill` keyword. All uninitialized variables are given the value of `nill`.

examples: `nill`



### Operators.
Zealot has following operators:
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

`>`  - is less than

`>=` - is less than or equals

`>`  - is greater than

`>=` - is greater than or equals




### Comments
Zealot support both single line and multi line comment.

- Single line comment
```c
// This is a comment.
// The Lexer completely ignores any line starting with 
// The Whole line is ignored.
```

- Multiline comment
```c
/*This is a mulit line comment similar to C syntax.*/
```

### Variables
```javascript
var x = 15; // number
var name = "your name"; // string
var yes = true; // booleans
var no = false; // booleans
var nullable = nil;
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
```python
var x = 15;
if(x > 0){
    print "x > 0";
}else {
    print "x <= 0";    
}
```

### Loops
```javascript
var i = 0;
while(i < 10){
    print i;
    i = i + 1;
}

for(var j=0; j<5; j = j + 1){
    print j;
}
```

### Functions
```javascript
fun greetings(name){
    print "hello " + name;
}

greetings("piscan");
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

## Installing Zealot

To run Zealot, First clone Zealot Repository and navigate to the `Zealot` directory in your terminal and execute the following command:

```bash
git clone https://github.com/kmr-ankitt/Zealot
cd Zealot
```


**For Linux and macOS:**

```bash
source .bashrc
```

- Repl Mode 
```bash
zealot 
```

- Zelaot file
```bash
zealot FILE_DIRECTORY
```

**For Windows:**

- Repl Mode
```powershell
./zealot.bat
```

- Zealot File 
```powershell
./zealot.bat FILE_DIRECTORY
```


**Or Using Make**

For Linux and MacOS :

- Repl mode
```bash
make compile
make repl 
```

- Zealot file
```bash
make run INPUT="FILE_DIRECTORY"
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
These commands will remove all compiled `.class` files, ensuring a clean environment 

