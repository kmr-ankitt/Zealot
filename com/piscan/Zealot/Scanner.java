package com.piscan.Zealot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.piscan.Zealot.TokenType.*;

class Scanner {
    private final String source;

    // creating a list of tokens
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    // scanning the tokens
    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens(){
        while (!isAtEnd) {
            start = current;
            scanTokens();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

}
