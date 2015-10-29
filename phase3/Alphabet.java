package phase3;

public class Alphabet {

    private int numDigits;
    private int numLowerCase;
    private int numUpperCase;
    private char[] values;
    public int size;
    private int curIndex;

    // Constructor to produce Alphabet object containing 0-9, a-z, A-Z
    public Alphabet() {
	
	this.numDigits = 58-48; // decimal values of char digits 
	this.numLowerCase = 123-97; // decimal values of lowercase letters
	this.numUpperCase = 91-65; // decimal values of uppercase letters
	this.size = this.numDigits + this.numLowerCase + this.numUpperCase;
	this.values = new char[this.size];
	int curIndex = 0;

	// digits
	for (int i=48; i<58; i++) {
	    this.values[curIndex] = (char)i;
	    curIndex++;
	}

	// lowercase characters
	for (int j=97; j<123; j++) {
	    this.values[curIndex] = (char)j;
	    curIndex++;
	}

	// uppercase characters
	for (int k=65; k<91; k++) {
	    this.values[curIndex] = (char)k;
	    curIndex++;
	}
	
    }

    public void printAll() {
	System.out.println(this.values);
    } 

    public char[] getValues() {
	return this.values;
    }

    public static void main(String[] args) {
	
	Alphabet alph = new Alphabet();
	alph.printAll();

    }

}