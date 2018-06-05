package javacode.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CoinPath {
    List<Integer> res = null;
    int min = Integer.MAX_VALUE;

    public List<Integer> cheapestJump(int[] A, int B) {

        int[] V = new int[A.length];
        V[0] = A[0];
        for (int i = 1; i < V.length; i++) {
            V[i] = Integer.MAX_VALUE;
        }

        List<Integer> pre = new ArrayList<>();
        pre.add(0);
        fn(A, B, pre, A[0], 0, V);

        if (res == null) {
            return new ArrayList<Integer>();
        }

        for (int i = 0; i < res.size(); i++) {
            res.set(i, res.get(i) + 1);
        }
        return res;
    }

    private void fn(int[] A, int B, List<Integer> pre, int preSum, int start, int[] V) {
        if (start == A.length - 1) {
            if (preSum < min) {
                res = pre;
                min = preSum;
            }
            return;
        }
        for (int i = 1; i <= B && start + i < A.length; i++) {
            int cur = start + i;
            if (A[cur] != -1 && preSum + A[cur] < V[cur]) {
                List<Integer> tmp = new ArrayList<>(pre);
                tmp.add(cur);
                V[cur] = preSum + A[cur];
                fn(A, B, tmp, V[cur], cur, V);
            }
        }
    }
}

class CoinPath2 {

    public List<Integer> cheapestJump(int[] A, int B) {
        int[] V = new int[A.length];
        V[0] = A[0];
        for (int i = 1; i < V.length; i++) {
            V[i] = Integer.MAX_VALUE;
        }
        fn(A, B, A[0], 0, V);

        List<Integer> res = new ArrayList<Integer>();
        if (V[A.length - 1] == Integer.MAX_VALUE) {
            return res;
        }
        int idx = A.length - 1;
        res.add(idx + 1);
        while (idx > 0) {
            int cur = idx - B;
            while (V[cur] != V[idx] - A[idx]) {
                cur++;
            }
            res.add(cur + 1);
            idx = cur;
        }
        Collections.reverse(res);
        return res;
    }

    private void fn(int[] A, int B, int preSum, int start, int[] V) {
        if (start == A.length - 1) {
            return;
        }
        for (int i = 1; i <= B && start + i < A.length; i++) {
            int cur = start + i;
            if (A[cur] != -1 && preSum + A[cur] < V[cur]) {
                V[cur] = preSum + A[cur];
                fn(A, B, V[cur], cur, V);
            }
        }
    }
}