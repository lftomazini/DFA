package phase3;
import phase3.ExpTree.Operation;

// javac phase3/REMatcher.java phase3/DFA.java phase3/ExpTree.java phase3/Alphabet.java

public class REMatcher {

    public ExpTree re;
    public DFA dfa;

    public REMatcher(ExpTree regExp) {
        this.re = regExp;
        this.dfa = new DFA();
        this.dfa.createDFA(regExp);
    }

    public boolean isMatch(String s) {
        String currentString = s;
        int currentState = 0;
        ExpTree[] states = dfa.states.states;
        Delta[] transitions = dfa.transitions.trans;
        for(int i = 0; i < s.length(); i++) {
            for(int j = 0; j < transitions.length; j++) {
                Delta transition = transitions[j];
                if (transition.current == currentState && transition.letter == currentString.charAt(0)) {
                    currentString = currentString.substring(1);
                    currentState = transition.next;
                    break;
                }
            }
        }

        // if final state contains & then it accepts
        if(this.dfa.dObj.v2(states[currentState]).equals("&")) return true;
        else return false;
    }

    public static void main(String[] args) {
        ExpTree a = new ExpTree("a");
        ExpTree aa = new ExpTree("aa");
        ExpTree union = new ExpTree(Operation.UNION);
        ExpTree star = new ExpTree(Operation.STAR);
        union.left = a;
        union.right = aa;
        star.right = union;

        REMatcher l = new REMatcher(star);
        System.out.println(l.isMatch("aa"));
    }

}
