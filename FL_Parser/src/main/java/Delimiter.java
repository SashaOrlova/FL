public class Delimiter extends Lexema {
    Delimiter(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    @Override
    public String toString() {
        return "DELIMITER"+'(' + "\"" + content + "\"" + ',' + row + ',' + start + ',' + finish + ')';
    }
}
