import java.util.ArrayList;

public class Parser {
    ArrayList<Token> expr;
    Parser(ArrayList<Token> ar) {
        expr = ar;
    }

    public Tree parseExpr() throws ParserException {
        Tree ret = parseTerm();
        if (expr.size() > 0) {
            Token tok = expr.get(expr.size() - 1);
            if (tok instanceof Operation && (((Operation) tok).c == '+' || ((Operation) tok).c == '-')) {
                expr.remove(expr.size() - 1);
                Tree ret2 = parseExpr();
                return new Tree(new Tree(tok, "Operation"), ret2, ret, "Expr");
            }
        }
        return ret;
    }

    public Tree parseTerm() throws ParserException {
        Tree ret = parseF();
        if (expr.size() > 0) {
        Token tok = expr.get(expr.size() - 1);
            if (tok instanceof Operation && (((Operation) tok).c == '*' || ((Operation) tok).c == '/')){
                expr.remove(expr.size() - 1);
                Tree ret2 = parseTerm();
                return new Tree(new Tree(tok, "Operation"), ret2, ret, "Term");
            }
        }
        return ret;
    }

    public Tree parseF() throws ParserException {
        Token tok = expr.get(expr.size() - 1);
        if (tok instanceof Number) {
            Tree t = new Tree(expr.get(expr.size() - 1), "Number");
            expr.remove(expr.size() - 1);
            return t;
        }
        if (tok instanceof Operation) {
            if (((Operation) tok).c == ')') {
                expr.remove(expr.size() - 1);
                Tree ret = parseExpr();
                if (expr.get(expr.size() - 1) instanceof Operation && ((Operation)expr.get(expr.size() - 1)).c == '(')
                    expr.remove(expr.size() - 1);
                else
                    throw new ParserException("Parse error at symbol " + expr.size());
                return new Tree(ret, new Tree(new Operation('('), "Operation"), new Tree(new Operation(')'), "Operation"), "Operation");
            }
        }
        return null;
    }

    static class ParserException extends Exception {
        ParserException(String s) {
            super(s);
        }
    }
}
