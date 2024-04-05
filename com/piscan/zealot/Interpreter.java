package com.piscan.zealot;

import static com.piscan.zealot.TokenType.BANG;
import static com.piscan.zealot.TokenType.BANG_EQUAL;
import static com.piscan.zealot.TokenType.EQUAL_EQUAL;
import static com.piscan.zealot.TokenType.GREATER;
import static com.piscan.zealot.TokenType.GREATER_EQUAL;
import static com.piscan.zealot.TokenType.LESS;
import static com.piscan.zealot.TokenType.LESS_EQUAL;
import static com.piscan.zealot.TokenType.MINUS;
import static com.piscan.zealot.TokenType.PLUS;
import static com.piscan.zealot.TokenType.SLASH;
import static com.piscan.zealot.TokenType.STAR;

// import com.piscan.zealot.Zealot;

import java.util.ArrayList;
import java.util.List;

// Object type is used here cus it represents all datatypes in zealot
class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    // We store variables as a field directly in Interpreter so that the variables
    // stay in memory as long as the interpreter is still running.
    // private Environment environment = new Environment();

    final Environment globals = new Environment();
    private Environment environment = globals;

    // If wanted to add new functionalites we can do it from here only like: Reading
    // input from the user, working with files, etc.—we could add them each as their
    // own anonymous class that implements LoxCallable.
    Interpreter() {

        // this will print the time taken to run the program
        globals.define("clock", new ZealotCallable() {
            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native fn>";
            }
        });
    }

    // void interpret(Expr expression) {
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {

            Zealot.runtimeError(error);

        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private void execute(Stmt stmt) {
        stmt.accept(this);
    }

    void executeBlock(List<Stmt> statements, Environment environment) {

        // visits all of the statements, and then restores the previous value
        Environment previous = this.environment;
        try {
            this.environment = environment;

            for (Stmt statement : statements) {
                execute(statement);
            }
        }

        // it restores the previous environment using a finally clause.
        finally {
            this.environment = previous;
        }
    }

    // this is for block statements
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    // this takes the expression part
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override 
    public Void visitFunctionStmt(Stmt.Function stmt){
        ZealotFunction function = new ZealotFunction(stmt , environment);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitIfStmt(Stmt.If stmt) {

        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }

        return null;
    }

    // this takes the statement part
    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt){
        Object value = null;
        if(stmt.value != null) 
            value = evaluate(stmt.value);

        throw new Return(value);
    }

    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }

        // if value is not initialized, it will be intialized with nill
        environment.define(stmt.name.lexeme, value);
        return null;
    }

    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
        while (isTruthy(evaluate(stmt.condtion))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);

        return value;
    }

    // we will now evaluate Binary operators
    // here also we evaluate the operant part first then we evaluate operator
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {

            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;

            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;

            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;

            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;

            case MINUS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;

            case PLUS:

                // this is addition of two numbers
                if (left instanceof Double && right instanceof Double)
                    return (double) left + (double) right;

                // this is concatination of two strings
                if (left instanceof String && right instanceof String)
                    return (String) left + (String) right;

                // throws this when operand is neither string or number
                throw new RuntimeError(expr.operator,
                        "Operands must be two numbers or two strings.");

            case SLASH:
                checkNumberOperands(expr.operator, left, right);
                return (double) left / (double) right;

            case STAR:
                checkNumberOperands(expr.operator, left, right);
                return (double) left * (double) right;

            case BANG_EQUAL:
                return !isEqual(left, right);

            case EQUAL_EQUAL:
                return isEqual(left, right);
        }

        // unreachable
        return null;

    }

    @Override
    public Object visitCallExpr(Expr.Call expr) {
        Object callee = evaluate(expr.callee);

        // firstly we evalute the expression for the callee
        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }

        // this is to handle this case: "totally not a function"();
        if (!(callee instanceof ZealotCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes.");
        }

        ZealotCallable function = (ZealotCallable) callee;

        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren,
                    "Expected " + function.arity() + "arguments but got" +
                            arguments.size() + ".");
        }
        return function.call(this, arguments);
    }

    // this takes the parenthesis part
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {

        // we need to convert the inner expression
        return evaluate(expr.expression);
    }

    // here we are converting literal tree node into runtime value
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);

        if (expr.operator.type == TokenType.OR) {
            if (isTruthy(left))
                return left;
        } else {
            if (!isTruthy(left))
                return left;
        }

        return evaluate(expr.right);
    }

    // this is to convert the unary expression
    // We do Post order traversal here
    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {

        // first we evaluate the right part and then operator
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:

                // checking right operant is choosed
                checkNumberOperand(expr.operator, right);
                return -(double) right;
        }

        // unreachable
        return null;
    }

    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
        return environment.get(expr.name);
    }

    private boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        // equals is a builin java fuction to check if it is equal
        return a.equals(b);
    }

    // this will check wheather the operand is a interger or not
    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number. ");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers. ");
    }

    // this take the zealot value and converts it to string
    private String stringify(Object object) {

        if (object == null)
            return "nill";

        if (object instanceof Double) {
            String text = object.toString();

            // means that it is like 8.0
            if (text.endsWith(".0"))
                text = text.substring(0, text.length() - 2);
            return text;
        }

        return object.toString();
    }

}