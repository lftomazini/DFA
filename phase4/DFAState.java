package phase4;

public class DFAState {
    public ExpTree re;
    public boolean halt;
    public Tuple[] trans;
    public Alphabet alpha;
    public Derivative deriv;

    public DFAState(ExpTree language) {
        this.re = language;
        this.halt = false;
        this.trans = new Tuple[100];
        this.alpha = new Alphabet();
        this.deriv = new Derivative();
        initialize();
    }

    public void initialize() {
        char[] a = this.alpha.getValues();
        for (int i = 0; i < a.length; i++) {
            char letter = a[i];
            DFAState newState = new DFAState(this.deriv.getDerivative(letter,
                                                                      this.re));
            for (int j = i; j >= 0; j--) {
                if (this.trans[j].tree.equals(newState.re)) {
                    this.trans[i].tree = this.trans[j].tree;
                } else {
                    this.trans[i] = new Tuple(letter, newState);
                }
            }
        }
    }

    public static void main(String[] args) {
        DFAState dfa = new DFAState(null);
    }
}

class Tuple {
    public char c;
    public DFAState tree;

    public Tuple(char ch, DFAState t) {
        this.c = ch;
        this.tree = t;
    }
}
