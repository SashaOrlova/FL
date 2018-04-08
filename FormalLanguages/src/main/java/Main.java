import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        while (true) {
            Scanner in = new Scanner(System.in);
            String st = in.nextLine();
            try {
                Tree t = new Parser(new Lexer(st).getResult()).parseExpr();
                t.print(0);
            } catch (Parser.ParserException | Lexer.LexerException parserException) {
                System.out.println(parserException.getMessage());
            }
        }
    }
}
