package phase3;
import phase3.ExpTree.Operation;

public class Derivative {
    public ExpTree t;
    
    public Derivative(char a, ExpTree tree){
        this.t = getDerivative(a, tree);
        
    }
    
    public ExpTree getDerivative(char c, ExpTree tree) {
        // Leaf -> Contains a string
        if (tree.op == null) {
            String s = tree.value;
            if (s.equals("&") || s.equals("@")) return new ExpTree("@");
            else if (s.length() == 1 && c == s.charAt(0)) return new ExpTree("&");
            else if (s.length() == 1 && c != s.charAt(0)) return new ExpTree("@");
            else if (s.charAt(0) == c) return new ExpTree(s.substring(1));
            else return new ExpTree("@");
        }
        // Not leaf -> Contains an operation
        else {
            Operation o = tree.op;
            if (o == Operation.UNION) {
               ExpTree newTree = new ExpTree(Operation.UNION);
               newTree.left = getDerivative(c, tree.left);
               newTree.right = getDerivative(c, tree.right);
               if (newTree.left.value == "@") return new ExpTree(newTree.right.value);
               else if (newTree.right.value == "@") return new ExpTree(newTree.left.value);
               return newTree;
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
//        ExpTree a = new ExpTree("a");
//        ExpTree b = new ExpTree("b");
//        ExpTree c = new ExpTree("c");
//        ExpTree star1 = new ExpTree(Operation.STAR);
//        ExpTree star2 = new ExpTree(Operation.STAR);
//        ExpTree union = new ExpTree(Operation.UNION);
//        ExpTree concat = new ExpTree(Operation.CONCAT);
        ExpTree a = new ExpTree("mello");
        ExpTree b = new ExpTree("happy");
        ExpTree c = new ExpTree(Operation.UNION);
        c.left = a;
        c.right = b;
        Derivative d = new Derivative('h', c);
        System.out.println(d.t.value);
    }
}