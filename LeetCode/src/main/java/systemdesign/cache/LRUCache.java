package systemdesign.cache;

import java.util.HashMap;

public class LRUCache {
    private HashMap<Integer, Node> map = new HashMap<Integer, Node>();
    private Node head;
    private Node end;
    private int capacity;
    private int len;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.len = 0;
    }

    public int get(int key) {
        if (map.containsKey(key)) {
            Node cur = map.get(key);
            removeNode(cur);
            setHead(cur);
            return cur.val;
        } else {
            return -1;
        }
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node cur = map.get(key);
            cur.val = value;
            removeNode(cur);
            setHead(cur);
            return;
        }
        // add new key
        Node newNode = new Node(key, value);
        if (len < capacity) {
            len++;
        } else {
            map.remove(end.key);
            end = end.pre;
            if (end != null) {
                end.next = null;
            }
        }
        setHead(newNode);
        map.put(key, newNode);
    }

    private void removeNode(Node node) {
        Node cur = node;
        Node pre = cur.pre;
        Node post = cur.next;

        if (pre != null) {
            pre.next = post;
        } else {
            head = post;
        }

        if (post != null) {
            post.pre = pre;
        } else {
            end = pre;
        }
    }

    private void setHead(Node node) {
        node.next = head;
        node.pre = null;
        if (head != null) {
            head.pre = node;
        }

        head = node;
        if (end == null) {
            end = node;
        }
    }

    private class Node {
        public int val;
        public int key;
        public Node pre;
        public Node next;

        public Node(int key, int value) {
            this.val = value;
            this.key = key;
        }
    }
}
