import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node head;
    private Node tail;

    public Deque() {
        head = new Node(null);
        tail = new Node(null);
        head.next = tail;
        tail.prev = head;
    }

    public boolean isEmpty() {
        return head.next == tail;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node node = new Node(item);
        node.next = head.next;
        node.prev = head.next.prev;
        head.next.prev = node;
        head.next = node;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node node = new Node(item);
        node.next = tail.prev.next;
        node.prev = tail.prev;
        tail.prev.next = node;
        tail.prev = node;
        size++;
    }

    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Node node = head.next;
        node.next.prev = head;
        head.next = node.next;
        size--;
        return node.item;
    }

    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Node node = tail.prev;
        node.prev.next = tail;
        tail.prev = node.prev;
        size--;
        return node.item;
    }

    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node node = head.next;

            public boolean hasNext() {
                return node != tail;
            }

            public Item next() {
                if (hasNext()) {
                    Item item = node.item;
                    node = node.next;
                    return item;
                } else {
                    throw new NoSuchElementException();
                }
            }
        };
    }

    public static void main(String[] args) {

    }

    private class Node {
        private Node next;
        private Node prev;
        private Item item;

        public Node(Item item) {
            this.item = item;
        }
    }
}
