import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File f = new File("src/main/resources/test.txt");
        try {
            FileReader is = new FileReader(f);
            Scanner s = new Scanner(is);
            StringBuilder st = new StringBuilder();
            while (s.hasNextLine()) {
                st.append(s.nextLine() + '\n');
            }
            Lexer l = new Lexer(st.toString());
            //if (args.length > 1 && args[1].equals("print"))
                l.print();
        } catch (FileNotFoundException e) {
            System.out.println("Problem this input file!");
        } catch (Lexer.LexerError lexerError) {
            System.out.println(lexerError.getMessage());
        }
    }
}
