package keycode;

import java.util.LinkedList;
import java.util.Stack;

import org.junit.Test;

public class CompileCode {

    public int canCompleteCircuit(int[] gas, int[] cost) {
        Stack<Integer> s = new Stack<>();
        s.pop();
        int gasTotal = 0;
        int costTotal = 0;
        for (int i = 0; i < gas.length; i++) {
            gasTotal += gas[i];
            costTotal += cost[i];
        }
        if (gasTotal < costTotal) {
            return -1;
        }
        int res = -1;
        gasTotal = 0;
        costTotal = 0;
        for (int i = 0; i < gas.length; i++) {
            gasTotal += gas[i];
            costTotal += cost[i];
            if (gasTotal >= costTotal) {
                if (res == -1) {
                    res = i;
                }
            } else {
                res = -1;
                gasTotal = 0;
                costTotal = 0;
            }
        }
        return res;
    }

    class PStack {
        private LinkedList<Integer> list = new LinkedList<>();
        private int size = 0;

        PStack() {

        }

        PStack(LinkedList<Integer> list, int size) {
            this.list = list;
            this.size = size;
        }

        public int size() {
            return size;
        }

        public PStack push(int x) {
            list.add(x);
            return new PStack(list, size + 1);
        }

        public PStack pop() {
            peek();
            return new PStack(list, size - 1);
        }

        public PStack peek() {
            if (size == 0) {
                throw new RuntimeException("empty stack");
            }
            return this;
        }

        public String toString() {
            if (size == 0) {
                return "[]";
            }
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < size; i++) {
                sb.append(list.get(i)).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("]");
            return sb.toString();
        }
    }

    @Test
    public void test() {
        PStack P0 = new PStack();
        PStack P1 = P0.push(1);
        PStack P2 = P1.push(2);
        PStack P3 = P2.pop();
        System.out.println(P1);
        System.out.println(P2);
        System.out.println(P3);
        String[] ss = "done d  v".split("[ ]+");
        for (String s : ss) {
            System.out.println(s);
        }
        System.out.println("done");
    }
}