import edu.princeton.cs.algs4.StdIn;

public class Subset {
    public static void main(String[] args) {
        int k = StdIn.readInt();
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (StdIn.hasNextChar()) {
            if (queue.size() == k) {
                queue.dequeue();
            }
            queue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < k; i++) {
            System.out.println(queue.dequeue());
        }
    }
}
