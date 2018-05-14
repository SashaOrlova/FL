import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer {
    ArrayList<Lexema> lex = new ArrayList<>();
    public final static char CR  = (char) 0x0D;
    public final static char LF  = (char) 0x0A;
    public final static char SP  = (char) 0x20;
    public final static char HT  = (char) 0x09;
    public final static char FF  = (char) 0x1C;
    Pattern keyWord = Pattern.compile("(if|then|else|while|do|read|write)(\\s|\\().*");
    Pattern operator = Pattern.compile("(\\+|-|\\*|%|/|=|==|!=|>|<|>=|<=|&&|\\|\\|).*");
    Pattern number = Pattern.compile("(^[-]?[0-9]*[.,]?[0-9]+(?:[eE][-+]?[0-9]+)?).*");
    Pattern delimiter = Pattern.compile("([,;()]).*");

    String takeWord(String st) {
        int pos = 0;
            StringBuilder ans = new StringBuilder();
            while ((pos < st.length()) && ((st.charAt(pos) <= 'z' && st.charAt(pos) >= 'a') || (st.charAt(pos) <= 'Z' && st.charAt(pos) >= 'A') ||
                    (st.charAt(pos) <= '9' && st.charAt(pos) >= '0') || st.charAt(pos) == '_')) {
                ans.append(st.charAt(pos));
                pos++;
            }
            return ans.toString();
    }

    String takeNum(String st) {
        int pos = 0;
        StringBuilder ans = new StringBuilder();
        boolean wasPoint = false;
        boolean wasE = false;
        while ((pos < st.length())&&(st.charAt(pos) == 'e' ||  st.charAt(pos) == '.' && !wasPoint || (st.charAt(pos) == '+' && wasE) || (st.charAt(pos) <= '9' && st.charAt(pos) >= '0') || st.charAt(pos) == '-')) {
            if (st.charAt(pos) == '.')
                wasPoint = true;
            if (st.charAt(pos) == 'e')
                wasE = true;
            else
                wasE = false;
            ans.append(st.charAt(pos));
            pos++;
        }
        return ans.toString();
    }

    private boolean pageSeparator(char c) {
        return c == CR || c == LF || c == FF || c == '\n' || c == '\r' || c == '\f';
    }

    private boolean separator(char c) {
        return pageSeparator(c) || c == ' ' || c == SP || c == HT || c == '\t';
    }
    Lexer(String st) throws LexerError {
        int pos = 0;
        int row = 0;
        boolean lastNum = false;
        while (!Objects.equals(st, "")) {
            if (pageSeparator(st.charAt(0)))
                row++;
            if (separator(st.charAt(0))) {
                pos++;
                st = st.substring(1);
                lastNum = false;
                continue;
            }
            if (keyWord.matcher(st).lookingAt()) {
                String s = takeWord(st);
                lex.add(new KeyWord(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                lastNum = false;
                continue;
            }
            if (operator.matcher(st).lookingAt()) {
                String s = "";
                s += st.charAt(0);
                if (1 < st.length() && (st.charAt(1) == '=' || st.charAt(1) == '&' || st.charAt(1) == '|'))
                    s += st.charAt(1);
                lex.add(new Op(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                lastNum = false;
                continue;
            }
            if (number.matcher(st).lookingAt() && !lastNum) {
                String s = takeNum(st);
                lex.add(new Num(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                lastNum = true;
                continue;
            }
            if (st.startsWith("true")) {
                lex.add(new BooleanValue(pos, pos + 4, row, "true"));
                pos += 4;
                st = st.substring(4);
                continue;
            }
            if (st.startsWith("false")) {
                lex.add(new BooleanValue(pos, pos + 5, row, "false"));
                pos+= 5;
                st = st.substring(5);
                continue;
            }
            if (delimiter.matcher(st).lookingAt()) {
                String s = "";
                s += st.charAt(0);
                lex.add(new Delimiter(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                lastNum = false;
                continue;
            }
            if (!(st.charAt(0) <= 'z' && st.charAt(0) >= 'a') || (st.charAt(0) <= 'Z' && st.charAt(0) >= 'A') ||
                    (st.charAt(0) <= '9' && st.charAt(0) >= '0') || st.charAt(0) == '_')
                throw new LexerError("unexpected symbol at position " + pos);
            String s = takeWord(st);
            lex.add(new Ident(pos, pos + s.length(), row, s));
            st = st.substring(s.length());
            pos += s.length();
            lastNum = false;
        }
    }

    void print() {
        for (int i = 0; i < lex.size() - 1; i++) {
            System.out.println(lex.get(i).toString() + ", ");
        }
        if (lex.size() > 0)
            System.out.println(lex.get(lex.size() - 1));
    }

    ArrayList<Lexema> result() {
        return lex;
    }

    class LexerError extends Exception {
        public LexerError(String m) {
            super(m);
        }
    }
}
