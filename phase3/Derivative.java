package phase3;
import phase3.ExpTree.Operation;

public class Derivative {
    public ExpTree t;

    public Derivative() {
        this.t = null;
    }

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
               if (newTree.left.op == null && newTree.left.value.equals("@")) return newTree.right;
               else if (newTree.right.op == null && newTree.right.value.equals("@")) {
                   System.out.println("retugning left tree");
                   return newTree.left;
               }

               return newTree;
            }
            //INTERSECT
            if (o == Operation.INTERSECT) {
                ExpTree newTree = new ExpTree(Operation.INTERSECT);
                newTree.left = getDerivative(c, tree.left);
                newTree.right = getDerivative(c, tree.right);
                if ("@".equals(newTree.left.value) || "@".equals(
                        newTree.right.value)) {
                    return new ExpTree("@");
                }
                if (newTree.left.value != null && newTree.right.value != null) {
                    if (newTree.left.value.charAt(0) != newTree.right.value.charAt(0)) {
                        return new ExpTree("&");
                    }
                }
                return newTree;
            }
            // CONCAT
            else if (o == Operation.CONCAT) {
                ExpTree newLeft = new ExpTree(Operation.CONCAT);
                newLeft.left = getDerivative(c, tree.left);
                newLeft.right = tree.right;
                if(newLeft.left.value != null) {
                    if(newLeft.left.value.equals("@")) return new ExpTree("@");
                    else if(newLeft.left.value.equals("&")) return newLeft.right;
                }
                else if(newLeft.right.value != null) {
                    if(newLeft.right.value.equals("&")) return newLeft.left;
                    else if(newLeft.right.value.equals("@")) return new ExpTree("@");
                }
                else if (newLeft.left.value != null && newLeft.right.value != null)
                    return new ExpTree(newLeft.left.value + newLeft.right.value);

                ExpTree newRight = new ExpTree(Operation.CONCAT);
                newRight.left = new ExpTree(v2(tree.left));
                newRight.right = getDerivative(c, tree.right);
                if(newRight.left.value != null && newRight.right.value != null) {
                    if(newRight.left.value == "@") {
                        System.out.println("returning null tree");
                        return new ExpTree("@");
                    }
                    else if(newRight.right.value.equals("@")) return new ExpTree("@");
                    else if(newRight.left.value.equals("&")) return newRight.right;
                    else if(newRight.right.value.equals("&")) return newRight.left;
                    else return new ExpTree(newRight.left.value + newRight.right.value);
                }
                ExpTree newTree = new ExpTree(Operation.UNION);
                newTree.left = newLeft;
                newTree.right = newRight;
                return newTree;
            }
            // STAR (value always on the right)
            else if (o == Operation.STAR) {
                if(tree.right.value != null && tree.right.value.length() == 1) {
                    if(tree.right.value.charAt(0) == c) return tree;
                    else return new ExpTree("@");
                }
                ExpTree newTree = new ExpTree(Operation.CONCAT);
                newTree.left = getDerivative(c, tree.right);
                newTree.right = tree;
                return newTree;
            }
        }
        return null;
    }



    public String v2(ExpTree t) {
        if (v(t)) {
            System.out.println("v2 is true");
            return "&";
        }
        else {
            System.out.println("v2 of " + t.op + " is false");
            return "@";
        }
    }

    public boolean v(ExpTree t) {
        String val = t.value;
        Operation op = t.op;
        if (val != null) {
            if (val.equals("&")) return true;
            else if (val.equals("@")) return false;
            else return false;
        }
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
