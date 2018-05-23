import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.Scanner;

public class ParserUI extends Application {

    public static Tree.Vertex root;


    public static void main(String[] args) throws Lexer.LexerError {
        File f = new File(args[0]);
        try {
            FileReader is = new FileReader(f);
            Scanner s = new Scanner(is);
            StringBuilder st = new StringBuilder();
            while (s.hasNextLine()) {
                st.append(s.nextLine()).append('\n');
            }
            String program = st.toString();
            Lexer l = new Lexer(program);
            Parser p = new Parser(l.result());
            p.preParser();
            root = p.parseListFunction(0,l.result().size());
                launch(args);
            if (!Objects.equals(p.errors, ""))
                System.out.println(p.errors);
        } catch (FileNotFoundException e) {
            System.out.println("Problem this input file!");
        } catch (Lexer.LexerError lexerError) {
            System.out.println(lexerError.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        if (root == null) {
            primaryStage.setScene(new Scene(new Label("Error"), 800, 500));
            primaryStage.show();
        }
        TreeItemClient rootItem = new TreeItemClient(root);
        TreeView<Label> tree = new TreeView<>(rootItem);
        primaryStage.setScene(new Scene(tree, 800, 500));
        primaryStage.show();
    }
}
