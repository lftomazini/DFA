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
            // UNION
            if (o == Operation.UNION) {
               ExpTree newTree = new ExpTree(Operation.UNION);
               newTree.left = getDerivative(c, tree.left);
               newTree.right = getDerivative(c, tree.right);
               if (newTree.left.value.equals("@")) return new ExpTree(newTree.right.value);
               else if (newTree.right.value.equals("@")) return new ExpTree(newTree.left.value);
               return newTree;
            }
            //INTERSECT
            if (o == Operation.INTERSECT) {
                ExpTree newTree = new ExpTree(Operation.UNION);
                newTree.left = getDerivative(c, tree.left);
                newTree.right = getDerivative(c, tree.right);
                if ("@".equals(newTree.left.value) || "@".equals(
                        newTree.right.value)) {
                    return new ExpTree("@");
                }
                return newTree;
            }
            // CONCAT
            else if (o == Operation.CONCAT) {
                ExpTree newLeft = new ExpTree(Operation.CONCAT);
                newLeft.left = getDerivative(c, tree.left);
                newLeft.right = tree.right;
                ExpTree newRight = new ExpTree(Operation.CONCAT);
                newRight.left = new ExpTree(v2(tree.left));
                newRight.right = getDerivative(c, tree.right);
                ExpTree newTree = new ExpTree(Operation.UNION);
                newTree.left = newLeft;
                newTree.right = newRight;
                return newTree;
            }
            // STAR (value always on the right)
            else if (o == Operation.STAR) {
                ExpTree newRight = new ExpTree(Operation.STAR);
                newRight.right = tree.right;
                ExpTree newTree = new ExpTree(Operation.CONCAT);
                newTree.left = getDerivative(c, tree.right);
                newTree.right = newRight;
                return newTree;
            }
        }
        return null;
    }
    
    
    
    public String v2(ExpTree t) {
        if (v(t)) return "&";
        else return "@";
    }
    
    public boolean v(ExpTree t) {
        String val = t.value;
        Operation op = t.op;
        if (val.equals("&")) return true;
        else if (val.equals("@")) return false;
        else if (val != null) return false;
        else if (op == Operation.CONCAT || op == Operation.INTERSECT) return v(t.left) && v(t.right);
        else if (op == Operation.UNION) return v(t.left) || v(t.right);
        else if (op == Operation.STAR) return true;
        else return false;
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
        ExpTree c = new ExpTree(Operation.STAR);
//        c.left = a;
        c.right = b;
        Derivative d = new Derivative('h', c);
        System.out.println(d.t.op);
        System.out.println(d.t.left.value);
        System.out.println(d.t.right.op);
        System.out.println(d.t.right.right.value);

    }
}