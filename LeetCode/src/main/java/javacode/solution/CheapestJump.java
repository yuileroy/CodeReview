package javacode.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CheapestJump {
    private boolean[][] connect;

    public List<Integer> cheapestJump(int[] A, int B) {
        int[] cost = new int[A.length];
        cost[0] = A[0];
        for (int i = 1; i < A.length; i++) {
            cost[i] = Integer.MAX_VALUE;
            if (A[i] != -1) {
                for (int j = Math.max(0, i - B); j < i; j++) {
                    if(cost[j] != Integer.MAX_VALUE) {
                        cost[i] = Math.min(cost[i], cost[j] + A[i]);
                    }
                }
            }
        }
        if (cost[A.length - 1] == Integer.MAX_VALUE) {
            return Collections.emptyList();
        }
        connect = new boolean[A.length][A.length];
        boolean[] done = new boolean[A.length];
        dfs(A, B, cost, A.length - 1, done);
        List<Integer> answer = new ArrayList<>();
        int cur = 0;
        answer.add(cur + 1);
        while (cur < A.length - 1) {
            for (int next = cur + 1; next <= cur + B && next < A.length; next++) {
                if (connect[cur][next]) {
                    cur = next;
                    answer.add(cur + 1);
                    break;
                }
            }
        }
        return answer;
    }

    private void dfs(int[] A, int B, int[] cost, int i, boolean[] done) {
        if (i == 0) {
            return;
        }
        if (done[i]) {
            return;
        }
        if (A[i] != -1) {
            for (int j = Math.max(0, i - B); j < i; j++) {
                if (cost[i] == cost[j] + A[i]) {
                    connect[j][i] = true;
                    dfs(A, B, cost, j, done);
                }
            }
        }
        done[i] = true;
    }
}
