public class KeyWord extends Lexema {
    KeyWord(int start, int finish, int row, String type) {
        super(start, finish, row, type);
    }

    @Override
    public String toString() {
        return "KW_"+super.content.toUpperCase()+'(' + row + ',' + start + ',' + finish + ')';
    }
}
