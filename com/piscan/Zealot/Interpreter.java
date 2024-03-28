package com.piscan.Zealot;

import com.piscan.Zealot.Zealot;

// Object type is used here cus it represents all datatypes in Zealot
class Interpreter implements Expr.Visitor<Object> {

    void interpret(Expr expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringify(value));
        } catch (RuntimeError error) {

            Zealot.runtimeError(error);

        }
    }

    // here we are converting literal tree node into runtime value
    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    // this takes the parenthesis part
    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {

        // we need to convert the inner expression
        return evaluate(expr.expression);
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
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

    // this take the Zealot value and converts it to string
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

}