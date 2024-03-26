package com.piscan.Zealot;

// Object type is used here cus it represents all datatypes in Zealot
class Interpreter implements Expr.Visitor<Object> {

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
                return -(double) right;
        }

        // unreachable
        return null;
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

    // we will now evaluate Binary operators
    // here also we evaluate the operant part first then we evaluate operator
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {

            case GREATER:
                return (double) left > (double) right;

            case GREATER_EQUAL:
                return (double) left >= (double) right;

            case LESS:
                return (double) left < (double) right;

            case LESS_EQUAL:
                return (double) left <= (double) right;

            case MINUS:
                return (double) left - (double) right;

            case PLUS:

                // this is addition of two numbers
                if (left instanceof Double && right instanceof Double)
                    return (double) left + (double) right;

                // this is concatination of two strings
                if (left instanceof String && right instanceof String)
                    return (String) left + (String) right;

            case SLASH:
                return (double) left / (double) right;

            case STAR:
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