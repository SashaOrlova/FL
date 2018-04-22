import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    public void SimpleTest() {
        Lexer l = new Lexer("x=1");
        ArrayList<Lexema> ans = l.result();
        assertEquals(new Ident(0, 1,0,"x").toString(), ans.get(0).toString());
        assertEquals(new Op(1,2,0, "=").toString(), ans.get(1).toString());
        assertEquals(new Num(2,3,0,"1").toString(), ans.get(2).toString());
    }

    @Test
    public void ExampleTest() {
        Lexer l = new Lexer("read x; if y + 1 == x then write y else write x");
        ArrayList<Lexema> ans = l.result();
        assertEquals(new KeyWord(0,4,0,"read").toString(), ans.get(0).toString());
        assertEquals(new Ident(5, 6, 0, "x").toString(), ans.get(1).toString());
        assertEquals(new Delimiter(6, 7, 0, ";").toString(), ans.get(2).toString());
        assertEquals(new KeyWord(8, 10, 0, "if").toString(), ans.get(3).toString());
        assertEquals(new Ident(11, 12, 0, "y").toString(), ans.get(4).toString());
        assertEquals(new Op(13,14, 0, "+").toString(), ans.get(5).toString());
        assertEquals(new Num(15,16,0,"1").toString(), ans.get(6).toString());
        assertEquals(new Op(17,19,0, "==").toString(), ans.get(7).toString());
        assertEquals(new Ident(20, 21, 0, "x").toString(), ans.get(8).toString());
        assertEquals(new KeyWord(22, 26, 0, "then").toString(), ans.get(9).toString());
        assertEquals(new KeyWord(27, 32, 0, "write").toString(), ans.get(10).toString());
        assertEquals(new Ident(33, 34, 0, "y").toString(), ans.get(11).toString());
        assertEquals(new KeyWord(35, 39, 0, "else").toString(), ans.get(12).toString());
        assertEquals(new KeyWord(40, 45, 0, "write").toString(), ans.get(13).toString());
        assertEquals(new Ident(46, 47, 0, "x").toString(), ans.get(14).toString());
    }

    @Test
    public void LinesTest() {
        Lexer l = new Lexer("read x;\n read y;");
        ArrayList<Lexema> ans = l.result();
        assertEquals(new KeyWord(0,4,0,"read").toString(), ans.get(0).toString());
        assertEquals(new Ident(5, 6, 0, "x").toString(), ans.get(1).toString());
        assertEquals(new Delimiter(6, 7, 0, ";").toString(), ans.get(2).toString());
        assertEquals(new KeyWord(9,13,1,"read").toString(), ans.get(3).toString());
        assertEquals(new Ident(14, 15, 1, "y").toString(), ans.get(4).toString());
        assertEquals(new Delimiter(15, 16, 1, ";").toString(), ans.get(5).toString());
    }

    @Test
    public void DifferentOperations() {
        Lexer l = new Lexer("read x;do x=y; write y");
        ArrayList<Lexema> ans = l.result();
        assertEquals(new KeyWord(0,4,0,"read").toString(), ans.get(0).toString());
        assertEquals(new Ident(5, 6, 0, "x").toString(), ans.get(1).toString());
        assertEquals(new Delimiter(6, 7, 0, ";").toString(), ans.get(2).toString());
        assertEquals(new KeyWord(7,9,0,"do").toString(), ans.get(3).toString());
        assertEquals(new Ident(10, 11, 0, "x").toString(), ans.get(4).toString());
        assertEquals(new Op(11,12,0, "=").toString(), ans.get(5).toString());
        assertEquals(new Ident(12, 13, 0, "y").toString(), ans.get(6).toString());
        assertEquals(new Delimiter(13, 14, 0, ";").toString(), ans.get(7).toString());
        assertEquals(new KeyWord(15,20,0,"write").toString(), ans.get(8).toString());
        assertEquals(new Ident(21, 22, 0, "y").toString(), ans.get(9).toString());
    }
}