import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private Integer[] arr;

    public CircularSuffixArray(String s) {
        int len = s.length();
        arr = new Integer[len];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i;
        }
        Arrays.sort(arr, new Comparator<Integer>() {
            public int compare(Integer n1, Integer n2) {
                int shift = 0;
                int res = 0;
                while (shift < len) {
                    char c1 = s.charAt((n1 + shift) % len);
                    char c2 = s.charAt((n2 + shift) % len);
                    if (c1 < c2) {
                        res = -1;
                        break;
                    } else if (c1 > c2) {
                        res =1;
                        break;
                    }
                    shift++;
                }
                return res;
            }
        });
    }

    public int length() {
        return arr.length;
    }

    public int index(int i) {
        return arr[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray arr = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < arr.length(); i++) {
            System.out.println(arr.index(i));
        }
    }
}
