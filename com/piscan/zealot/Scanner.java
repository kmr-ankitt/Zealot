package com.piscan.zealot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.piscan.zealot.TokenType.*;

class Scanner {

    // creating a map of identifier tokens
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();

        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    private final String source;

    // creating a list of tokens
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int line = 1;

    // scanning the tokens as string
    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {

        // we are at the beginning of the lexeme
        while (!isAtEnd()) {
            start = current;

            // we will scan tokens here
            scanToken();
        }

        // we are basically adding the scanned tokens to the list
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Identifing the scanned tokens
    private void scanToken() {

        // we are making current pointer point to the next character
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;

            // to handle the case of != , <= ,>= and all we are checking the next character
            // after every check
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            /*
             * This is similar to the other two-character operators, except that when we
             * find a second /, we don’t end the token yet. Instead, we keep consuming
             * characters until we reach the end of the line.
             */
            case '/':
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                }
                
                // Multiline comment
                else if (match('*')) {
                    int toMatch = 1;
                    while (!isAtEnd()) {
                        if (peek() == '*' && peekNext() == '/')
                            toMatch--;
                        if (peek() == '/' && peekNext() == '*')
                            toMatch++;
                        if (peek() == '\n')
                            line++;
                        if (toMatch == 0)
                            break;
                        advance();
                    }
                    if (peek() == '*' && peekNext() == '/') {
                        advance();
                        advance();
                    } else {
                        Zealot.error(line, "No closing of block comment.");
                    }
                } else {
                    addToken(SLASH);
                }
                break;

            // Ignoring whitespaces
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;

            // handling strings
            case '"':
                string();
                break;

            // it handles the identifiers like or
            // case 'o':
            // if (match('r')) {
            // addToken(OR);
            // }
            // break;

            // for handling the lexemes that are not available on the language like @ ^ #
            default:

                if (isDigit(c)) {
                    number();
                }

                else if (isAlpha(c)) {
                    identifier();
                }

                else {
                    Zealot.error(line, "Unexpected character.");
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(start, current);

        // Extracts the token type of the character
        TokenType type = keywords.get(text);
        if (type == null) {
            type = IDENTIFIER;
        }

        // addToken(IDENTIFIER); //Got my mind fucked fixing this small bug. It too
        
        // literally took 2 days to fix.
        addToken(type);
    }

    // checking for numbers
    private void number() {

        while (isDigit(peek()))
            advance();

        // looking for the fractional part
        if (peek() == '.' && isDigit(peekNext())) {
            advance();

            while (isDigit(peek()))
                advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));

    }

    // Checking for strings
    private void string() {

        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        if (isAtEnd()) {
            Zealot.error(line, "Unterminated String.");
            return;
        }

        // the closing "
        advance();

        /* we are only taking the value between the double quotes */
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);

    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    // this checks if the scanned lexeme is a alphabet or not
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // This function makes the current pointer point to the next Lexeme on every
    // iteration
    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
