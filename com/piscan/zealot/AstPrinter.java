package com.piscan.zealot;

class AstPrinter implements Expr.Visitor<String> {
    String print(Expr expr) {
        return expr.accept(this);
    }

    // Implementing visitBinaryExpr method for handling binary expressions
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.operator.lexeme,
                expr.left, expr.right);
    }

    // Implementing visitGroupingExpr method for handling grouping expressions
    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    // Implementing visitLiteralExpr method for handling literal expressions
    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if (expr.value == null)
            return "nil";
        return expr.value.toString();
    }

    // Implementing visitUnaryExpr method for handling unary expressions
    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    // Implementing visitVariableExpr method for handling variable expressions
    @Override
    public String visitVariableExpr(Expr.Variable expr) {
        return expr.name.lexeme;
    }

    // Implementing visitAssignExpr method for handling assignment expressions
    @Override
    public String visitAssignExpr(Expr.Assign expr) {
        return parenthesize("assign", expr.name.lexeme, expr.value);
    }

    private String parenthesize(String name, Object... parts) {
        StringBuilder builder = new StringBuilder();

        builder.append("(").append(name);
        for (Object part : parts) {
            builder.append(" ");
            if (part instanceof Expr) {
                builder.append(((Expr) part).accept(this));
            } else {
                builder.append(part);
            }
        }
        builder.append(")");

        return builder.toString();
    }

    // Main method for testing
    public static void main(String[] args) {
        Expr expression = new Expr.Binary(
                new Expr.Unary(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new Expr.Literal(123)),
                new Token(TokenType.STAR, "*", null, 1),
                new Expr.Grouping(
                        new Expr.Literal(45.67)));

        System.out.println(new AstPrinter().print(expression));
    }
}
