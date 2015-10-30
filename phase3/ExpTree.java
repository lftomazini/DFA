package phase3;

public class ExpTree {
    public ExpTree left;
    public ExpTree right;
    public String value; 
    public Operation op;
    
    public ExpTree(String s) {
        this.value = s;
        this.op = null;
        this.left = null;
        this.right = null;
    }
    
    public ExpTree(Operation o) {
        this.value = null;
        this.op = o;
        this.left = null;
        this.right = null;
    }
    
    public boolean isEqual( ExpTree otherTree ) {
//        if(this.right == null && otherTree.right != null) return false;
//        if(this.left == null && otherTree.left != null) return false;
//        if(this.right != null && otherTree.right == null) return false;
//        if(this.left != null && otherTree.right == null) return false;
//        if(this.left == null && otherTree.left == null && this.right == null && otherTree.right == null && this.value == otherTree.value);
//        System.out.println(this.op + this.value + otherTree.op + otherTree.value);
        if (this.op == otherTree.op && this.value==otherTree.value) {
            if(this.left != null && otherTree.left != null && this.right != null && otherTree.right != null) {
                return this.left.isEqual(otherTree.left) && this.right.isEqual(otherTree.right);
            }
            else {
                boolean left;
                boolean right;
                if(this.left == null || otherTree.left == null) {
                    left = this.left == otherTree.left;
                }
                else {
                    left = this.left.isEqual(otherTree.left);
                }
                if(this.right == null || otherTree.right == null) {
                    right = this.right == otherTree.right;
                }
                else {
                    right = this.right.isEqual(otherTree.right);
                }
                return left && right;
            }
        }
        return false;
//        if (this.op==otherTree.op && this.value==otherTree.value && 
//                this.left.isEqual(otherTree.left) && this.right.isEqual(otherTree.right)) {
//            return true;
//        } else return false;
    }
    
    public enum Operation {
        CONCAT, STAR, UNION, INTERSECT, NOT
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