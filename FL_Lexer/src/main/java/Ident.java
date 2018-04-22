public class Ident extends Lexema {
    Ident(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    @Override
    public String toString() {
        return "IDENT"+'(' + "\"" + content + "\"" + ',' + row + ',' + start + ',' + finish + ')';
    }
}
