/**
 * Possible variants '+','-','*',':','^'
 */
public class Operation implements Token {
    public char c;
    Operation(char ch) {
        c = ch;
    }
}
