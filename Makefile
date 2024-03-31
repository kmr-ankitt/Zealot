# Makefile for running Java program

# Java compiler
JAVAC = javac

# Java runtime
JAVA = java

# Source directory
SRC_DIR = com/piscan/zealot

# Class directory
CLASS_DIR = classes

# Main class
MAIN_CLASS = com.piscan.zealot.Zealot

# Source files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Class files
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(CLASS_DIR)/%.class)

# Default target
.PHONY: all
all: $(MAIN_CLASS)

# Rule to create the class directory if it doesn't exist
$(CLASS_DIR):
	mkdir -p $(CLASS_DIR)

# Rule to compile Java source files
$(CLASS_DIR)/%.class: $(SRC_DIR)/%.java | $(CLASS_DIR)
	$(JAVAC) -d $(CLASS_DIR) $<

# Rule to run the Java program
$(MAIN_CLASS): $(CLASSES)
	$(JAVA) -cp $(CLASS_DIR) $(MAIN_CLASS)

# Clean target
.PHONY: clean
clean:
	rm -rf $(CLASS_DIR)
