import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BurrowsWheeler {
    public static void encode() {
        String s = BinaryStdIn.readString();
        int len = s.length();
        CircularSuffixArray arr = new CircularSuffixArray(s);
        int[] indices = new int[arr.length()];
        for (int i = 0; i < s.length(); i++) {
            indices[i] = arr.index(i);
            if (arr.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt((indices[i] + len - 1) % len));
        }
        BinaryStdOut.flush();
    }

    public static void decode() {
        int first = BinaryStdIn.readInt();
        int counter = 0;
        int len = 0;
        Map<Character, List<Integer>> arrays = new HashMap<>();
        while (!BinaryStdIn.isEmpty()) {
            len++;
            char c = BinaryStdIn.readChar();
            if (!arrays.containsKey(c)) {
                arrays.put(c, new ArrayList<>());
            }
            arrays.get(c).add(counter);
            counter++;
        }
        int[] first_positions = new int[arrays.size()];
        int[] num_occurrences = new int[arrays.size()];
        int[] cur_positions = new int[arrays.size()];
        Character[] chars = arrays.keySet().toArray(new Character[arrays.size()]);
        Arrays.sort(chars);
        for (int i = 0; i < chars.length; i++) {
            num_occurrences[i] = arrays.get(chars[i]).size();
            if (i > 0) {
                first_positions[i] = first_positions[i - 1] + num_occurrences[i - 1];
            }
        }
        int d = first;
        for (int i = 0; i < len; i++) {
            int j = 0;
            for (j = 0; j < chars.length; j++) {
                if (d >= first_positions[j] && d < first_positions[j] + num_occurrences[j]) {
                    break;
                }
            }
            BinaryStdOut.write(chars[j]);
            d = arrays.get(chars[j]).get(d - first_positions[j]);
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
