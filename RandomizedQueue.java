import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int start;
    private int size;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        start = 0;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size == items.length) {
            Item[] _items = (Item[]) new Object[size * 2];
            for (int i = 0; i < size; i++) {
                int idx = (start + i) % items.length;
                _items[i] = items[idx];
            }
            items = _items;
        }
        items[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int idx = (int) (size * StdRandom.uniform());
        Item item = items[(start + idx) % items.length];
        if (size > 1 && idx < size - 1) {
            items[(start + idx) % items.length] = items[(start + size - 1) % items.length];
        }
        size--;
        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        int idx = (int) (size * StdRandom.uniform());
        return items[(start + idx) % items.length];
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private int idx = 0;
            private Item[] items1 = construct();

            public boolean hasNext() { return idx < items1.length; }

            public Item next() {
                if (hasNext())
                    return items1[idx++];
                throw new NoSuchElementException();
            }

            private Item[] construct() {
                Item[] helper = (Item[]) new Object[size];
                for (int i = 0; i < size; i++) {
                    int idx = (int) ((i + 1) * StdRandom.uniform());
                    helper[i] = helper[idx];
                    helper[idx] = items[(start + i) % items.length];
                }
                return helper;
            }
        };
    }

    public static void main(String[] args) {

    }
}
