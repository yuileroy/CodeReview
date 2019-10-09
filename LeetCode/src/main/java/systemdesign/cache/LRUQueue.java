package systemdesign.cache;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

public class LRUQueue {
    Deque<Integer> dq;
    HashSet<Integer> set;
    int size;

    LRUQueue(int size) {
        this.size = size;
        dq = new ArrayDeque<>();
        set = new HashSet<>();
    }

    public void refer(int x) {
        if (!set.contains(x)) {
            if (dq.size() == size) {
                int last = dq.removeLast();
                set.remove(last);
            }
        } else {
            dq.remove(Integer.valueOf(x));
        }
        dq.add(x);
        set.add(x);
    }
}
