package phase4;

import phase4.ExpTree.Operation;
import java.util.Random;

// javac phase4/Tester.java phase4/REMatcher.java phase4/DFA.java phase4/ExpTree.java phase4/Alphabet.java
public class Tester {

    public ExpTree randomRE(int depth, int prob) {
        RandStrGen randString = new RandStrGen("abc");
        Random rand = new Random();
        
        int leafProb = rand.nextInt(prob) + 1;
        if(leafProb > depth) {        
            String val = randString.genString(rand.nextInt(3) + 1);
            return new ExpTree(val);
        }
        else {
            Operation op;
            int r = rand.nextInt(Operation.values().length);
            switch(r) {
                case 0: op = Operation.CONCAT;
                        break;
                case 1: op = Operation.UNION;
                        break;
                case 2: op = Operation.STAR;
                        break;
                case 3: op = Operation.INTERSECT;
                        break;
                default: op = null;
            }
            
            ExpTree tree = new ExpTree(op);
            tree.right = randomRE(depth - 1, prob);
            if(op != Operation.STAR) tree.left = randomRE(depth - 1, prob);
            return tree;
               
        }
    }
    
    public String printRE(ExpTree re) {
        String ans = "";
        ans = ans + re.op + "(";
        if(re.op != Operation.STAR) {
            if(re.left.value != null) ans = ans + re.left.value + ",";
            else ans = ans + printRE(re.left) + ",";
        }
        if(re.right.value != null) ans = ans + re.right.value;
        else ans = ans + printRE(re.right);
        ans = ans + ")";
        return ans;
        
    }
    
    public static void main(String[] args) {
        Tester t = new Tester();
        ExpTree randTree = t.randomRE(4,4);
        System.out.println(t.printRE(randTree));
        DFA dfa = new DFA();
        dfa.createDFA(randTree);
        for (int i = 0; i < dfa.transitions.size; i++) {
            System.out.println(dfa.transitions.trans[i].current + " " + dfa.transitions.trans[i].letter + " " + dfa.transitions.trans[i].next);
        }
        
        RandStrGen rsg = new RandStrGen("abc");
        String[] randStrings = new String[92];
        randStrings[0] = "a";
        randStrings[1] = "b";
        randStrings[2] = "c";
        randStrings[3] = "aa";
        randStrings[4] = "ab";
        randStrings[5] = "ac";
        randStrings[6] = "ba";
        randStrings[7] = "bb";
        randStrings[8] = "bc";
        randStrings[9] = "ca";
        randStrings[10] = "cb";
        randStrings[11] = "cc";
        int i = 12;
        for(int j = 3; j < 7; j++) {
            for(int k = 0; k < 20; k++) {
                randStrings[i] = rsg.genString(j);
                i++;
            }
        }
        
        REMatcher matcher = new REMatcher(randTree);
        String[] matches = new String[92];
        String[] nots = new String[92];
        int matchIndex = 0;
        int notIndex = 0;
        for(i = 0; i < 92; i++) {
            if(matcher.isMatch(randStrings[i])) {
                matches[matchIndex] = randStrings[i];
                matchIndex++;
            }
            else {
                nots[notIndex] = randStrings[i];
                notIndex++;
            }
        }
        String toPrint = "matches: ";
        for(int a = 0; a < matchIndex; a++) toPrint = toPrint + matches[a] + ", ";
        toPrint = toPrint + "\nnots: ";
        for(int a = 0; a < notIndex; a++) toPrint = toPrint + nots[a] +  ", ";
        System.out.println(toPrint);
    }
}
