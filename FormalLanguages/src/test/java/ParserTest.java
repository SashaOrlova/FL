import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {
    @Test
    public void SimpleTestParseF() throws Exception {
        String s = "12";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseF();
        assertEquals(12, ((Number)t.inf).n);
    }

    @Test
    public void SimpleTestParseTerm() throws Exception {
        String s = "12";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseTerm();
        assertEquals(12, ((Number)t.inf).n);
    }

    @Test
    public void SimpleTestParseExpr() throws Exception {
        String s = "12";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals(12, ((Number)t.inf).n);
    }

    @Test
    public void SimpleTestWithOperationPlus() throws Exception {
        String s = "1+2";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals('+', ((Operation)t.saved.inf).c);
        assertEquals(1, ((Number)t.leftSon.inf).n);
        assertEquals(2, ((Number)t.rightSon.inf).n);
    }

    @Test
    public void SimpleTestBrackets() throws Exception {
        String s = "(1)";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals(1, ((Number)t.saved.inf).n);
        assertEquals('(', ((Operation)t.leftSon.inf).c);
        assertEquals(')', ((Operation)t.rightSon.inf).c);
    }

    @Test
    public void SimpleTestManyOperations() throws Exception {
        String s = "1+2+3";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals('+', ((Operation)t.saved.inf).c);
        assertEquals(3, ((Number)t.rightSon.inf).n);
        assertEquals('+', ((Operation)t.leftSon.saved.inf).c);
        assertEquals(2, ((Number)t.leftSon.rightSon.inf).n);
        assertEquals(1, ((Number)t.leftSon.leftSon.inf).n);
    }

    @Test
    public void SimpleTestManyDifferentOperations() throws Exception {
        String s = "1+2*3";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals('+', ((Operation)t.saved.inf).c);
        assertEquals(1, ((Number)t.leftSon.inf).n);
        assertEquals('*', ((Operation)t.rightSon.saved.inf).c);
        assertEquals(3, ((Number)t.rightSon.rightSon.inf).n);
        assertEquals(2, ((Number)t.rightSon.leftSon.inf).n);
    }

    @Test
    public void SimpleTestUnaryMinus() throws Exception {
        String s = "1*-2";
        Lexer l = new Lexer(s);
        Parser p = new Parser(l.getResult());
        Tree t = p.parseExpr();
        assertEquals('*', ((Operation)t.saved.inf).c);
        assertEquals(1, ((Number)t.leftSon.inf).n);
        assertEquals(-2, ((Number)t.rightSon.inf).n);
    }
}