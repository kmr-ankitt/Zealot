package com.piscan.zealot;

// import java.util.logging.Logger;

// import java.beans.Expression;
import java.util.ArrayList;
import java.util.List;

// import com.piscan.zealot.zealot;

import static com.piscan.zealot.TokenType.*;

class Parser {

    private static class ParseError extends RuntimeException {
    }

    private final List<Token> tokens;

    // private static final Logger LOGGER =
    // Logger.getLogger(Parser.class.getName());

    // we use current to point to the next token to be parsed
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // // this part was to try our intiail parser
    // Expr parse() {
    // try {
    // return expression();
    // } catch (ParseError error) {
    // return null;
    // }
    // }

    // private void logTokens(List<Token> tokens) {
    // for (Token token : tokens) {
    // LOGGER.info(token.toString());
    // }
    // }

    List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd()) {

            // logTokens(tokens.subList(current, tokens.size()));

            // statements.add(statement());
            statements.add(declaration());
        }

        return statements;
    }

    // expressions => equality
    private Expr expression() {

        // LOGGER.info("Found expression");
        // return equality();
        return assignment();
    }

    // equality → comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr expr = comparison();

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Stmt declaration(){
        try{
            if(match(VAR))
                return varDeclaration();
            
            return statement();
        }
        catch(ParseError error){
            synchronize();
            return null;
        }
    }

    // comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    private Expr comparison() {
        Expr expr = term();

        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // in order of precedence of + and -
    private Expr term() {
        Expr expr = factor();

        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // in order for precedence of * and /
    private Expr factor() {
        Expr expr = unary();

        while (match(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    // unary → ( "!" | "-" ) unary | primary which is a bit different from others
    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    // // primary → NUMBER | STRING | "true" | "false" | "nil"
    // | "(" expression ")" ;
    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(NIL))
            return new Expr.Literal(null);

        if (match(NUMBER, STRING))
            return new Expr.Literal(previous().literal);

        if(match(IDENTIFIER)){
            return new Expr.Variable(previous());
        }

        if (match(LEFT_PAREN)) {
            Expr expr = expression();

            // we are basically ignoring parenthesis and directly jumping into the
            // expression
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }

        // System.out.println("Expected primary but found: " + peek().type);

        throw error(peek(), "Expect expression.");
    }

    // here we will check wheather there is a Print statement or not
    private Stmt statement() {
        // logTokens(tokens.subList(current, tokens.size()));

        if (match(PRINT)) {

            // LOGGER.info("Found print statement");

            return printStatement();
        }

        if(match(LEFT_BRACE))
            return new Stmt.Block(block());

        return expressionStatement();
    }

    private Stmt printStatement() {

        // LOGGER.info("Found print statement");

        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt varDeclaration(){
        Token name  = consume(IDENTIFIER, "Expect variable name.");

        // if no expression then it by default makes it null
        Expr initializer = null;
        if(match(EQUAL))
            initializer = expression();

        consume(SEMICOLON, "Expect ';' after variable declaration.");
        return new Stmt.Var(name, initializer);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();

        while(!check(RIGHT_BRACE) && !isAtEnd()){
            statements.add(declaration());
        }

        consume(RIGHT_BRACE , "Expect '}' after block.");
        return statements;
    }

    private Expr assignment(){
        Expr expr = equality();

        if (match(EQUAL)) {
            Token equals = previous();
            Expr value = assignment();

            if (expr instanceof Expr.Variable) {
                Token name = ((Expr.Variable)expr).name;
                return new Expr.Assign(name, value);
            }
            error(equals, "Invaild assignment target.");
        }
        return expr;
    }

    // This checks to see if the current token has any of the given types.
    // If so, it consumes the token and returns true. Otherwise, it returns false
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {

                // LOGGER.info("Found token: " + current + " with type: " + type);

                advance();
                return true;
            }
        }
        return false;
    }

    // it checks wheather a particular token is present or not
    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        throw error(peek(), message);
    }

    // this check method returns true when token is consumed. Unlike match it never
    // consumes tokens
    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

    // this advance method consumes the current token and returns it
    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private ParseError error(Token token, String message) {
        Zealot.error(token, message);
        return new ParseError();
    }

    // we will discard the tokens until we find the semicolon and the next token
    // starting with for, if, return, etc
    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().type == SEMICOLON)
                return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }
            advance();
        }
    }

}
