package phase3;
import java.util.Random;
import java.lang.Math;

public class RandStrGen {
    
    private Alphabet Alph; 
    private Random rand;
    
    public RandStrGen(Alphabet A) {
        this.Alph = A;
        this.rand = new Random();
    }
    
    public String genString(int length) {
        
        char[] values = this.Alph.getValues();
        String output = "";
        
        for ( int i=0; i<length; i++ ) {
            // gen random int b/t 0 and length of alphabet
            int charIndex = this.rand.nextInt(this.Alph.size);
            output += values[charIndex];
        }
        
        this.rand = new Random();
        return output;
    }
    
    public String[] genStrings(int numStrings, int length ) {
        
        String[] strList = new String[numStrings];
        for ( int i=0; i<numStrings; i++ ) {
            strList[i] = this.genString(length);
        }
        return strList;
    }
    
    public static void main(String[] args) {
        
        int numToGen = Integer.parseInt(args[0]);
        int strLen = Integer.parseInt(args[1]);
        System.out.println( "Testing: generating " + numToGen + " strings of length... " + strLen );
        
        Alphabet Alph = new Alphabet();
        RandStrGen generator = new RandStrGen(Alph);
        String[] strList = generator.genStrings(numToGen, strLen);
        
        String format = "%" + ((int)Math.log10(numToGen)+2) + "d%" + (int)(strLen+4) + "s";
        for ( int i=0; i<strList.length; i++ ) {
            String outStr = String.format(format, i+1, strList[i] );
            System.out.println( outStr );
        }
        
        System.out.println( "Testing complete." );
            
    }
    
}