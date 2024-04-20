# Makefile for running Zealot program

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

# Target to compile Java source files
.PHONY: compile
compile: $(CLASSES)

$(CLASS_DIR)/%.class: $(SRC_DIR)/%.java | $(CLASS_DIR)
	$(JAVAC) -d $(CLASS_DIR) $<

# Target to run Zealot in REPL mode
.PHONY: repl
repl: compile
	$(JAVA) -cp $(CLASS_DIR) $(MAIN_CLASS)

# Target to execute a Zealot program stored in a file
.PHONY: run
run: compile
	$(JAVA) -cp $(CLASS_DIR) $(MAIN_CLASS) "$(INPUT)"

# Clean target
.PHONY: clean
clean:
	rm -rf $(CLASS_DIR)
