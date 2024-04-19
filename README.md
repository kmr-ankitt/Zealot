# Zealot

**Under Development!**

Zealot is a Tree-Walking Interpreter designed for simplicity, efficiency, and ease of use. It aims to provide a clean and concise syntax while offering powerful features for developing a wide range of applications.

## Prerequisites

Before running Zealot, ensure that you have the following installed on your system:

- **Java Development Kit (JDK):** You can download and install JDK from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk16-downloads.html).
- **Make:** Ensure that you have `make` installed on your system. This is typically available on Unix-like systems such as Linux and macOS.

## Running Zealot

To run Zealot, navigate to the `Zealot` directory in your terminal and execute the following command:

**For Linux and macOS:**

```
make
```

For Windows:

```
javac com/piscan/zealot/*.java
java com/piscan/zealot/Zealot
```

## Cleaning Up Compiled Files

After running Zealot, you may want to clean up compiled files. Use the following commands:

**For Linux and macOS:**

```
make clean
```

**For Windows:**

```
Get-ChildItem -Path .\com\piscan\zealot -Filter *.class -Recurse | Remove-Item -Force
```
These commands will remove all compiled `.class` files, ensuring a clean environment 


## Zealot Grammer

### Variables
```console
    var x = 15; // number
    var name = "your name"; // string
    var yes = true; // booleans
    var no = false; // booleans
    var nullable = nil;
```

### Logical operators
```console
!true;  // false.
!false; // true.
true and false; // false.
true and true;  // true.
false or false; // false.
true or false;  // true.
```

### Control flow
```console
    var x = 15;
    if(x > 0){
        print "x > 0";
    }else {
        print "x <= 0";    
    }
```

### Loops
```console
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
```console
    fun greetings(name){
        print "hello " + name;
    }
    
    greetings("leonard");
```

### Closures
```console
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