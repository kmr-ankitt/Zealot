package com.piscan.Zealot;

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
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }

        else if (args.length == 1) {
            runFile(args[0]);
        }

        else {
            runPrompt();
        }
    }

    // test
    // We’ll use this to ensure we don’t try to execute code that has a known error.
    static boolean hadError = false;

    // Running with prompt

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        // Indicate an error in the exit code.
        if (hadError)
            System.exit(65);
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

        // Printing all tokens
        for (Token token : tokens) {
            System.out.println(token);
        }

    }

    // Finding Error
    static void error(int line, String message) {
        report(line, "", message);
    }

    // Reporting Error
    private static void report(int line, String where, String message) {
        System.err.println("[line " + "] Error" + where + ": " + message);
        hadError = true;
    }
}
