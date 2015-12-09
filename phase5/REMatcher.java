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
   
            if (t.op==Operation.STAR) {
                if ( t.right.op==null) { // Child is a leaf node
                    return this.convert(t.right) + "*";
                } else { 
                    return "(" + this.convert(t.right) + ")*";
                }
            }
            
            else if (t.op==Operation.UNION || t.op==Operation.INTERSECT) {
                String left, right;
                if (t.left.op==null || t.left.op==Operation.STAR) {
                    left = "(" + this.convert(t.left);
                } else {
                    left = "(" + this.convert(t.left);
                }
                
                if (t.right.op==null || t.right.op==Operation.STAR) {
                    right = this.convert(t.right) + ")";
                } else {
                    right = this.convert(t.right) + ")";
                }
                String op_text;
                if (t.op==Operation.UNION) {
                    op_text = "|";
                } else {
                    op_text = "&&";
                }
                return left + op_text + right;
            }
            
            else {  // Op is concat
                String text = "";
                if (t.left.op==null || t.left.op==Operation.STAR || t.left.op==Operation.CONCAT) {
                    text += this.convert(t.left);
                } else {
                    text += "(" + this.convert(t.left) + ")";
                }
                
                if (t.right.op==null || t.right.op==Operation.STAR || t.right.op==Operation.CONCAT) {
                    text += this.convert(t.right);
                } else {
                    text += "(" + this.convert(t.right) + ")";
                }
                return text;
            }
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
        for ( int i=0; i<10; i++ ) {
            ExpTree tree = t.randomRE(6);
            System.out.println(tree.print());
            REMatcher r = new REMatcher(tree);
            System.out.println( r.convert(r.re) );
        }
        
    }

}
