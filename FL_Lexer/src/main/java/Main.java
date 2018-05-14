import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File f = new File(args[0]);
        try {
            FileReader is = new FileReader(f);
            Scanner s = new Scanner(is);
            StringBuilder st = new StringBuilder();
            while (s.hasNextLine()) {
                st.append(s.nextLine()).append('\n');
            }
            Lexer l = new Lexer(st.toString());
            l.print();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println("\nProblem this input file!");
        } catch (Lexer.LexerError lexerError) {
            System.out.println(lexerError.getMessage());
        }
    }
}
