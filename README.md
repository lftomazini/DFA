## Phase 3 Notes
- compile DFA.java: `javac phase3/DFA.java phase3/ExpTree.java phase3/Alphabet.java`
- compile REMatcher.java: `javac phase3/REMatcher.java phase3/DFA.java phase3/ExpTree.java phase3/Alphabet.java`
- compile Tester.java: `javac phase3/Tester.java phase3/REMatcher.java phase3/DFA.java phase3/ExpTree.java phase3/Alphabet.java`
- DFA.java can be run to view the transitions of a tree that is created. REMatcher.java can be used to see if any word matches any DFA. Tester.java is used to run multiple words on a somewhat complex DFA, as well as giving runtime and spacetime efficencies.

## Phase 4 Notes:
- compile all files: 'make'
- remove class files: 'make clean'
- Tester.java can be run to test the creation of a DFA and print out the number of states it has, the number of transistions it has (Q*|Sigma|), and the number of steps required to create the DFA (|Q|^2 * |Sigma| * 2^(depth)). Before running Tester.java, set the MAX_POWER and DEPTH static variables defined just beneath the class declaration statement to the values of your choosing.