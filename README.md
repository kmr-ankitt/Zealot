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