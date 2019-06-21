package javacode.code;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class LeetCode700 {
    {
        System.out.println("Snowy");
    }

    public boolean canTransform(String start, String end) {
        if (!start.replace("X", "").equals(end.replace("X", ""))) {
            return false;
        }

        int p1 = 0, p2 = 0;
        while (p1 < start.length() && p2 < end.length()) {
            while (p1 < start.length() && start.charAt(p1) == 'X') {
                p1++;
            }
            while (p2 < end.length() && end.charAt(p2) == 'X') {
                p2++;
            }

            // if both of the pointers reach the end the strings are transformable
            if (p1 == start.length() && p2 == end.length()) {
                return true;
            }
            // if only one of the pointer reach the end they are not transformable
            if (p1 == start.length() || p2 == end.length()) {
                return false;
            }

            if (start.charAt(p1) != end.charAt(p2)) {
                return false;
            }
            // if the character is 'L', it can only be moved to the left. p1 should be greater or equal to p2.
            if (start.charAt(p1) == 'L' && p1 < p2) {
                return false;
            }
            // if the character is 'R', it can only be moved to the right. p2 should be greater or equal to p1.
            if (start.charAt(p1) == 'R' && p1 > p2) {
                return false;
            }
            p1++;
            p2++;
        }
        return true;
    }

    int[][] steps = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

    public int swimInWater(int[][] grid) {
        int n = grid.length;
        int[][] max = new int[n][n];
        for (int[] line : max)
            Arrays.fill(line, Integer.MAX_VALUE);
        dfs(grid, max, 0, 0, grid[0][0]);
        return max[n - 1][n - 1];
    }

    private void dfs(int[][] grid, int[][] max, int x, int y, int cur) {
        int n = grid.length;
        if (x < 0 || x >= n || y < 0 || y >= n || Math.max(cur, grid[x][y]) >= max[x][y])
            return;
        max[x][y] = Math.max(cur, grid[x][y]);
        for (int[] s : steps) {
            dfs(grid, max, x + s[0], y + s[1], max[x][y]);
        }
    }

    String decode(String s) {
        int idx = s.length();
        char[] res = new char[idx];
        for (char c : s.toCharArray()) {
            idx--;
            char val = (char) ((c - idx) / 2);
            res[idx] = val;
        }
        return String.valueOf(res);
    }

    @Test
    public void test3() {
        char c = (char) 3;
        System.out.println( 0 + (c += c + 0));
    }

    @SuppressWarnings("unused")
    // @Test
    public void test() {

        int[][] grid = { { 0, 1, 2, 3, 4 }, { 24, 23, 22, 21, 5 }, { 12, 13, 14, 15, 16 }, { 11, 17, 18, 19, 20 },
                { 10, 9, 8, 7, 6 } };

        int k = swimInWater(grid);

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\journaldev.txt"))) {
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
