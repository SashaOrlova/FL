import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    public void SimpleTest() throws Exception {
        String st = "13";
        Lexer l = new Lexer(st);
        assertEquals(13, ((Number)l.getResult().get(0)).n);
    }

    @Test
    public void SimpleTestPlus() throws Exception {
        String st = "13+123";
        Lexer l = new Lexer(st);
        assertEquals(13, ((Number)l.getResult().get(0)).n);
        assertEquals('+', ((Operation)l.getResult().get(1)).c);
        assertEquals(123, ((Number)l.getResult().get(2)).n);
    }

    @Test
    public void TestAllOperations() throws Exception {
        String st = "13+123*3/8888";
        Lexer l = new Lexer(st);
        assertEquals(13, ((Number)l.getResult().get(0)).n);
        assertEquals('+', ((Operation)l.getResult().get(1)).c);
        assertEquals(123, ((Number)l.getResult().get(2)).n);
        assertEquals('*', ((Operation)l.getResult().get(3)).c);
        assertEquals(3, ((Number)l.getResult().get(4)).n);
        assertEquals('/', ((Operation)l.getResult().get(5)).c);
        assertEquals(8888, ((Number)l.getResult().get(6)).n);
    }

    @Test
    public void TestAllOperationsWithBrackets() throws Exception {
        String st = "(1+2)*3/10";
        Lexer l = new Lexer(st);
        assertEquals('(', ((Operation)l.getResult().get(0)).c);
        assertEquals(1, ((Number)l.getResult().get(1)).n);
        assertEquals('+', ((Operation)l.getResult().get(2)).c);
        assertEquals(2, ((Number)l.getResult().get(3)).n);
        assertEquals(')', ((Operation)l.getResult().get(4)).c);
        assertEquals('*', ((Operation)l.getResult().get(5)).c);
        assertEquals(3, ((Number)l.getResult().get(6)).n);
        assertEquals('/', ((Operation)l.getResult().get(7)).c);
        assertEquals(10, ((Number)l.getResult().get(8)).n);
    }

    @Test
    public void TestUnaryMinus() throws Exception {
        String st = "2*-3";
        Lexer l = new Lexer(st);
        assertEquals(2, ((Number)l.getResult().get(0)).n);
        assertEquals('*', ((Operation)l.getResult().get(1)).c);
        assertEquals(-3, ((Number)l.getResult().get(2)).n);
    }
}