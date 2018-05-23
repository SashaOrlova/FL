import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void findFirstPlus() throws Exception {
        String expr = "(1+2)+4*5+4";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        assertEquals(9, p.findFirstPlus(0, expr.length()));
        expr = "(1+2)+3-3";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(7, p.findFirstPlus(0, expr.length()));
        expr = "(1+2)+(1*4)*5";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(5, p.findFirstPlus(0, expr.length()));
        expr = "((1+2)+(1*4))*5";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(-1, p.findFirstPlus(0, expr.length()));
    }

    @Test
    public void findFirstMult() throws Exception {
        String expr = "(1+2)+4*5+4";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        assertEquals(7, p.findFirstMult(0, expr.length()));
        expr = "((1+2)+(1*4))*5";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(13, p.findFirstMult(0, expr.length()));
        expr = "(((1+2)+(1*4))*(5-4))^4";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(-1, p.findFirstMult(0, expr.length()));
        expr = "(1+3)*(1*4+2)";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(5, p.findFirstMult(0, expr.length()));
    }

    @Test
    public void findFirstPow() throws Exception {
        String expr = "(1+2)^5";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        assertEquals(5, p.findFirstPow(0, expr.length()));
        expr = "((1+2)+(1*4)^3)^(5^4)";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(15, p.findFirstPow(0, expr.length()));
        expr = "(((1+2)^(1*4))*(5-4))+4";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(-1, p.findFirstPow(0, expr.length()));
        expr = "(1+3*7)^(1*4+2)+6*7";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(7, p.findFirstPow(0, expr.length()));
    }

    @Test
    public void findSemicolon() throws Exception {
        String expr = "1+3;";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        assertEquals(3, p.findSemicolon(0, expr.length(), ";"));
        expr = "(1+3*7); (1*4+2)+6*7;";
        l = new Lexer(expr);
        p = new Parser(l.result());
        assertEquals(7, p.findSemicolon(0, expr.length(), ";"));
    }

    @Test
    public void parseMathExpr() throws Exception {
        String expr = "(38+44)*42-5";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        Tree.Vertex v = p.parseMathExpr(0, 9);
        assertEquals(new Op(10, 11, 0, "-").toString(), v.content.toString());
        assertEquals(new Num(11, 12, 0, "5").toString(), v.children.get(1).content.toString());
        v = v.children.get(0);
        assertEquals(new Op(7,8,0, "*").toString(), v.content.toString());
        assertEquals(new Num(8,10,0, "42").toString(), v.children.get(1).content.toString());
        v = v.children.get(0);
        assertEquals(new Op(3,4,0, "+").toString(), v.content.toString());
        assertEquals(new Num(1,3,0, "38").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(4,6,0, "44").toString(), v.children.get(1).content.toString());

        expr = "(28-3)^4+28*76-(89*67)";
        l = new Lexer(expr);
        p = new Parser(l.result());
        v = p.parseMathExpr(0, 17);
        assertEquals(new Op(14,15,0, "-").toString(), v.content.toString());
        Tree.Vertex left = v.children.get(0);
        Tree.Vertex right = v.children.get(1);
        assertEquals(new Op(18, 19, 0, "*").toString(), right.content.toString());
        assertEquals(new Num(16, 18, 0, "89").toString(), right.children.get(0).content.toString());
        assertEquals(new Num(19,21,0,"67").toString(), right.children.get(1).content.toString());
        assertEquals(new Op(8,9,0,"+").toString(), left.content.toString());
        right = left.children.get(1);
        assertEquals(new Op(11,12,0,"*").toString(), right.content.toString());
        assertEquals(new Num(9,11,0, "28").toString(), right.children.get(0).content.toString());
        assertEquals(new Num(12,14,0, "76").toString(), right.children.get(1).content.toString());
        v = left.children.get(0);
        assertEquals(new Op(6,7,0,"^").toString(), v.content.toString());
        assertEquals(new Num(7,8,0,"4").toString(), v.children.get(1).content.toString());
        v = v.children.get(0);
        assertEquals(new Op(3,4,0,"-").toString(), v.content.toString());
        assertEquals(new Num(1,3,0,"28").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(4,5,0,"3").toString(), v.children.get(1).content.toString());
    }

    @Test
    public void parseMathTerm() throws Exception {
        String expr = "3*5";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        Tree.Vertex v = p.parseMathTerm(0, expr.length());
        assertEquals(new Op(1,2,0, "*").toString(), v.content.toString());
        assertEquals(new Num(0,1,0, "3").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(2,3,0, "5").toString(), v.children.get(1).content.toString());
        expr = "3*(4^5)*4";
        l = new Lexer(expr);
        p = new Parser(l.result());
        v = p.parseMathTerm(0, expr.length());
        assertEquals(new Op(7,8,0, "*").toString(), v.content.toString());
        assertEquals(new Num(8,9,0, "4").toString(), v.children.get(1).content.toString());
        v = v.children.get(0);
        assertEquals(new Op(1,2,0, "*").toString(), v.content.toString());
        assertEquals(new Num(0,1,0, "3").toString(), v.children.get(0).content.toString());
        v = v.children.get(1);
        assertEquals(new Op(4,5,0, "^").toString(), v.content.toString());
        assertEquals(new Num(3,4,0, "4").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(5,6,0, "5").toString(), v.children.get(1).content.toString());
    }

    @Test
    public void parseMathFact() throws Exception {
        String expr = "3^5";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        Tree.Vertex v = p.parseMathFact(0, expr.length());
        assertEquals(new Op(1,2,0, "^").toString(), v.content.toString());
        assertEquals(new Num(0,1,0, "3").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(2,3,0, "5").toString(), v.children.get(1).content.toString());
        expr = "2^(2)";
        l = new Lexer(expr);
        p = new Parser(l.result());
        v = p.parseMathFact(0, expr.length());
        assertEquals(new Op(1,2,0, "^").toString(), v.content.toString());
        assertEquals(new Num(0,1,0, "2").toString(), v.children.get(0).content.toString());
        assertEquals(new Num(3,4,0, "2").toString(), v.children.get(1).content.toString());

    }

    @Test
    public void parseMathSmall() throws Exception {
        String expr = "3";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        assertEquals(new Lexema(0, 1, 0, "3"), p.parseMathSmall(0, expr.length()).content);
    }

    @Test
    public void minusTest() throws Exception {
        String expr = "(-1)";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        p.preParser();
        assertEquals(new Num(0, 4, 0, "-1"), p.parseMathSmall(0, 1).content);
    }

    @Test
    public void sumWithMinus() throws Exception {
        String expr = "3+(-1)";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseMathExpr(0, expr.length() - 3);
        assertEquals(new Op(1, 2, 0, "+"), v.content);
        assertEquals(new Num(0, 1, 0, "3"), v.children.get(0).content);
        assertEquals(new Num(2, 6, 0, "-1"), v.children.get(1).content);
    }

    @Test
    public void logicExpr() throws Exception {
        String expr = "1+5==2*3";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseLogic(0, 7);
        assertEquals(new Op(3, 5, 0, "=="), v.content);
        Tree.Vertex left =  v.children.get(0);
        Tree.Vertex right = v.children.get(1);
        assertEquals(new Op(1, 2, 0, "+"), left.content);
        assertEquals(new Op(6, 7, 0, "*"), right.content);
        assertEquals(new Num(0, 1, 0, "1"), left.children.get(0).content);
        assertEquals(new Num(2, 3, 0, "5"), left.children.get(1).content);
        assertEquals(new Num(5, 6, 0, "2"), right.children.get(0).content);
        assertEquals(new Num(7, 8, 0, "3"), right.children.get(1).content);
    }

    @Test
    public void exprWithIdent() throws Exception {
        String expr = "x<y";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseLogic(0, 3);
        assertEquals(new Op(1, 2, 0, "<"), v.content);
        assertEquals(new Ident(0, 1, 0, "x"), v.children.get(0).content);
        assertEquals(new Ident(2, 3, 0, "y"), v.children.get(1).content);
    }

    @Test
    public void testAssignment() throws Exception {
        String expr = "x=(-2)";
        Lexer l = new Lexer(expr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseAssignment(0, 3);
        assertEquals(new Op(1, 2, 0, "="), v.content);
        assertEquals(new Ident(0, 1, 0, "x"), v.children.get(0).content);
        assertEquals(new Num(2, 6, 0, "-2"), v.children.get(1).content);
    }

    @Test
    public void simpleFunction() throws Exception {
        String program = "function fun(i,j){" +
                "x=y;}";
        Lexer l = new Lexer(program);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseFunctionDefinition(0, 13);
        Tree.Vertex args = v.children.get(0);
        Tree.Vertex body = v.children.get(1);
        assertEquals(new Ident(9, 12, 0, "fun"), v.content);
        assertEquals(new Ident(13, 14, 0, "i"), args.children.get(0).content);
        assertEquals(new Ident(15, 16, 0, "j"), args.children.get(1).content);
        assertEquals(new Op(19, 20, 0, "="), body.children.get(0).content);
        assertEquals(new Ident(18, 19, 0, "x"), body.children.get(0).children.get(0).content);
        assertEquals(new Ident(20, 21, 0, "y"), body.children.get(0).children.get(1).content);
    }

    @Test
    public void longFunction() throws Exception {
        String program = "function mult(){" +
                "x=y;" +
                "i=(-1);" +
                "j=3;" +
                "y=i*j;" +
                "}";
        Lexer l = new Lexer(program);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseFunctionDefinition(0, 24);
        Tree.Vertex args = v.children.get(0);
        Tree.Vertex body = v.children.get(1);
        assertEquals(0, args.children.size());
        assertEquals(new Ident(9,13, 0, "mult"), v.content);
        Tree.Vertex expr1 = body.children.get(0);
        Tree.Vertex expr2 = body.children.get(1);
        Tree.Vertex expr3 = body.children.get(2);
        Tree.Vertex expr4 = body.children.get(3);
        assertEquals(new Op(17,18,0,"="), expr1.content);
        assertEquals(new Ident(16,17,0,"x"), expr1.children.get(0).content);
        assertEquals(new Ident(18,19,0,"y"), expr1.children.get(1).content);
        assertEquals(new Op(21,22,0,"="), expr2.content);
        assertEquals(new Ident(20,21,0,"i"), expr2.children.get(0).content);
        assertEquals(new Num(22,26,0,"-1"), expr2.children.get(1).content);
        assertEquals(new Op(28,29,0,"="), expr3.content);
        assertEquals(new Ident(27,28,0,"j"), expr3.children.get(0).content);
        assertEquals(new Num(29,30,0,"3"), expr3.children.get(1).content);
        assertEquals(new Op(32,33,0,"="), expr4.content);
        assertEquals(new Ident(31,32,0,"y"), expr4.children.get(0).content);
        assertEquals(new Op(34,35,0,"*"), expr4.children.get(1).content);
        assertEquals(new Ident(33,34,0,"i"), expr4.children.get(1).children.get(0).content);
        assertEquals(new Ident(35,36,0,"j"), expr4.children.get(1).children.get(1).content);
    }

    @Test
    public void simpleCall() throws Exception {
        String call = "write(i,j)";
        Lexer l = new Lexer(call);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseFunctionCall(0, 6);
        assertEquals(new KeyWord(0,5,0,"write"), v.content);
        assertEquals(new Ident(6,7,0,"i"), v.children.get(0).content);
        assertEquals(new Ident(8,9,0,"j"), v.children.get(1).content);
    }

    @Test
    public void callWithCalls() throws Exception {
        String call = "write(write(x),y,z)";
        Lexer l = new Lexer(call);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseFunctionCall(0, 11);
        assertEquals(new KeyWord(0,5,0,"write"), v.content);
        assertEquals(new KeyWord(6,11,0,"write"), v.children.get(0).content);
        assertEquals(new Ident(12,13,0,"x"), v.children.get(0).children.get(0).content);
        assertEquals(new Ident(15,16,0,"y"), v.children.get(1).content);
        assertEquals(new Ident(17,18,0,"z"), v.children.get(2).content);
    }

    @Test
    public void callWithExpr() throws Exception {
        String call = "write(2+4,read(i),z)";
        Lexer l = new Lexer(call);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseFunctionCall(0, 13);
        assertEquals(new KeyWord(0,5,0,"write"), v.content);
        Tree.Vertex arg1 = v.children.get(0);
        Tree.Vertex arg2 = v.children.get(1);
        Tree.Vertex arg3 = v.children.get(2);
        assertEquals(new Op(7,8,0,"+"), arg1.content);
        assertEquals(new Num(6,7,0,"2"), arg1.children.get(0).content);
        assertEquals(new Num(8,9,0,"4"), arg1.children.get(1).content);
        assertEquals(new KeyWord(10,14,0,"read"), arg2.content);
        assertEquals(new Ident(15,16,0,"i"), arg2.children.get(0).content);
        assertEquals(new Ident(18,19,0,"z"), arg3.content);
    }

    @Test
    public void simpleIf() throws Exception {
        String ifPr = "if (2==3) then { x=4; } else { y=3; }";
        Lexer l = new Lexer(ifPr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseIf(0, 20);
        assertEquals(new KeyWord(0,2,0,"if"), v.content);
        Tree.Vertex cond = v.children.get(0);
        Tree.Vertex thenB = v.children.get(1);
        Tree.Vertex elseB = v.children.get(2);
        assertEquals(new Op(5,7,0, "=="), cond.content);
        assertEquals(new Num(4,5,0, "2"), cond.children.get(0).content);
        assertEquals(new Num(7,8,0, "3"), cond.children.get(1).content);
        assertEquals(new KeyWord(10,14,0, "then"), thenB.content);
        assertEquals(new Op(18,19,0, "="), thenB.children.get(0).content);
        assertEquals(new Ident(17,18,0, "x"), thenB.children.get(0).children.get(0).content);
        assertEquals(new Ident(19,20,0, "4"), thenB.children.get(0).children.get(1).content);
        assertEquals(new KeyWord(24,28,0, "else"), elseB.content);
        assertEquals(new Op(32,33,0, "="), elseB.children.get(0).content);
        assertEquals(new Ident(31,32,0, "y"), elseB.children.get(0).children.get(0).content);
        assertEquals(new Ident(33,34,0, "3"), elseB.children.get(0).children.get(1).content);
    }

    @Test
    public void ifAndIf() throws Exception {
        String ifPr = "if (1) then {3;} else { if (0 == 1) then {4;} else {3+5;};}";
        Lexer l = new Lexer(ifPr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseIf(0, l.result().size());
        Tree.Vertex cond1 = v.children.get(0);
        Tree.Vertex thenB1 = v.children.get(1);
        Tree.Vertex elseB1 = v.children.get(2);
        Tree.Vertex cond2 = elseB1.children.get(0).children.get(0);
        Tree.Vertex thenB2 = elseB1.children.get(0).children.get(1);
        Tree.Vertex elseB2 = elseB1.children.get(0).children.get(2);
        assertEquals(new KeyWord(0,2,0,"if"), v.content);
        assertEquals(new Num(4,5,0, "1"), cond1.content);
        assertEquals(new KeyWord(7,11,0, "then"), thenB1.content);
        assertEquals(new Num(13,14,0, "3"), thenB1.children.get(0).content);
        assertEquals(new KeyWord(17,21,0, "else"), elseB1.content);
        assertEquals(new Op(30,32,0, "=="), cond2.content);
        assertEquals(new Num(28,29,0, "0"), cond2.children.get(0).content);
        assertEquals(new Num(33,34,0, "1"), cond2.children.get(1).content);
        assertEquals(new Num(42,43,0, "4"), thenB2.children.get(0).content);
        assertEquals(new Op(53,54,0, "+"), elseB2.children.get(0).content);
        assertEquals(new Num(52,53,0, "3"), elseB2.children.get(0).children.get(0).content);
        assertEquals(new Num(54,55,0, "5"), elseB2.children.get(0).children.get(1).content);
    }

    @Test
    public void ManyFunctions() throws Exception {
        String program = "function fun(i,j){" +
                "x=y;}" +
                "function mult(){" +
                "x=y;" +
                "i=(-1);" +
                "j=3;" +
                "y=i*j;" +
                "}";
        Lexer l = new Lexer(program);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseListFunction(0, l.result().size());
        v = v.children.get(0);
        Tree.Vertex args = v.children.get(0);
        Tree.Vertex body = v.children.get(1);
        assertEquals(new Ident(9, 12, 0, "fun"), v.content);
        assertEquals(new Ident(13, 14, 0, "i"), args.children.get(0).content);
        assertEquals(new Ident(15, 16, 0, "j"), args.children.get(1).content);
        assertEquals(new Op(19, 20, 0, "="), body.children.get(0).content);
        assertEquals(new Ident(18, 19, 0, "x"), body.children.get(0).children.get(0).content);
        assertEquals(new Ident(20, 21, 0, "y"), body.children.get(0).children.get(1).content);
    }

    @Test
    public void ErrorTest() throws Exception {
        String program = "x = 3 +";
        Lexer l = new Lexer(program);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseExpr(0, l.result().size());
        assertEquals("Error in OPERATOR_+(0,6,7) parser math expr\n", p.errors);
    }

    @Test
    public void FunTest() throws Exception {
        String  pr = "function fun(i){i=2;}";
        Lexer l = new Lexer(pr);
        Parser p = new Parser(l.result());
        p.preParser();
        Tree.Vertex v = p.parseListFunction(0, l.result().size());
        assertEquals("", p.errors);
    }
}