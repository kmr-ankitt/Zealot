package com.piscan.zealot;

import java.util.HashMap;
import java.util.Map;


// this maintains mapping of variables to the corresponding value
class Environment {

    final Environment enclosing;

    // this is for global scope's environment
    Environment(){
        enclosing = null;
    }
    
    // this is for local scope's environment
    Environment(Environment enclosing){
        this.enclosing = enclosing;
    }

    private final Map<String, Object> values = new HashMap<>();

    Object get(Token name) {

        // if variable found then this simply returns the value mapped to it
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if(enclosing != null)
            return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // this is assignment part. The key difference between assignment and definition
    // is that assignment is not allowed to create a new variable.
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if(enclosing != null){
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    // we are basically mapping our variables with the values
    void define(String name, Object value) {
        values.put(name, value);
    }

}
