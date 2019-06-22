package leetcodelock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution302 {

    // 302. Smallest Rectangle Enclosing Black Pixels
    public int minArea(char[][] image, int x, int y) {
        int top = x, bottom = x;
        int left = y, right = y;
        for (x = 0; x < image.length; ++x) {
            for (y = 0; y < image[0].length; ++y) {
                if (image[x][y] == '1') {
                    top = Math.min(top, x);
                    bottom = Math.max(bottom, x + 1);
                    left = Math.min(left, y);
                    right = Math.max(right, y + 1);
                }
            }
        }
        return (right - left) * (bottom - top);
    }

    class Solution {
        public int minArea(char[][] image, int x, int y) {
            int m = image.length, n = image[0].length;
            int left = searchColumns(image, 0, y, 0, m, true);
            int right = searchColumns(image, y + 1, n, 0, m, false);
            int top = searchRows(image, 0, x, left, right, true);
            int bottom = searchRows(image, x + 1, m, left, right, false);
            return (right - left) * (bottom - top);
        }

        private int searchColumns(char[][] image, int i, int j, int top, int bottom, boolean whiteToBlack) {
            while (i != j) {
                int k = top, mid = (i + j) / 2;
                while (k < bottom && image[k][mid] == '0')
                    ++k;
                if (k < bottom == whiteToBlack) // k < bottom means the column mid has black pixel
                    j = mid; // search the boundary in the smaller half
                else
                    i = mid + 1; // search the boundary in the greater half
            }
            return i;
        }

        private int searchRows(char[][] image, int i, int j, int left, int right, boolean whiteToBlack) {
            while (i != j) {
                int k = left, mid = (i + j) / 2;
                while (k < right && image[mid][k] == '0')
                    ++k;
                if (k < right == whiteToBlack) // k < right means the row mid has black pixel
                    j = mid;
                else
                    i = mid + 1;
            }
            return i;
        }
    }

    // 305. Number of Islands II
    // count the number of islands after each addLand operation
    public List<Integer> numIslands2(int m, int n, int[][] positions) {

        int[][] B = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int[] U = new int[m * n];
        // for (int i = 0; i < U.length; i++) U[i] = i;
        // set U[i] = i to indicate it's an island, -1 as water
        Arrays.fill(U, -1);
        List<Integer> res = new ArrayList<>();
        int cnt = 0;

        for (int[] e : positions) {
            int idx = e[0] * n + e[1];
            if (U[idx] != -1) {
                res.add(cnt);
                continue;
            }
            U[idx] = idx;
            cnt++;
            for (int[] b : B) {
                int nx = e[0] + b[0];
                int ny = e[1] + b[1];
                int idx2 = nx * n + ny;
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || U[idx2] == -1) {
                    continue;
                }
                // union
                int v1 = find(U, idx);
                int v2 = find(U, idx2);
                U[v1] = v2;
                if (v1 != v2)
                    cnt--;
            }
            res.add(cnt);
        }
        return res;
    }

    private int find(int[] U, int i) {
        if (U[i] == i) {
            return i;
        }
        U[i] = find(U, U[i]);
        return U[i];
    }
}
