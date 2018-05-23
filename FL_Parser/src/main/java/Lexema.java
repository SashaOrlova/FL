public class Lexema {
    int start, finish, row;
    String content;
    Lexema(int start, int finish, int row, String type) {
        this.start = start;
        this.finish = finish;
        this.content = type;
        this.row = row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Lexema) {
            return start == ((Lexema) obj).start && finish == ((Lexema) obj).finish && row == ((Lexema) obj).row && content.equals(((Lexema) obj).content);
        }
        return false;
    }
}
