package phase5;

import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
// import java.util.Math;
import phase5.ExpTree.Operation;

// javac phase5/Tester.java phase5/REMatcher.java phase5/DFA.java phase5/ExpTree.java phase5/Alphabet.java
public class Tester {
    public static int BASE;
    public static final int MAX_POWER = 6;
    public static final int SIZE = 5;
    public static final String ALPHABET = "abc";

    // Returns a randomly generated ExpTree with up to 'size' nodes 
    // (calls w/ size=2 instead return a tree with only 1 node - otherwise, 
    // there would be a disproportionate number of * nodes, as they would be 
    // added when RNG picks * for a node AND when a call is made with size=2
    public ExpTree randomRE(int size) {
        RandStrGen randString = new RandStrGen(ALPHABET);
        Random rand = new Random();
        
        if(size == 1 || size == 2) {
            String rs = randString.genString(rand.nextInt(3) + 1);
            return new ExpTree(rs);
        }
        
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
        if(op == Operation.STAR) {
            tree.right = randomRE(size - 1);
        }
        else {
            int lsize = rand.nextInt(size - 2) + 1;
            if(lsize == 2) lsize = 1;
            else if(lsize == size - 3) lsize = size - 2;
            int rsize = size - lsize - 1;
            tree.left = randomRE(lsize);
            tree.right = randomRE(rsize);
        }
        return tree;
    }

    // Given an ArrayList<String>, adds a number of randomly generated strings
    // to that ArrayList
    // BEFORE CALLING ME: Set global variable 'BASE'
    //                    Set global variable 'MAX_POWER'
    public static void assign(ArrayList<String> randStrings) {
        int sum = 0;
        for (int i = 0; i < MAX_POWER; i++) {
            sum += Math.pow(BASE, i + 1);
        }
        for (int i = 1; i <= sum; i++) {// Math.pow(BASE, MAX_POWER); i++) {
            randStrings.add(generate(i));
        }
    }

    // Returns a randomly generated string using 'aux' as a seed
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
    
    // Returns true if the given ExpTree has at least one * node with * as its child(    eg. (a*)*   ) 
    public static boolean hasStarStack(ExpTree t) {
        
        if (t.op==null) { // Leaf node
            return false;
        
        } else { // Op node
            
            if (t.op==Operation.STAR) { // Op is STAR
                
                if (t.right.op==Operation.STAR) { // And right child is star -- found a star stack
                    return true;
                
                } else { // Right child not star -- recurse
                    return (hasStarStack(t.right));
                }
            
            } else { // Op is UNION/INTERSECT/CONCAT
                return (hasStarStack(t.left) || hasStarStack(t.right));
            }
        }
        
    }
    
    // Run hasStarStack on the list of ExpTrees, return list of ExpTrees containing star stacks
    public ArrayList<String> findStarStacks(ExpTree[] trees) {
        ArrayList<String> found = new ArrayList<>();
        for (ExpTree t: trees) {
            if (hasStarStack(t)) {
                found.add(t.convert(t));
            }
        }
        return found;
    }
    
    // Returns false as soon as a randomly generated string is accepted by
    // either the DFA or the Java Regex and is not accepted by the other. If 
    // no strings do so, returns true
    public boolean testCompleteness(int numTrials, int lenOfStrs) {
        int numToGen = numTrials;
        int strLen = lenOfStrs;
        String[] randStrs;
        RandStrGen randString = new RandStrGen(ALPHABET);
        randStrs = randString.genStrings(numToGen, strLen);
        System.out.println("Finished generating random strings.");
        
	// ###################################### TESTING FOR STAR STACKS #####################################################
	/*
        Derivative d = new Derivative();
        ExpTree[] trees = new ExpTree[50]; // Empty array to hold 50 ExpTrees
        
        for (int i=0; i<50; i++) {                 // Fill each slot in trees by first generating a randomRE of size 10,
            trees[i] = d.simplify(randomRE(10));   // then simplifying it and adding to the array
        }
        ArrayList<String> found = findStarStacks(trees);  // The final array containing the simplified ExpTrees that had star stacks
        for ( String item: found ) {
            System.out.println( item );
	    } */
	// ########################################################################################################################### 
       
        for ( int i=0; i<numToGen; i++ ) {
            ExpTree tree = randomRE(strLen);
            Derivative d = new Derivative();
            ExpTree simp = d.simplify(tree);
            String treeStr = simp.convert(simp);
            Pattern p = Pattern.compile(treeStr);
            Matcher m = p.matcher(randStrs[i]);
            REMatcher matcher = new REMatcher(simp);
            String format = "%" + ((int) Math.log10(numToGen) + 2) + "d%" + (int) (strLen + 4) + "s";
            String outStr = String.format(format, i + 1, randStrs[i]);
            System.out.println(outStr + ", RE: " + treeStr );
            if (m.matches() && !matcher.isMatch(randStrs[i]) ) {
                return false;
            } else if ( !m.matches() && matcher.isMatch(randStrs[i]) ) {
                return false;
            }
        }
            
        return true;
        
    } 

    public static void main(String[] args) {
        Tester t = new Tester();
        System.out.println("Generating tree with alphabet \"" + ALPHABET + "\" of size " + SIZE + "...");
        ExpTree randTree = t.randomRE(SIZE);
        System.out.println(t.printRE(randTree));
        DFA dfa = new DFA();
        dfa.createDFA(randTree);

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
        System.out.println("Steps to create DFA (Q^2 x |Sigma| x 2^(size)): " + (int)(Math.pow(ourdfa.states.size, 2) * ourdfa.transitions.size * Math.pow(2, SIZE)));
    
	// Uncomment me for completeness testing
        // System.out.println(t.testCompleteness(50,10)); */
    }
}
