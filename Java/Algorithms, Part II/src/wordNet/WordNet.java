package wordNet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac WordNet.java
 * Execution:    java WordNet synsets.txt hypernyms.txt
 * Dependencies: SAP.java, Digraph.java, DirectedCycle.java, In.java
 *
 * Description:  A immutable data type for finding a synset in a shortest
 *               ancestral path between nounA and nounB in a wordnet.
 * http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html
 *
 *************************************************************************/

public class WordNet {
    
    private HashMap<Integer, String> id_noun;        // stores ID(key) - words(value) of synsets
    private HashMap<String, List<Integer>> noun_id;  // stores word(key) - IDs(value) of synsets
    private SAP sap;                                 // a SAP object
    
    /**
     * Initializes a wordnet from given filenames of synsets and hypernyms in
     * format specified below.
     */
    public WordNet(String synsets, String hypernyms) {
        id_noun = new HashMap<Integer, String>();
        noun_id = new HashMap<String, List<Integer>>();
        
        In in1 = new In(synsets);
        int V = 0;
        List<Integer> ids;
        while (in1.hasNextLine()) {
            String[] synset = in1.readLine().split(",");
            int id = Integer.parseInt(synset[0]);
            String nouns = synset[1];
            
            // put id and nouns in id_noun
            id_noun.put(id, nouns);
            
            // put noun and its id in noun_id
            String[] noun_array = nouns.split(" ");
            for (String noun : noun_array) {
                if (!noun_id.containsKey(noun)) ids = new ArrayList<Integer>();
                else                            ids = noun_id.get(noun);
                ids.add(id);
                noun_id.put(noun, ids);
            }
            V++;
        }
        
        // initializes an empty digraph with V vertices and add edges from hypernyms
        Digraph G = new Digraph(V);
        In in2 = new In(hypernyms);
        while (in2.hasNextLine()) {
            String[] st = in2.readLine().split(",");
            int v = Integer.parseInt(st[0]);
            for (int i = 1; i < st.length; i++) {
                int w = Integer.parseInt(st[i]);
                G.addEdge(v, w);
            }
        }
        
        // throw exception if G contains cycle
        DirectedCycle DG = new DirectedCycle(G);
        if (DG.hasCycle()) {
            throw new IllegalArgumentException();
        }
        
        // throw exception if G contains more than 1 root
        int root = 0;
        for (int i = 0; i < G.V(); i++) {
            if (!G.adj(i).iterator().hasNext()) {
                root++;
                if (root > 1) throw new IllegalArgumentException();
            }
        }
        
        // initializes a SAP
        sap = new SAP(G);
    }
    
    
    /**
     * Returns all WordNet nouns.
     */
    public Iterable<String> nouns() { return noun_id.keySet(); }
    
    /**
     * Is the word a WordNet noun?
     */
    public boolean isNoun(String word) {
        if (word == null) throw new NullPointerException();
        return noun_id.containsKey(word);
    }
    
    /**
     * Returns distance between nounA and nounB (defined below).
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        List<Integer> idsA = noun_id.get(nounA);
        List<Integer> idsB = noun_id.get(nounB);
        return sap.length(idsA, idsB);
    }
    
    /**
     * Returns a synset (second field of synsets.txt) that is the common ancestor of
     * nounA and nounB in a shortest ancestral path (defined below).
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        List<Integer> idsA = noun_id.get(nounA);
        List<Integer> idsB = noun_id.get(nounB);
        int ancestor = sap.ancestor(idsA, idsB);
        if (ancestor == -1) return null;
        return id_noun.get(ancestor);
    }
    
    public static void main(String[] args) {
        
        // do unit testing of this class
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);
        System.out.println(wn.distance("field", "event"));
    }
}