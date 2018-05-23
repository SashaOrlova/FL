public class BooleanValue extends Lexema {
    BooleanValue(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    @Override
    public String toString() {
        return "BOOL_VAL"+'(' + "\"" + content + "\"" + ',' + row + ',' + start + ',' + finish + ')';
    }
}
