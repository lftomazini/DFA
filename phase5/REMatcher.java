package phase5;

import phase5.ExpTree.Operation;

// javac phase5/REMatcher.java phase5/DFA.java phase5/ExpTree.java phase5/Alphabet.java
public class REMatcher {

    public ExpTree re;
    public DFA dfa;

    public REMatcher(ExpTree regExp) {
        this.re = regExp;
        this.dfa = new DFA();
        this.dfa.createDFA(regExp);
    }

    // Converts the given ExpTree to its equivalent String RE
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
    
    public boolean isMatch(String s) {
        String currentString = s;
        int currentState = 0;
        ExpTree[] states = dfa.states.states;
        Delta[] transitions = dfa.transitions.trans;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < transitions.length; j++) {
                Delta transition = transitions[j];
                if (transition.current == currentState && transition.letter == currentString.charAt(
                        0)) {
                    currentString = currentString.substring(1);
                    currentState = transition.next;
                    break;
                }
            }
        }

        // if final state contains & then it accepts
        if (this.dfa.dObj.v2(states[currentState]).equals("&")) {
            return true;
        } else {
            return false;
        }
    }
    
    public static void main(String[] args) {
        ExpTree a = new ExpTree("a");
        ExpTree aa = new ExpTree("ab");
        ExpTree union = new ExpTree(Operation.UNION);
        ExpTree star = new ExpTree(Operation.STAR);
        union.left = a;
        union.right = aa;
        star.right = union;

        REMatcher l = new REMatcher(star);
        System.out.println(l.isMatch("aa"));
        
        Tester t = new Tester();
        for ( int i=0; i<3; i++ ) {
            ExpTree tree = t.randomRE(10);
            System.out.println(tree.print());
            REMatcher r = new REMatcher(tree);
            System.out.println( r.convert(r.re) );
        }
        
    }

}
