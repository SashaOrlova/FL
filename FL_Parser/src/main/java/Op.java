public class Op extends Lexema {
    Op(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    @Override
    public String toString() {
        return "OPERATOR_"+super.content.toUpperCase()+'(' + row + ',' + start + ',' + finish + ')';
    }
}
