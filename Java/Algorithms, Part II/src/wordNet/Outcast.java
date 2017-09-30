package wordNet;

import edu.princeton.cs.algs4.In;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac Outcast.java
 * Execution:    java Outcast synsets.txt hypernyms.txt outcast.txt
 * Dependencies: WordNet.java
 *
 * Description:  A immutable data type for identifying an outcast of
 *               a list of wordnet nouns.
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 *************************************************************************/

public class Outcast {
    
    private WordNet wn;    // a WordNet object
    
    /**
     * Constructor that takes a WordNet object.
     */
    public Outcast(WordNet wordnet) { wn = wordnet; }
    
    /**
     * Given an array of WordNet nouns, returns an outcast.
     */
    public String outcast(String[] nouns) {
        int outcast = 0;
        long maxDistance = 0;
        for (int i = 0; i < nouns.length; i++) {
            long iDistance = 0;
            for (int j = 0; j < i; j++) {
                iDistance += wn.distance(nouns[i], nouns[j]);
            }
            for (int j = i + 1; j < nouns.length; j++) {
                iDistance += wn.distance(nouns[i], nouns[j]);
            }
            if (iDistance > maxDistance) {
                maxDistance = iDistance;
                outcast = i;
            }
        }
        return nouns[outcast];
    }
    
    /**
     * Unit tests the Outcast data type.
     */
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);
        
        Outcast oc = new Outcast(wn);
        In in = new In(args[2]);
        String[] nouns = in.readAllStrings();
        System.out.println(oc.outcast(nouns));
    }
}