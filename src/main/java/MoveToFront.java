import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class MoveToFront {
    private static final int TOTAL_CHARS = 256;

    public static void encode() {
        List<Character> seq = new LinkedList<>();
        for (int i = 0; i < TOTAL_CHARS; i++) {
            seq.add((char) i);
        }
        for (int i = 0; !BinaryStdIn.isEmpty(); i++) {
            int counter = 0;
            char c = BinaryStdIn.readChar();
            Iterator<Character> iter = seq.iterator();
            while (iter.hasNext()) {
                if (iter.next().equals(c)) {
                    BinaryStdOut.write((char) (0xff & counter));
                    iter.remove();
                    seq.add(0, c);
                    break;
                }
                counter++;
            }
        }
        BinaryStdOut.flush();
    }

    public static void decode() {
        List<Character> seq = new LinkedList<>();
        for (int i = 0; i < TOTAL_CHARS; i++) {
            seq.add((char) i);
        }
        for (int i = 0; !BinaryStdIn.isEmpty(); i++) {
            int counter = 0;
            int d = (int) BinaryStdIn.readChar();
            Iterator<Character> iter = seq.iterator();
            while (iter.hasNext()) {
                char c = iter.next();
                if (d == counter) {
                    BinaryStdOut.write(c);
                    iter.remove();
                    seq.add(0, c);
                    break;
                }
                counter++;
            }
        }
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        String mode = args[0];
        if (mode.equals("-")) {
            encode();
        } else if (mode.equals("+")) {
            decode();
        }
    }
}
