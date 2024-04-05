package com.piscan.zealot;

import java.util.List;

// We don’t want the runtime phase of the interpreter to bleed into the front end’s syntax classes so we don’t want Stmt.Function itself to implement that. Instead, we wrap it in a new class. 
class ZealotFunction implements ZealotCallable {

    private final Stmt.Function declaration;

    private final Environment closure;

    ZealotFunction(Stmt.Function declaration, Environment closure) {
        this.closure = closure;
        this.declaration = declaration;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    @Override
    public int arity() {
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        // Environment environment = new Environment(interpreter.globals);
        Environment environment = new Environment(closure);

        // this handles recursive environment
        for (int i = 0; i < declaration.params.size(); i++) {

            // binding part
            environment.define(declaration.params.get(i).lexeme, arguments.get(i));
        }

        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            return returnValue.value;
        }
        
        return null;
    }
}
