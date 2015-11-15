package phase3;
import phase3.ExpTree.Operation;

// javac phase3/Derivative.java phase3/ExpTree.java

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
               if (newTree.left.isEqual(newTree.right)) return newTree.left;
               else if (newTree.left.op == null && newTree.left.value.equals("@")) return newTree.right;
               else if (newTree.right.op == null && newTree.right.value.equals("@")) return newTree.left;
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
                else if (newTree.left.isEqual(newTree.right)) return newTree.left;
                if (newTree.left.value != null && newTree.right.value != null) {
                    if (newTree.left.value.charAt(0) != newTree.right.value.charAt(0)) {
                        return new ExpTree("&");
                    }
                }
                return newTree;
            }
            // CONCAT
            else if (o == Operation.CONCAT) {
                if (tree.left.value != null && tree.right.value != null && tree.left.value.charAt(0) == c) {
                    ExpTree concat = new ExpTree(Operation.CONCAT);
                    concat.left = getDerivative(c, tree.left);
                    concat.right = tree.right;
                    return concat;
                }

                ExpTree newLeft = new ExpTree(Operation.CONCAT);
                newLeft.left = getDerivative(c, tree.left);
                newLeft.right = tree.right;
                if(newLeft.left.value != null) {
                    if(newLeft.left.value.equals("@")) newLeft = new ExpTree("@");
                    else if(newLeft.left.value.equals("&")) newLeft = newLeft.right;
                }
                else if(newLeft.right.value != null) {
                    if(newLeft.right.value.equals("&")) newLeft = newLeft.left;
                    else if(newLeft.right.value.equals("@")) newLeft = new ExpTree("@");
                }
                else if (newLeft.left.value != null && newLeft.right.value != null) {
                    return new ExpTree(newLeft.left.value + newLeft.right.value);
                }

                ExpTree newRight = new ExpTree(Operation.CONCAT);
                String nullable = v2(tree.left);
                if(nullable.equals("@")) newRight = new ExpTree("@");
                else if(nullable.equals("&")) newRight = getDerivative(c, tree.right);

                ExpTree newTree = new ExpTree(Operation.UNION);
                newTree.left = newLeft;
                newTree.right = newRight;
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
                else if (tree.right.value != null && (tree.right.value == "&" || tree.right.value == "@")) return new ExpTree("&");
                ExpTree newTree = new ExpTree(Operation.CONCAT);
                newTree.left = getDerivative(c, tree.right);
                if (newTree.left.value != null && newTree.left.value.equals("@")) return new ExpTree("@");
                newTree.right = tree;
                if (newTree.right.right.op != null && newTree.right.right.op == Operation.STAR) newTree.right = newTree.right.right;
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
            else if(right.value != null && right.value == "&") return new ExpTree("&");
            // @* = &
            else if(right.value != null && right.value == "@") return new ExpTree("@");
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
            int ldepth = depth(left);
            int rdepth = depth(right);
            if(rdepth > ldepth) {
                simp.left = right;
                simp.right = left;
            }
            // @ and r || r and @ = @
            if(left.value != null && left.value == "@") return new ExpTree("@");
            else if(right.value != null && right.value == "@") return new ExpTree("@");
            // Simplified
            else return simp;
        }

        // UNION simplifications
        else if(op == Operation.UNION) {
            // r+r = r
            if(left.isEqual(right)) return right;
            // r+s = s+r (put bigger tree on left)
            int ldepth = depth(left);
            int rdepth = depth(right);
            if(rdepth > ldepth) {
                simp.left = right;
                simp.right = left;
            }
            // @+r || r+@ = r
            else if(left.value != null && left.value == "@") return right;
            else if(right.value != null && right.value == "@") return left;
            // Simplified
            else return simp;
        }

        // CONCAT simplifications
        else if(op == Operation.CONCAT) {
            // @r = @ || r@ = @
            if(left.value != null && left.value == "@") return new ExpTree("@");
            else if(right.value != null && right.value == "@") return new ExpTree("@");
            // &r = r || r& = r
            else if(left.value != null && left.value == "&") return right;
            else if(right.value != null && right.value == "&") return left;
            // Simplified
            else return simp;
        }

        else {
            System.out.println("Error in Derivative.simplify()");
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
        ExpTree and = new ExpTree(Operation.INTERSECT);
        ExpTree concat = new ExpTree(Operation.CONCAT);
        concat.left = new ExpTree("&");
        ExpTree star1 = new ExpTree(Operation.STAR);
        ExpTree star2 = new ExpTree(Operation.STAR);
        star2.right = new ExpTree("@");
        star1.right = star2;
        concat.right = star1;
        and.left = concat;
        and.right = new ExpTree("ab");
        Derivative d = new Derivative();
        ExpTree simp = d.simplify(and);
        // System.out.println(simp.value);
        // System.out.println(d.depth(and));

        // Depth Testing
        ExpTree intersect = new ExpTree(Operation.INTERSECT);
        intersect.left = new ExpTree("a");
        ExpTree union = new ExpTree(Operation.UNION);
        union.left = new ExpTree("b");
        union.right = new ExpTree("c");
        intersect.right = union;
        // System.out.println(d.depth(intersect));

        ExpTree u = new ExpTree(Operation.UNION);
        ExpTree n1 = new ExpTree(Operation.INTERSECT);
        ExpTree n2 = new ExpTree(Operation.INTERSECT);
        n1.left = new ExpTree("a");
        ExpTree c = new ExpTree(Operation.CONCAT);
        c.left = new ExpTree("b");
        c.right = new ExpTree("c");
        n1.right = c;
        n2.left = c;
        n2.right = new ExpTree("a");
        u.right = n1;
        u.left = n2;

        ExpTree simp2 = d.simplify(u);
        System.out.println(simp2.left.right.value);

    }
}
