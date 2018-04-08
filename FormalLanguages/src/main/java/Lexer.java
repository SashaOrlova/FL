import java.lang.reflect.Executable;
import java.util.ArrayList;

public class Lexer {
    private ArrayList<Token> ar = new ArrayList<>();
    Lexer(String st) throws LexerException {
        char[] s = st.toCharArray();
        int i = 0;
        boolean isPrewOp = false;
        boolean isBr = false;
        while (i < st.length()) {
            if (!isPrewOp && (s[i] == '+' || s[i] == '-' || s[i] == '*' || s[i] == '/' || s[i] == '^' )) {
                ar.add(new Operation(s[i]));
                i++;
                isPrewOp = true;
                isBr = false;
                continue;
            }
            if (s[i] == ')' || s[i] == '(') {
                ar.add(new Operation(s[i]));
                isBr = true;
                isPrewOp = false;
                i++;
                continue;
            }
            if ((isPrewOp || isBr) && s[i] == '-' || s[i] - '0' >= 0 && s[i] - '0' <= 9) {
                int num = 0;
                int negative = 1;
                if ((isPrewOp || isBr)&& s[i] == '-') {
                    negative = -1;
                    i++;
                }
                while (i < st.length() && s[i] - '0' >= 0 && s[i] - '0' <= 9) {
                    num = num * 10 + (s[i] - '0');
                    i++;
                }
                isPrewOp = false;

                ar.add(new Number(num*negative));
                continue;
            }
            if (s[i] == ' ')
                continue;
            throw new LexerException("Lexer error at " + (i + 1) + "symbol");
        }
    }

    public ArrayList<Token> getResult() {
        return ar;
    }

    public static class LexerException extends Exception {
        LexerException(String st) {
            super(st);
        }
    }
}
