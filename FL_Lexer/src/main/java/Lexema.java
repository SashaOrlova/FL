public class Lexema {
    int start, finish, row;
    String content;
    Lexema(int start, int finish, int row, String type) {
        this.start = start;
        this.finish = finish;
        this.content = type;
        this.row = row;
    }
}
