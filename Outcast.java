public class Outcast {
    private WordNet net;

    public Outcast(WordNet wordnet) {
        this.net = wordnet;
    }

    public String outcast(String[] nouns) {
        int[] dists = new int[nouns.length];
        for (int i = 0; i < nouns.length - 1; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int dist = net.distance(nouns[i], nouns[j]);
                dists[i] += dist;
                dists[j] += dist;
            }
        }
        int outcast = 0;
        int maxDist = dists[0];
        for (int i = 1; i < nouns.length; i++) {
            if (dists[i] > maxDist) {
                outcast = i;
                maxDist = dists[i];
            }
        }
        return nouns[outcast];
    }

    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);
        Outcast out = new Outcast(net);
        String[] words = {"probability", "statistics", "mathematics", "physics"};
        System.out.println(out.outcast(words));
    }
}
