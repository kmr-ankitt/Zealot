package com.piscan.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// this code will generate sytanx tree subclasses 
// we have to give directory where we want to use this script
public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage: generate_ast <output directory>");
            System.exit(64);
        }
        String outputDir = args[0];

        // this is for expressions
        defineAst(outputDir, "Expr", Arrays.asList(
                "Assign   : Token name , Expr value",
                "Binary   : Expr left, Token operator, Expr right",
                "Call     : Expr callee, Token paren, List<Expr> arguments",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Logical  : Expr left , Token operator , Expr right",
                "Unary    : Token operator, Expr right",
                "Variable : Token name"));

        // this is for statements
        defineAst(outputDir, "Stmt", Arrays.asList(
                "Block      : List<Stmt> statements",
                "Expression : Expr expression",
                "If         : Expr condition , Stmt thenBranch," + " Stmt elseBranch",
                "Print      : Expr expression",
                "Var        : Token name , Expr initializer ",
                "While      : Expr condtion , Stmt body"));

    }

    // this will create a new file expr.java and put the basic code there
    private static void defineAst(
            String outputDir, String baseName, List<String> types)
            throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.piscan.zealot;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");

        defineVisitor(writer, baseName, types);

        /*
         * When we call this, baseName is “Expr”, which is both the name of the class
         * and the name of the file it outputs. We pass this as an argument instead of
         * hardcoding the name because we’ll add a separate family of classes later for
         * statements.
         */
        // The AST classes
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, baseName, className, fields);
        }

        // base method accpeted
        writer.println();
        writer.println(" abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    // here we are declaring visit method of all subclasses
    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface Visitor<R> {");

        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println("    R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }

        writer.println("  }");
    }

    private static void defineType(
            PrintWriter writer, String baseName,
            String className, String fieldList) {
        writer.println("  static class " + className + " extends " +
                baseName + " {");

        // Constructor.
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields.
        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // visitor pattern
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" +
                className + baseName + "(this);");
        writer.println("    }");

        // Fields.
        writer.println();
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }

        writer.println("  }");

    }
}
