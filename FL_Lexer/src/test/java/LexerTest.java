import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class LexerTest {
    @Test
    public void SimpleTest() throws Exception {
        Lexer l = new Lexer("x=1");
        ArrayList<Lexema> ans = l.result();
        assertEquals(new Ident(0, 1,0,"x").toString(), ans.get(0).toString());
        assertEquals(new Op(1,2,0, "=").toString(), ans.get(1).toString());
        assertEquals(new Num(2,3,0,"1").toString(), ans.get(2).toString());
    }

    @Test
    public void ExampleTest() throws Exception {
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
    public void LinesTest() throws Exception {
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
    public void DifferentOperations() throws Exception {
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

    @Test
    public void AllOperators() throws Exception {
        Lexer l = new Lexer("+ - * / % == != > >= < <= && ||");
        ArrayList<Lexema> ans = l.result();
        StringBuilder st = new StringBuilder();
        for (Lexema lem : ans) {
            st.append(lem.toString());
            st.append(' ');
        }
        assertEquals("OPERATOR_+(0,0,1) OPERATOR_-(0,2,3) OPERATOR_*(0,4,5) OPERATOR_/(0,6,7)" +
                        " OPERATOR_%(0,8,9) OPERATOR_==(0,10,12) OPERATOR_!=(0,13,15) OPERATOR_>(0,16,17) OPERATOR_>=(0,18,20)" +
                        " OPERATOR_<(0,21,22) OPERATOR_<=(0,23,25) OPERATOR_&&(0,26,28) OPERATOR_||(0,29,31) ",
                st.toString());
    }

    @Test
    public void SpaceSymbols() throws Exception {
        Lexer l = new Lexer("write" + Lexer.SP + "x if" + Lexer.LF + "(true)" + Lexer.FF);
        ArrayList<Lexema> ans = l.result();
        StringBuilder st = new StringBuilder();
        for (Lexema lem : ans) {
            st.append(lem.toString());
            st.append(' ');
        }
        assertEquals("KW_WRITE(0,0,5) IDENT(\"x\",0,6,7) KW_IF(0,8,10) DELIMITER(\"(\",1,11,12)" +
                " BOOL_VAL(\"true\",1,12,16) DELIMITER(\")\",1,16,17) ", st.toString());
    }

    @Test
    public void IdentTest() throws Exception {
        Lexer l = new Lexer("x=10, cat = 100, t = true, dog     =            1e10");
        ArrayList<Lexema> ans = l.result();
        StringBuilder st = new StringBuilder();
        for (Lexema lem : ans) {
            st.append(lem.toString());
            st.append(' ');
        }
        assertEquals("IDENT(\"x\",0,0,1) OPERATOR_=(0,1,2) NUM(\"10\",0,2,4) DELIMITER(\",\",0,4,5) " +
                "IDENT(\"cat\",0,6,9) OPERATOR_=(0,10,11) NUM(\"100\",0,12,15) DELIMITER(\",\",0,15,16) " +
                "IDENT(\"t\",0,17,18) OPERATOR_=(0,19,20) BOOL_VAL(\"true\",0,21,25) DELIMITER(\",\",0,25,26)" +
                " IDENT(\"dog\",0,27,30) OPERATOR_=(0,35,36) NUM(\"1e10\",0,48,52) ", st.toString());
    }
}