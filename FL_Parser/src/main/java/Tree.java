import java.util.ArrayList;

public class Tree {
    Vertex root = null;
    public static class Vertex {
        Lexema content;
        ArrayList<Vertex> children = new ArrayList<>();
        public Vertex(Lexema l) {
            content = l;
        }
        public Vertex() {
        }
        public void add(Vertex v){
            children.add(v);
        }
    }
}
