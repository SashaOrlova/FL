import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
            File f = new File(args[0]);
        try {
            FileReader fr = new FileReader(f);
            Scanner in = new Scanner(fr);
            while (in.hasNextLine()) {
                String st = in.nextLine();
                try {
                    Tree t = new Parser(new Lexer(st).getResult()).parseExpr();
                    t.print(0);
                } catch (Parser.ParserException | Lexer.LexerException parserException) {
                    System.out.println(parserException.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error in input file");
        }
    }
}
