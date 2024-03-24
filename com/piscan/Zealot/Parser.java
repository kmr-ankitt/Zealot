package com.piscan.Zealot;

import java.beans.Expression;
import java.util.List;

import static com.piscan.Zealot.TokenType.*;

class Parser {
    private final List<Token> tokens;

    // we use current to point to the next token to be parsed
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // expressions => equality
    private Expr expression() {
        return equality();
    }

    // equality → comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr expr = comparision();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // This checks to see if the current token has any of the given types. 
    // If so, it consumes the token and returns true. Otherwise, it returns false
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    // this check method returns true when token is consumed. Unlike match it never consumes tokens
    private boolean check(TokenType type){
        if(isAtEnd())
            return false;
        return peek().type == type;
    }

    // this advance method consumes the current token and returns it
    private Token advance(){
        if(!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd(){
        return peek().type == EOF;
    }

    private Token peek(){
        return tokens.get(current);
    }

    private Token previous(){
        return tokens.get(current - 1);
    }
}
