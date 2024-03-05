package com.piscan.Zealot;

class Token {
    final TokenType type;
    final String lexeme;

    /**
     * Using Object as the type means that it can store any type of literal, but it
     * also means that you'll need to cast it to the appropriate type when you
     * retrieve the value.
     **/

    final Object literal;
    final int line;

    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
