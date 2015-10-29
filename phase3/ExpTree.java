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
        if (this.op==otherTree.op && this.value==otherTree.value && 
                this.left.isEqual(otherTree.left) && this.right.isEqual(otherTree.right)) {
            return true;
        } else return false;
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
        
    }
    
}