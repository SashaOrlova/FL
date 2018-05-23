import java.util.ArrayList;

public class Parser {
    private ArrayList<Lexema> tokens;
    public String errors = "";

    public Parser(ArrayList<Lexema> lexemas) {
        tokens = lexemas;
    }

    public void preParser(){
        for (int i = 0 ; i < tokens.size(); i++) {
            if (tokens.get(i).content.equals("(") && i + 3 < tokens.size() && tokens.get(i+1).content.equals("-") &&
                    tokens.get(i+2) instanceof Num && tokens.get(i+3).content.equals(")")) {
                tokens.remove(i+3);
                tokens.get(i+2).content = "-" + tokens.get(i+2).content;
                tokens.get(i+2).start = tokens.get(i+2).start - 2;
                tokens.get(i+2).finish = 1 + tokens.get(i+2).finish;
                tokens.remove(i+1);
                tokens.remove(i);
            }
        }
        for (int i = 0; i < tokens.size() - 1; i++) {
            if ((tokens.get(i).content.equals("+") || tokens.get(i).content.equals("-") || tokens.get(i).content.equals("*")
                    || tokens.get(i).content.equals("/")) && (tokens.get(i+1).content.equals("="))) {
                tokens.add(i+2, tokens.get(i-1));
                tokens.add(i+3, tokens.get(i));
                tokens.remove(i);
            }
        }
        for (int i = 0; i < tokens.size() - 1; i++) {
            if ((tokens.get(i).content.equals("+") || tokens.get(i).content.equals("-"))
                    && (tokens.get(i).content.equals(tokens.get(i+1).content))) {
                tokens.add(i, new Op(0,0,0, "="));
                tokens.add(i+1, tokens.get(i-1));
                tokens.set(i+3, new Num(0,0,0, "1"));
            }
        }
//        for (int i = 0; i < tokens.size(); i++) {
//            System.out.println(tokens.get(i).toString() + ", ");
//        }
    }

    public int findFirstLogic(int start, int finish) {
        int brakets = 0;
        for (int i = finish - 1; i >= start; i--) {
            if (tokens.get(i).content.equals(")"))
                brakets--;
            if (tokens.get(i).content.equals("("))
                brakets++;
            if ((tokens.get(i).content.equals("=") || tokens.get(i).content.equals("==")
            || tokens.get(i).content.equals("<") || tokens.get(i).content.equals(">")
                    || tokens.get(i).content.equals("<=") || tokens.get(i).content.equals(">=") ||
                    tokens.get(i).content.equals("||") || tokens.get(i).content.equals("&&")
            ) && brakets == 0)
                return i;
        }
        return -1;
    }

    public int findFirstPlus(int start, int finish) {
        int brakets = 0;
        for (int i = finish - 1; i >= start; i--) {
            if (tokens.get(i).content.equals(")"))
                brakets--;
            if (tokens.get(i).content.equals("("))
                brakets++;
            if ((tokens.get(i).content.equals("+") || tokens.get(i).content.equals("-")) && brakets == 0)
                return i;
        }
        return -1;
    }

    public int findFirstMult(int start, int finish) {
        int brakets = 0;
        for (int i = finish - 1; i >= start; i--) {
            if (tokens.get(i).content.equals(")"))
                brakets--;
            if (tokens.get(i).content.equals("("))
                brakets++;
            if ((tokens.get(i).content.equals("*") || tokens.get(i).content.equals("/") ||
                    tokens.get(i).content.equals("%")) && brakets == 0)
                return i;
        }
        return -1;
    }

    public int findFirstPow(int start, int finish) {
        int brakets = 0;
        for (int i = start; i < finish; i++) {
            if (tokens.get(i).content.equals(")"))
                brakets--;
            if (tokens.get(i).content.equals("("))
                brakets++;
            if ((tokens.get(i).content.equals("^")) && brakets == 0)
                return i;
        }
        return -1;
    }

    public int findSemicolon(int start, int finish, String del) {
        int balance = 0;
        int bal = 0;
        for (int i = start ; i < finish; i++) {
            if (tokens.get(i).content.equals(del) && balance == 0 && bal == 0)
                return i;
            if (tokens.get(i).content.equals("("))
                balance++;
            if (tokens.get(i).content.equals("{"))
                bal++;
            if (tokens.get(i).content.equals("}"))
                bal--;
            if (tokens.get(i).content.equals(")"))
                balance--;
        }
        return -1;
    }


    public int findBracket(int start, int finish) {
        int balace = 0;
         for (int i = start ; i < finish; i++) {
            if (tokens.get(i).content.equals(")"))
                balace--;
            if (tokens.get(i).content.equals("("))
                balace++;
            if (balace == 0 && tokens.get(i).content.equals(")"))
                return i;
        }
        return -1;
    }

    public Tree.Vertex parseMathExpr(int start, int finish) {
        if (findFirstPlus(start, finish) == -1)
            return parseMathTerm(start, finish);
        else {
            Tree.Vertex left = parseMathExpr(start, findFirstPlus(start, finish));
            Tree.Vertex right = parseMathTerm(findFirstPlus(start, finish) + 1, finish);
            Tree.Vertex ver = new Tree.Vertex(tokens.get(findFirstPlus(start, finish)));
            ver.add(left);
            ver.add(right);
            return ver;
        }
    }

    public Tree.Vertex parseMathTerm(int start, int finish) {
        if (findFirstMult(start, finish) == -1)
            return parseMathFact(start, finish);
        else {
            Tree.Vertex left = parseMathTerm(start, findFirstMult(start, finish));
            Tree.Vertex right = parseMathFact(findFirstMult(start, finish) + 1, finish);
            Tree.Vertex ver = new Tree.Vertex(tokens.get(findFirstMult(start, finish)));
            ver.add(left);
            ver.add(right);
            return ver;
        }
    }

    public Tree.Vertex parseMathFact(int start, int finish) {
        if (findFirstPow(start, finish) == -1)
            return parseMathSmall(start, finish);
        else {
            Tree.Vertex left = parseMathSmall(start, findFirstPow(start, finish));
            Tree.Vertex right = parseMathFact(findFirstPow(start, finish) + 1, finish);
            Tree.Vertex ver = new Tree.Vertex(tokens.get(findFirstPow(start, finish)));
            ver.add(left);
            ver.add(right);
            return ver;
        }
    }

    public Tree.Vertex parseMathSmall(int start, int finish) {
        if (start + 1 < finish && tokens.get(start) instanceof Ident && tokens.get(start+1).content.equals("("))
            return parseFunctionCall(start, finish);
        if (start < finish &&( tokens.get(start) instanceof Num || tokens.get(start) instanceof Ident))
            return new Tree.Vertex(tokens.get(start));
        if (start < finish && (tokens.get(start).content.equals("(") && tokens.get(finish-1).content.equals(")")))
            return parseMathExpr(start + 1, finish - 1);
        errors += "Error in " + (start == finish ? tokens.get(tokens.size()-1) : tokens.get(start)) + " parser math expr\n";
        return new Tree.Vertex(new Stub(0,0,0,""));
    }

    public Tree.Vertex parseLogic(int start, int finish) {
        if (findFirstLogic(start, finish) == -1)
            return parseMathExpr(start, finish);
        Tree.Vertex left = parseLogic(start, findFirstLogic(start, finish));
        Tree.Vertex right = parseLogic(findFirstLogic(start, finish) + 1, finish);
        Tree.Vertex ver = new Tree.Vertex(tokens.get(findFirstLogic(start, finish)));
        ver.add(left);
        ver.add(right);
        return ver;
    }

    public Tree.Vertex parseAssignment(int start, int finish) {
        if (tokens.get(start) instanceof Ident && tokens.get(start+1).content.equals("=")) {
            Tree.Vertex ver = new Tree.Vertex(tokens.get(start+1));
            Tree.Vertex right = parseLogic(start+2, finish);
            ver.add(new Tree.Vertex(tokens.get(start)));
            ver.add(right);
            return ver;
        }
        errors += "Error in " + tokens.get(start) + " parser assignment\n";
        return new Tree.Vertex(new Stub(0,0,0,""));
    }

    public Tree.Vertex parseExpr(int start, int finish) {
        if ((tokens.get(start) instanceof Ident || tokens.get(start).content.equals("write") ||
                tokens.get(start).content.equals("read"))) {
            if (tokens.get(start+1).content.equals("="))
                return parseAssignment(start, finish);
            if (tokens.get(start+1).content.equals("("))
                return parseFunctionCall(start, finish);
            return new Tree.Vertex(tokens.get(start));
        }
        if (tokens.get(start).content.equals("if")) {
            return parseIf(start, finish);
        }
        return parseLogic(start, finish);
    }

    public void parseArgList(int start, int finish, Tree.Vertex args) {
        for (int i = start ; i < finish; i+=2) {
            args.add(new Tree.Vertex(tokens.get(i)));
        }
    }

    public void parseExprList(int start, int finish, Tree.Vertex args, String del) {
        int lastInd = start;
        for (int i = start; i<finish;) {
            lastInd = i;
            int end = findSemicolon(i, finish, del);
            if (end == -1)
                break;
            Tree.Vertex expr = parseExpr(i, end);
            args.add(expr);
            i = end + 1;
        }
        if (del.equals(",")) {
            Tree.Vertex expr = parseExpr(lastInd, finish);
            args.add(expr);
        }
    }

    public Tree.Vertex parseFunctionDefinition(int start, int finish) {
        if (start < tokens.size() && tokens.get(start).content.equals("function") && tokens.get(start+1) instanceof Ident) {
            Tree.Vertex ver = new Tree.Vertex(tokens.get(start+1));
            Tree.Vertex args = new Tree.Vertex();
            Tree.Vertex body = new Tree.Vertex();
            parseArgList(start+3, findBracket(start+2, finish), args);
            int finishArgList = findBracket(start+2, finish);
            parseExprList(finishArgList+2, finish, body, ";");
            ver.add(args);
            ver.add(body);
            return ver;
        }
        errors += "Error in " + tokens.get(start) + " parser function definition\n";
        return new Tree.Vertex(new Stub(0,0,0,""));
    }

    public Tree.Vertex parseFunctionCall(int start, int finish) {
        if ((tokens.get(start) instanceof Ident || tokens.get(start).content.equals("write") ||
                tokens.get(start).content.equals("read")) && tokens.get(start+1).content.equals("(")) {
            Tree.Vertex ver = new Tree.Vertex(tokens.get(start));
            parseExprList(start+2, finish, ver, ",");
            return ver;
        }
        errors += "Error in " + tokens.get(start) + " parser function call\n";
        return new Tree.Vertex(new Stub(0,0,0,""));
    }

    public Tree.Vertex parseIf(int start, int finish) {
        if (tokens.get(start).content.equals("if")) {
            int closeBracket = findBracket(start, finish);
            Tree.Vertex cond = parseExpr(start+2, closeBracket);
            int elseStart = -1;
            int balance = 0;
            for (int i = closeBracket+1 ; i < finish; i++) {
                if (tokens.get(i).content.equals("else"))
                    balance--;
                if (tokens.get(i).content.equals("then"))
                    balance++;
                if (balance == 0 && tokens.get(i).content.equals("else")) {
                    elseStart = i;
                    break;
                }
            }
            Tree.Vertex thenBranch = new Tree.Vertex(tokens.get(closeBracket+1));
            parseExprList(closeBracket + 3, elseStart, thenBranch, ";");
            Tree.Vertex elseBranch = new Tree.Vertex(tokens.get(elseStart));
            parseExprList(elseStart + 2, finish, elseBranch, ";");
            Tree.Vertex ifStatement = new Tree.Vertex(tokens.get(start));
            ifStatement.add(cond);
            ifStatement.add(thenBranch);
            ifStatement.add(elseBranch);
            return ifStatement;
        }
        errors += "Error in " + tokens.get(start) + " parser if\n";
        return new Tree.Vertex(new Stub(0,0,0,""));
    }

    public int findFunction(int start, int finish) {
        for (int i = start ; i < finish; i++) {
            if (tokens.get(i).content.equals("function")) {
                return i;
            }
        }
        return -1;
    }

    public Tree.Vertex parseListFunction(int start, int finish) {
        int lastF = findFunction(start, finish);
        if (lastF == -1) {
            errors += "Error in parse function list\n";
            return new Tree.Vertex(new Stub(0,0,0, ""));
        }
        Tree.Vertex root = new Tree.Vertex();
        for (int i = 0 ; i < finish;) {
            int funEnd = findFunction(lastF+1, finish);
            Tree.Vertex v = parseFunctionDefinition(lastF, funEnd == -1 ? finish : funEnd);
            root.add(v);
            lastF = funEnd;
            if (lastF == -1) {
                break;
            }
        }
        return root;
    }
}
