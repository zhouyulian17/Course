package baseball;

import java.util.ArrayList;
import java.util.HashMap;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/*************************************************************************
 * Name: Yulian Zhou
 * Email: zhouyulian17@gmail.com
 *
 * Compilation:  javac BaseballElimination.java
 * Execution:    java BaseballElimination team.txt
 * Dependencies: FlowEdge.java, FlowNetwork.java, FordFulkerson.java,
 *               In.java
 *
 * Description:  An immutable data type that represents a sports division
 *               and determines which teams are mathematically eliminated.
 * http://coursera.cs.princeton.edu/algs4/assignments/baseball.html
 *
 *************************************************************************/

public class BaseballElimination {
    
    private HashMap<String, Integer> team_id;  // stores name(key) - id(value) of teams
    private HashMap<Integer, String> id_team;  // stores id(key) - name(value) of teams
    private final int N;    // number of teams
    private final int V;    // number of vertices in flow network
    private int[] w;        // w[i] = wins team i has
    private int[] l;        // l[i] = losses team i has
    private int[] r;        // r[i] = remaining games team i has
    private int[][] g;      // g[i][j] = games left team i has to play against team j
    
    /**
     * Creates a baseball division from given filename in format specified below.
     */
    public BaseballElimination(String filename) {
        In in = new In(filename);
        N = in.readInt();
        V = (N - 1) * (N - 2) / 2 + N + 1;
        team_id = new HashMap<String, Integer>();
        id_team = new HashMap<Integer, String>();
        w = new int[N];
        l = new int[N];
        r = new int[N];
        g = new int[N][N];
        for (int i = 0; i < N; i++) {
            String s = in.readString();
            team_id.put(s, i);
            id_team.put(i, s);
            w[i] = in.readInt();
            l[i] = in.readInt();
            r[i] = in.readInt();
            for (int j = 0; j < N; j++) {
                g[i][j] = in.readInt();
            }
        }
    }
    
    /**
     * Returns the number of teams.
     */
    public int numberOfTeams() { return N; }
    
    /**
     * Returns names of all teams.
     */
    public Iterable<String> teams() { return team_id.keySet(); }
    
    /**
     * Returns number of wins for given team.
     */
    public int wins(String team) {
        if (!team_id.containsKey(team)) throw new IllegalArgumentException();
        return w[team_id.get(team)];
    }
    
    /**
     * Returns number of losses for given team.
     */
    public int losses(String team) {
        if (!team_id.containsKey(team)) throw new IllegalArgumentException();
        return l[team_id.get(team)];
    }
    
    /**
     * Returns number of remaining games for given team.
     */
    public int remaining(String team) {
        if (!team_id.containsKey(team)) throw new IllegalArgumentException();
        return r[team_id.get(team)];
    }
    
    /**
     * Returns number of remaining games between team1 and team2.
     */
    public int against(String team1, String team2) {
        if (!team_id.containsKey(team1)) throw new IllegalArgumentException();
        if (!team_id.containsKey(team2)) throw new IllegalArgumentException();
        return g[team_id.get(team1)][team_id.get(team2)];
    }
    
    /**
     * Is given team eliminated?
     */
    public boolean isEliminated(String team) {
        if (!team_id.containsKey(team)) throw new IllegalArgumentException();
        int s = team_id.get(team);
        
        // max number of games another team can win when a given team is not eliminated
        int max_win = w[s] + r[s];
        
        // a team is eliminated if any other team wins more than max_win (trivial elimination)
        for (int i = 0; i < N; i++) {
            if (max_win < w[i]) return true;
        }
        
        // reduce the baseball elimination problem to the maxflow problem
        FordFulkerson ff = getFordFulkerson(s, max_win);
        
        // a team is eliminated if some edges pointing from s are not full in the maxflow (nontrivial elimination)
        for (int i = 0; i < N; i++) {
            if (ff.inCut(i) && i != s) return true;
        }
        
        return false;
    }
    
    /**
     * Returns a FordFulkerson object from an artificial source s to each game vertex i-j with capacity g[i][j];
     * game vertex i-j is connected to team vertex i and vertex j with capacity g[i][j];
     * each team vertex is connected to the sink vertex t (N) with capacity max_win - w[i].
     */
    private FordFulkerson getFordFulkerson(int s, int max_win) {
        FlowNetwork G = new FlowNetwork(V);
        int match_ij = N + 1;
        for (int i = 0; i < N; i++) {
            if (i == s) continue;
            FlowEdge Ei_t = new FlowEdge(i, N, max_win - w[i]);
            G.addEdge(Ei_t);
            for (int j = i + 1; j < N; j++) {
                if (j == s) continue;
                FlowEdge Es_ij = new FlowEdge(s, match_ij, g[i][j]);
                FlowEdge Eij_i = new FlowEdge(match_ij, i, g[i][j]);
                FlowEdge Eij_j = new FlowEdge(match_ij, j, g[i][j]);
                G.addEdge(Es_ij);
                G.addEdge(Eij_i);
                G.addEdge(Eij_j);
                match_ij++;
            }
        }
        return new FordFulkerson(G, s, N);
    }
    
    /**
     * Returns subset R of teams that eliminates given team; null if not eliminated.
     */
    public Iterable<String> certificateOfElimination(String team) {
        if (!team_id.containsKey(team)) throw new IllegalArgumentException();
        int s = team_id.get(team);
        int max_win = w[s] + r[s];
        ArrayList<String> list = new ArrayList<String>();
        
        // trivial elimination
        for (int i = 0; i < N; i++) {
            if (max_win < w[i]) list.add(id_team.get(i));
        }
        if (!list.isEmpty()) return list;
        
        // nontrivial elimination
        FordFulkerson ff = getFordFulkerson(s, max_win);
        for (int i = 0; i < N; i++) {
            if (ff.inCut(i) && i != s) list.add(id_team.get(i));
        }
        return list;
    }
    
    /**
     * Unit tests the BaseballElimination data type.
     */
    public static void main(String[] args) {
        String file = args[0];
        BaseballElimination division = new BaseballElimination(file);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}