import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    ArrayList<Lexema> lex = new ArrayList<>();

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
        while ((pos < st.length())&&(st.charAt(pos) == 'e' || st.charAt(pos) == '+' || (st.charAt(pos) <= '9' && st.charAt(pos) >= '0') || st.charAt(pos) == '-')) {
            ans.append(st.charAt(pos));
            pos++;
        }
        return ans.toString();
    }

    Lexer(String st) {
        Pattern keyWord = Pattern.compile("(if|then|else|while|do|read|write)(\\s|\\().*");
        Pattern operator = Pattern.compile("(\\+|-|\\*| %|//|=|==|!=|>|<|>=|<=|&&|\\|\\|).*");
        Pattern number = Pattern.compile("^[-+]?[0-9]*[.,]?[0-9]+(?:[eE][-+]?[0-9]+)?.*");
        Pattern delimiter = Pattern.compile("([,;()]).*");
        int pos = 0;
        int row = 0;
        while (!Objects.equals(st, "")) {
            if (st.charAt(0) == '\n')
                row++;
            if (st.charAt(0) == ' ' || st.charAt(0) == '\n') {
                pos++;
                st = st.substring(1);
                continue;
            }
            if (keyWord.matcher(st).lookingAt()) {
                String s = takeWord(st);
                lex.add(new KeyWord(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
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
                continue;
            }
            if (number.matcher(st).lookingAt()) {
                String s = takeNum(st);
                lex.add(new Num(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                continue;
            }
            if (delimiter.matcher(st).lookingAt()) {
                String s = "";
                s += st.charAt(0);
                lex.add(new Delimiter(pos, pos + s.length(), row, s));
                st = st.substring(s.length());
                pos += s.length();
                continue;
            }
            String s = takeWord(st);
            lex.add(new Ident(pos, pos + s.length(), row, s));
            st = st.substring(s.length());
            pos += s.length();
        }
    }

    void print() {
        for (Lexema l : lex) {
            System.out.print(l.toString() + ", ");
        }
    }

    ArrayList<Lexema> result() {
        return lex;
    }
}
