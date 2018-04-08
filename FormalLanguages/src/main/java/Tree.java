public class Tree {
    Token inf;
    Tree saved;
    Tree leftSon;
    Tree rightSon;
    String message;
    static int t = 0;
    Tree (Token s, String m) {
        message = m;
        inf = s;
    }

    Tree (Tree s, Tree l, Tree r, String m) {
        message = m;
        saved = s;
        leftSon = l;
        rightSon = r;
    }

    public void print(int num) {
        System.out.print("№" + num + " Children ");
        int left = t++;
        int right = t++;
        int sav = t++;
        if (leftSon != null)
            System.out.print(left);
        if (rightSon != null)
            System.out.print(" and " + right);
        if (saved != null)
            System.out.print(" and " + sav);
        System.out.print(" " + message + " ");
        if (inf != null) {
            if (inf instanceof Number)
                System.out.print("Information " + ((Number) inf).n);
            else
                System.out.print("Information " + ((Operation)inf).c);
        }
        System.out.println('\n');
        if (leftSon != null)
            leftSon.print(left);
        if (saved != null)
            saved.print(sav);
        if (rightSon != null)
            rightSon.print(right);
    }
}
