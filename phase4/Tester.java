package phase4;

import java.util.ArrayList;
import java.util.Random;
// import java.util.Math;
import phase4.ExpTree.Operation;

// javac phase4/Tester.java phase4/REMatcher.java phase4/DFA.java phase4/ExpTree.java phase4/Alphabet.java
public class Tester {
    public static int BASE;
    public static final int MAX_POWER = 6;
    public static final int DEPTH = 7;
    public static final String ALPHABET = "abc";

    public ExpTree randomRE(int depth, int prob) {
        RandStrGen randString = new RandStrGen(ALPHABET);
        Random rand = new Random();

        int leafProb = rand.nextInt(prob) + 1;
        if (leafProb > depth) {
            String val = randString.genString(rand.nextInt(3) + 1);
            return new ExpTree(val);
        } else {
            Operation op;
            int r = rand.nextInt(Operation.values().length);
            switch (r) {
                case 0:
                    op = Operation.CONCAT;
                    break;
                case 1:
                    op = Operation.UNION;
                    break;
                case 2:
                    op = Operation.STAR;
                    break;
                case 3:
                    op = Operation.INTERSECT;
                    break;
                default:
                    op = null;
            }

            ExpTree tree = new ExpTree(op);
            tree.right = randomRE(depth - 1, prob);
            if (op != Operation.STAR) {
                tree.left = randomRE(depth - 1, prob);
            }
	    /*
	    if (op != Operation.STAR) {
                tree.left = randomRE(size - 1, prob);
            }else{
		int i = rand.nextInt(size);
		tree.left = randomRE(i, prob);
		tree.right = randomRE(size -i, prob);
		}*/

	    
            return tree;

        }
    }

    public static void assign(ArrayList<String> randStrings) {
        int sum = 0;
        for (int i = 0; i < MAX_POWER; i++) {
            sum += Math.pow(BASE, i + 1);
        }
        for (int i = 1; i <= sum; i++) {// Math.pow(BASE, MAX_POWER); i++) {
            randStrings.add(generate(i));
        }
    }

    public static String generate(int aux) {
        if (aux < BASE + 1) {
            return Character.toString((char) (aux + 96));
        } else {
            if (aux % BASE == 0) {
                return generate((aux / BASE) - 1) + generate(
                        ((aux - 1) % BASE + 1));
            } else {
                return generate(aux / BASE) + generate(aux % BASE);
            }
        }
    }

    public String printRE(ExpTree re) {
        String ans = "";
        ans = ans + re.op + "(";
        if (re.op != Operation.STAR) {
            if (re.left.value != null) {
                ans = ans + re.left.value + ",";
            } else {
                ans = ans + printRE(re.left) + ",";
            }
        }
        if (re.right.value != null) {
            ans = ans + re.right.value;
        } else {
            ans = ans + printRE(re.right);
        }
        ans = ans + ")";
        return ans;

    }

    public static void main(String[] args) {
        Tester t = new Tester();
        System.out.println("Generating tree with alphabet \"" + ALPHABET + "\" of depth " + DEPTH + "...");
        ExpTree randTree = t.randomRE(DEPTH, DEPTH);
        System.out.println(t.printRE(randTree));
        DFA dfa = new DFA();
        dfa.createDFA(randTree);
        // for (int i = 0; i < dfa.transitions.size; i++) {
        //     System.out.println(
        //             dfa.transitions.trans[i].current + " " + dfa.transitions.trans[i].letter + " " + dfa.transitions.trans[i].next);
        // }

        String alphabet = "abc";
        RandStrGen rsg = new RandStrGen(alphabet);
        BASE = alphabet.length();
        ArrayList<String> randStrings = new ArrayList<>();

        assign(randStrings);

        REMatcher matcher = new REMatcher(randTree);
        ArrayList<String> matches = new ArrayList<>();
        ArrayList<String> nots = new ArrayList<>();
        for (int i = 0; i < randStrings.size(); i++) {
            if (matcher.isMatch(randStrings.get(i))) {
                matches.add(randStrings.get(i));
            } else {
                nots.add(randStrings.get(i));
            }
        }
        String toPrint = "matches for all strings of length " + MAX_POWER + " or less: ";
        for (int a = 0; a < matches.size(); a++) {
            toPrint = toPrint + matches.get(a);
            if(a != matches.size() - 1) toPrint = toPrint + ", ";
        }
        System.out.println(toPrint);

        System.out.println("\nDFA Information");
        DFA ourdfa = matcher.dfa;
        System.out.println("---------------");
        System.out.println("Number of states (Q):                            " + ourdfa.states.size);
        System.out.println("Number of transitions (Q x |Sigma|):             " + ourdfa.transitions.size);
        System.out.println("Steps to create DFA (Q^2 x |Sigma| x 2^(depth)): " + (int)(Math.pow(ourdfa.states.size, 2) * ourdfa.transitions.size * Math.pow(2, DEPTH)));
    }
}
