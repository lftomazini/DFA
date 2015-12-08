package phase5;

import phase5.ExpTree.Operation;
// javac phase5/DFA.java phase5/ExpTree.java phase5/Alphabet.java

class Delta {
    public int current;
    public char letter;
    public int next;

    public Delta(int c, char l, int n) {
        this.current = c;
        this.letter = l;
        this.next = n;
    }
}

class Trans {
    public Delta[] trans;
    public int size;

    public Trans() {
        this.trans = new Delta[10];
        this.size = 0;
    }

    public void add(Delta item) {
        if (this.size == this.trans.length) {
            Delta[] temp = new Delta[this.trans.length * 2];
            for (int i = 0; i < this.trans.length; i++) {
                temp[i] = this.trans[i];
            }
            this.trans = temp;
        }
        this.trans[this.size] = item;
        this.size++;
    }
}

class States {
    public ExpTree[] states;
    public int size;

    public States() {
        this.states = new ExpTree[10];
        this.size = 0;
    }

    public void add(ExpTree item) {
        if (this.size == this.states.length) {
            ExpTree[] temp = new ExpTree[this.states.length * 2];
            for (int i = 0; i < this.states.length; i++) {
                temp[i] = this.states[i];
            }
            this.states = temp;
        }
        this.states[this.size] = item;
        this.size++;
    }
}

public class DFA {
    public States states;
    public char[] alphabet;
    public Trans transitions;
    public Derivative dObj;

    public DFA() {
        this.states = new States();
        Alphabet abc = new Alphabet();
//        this.alphabet = abc.getValues();
        this.alphabet = new char[3];
        this.alphabet[0] = 'a';
        this.alphabet[1] = 'b';
        this.alphabet[2] = 'c';
        this.transitions = new Trans();
        this.dObj = new Derivative();
    }

    public void createDFA(ExpTree regExp) {
        this.states.add(regExp);
        // go through alphabet creating transitions adding new states when necessary
        int index = 0;
        // for each state in the DFA
        while (true) {
            // for each letter in the alphabet
            boolean noNew = true;
            for (int i = 0; i < this.alphabet.length; i++) {
                ExpTree derivative = this.dObj.getDerivative(this.alphabet[i],
                                                             this.states.states[index]);
                // System.out.println(derivative.value);
                // for each state in Q
                boolean found = false;
                for (int j = 0; j < this.states.size; j++) {
                    if (derivative.isEqual(states.states[j])) {
                        transitions.add(new Delta(index, this.alphabet[i], j));
//                         System.out.println(index + " " + this.alphabet[i] + " " + j);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    transitions.add(new Delta(index, this.alphabet[i],
                                              this.states.size));
                    // System.out.println(index + " " +  this.alphabet[i] + " " + this.states.size);
                    states.add(derivative);
                    noNew = false;
                }
            }
            if (index == this.states.size - 1) {
                break;
            }
            index++;
        }
    }

    public static void main(String[] args) {
        DFA dfa = new DFA();

        ExpTree a = new ExpTree("a");
        ExpTree aa = new ExpTree("ab");
        ExpTree union = new ExpTree(Operation.UNION);
        ExpTree star = new ExpTree(Operation.STAR);
        union.left = a;
        union.right = aa;
        star.right = union;

        // or.left = ab;
        // or.right = ac;
        // or2.left = ba;
        // or2.right = ca;
        // plus.left = or2;
        // plus.right = or;
        dfa.createDFA(star);
        for (int i = 0; i < dfa.transitions.size; i++) {
            System.out.println(
                    dfa.transitions.trans[i].current + " " + dfa.transitions.trans[i].letter + " " + dfa.transitions.trans[i].next);
        }

//        System.out.println(dfa.states.states[1].op);
//        System.out.println(dfa.states.states[1].left.value);

        // System.out.println(dfa.states.states[0].op);
//        System.out.println(dfa.states.states[3].left);
//        System.out.println("new DFA");
//        DFA dfa2 = new DFA();
//
//        ExpTree a = new ExpTree("a");
//        ExpTree b = new ExpTree("b");
//        ExpTree c = new ExpTree("c");
//        ExpTree star1 = new ExpTree(Operation.STAR);
//        ExpTree star2 = new ExpTree(Operation.STAR);
//        ExpTree union = new ExpTree(Operation.UNION);
//        ExpTree concat = new ExpTree(Operation.CONCAT);
//        union.left = b;
//        union.right = c;
//        star2.right = union;
//        star1.right = a;
//        concat.left = star1;
//        concat.right = star2;
//
//        dfa2.createDFA(concat);
    }

}
