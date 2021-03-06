package phase5;

public class ExpTree {
    public ExpTree left;
    public ExpTree right;
    public String value;
    public Operation op;

    // Leaf node constructor
    public ExpTree(String s) {
        this.value = s;
        this.op = null;
        this.left = null;
        this.right = null;
    }
    
    // Op node constructor
    public ExpTree(Operation o) {
        this.value = null;
        this.op = o;
        this.left = null;
        this.right = null;
    }

    // Returns the String RE equivalent to the regexp represented by t
    public String convert(ExpTree t) {
        
        // Base case: leaf node, return value
        if (t.op==null) {
            return (String)t.value;
        }
        // Rec. case: op node
        else {
            String cur_op = "";
            boolean leftParens = false; 
            boolean rightParens = false;
            
            // Current node's op is...
            if (t.op==Operation.STAR) {
                if (t.right.op==null && t.right.value.length()==1) { // leaf node w/ value only one char
                    return this.convert(t.right) + "*";
                } else {
                    return "(" + this.convert(t.right) + ")*"; 
                }
            } else if (t.op==Operation.UNION) {
                cur_op = "|";
            } else if (t.op==Operation.INTERSECT) {
                cur_op = "&&";
            }
            
            if (t.left.op!=null) {
                // Left child's op is...
                switch(t.left.op) {
                    case UNION:
                        leftParens = true;
                        break;
                    case INTERSECT:
                        leftParens = true;
                        break;
                }
            }
            
            if (t.right.op!=null) {
                // Right child's op is...
                switch(t.right.op) {
                    case UNION:
                        rightParens = true;
                        break;
                    case INTERSECT:
                        rightParens = true;
                        break;
                }
            }
            
            String text;
            if (leftParens) {
                text = "(" + this.convert(t.left) + ")";
            } else {
                text = this.convert(t.left);
            }
            text += cur_op;
            if (rightParens) {
                text += "(" + this.convert(t.right) + ")";
            } else {
                text += this.convert(t.right);
            }
            
            return text;
        }     
    }
    
    // Equality checker for trees -- returns true if equal, false if not
    public boolean isEqual(ExpTree otherTree) {
//        if(this.right == null && otherTree.right != null) return false;
//        if(this.left == null && otherTree.left != null) return false;
//        if(this.right != null && otherTree.right == null) return false;
//        if(this.left != null && otherTree.right == null) return false;
//        if(this.left == null && otherTree.left == null && this.right == null && otherTree.right == null && this.value == otherTree.value);
//        System.out.println(this.op + this.value + otherTree.op + otherTree.value);
        boolean equalVals = true;
        if(this.value != null && otherTree.value != null && !this.value.equals(otherTree.value)) {
            equalVals = false;
        }
        if (this.op == otherTree.op && equalVals) {
            if (this.op == Operation.STAR) {
                return this.right.isEqual(otherTree.right);
            }
            else if (this.left != null && otherTree.left != null && this.right != null && otherTree.right != null) {
                return this.left.isEqual(otherTree.left) && this.right.isEqual(
                        otherTree.right);
            } else {
                boolean left;
                boolean right;
                if (this.left == null || otherTree.left == null) {
                    left = this.left == otherTree.left;
                } else {
                    left = this.left.isEqual(otherTree.left);
                }
                if (this.right == null || otherTree.right == null) {
                    right = this.right == otherTree.right;
                } else {
                    right = this.right.isEqual(otherTree.right);
                }
                return left && right;
            }
        }
//        System.out.println("returning false");
        return false;
//        if (this.op==otherTree.op && this.value==otherTree.value &&
//                this.left.isEqual(otherTree.left) && this.right.isEqual(otherTree.right)) {
//            return true;
//        } else return false;
    }
    
    // Returns a String representation of the tree in the form: OPERATION(left_child, right_child)
    public String print() {
        String ans = "";
        if(this.value != null) {
            return this.value;
        }
        if(this.op != null) {
            ans = ans + this.op + "(";
        }
        if (this.op != Operation.STAR) {
            if (this.left.value != null) {
                ans = ans + this.left.value + ",";
            } else {
                ans = ans + this.left.print() + ",";
            }
        }
        if (this.right.value != null) {
            ans = ans + this.right.value;
        } else {
            ans = ans + this.right.print();
        }
        ans = ans + ")";
        return ans;
    }

    public enum Operation {
        CONCAT, STAR, UNION, INTERSECT
    }

    public static void main(String[] args) {
        ExpTree a = new ExpTree("a");
        ExpTree b = new ExpTree("b");
        ExpTree c = new ExpTree("c");
        ExpTree star1 = new ExpTree(Operation.STAR);
        ExpTree star2 = new ExpTree(Operation.STAR);
        ExpTree union = new ExpTree(Operation.UNION);
        ExpTree concat = new ExpTree(Operation.CONCAT);
        union.left = b;
        union.right = c;
        star2.right = union;
        star1.right = a;
        concat.left = star1;
        concat.right = star2;

        System.out.println(b.isEqual(a));

    }

}
