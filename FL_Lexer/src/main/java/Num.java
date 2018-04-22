public class Num extends Lexema {
    Num(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    double takeValue() {
        return Double.parseDouble(content);
    }

    @Override
    public String toString() {
        return "NUM"+'(' + "\"" + content + "\"" + ',' + row + ',' + start + ',' + finish + ')';
    }
}
