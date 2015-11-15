package phase4;
import phase4.ExpTree.Operation;
import java.io.*;

// javac phase4/Derivative.java phase4/ExpTree.java

public class Derivative {
    public ExpTree t;

    public Derivative() {
        this.t = null;
    }

    public Derivative(char a, ExpTree tree){
        this.t = getDerivative(a, tree);

    }

    public ExpTree getDerivative(char c, ExpTree expTree) {
        ExpTree tree = simplify(expTree);
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
                newTree = simplify(newTree);
                return newTree;
            }
            //INTERSECT
            if (o == Operation.INTERSECT) {
                ExpTree newTree = new ExpTree(Operation.INTERSECT);
                newTree.left = getDerivative(c, tree.left);
                newTree.right = getDerivative(c, tree.right);
                newTree = simplify(newTree);
                return newTree;
            }
            // CONCAT
            else if (o == Operation.CONCAT) {
                ExpTree newTree = new ExpTree(Operation.UNION);
                ExpTree left = new ExpTree(Operation.CONCAT);
                ExpTree right = new ExpTree(Operation.CONCAT);
                left.left = getDerivative(c, tree.left);
                left.right = tree.right;
                right.left = new ExpTree(v2(tree.left));
                right.right = getDerivative(c, tree.right);
                newTree.left = left;
                newTree.right = right;
//                System.out.println("before simplify: " + newTree.left.right.right.right.value);
                newTree = simplify(newTree);
//                System.out.println("after simplify: " + newTree.left.left.op);
                return newTree;
            }
            // STAR (value always on the right)
            else if (o == Operation.STAR) {
                
                 if(tree.right.value != null && tree.right.value.length() == 1) {
                    if(tree.right.value.charAt(0) == c) {
                        return tree;
                    }
                    else return new ExpTree("@");
                }         
                else if(tree.right.value != null) {
                    if(tree.right.value.charAt(0) != c) {
                        return new ExpTree("@");
                    }
                }
                else if (tree.right.value != null && (tree.right.value.equals("&") || tree.right.value.equals("@"))) return new ExpTree("&");
                
                ExpTree newTree = new ExpTree(Operation.CONCAT);
                newTree.left = getDerivative(c, tree.right);
                if (newTree.left.value != null && newTree.left.value.equals("@")) return new ExpTree("@");
                newTree.right = tree;
                if (newTree.right.right.op != null && newTree.right.right.op == Operation.STAR) newTree.right = newTree.right.right;
                newTree = simplify(newTree);
                return newTree;
            }
        }
        return null;
    }



    // Helper Functions

    // Calculates the depth of an ExpTree
    public int depth(ExpTree t) {
        if(t == null) return 0;
        if(t.op == null) return 1;
        else {
            int rdepth = 1 + depth(t.left);
            int ldepth = 1 + depth(t.right);
            if(rdepth < ldepth) return ldepth;
            else return rdepth;
        }
    }

    // Simplifies a tree to it's most basic tree using the rules from 4.1
    public ExpTree simplify(ExpTree t) {
        if(t.op == null) return t;
        Operation op = t.op;
        ExpTree simp = new ExpTree(op);

        ExpTree right = t.right;
        if (right.op != null) right = simplify(right);
        simp.right = right;

        // STAR simplifications
        if(op == Operation.STAR) {
            //(r*)* = r*
            if(right.op != null && right.op == Operation.STAR) return right;
            // &* = &
            else if(right.value != null && right.value.equals("&")) return new ExpTree("&");
            // @* = &
            else if(right.value != null && right.value.equals("@")) return new ExpTree("@");
            // Simplified
            else return t;
        }

        ExpTree left = t.left;
        if (left.op != null) left = simplify(left);
        simp.left = left;

        // INTERSECT simplifications
        if(op == Operation.INTERSECT) {
            // r and r = r
            if(left.isEqual(right)) return right;
            // r and s = s and r (put bigger tree on left)
//            int ldepth = depth(left);
//            int rdepth = depth(right);
//            if(rdepth > ldepth) {
//                simp.left = right;
//                simp.right = left;
//            }
            // @ and r || r and @ = @
            if(left.value != null && left.value.equals("@")) return new ExpTree("@");
            else if(right.value != null && right.value.equals("@")) return new ExpTree("@");
            // Simplified
            else return simp;
        }

        // UNION simplifications
        else if(op == Operation.UNION) {
            // r+r = r
            if(left.isEqual(right)) return right;
            // r+s = s+r (put bigger tree on left)
//            int ldepth = depth(left);
//            int rdepth = depth(right);
//            if(rdepth > ldepth) {
//                simp.left = right;
//                simp.right = left;
//            }
            // @+r || r+@ = r
            if(left.value != null && left.value.equals("@")) return right;
            else if(right.value != null && right.value.equals("@")) return left;
            // Simplified
            return simp;
        }

        // CONCAT simplifications
        else if(op == Operation.CONCAT) {
            // @r = @ || r@ = @
            if(left.value != null && left.value.equals("@")) return new ExpTree("@");
            else if(right.value != null && right.value.equals("@")) return new ExpTree("@");
            // &r = r || r& = r
            else if(left.value != null && left.value.equals("&")) return right;
            else if(right.value != null && right.value.equals("&")) return left;
            else if(left.value != null && right.value != null) return new ExpTree(left.value + right.value);
            // Weird Case
//            else if(left.op != null && right.op != null && 
//                    left.op == Operation.UNION && right.op == Operation.STAR &&
//                    right.right.op != null && right.right.op == Operation.UNION &&
//                    left.left.value != null && left.right.value != null && 
//                    right.right.left.value != null && right.right.right.value != null) {
//                String leftVal = left.left.value;
//                String rightVal = left.right.value;
//                if(leftVal.equals("&") || right.right.left.value.indexOf(leftVal) == 0 ||
//                   right.right.right.value.indexOf(leftVal) == 0 || right.right.left.value.indexOf(rightVal) == 0 ||
//                   right.right.right.value.indexOf(rightVal) == 0 || rightVal.equals("&")) {
//                    return right;
//                }
//                return simp;
//            }
            // Simplified
            else return simp;
        }

        else {
            System.out.println("Error in Derivative.simplify");
            return null;
        }
    }

    public String v2(ExpTree t) {
        if (v(t)) {
            return "&";
        }
        else {
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
        // Simplify Testing
//        ExpTree union = new ExpTree(Operation.STAR);
////        union.left = new ExpTree("bb");
//        union.right = new ExpTree("bb");
        Derivative d = new Derivative();
////        ExpTree deriv = d.getDerivative('b', union);
////        System.out.println(deriv.right.op);

        ExpTree a = new ExpTree("a");
        ExpTree aa = new ExpTree("ab");
        ExpTree union = new ExpTree(Operation.UNION);
        ExpTree star = new ExpTree(Operation.STAR);
        union.left = a;
        union.right = aa;
        star.right = union;
        ExpTree deriv = d.getDerivative('a', star);
        System.out.println("Deriv 2");
        ExpTree deriv2 = d.getDerivative('a', deriv);
        System.out.println(deriv2.right.right.right.right.value);
        
        ExpTree u = new ExpTree(Operation.UNION);
        u.left = new ExpTree("&");
        u.right = new ExpTree("a");
        ExpTree der = d.getDerivative('a',u);
//        System.out.println(der.value);
    }
}
