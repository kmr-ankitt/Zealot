package com.piscan.zealot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
// import java.util.Scanner;

public class Zealot {

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: Zealot [script]");
            System.exit(64);
        }

        else if (args.length == 1) {
            runFile(args[0]);
        }

        else {
            runPrompt();
        }
    }

    private static final Interpreter interpreter = new Interpreter();
    // test
    // We’ll use this to ensure we don’t try to execute code that has a known error.
    static boolean hadError = false;
    static boolean hadRuntimeError = false;

    // Running with prompt

    private static void runFile(String path) throws IOException {

        // Check if the file extension is .zlt
        if (!path.endsWith(".zlt")) {
            System.err.println("Error: Invalid file extension. The file must have a .zlt extension.");
            System.exit(66); // Exit with a custom error code for invalid file extension
        }
     
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError)
            System.exit(65);
        
        if(hadRuntimeError)
            System.exit(70);
    }

    // Running without any prompt

    private static void runPrompt() throws IOException {

        // Bridging byte stream and Character Stream
        InputStreamReader input = new InputStreamReader(System.in);

        // Extracting text from character stream
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.print("> ");

            // Reading one line at a time
            String line = reader.readLine();
            if (line == null)
                break;
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);

        // Creating token
        List<Token> tokens = scanner.scanTokens();

        // // Printing all tokens
        // for (Token token : tokens) {
        //     System.out.println(token);
        // }

        Parser parser = new Parser(tokens);

        // Expr expression = parser.parse();
        List<Stmt> statements = parser.parse();

        //stop if there was a syntax error
        if(hadError)
            return ;
        
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);

        // Stop if there was a resolution error.
        if (hadError) return;
        
        // System.out.println(new AstPrinter().print(expression));
        interpreter.interpret(statements);
        
    }

    // Finding Error
    static void error(int line, String message) {
        report(line, "", message);
    }

    //> Parsing Expressions token-error
    static void error(Token token, String message) {
        if (token.type == TokenType.EOF) {
        report(token.line, " at end", message);
        } 
        
        else {
        report(token.line, " at '" + token.lexeme + "'", message);
        }
    }

    static void runtimeError(RuntimeError error){
        System.err.println(error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    // Reporting Error
    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
