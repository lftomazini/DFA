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
                    System.out.println(transition.next);
                    currentString = currentString.substring(1);
                    currentState = transition.next;
                    break;
                }
            }
        }
        if (states[currentState].value.equals("&")) return true;
        else return false;
    }

    public static void main(String[] args) {
        ExpTree ab = new ExpTree("ab");
        ExpTree ac = new ExpTree("ac");
        ExpTree or = new ExpTree(Operation.UNION);
        or.left = ab;
        or.right = ac;

        REMatcher l = new REMatcher(or);
        System.out.println(l.isMatch("ab"));
    }

}
