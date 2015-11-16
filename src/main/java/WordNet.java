import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {
    private Map<String, List<Integer>> nouns = new HashMap<>();
    private Map<Integer, String> ids = new HashMap<>();
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        In synsetsStream = new In(synsets);
        int id = 0;
        while (synsetsStream.hasNextLine()) {
            String[] line = synsetsStream.readLine().split(",");
            id = Integer.parseInt(line[0]);
            ids.put(id, line[1]);
            String[] words = line[1].split(" ");
            for (String word : words) {
                if (!nouns.containsKey(word)) {
                    nouns.put(word, new ArrayList<>());
                }
                nouns.get(word).add(id);
            }
        }
        Digraph graph = new Digraph(id + 1);
        In hypernymsStream = new In(hypernyms);
        while (hypernymsStream.hasNextLine()) {
            String[] line = hypernymsStream.readLine().split(",");
            int synset = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int hypernym = Integer.parseInt(line[i]);
                graph.addEdge(synset, hypernym);
            }
        }
        int root = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) {
                root = i;
                break;
            }
        }
        BreadthFirstDirectedPaths bfsRoot = new BreadthFirstDirectedPaths(graph.reverse(), root);
        for (int i = 0; i < graph.V(); i++) {
            if (!bfsRoot.hasPathTo(i)) {
                throw new IllegalArgumentException("Input is not rooted");
            }
        }
        if (new DirectedCycle(graph).hasCycle()) {
            throw new IllegalArgumentException("Input has cycle");
        }
        this.sap = new SAP(graph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        if (nouns.containsKey(nounA) && nouns.containsKey(nounB)) {
            return sap.length(nouns.get(nounA), nouns.get(nounB));
        } else {
            throw new IllegalArgumentException("word 1: <" + nounA + "> and word 2: <" + nounB + ">");
        }
    }

    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        if (nouns.containsKey(nounA) && nouns.containsKey(nounB)) {
            return ids.get(sap.ancestor(nouns.get(nounA), nouns.get(nounB)));
        } else {
            throw new IllegalArgumentException("word 1: <" + nounA + "> and word 2: <" + nounB + ">");
        }
    }

    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        System.out.println(net.distance("thing", "change"));
    }
}
