package javacode.solution;

import java.util.ArrayDeque;
import java.util.Deque;

import org.junit.Test;

public class Solution695 {
    public int maxAreaOfIsland(int[][] grid) {
        if (grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int[] res = { 0 };
        int[][] M = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                bfs(grid, i, j, res, M);
            }
        }
        return res[0];
    }

    private void bfs(int[][] G, int i, int j, int[] res, int[][] M) {
        System.out.println(i + ",i,j," + j);
        if (G[i][j] == 1) {
            Deque<int[]> que = new ArrayDeque<>();
            que.add(new int[] { i, j });
            int cnt = 0;
            while (!que.isEmpty()) {
                int[] cur = que.removeFirst();
                if (G[cur[0]][cur[1]] == 1) {
                    System.out.println(cur[0] + ", " + cur[1]);
                    cnt++;
                    System.out.println("cnt, " + cnt);
                    G[cur[0]][cur[1]] = 0;
                    for (int[] move : M) {
                        int x = cur[0] + move[0];
                        int y = cur[1] + move[1];
                        if (0 <= x && x < G.length && 0 <= y && y < G[0].length && G[x][y] == 1) {
                            que.add(new int[] { x, y });
                        }
                    }
                }
            }
            res[0] = Math.max(res[0], cnt);
        }
    }

    @Test
    public void test() {
        System.out.println(maxAreaOfIsland(new int[][] { { 1, 1 }, { 1, 1 } }));
    }
}

class Solution695V2 {
    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    max = Math.max(max, AreaOfIsland(grid, i, j));
                }
            }
        }
        return max;
    }

    private int AreaOfIsland(int[][] grid, int i, int j) {
        if (i >= 0 && i < grid.length && j >= 0 && j < grid[0].length && grid[i][j] == 1) {
            grid[i][j] = 0;
            return 1 + AreaOfIsland(grid, i - 1, j) + AreaOfIsland(grid, i + 1, j) + AreaOfIsland(grid, i, j - 1)
                    + AreaOfIsland(grid, i, j + 1);
        } else {
            return 0;
        }

    }
}
