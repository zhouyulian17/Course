package eliza;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/*************************************************************************
 * Name:  Yulian Zhou, Zheng Kuang
 * Email: zhouyulian17@gmail.com, kuangzheng04@gmail.com
 *
 * Compilation:  javac Eliza.java
 * Execution:    java Eliza eliza.txt
 * Dependencies: Key.java, Decomp.java, EString.java
 *
 * Description:  A data type that takes user input string from console,
 *               processes it and formulates a reply according to its
 *               decomposition and reassembly rules.
 *               (synonym matching and goto key not supported)
 *
 *************************************************************************/

public class Eliza {
    
    private LinkedList<Key> keyList = new LinkedList<Key>();            // list of keys sorted by rank
    private Map<String, String> pre = new HashMap<String, String>();    // word transformation entries before processing
    private Map<String, String> post = new HashMap<String, String>();   // word transformation entries after processing
    private Set<String> quit = new HashSet<String>();                   // word for quit
    private String hello;                                               // welcome words
    private String bye;                                                 // farewell words
    private boolean finished = false;                                   // is the chat finished?
    
    /**
     * Initializes an Eliza that stores keys with decomps and reasmbs.
     */
    public Eliza(Scanner sc) {
        String[] lines;        // to store match string
        int decomp_num = 0;    // number decomps for a given key
        
        // read lines
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            lines = new String[4];
            if (EString.match(s, "*reasmb: *", lines)) {
                Key k = keyList.get(0);
                List<Decomp> dList = k.getDecomp();
                Decomp d = dList.get(decomp_num - 1);
                d.addReasmb(lines[1]);
            }
            else if (EString.match(s, "*decomp: *", lines)) {
                Key k = keyList.get(0);
                k.addDecomp(new Decomp(lines[1]));
                decomp_num++;
            }
            else if (EString.match(s, "*key: * #*", lines)) {
                int n = 0;
                if (lines[2].length() != 0)
                    n = Integer.parseInt(lines[2]);
                keyList.addFirst(new Key(lines[1], n));
                decomp_num = 0;
            }
            else if (EString.match(s, "*key: *", lines)) {
                keyList.addFirst(new Key(lines[1], 0));
                decomp_num = 0;
            }
            else if (EString.match(s, "*pre: * *", lines))   pre.put(lines[1], lines[2]);
            else if (EString.match(s, "*post: * *", lines))  post.put(lines[1], lines[2]);
            else if (EString.match(s, "*quit: *", lines))    quit.add(lines[1]);
            else if (EString.match(s, "*initial: *", lines)) hello = lines[1];
            else if (EString.match(s, "*final: *", lines))   bye = lines[1];
        }
        Collections.sort(keyList); // sort Keys in keyList by its rank
        // for (Key k : keyList) System.out.println(k);
    }
    
    /**
     * Starts a new chat: reads user's input string, does the input
     * transformation and output the processed script.
     */
    public void run() {
        System.out.println(hello);
        String s = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        do {
            try {
                s = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String reply = resp(s);
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(reply);
        } while (!finished);
    }
    
    // process an input string s, and generate a reply string.
    private String resp(String s) {
        s = EString.process(s);
        String[] lines = new String[2];
        String reply = null;
        if (EString.match(s, "*.*", lines)) {
            reply = reply(lines[0]);
            if (reply != null) return reply;
            s = EString.trim(lines[1]);
        }
        if (s.length() != 0) {
            reply = reply(s);
            if (reply != null) return reply;
        }
        Key key = null;
        for (Key k : keyList) {
            if (k.getKey().equals("xnone")) {
                key = k;
                break;
            }
        }
        if (key != null) {
            reply = decompose(key, s);
            if (reply != null) return reply;
        }
        return "I don't know what to say.";
    }
    
    // transformation with pre entries:
    // returns farewell words if input string s contains quit word
    // otherwise try to match s with keys in keyList
    // - if match, generates reply by decomposing and reassembling
    // - if failed, reply is null
    private String reply(String s) {
        s = replace(s, pre);
        if (quit.contains(s)) {
            finished = true;
            return bye;
        }
        s = EString.addPad(s);
        for (Key k : keyList) {
            String[] lines = new String[2];
            if (EString.match(s, "*" + k.getKey() + "*", lines)) {
                String reply = decompose(k, s);
                if (reply != null) return reply;
            }
        }
        return null;
    }
    
    // try to match string s with decompose pattern in a given Key k, and
    // assembles a reply if succeeded; otherwise reply is null
    private String decompose(Key k, String s) {
        String[] inputLines = new String[10];
        for (Decomp d : k.getDecomp()) {
            String pat = d.getPattern();
            if (EString.match(s, pat, inputLines)) {
                String reply = assemble(d, inputLines);
                if (reply != null) return reply;
            }
        }
        return null;
    }
    
    // assembles a reply from inputLines[] (transformed with post entries)
    // with current rule in decomp d, and updates rule in d
    private String assemble(Decomp d, String[] inputLines) {
        String[] reasmbLines = new String[3];
        String reasmb = d.currentRule();
        d.nextRule();
        String s = "";
        if (EString.match(reasmb, "* (#)*", reasmbLines)) {
            int n = Integer.parseInt(reasmbLines[1]) - 1;
            s = reasmbLines[0] + " " + replace(inputLines[n], post);
            reasmb =reasmbLines[2];
        }
        s += reasmb;
        return s;
    }
    
    // transformation of words from input string with pre or post entries
    // (substitute the key word with its value in entry map)
    private String replace(String s, Map<String, String> map) {
        String[] lines = new String[2];
        s = EString.trim(s);
        String t = "";
        while(EString.match(s, "* *", lines)) {
            if (map.containsKey(lines[0])) lines[0] = map.get(lines[0]);
            t += lines[0] + " ";
            s = EString.trim(lines[1]);
        }
        if (pre.containsKey(s)) s = pre.get(s);
        t += s;
        if (t.charAt(t.length() - 1) == ' ') t = t.substring(0, t.length() - 1);
        return t;
    }
    
    public static void main(String[] args) {
        // unit test
        Scanner sc = null;
        try {
            sc = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Eliza eliza = new Eliza(sc);
        eliza.run();
    }
}