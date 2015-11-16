import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

import java.util.ArrayList;
import java.util.List;

public class SAP {
    private Digraph G;

    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    public int length(int v, int w) {
        List<Integer> sourcesV = new ArrayList<>();
        sourcesV.add(v);
        List<Integer> sourcesW = new ArrayList<>();
        sourcesW.add(w);
        return length(sourcesV, sourcesW);
    }

    public int ancestor(int v, int w) {
        List<Integer> sourcesV = new ArrayList<>();
        sourcesV.add(v);
        List<Integer> sourcesW = new ArrayList<>();
        sourcesW.add(w);
        return ancestor(sourcesV, sourcesW);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int minDist = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < minDist || minDist == -1) {
                    minDist = dist;
                }
            }
        }
        return minDist;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int ancestor = -1;
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        int minDist = -1;
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if (dist < minDist || minDist == -1) {
                    ancestor = i;
                    minDist = dist;
                }
            }
        }
        return ancestor;
    }
}
