package phase4;

import phase4.ExpTree.Operation;

// javac phase3/Tester.java phase3/REMatcher.java phase3/DFA.java phase3/ExpTree.java phase3/Alphabet.java
public class Tester {

    public static void main(String[] args) {
        ExpTree c = new ExpTree("c");
        ExpTree ab = new ExpTree("ab");

        ExpTree cab = new ExpTree(Operation.CONCAT);
        cab.left = c;
        cab.right = ab;

        ExpTree dac = new ExpTree("dac");

        ExpTree or = new ExpTree(Operation.UNION);
        or.left = cab;
        or.right = dac;

        ExpTree a = new ExpTree("a");
        ExpTree astar = new ExpTree(Operation.STAR);
        astar.right = a;

        ExpTree b = new ExpTree("b");
        ExpTree bstar = new ExpTree(Operation.STAR);
        bstar.right = b;

        ExpTree astarbstar = new ExpTree(Operation.UNION);
        astarbstar.left = astar;
        astarbstar.right = bstar;

        ExpTree or2 = new ExpTree(Operation.UNION);
        or2.left = or;
        or2.right = astarbstar;

        REMatcher re = new REMatcher(or2);
        RandStrGen rand = new RandStrGen("abcd");

        System.out.println("Checking soundness and completeness...");
        System.out.println(
                "Language: ((c CONCAT ab) UNION dac UNION (a STAR UNION b STAR))");
        System.out.println(
                "Generating 100 random strings and checking if they match the above RE...\n");
        String[] randomStrings = rand.genStrings(100, 3);
        for (int i = 0; i < randomStrings.length; i++) {
            String s = randomStrings[i];
            System.out.println(s + ": " + re.isMatch(s));
        }

        System.out.println(
                "\nChecking efficency of the same regular expression...");
        System.out.println("Total number of states:      " + re.dfa.states.size);
        System.out.println(
                "Total number of transitions: " + re.dfa.transitions.size);
        System.out.println(
                "The time it takes to create a DFA is O(|states|^2 * |transitions|)");
    }

}
