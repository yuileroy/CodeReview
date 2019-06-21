package leetcodelock;

// 307. Range Sum Query - Mutable
// Given an integer array nums, find the sum of the elements between indices i and j (i ¡Ü j), inclusive.
// The update(i, val) function modifies nums by updating the element at index i to val.
public class Solution307 {
    int[] tree;
    int n;

    public Solution307(int[] nums) {
        if (nums.length > 0) {
            n = nums.length;
            tree = new int[n * 2];
            buildTree(nums);
        }
    }

    // k -> (2k, 2k + 1) -> (even, odd)
    // [_, 10, 3, 7, 1, 2, 3, 4]
    // [_, 15, 10, 5, 9, 1, 2, 3, 4, 5]
    private void buildTree(int[] nums) {
        for (int i = n, j = 0; i < 2 * n; i++, j++)
            tree[i] = nums[j];
        for (int i = n - 1; i > 0; i--)
            tree[i] = tree[i * 2] + tree[i * 2 + 1];
    }

    public void update(int pos, int val) {
        pos += n;
        int diff = val - tree[pos];
        tree[pos] = val;
        while (pos > 0) {
            pos /= 2;
            tree[pos] += diff;
        }
    }

    public int sumRange(int l, int r) {
        l += n;
        r += n;
        int sum = 0;
        while (l <= r) {
            if ((l % 2) == 1) {
                sum += tree[l];
                l++;
            }
            if ((r % 2) == 0) {
                sum += tree[r];
                r--;
            }
            l /= 2;
            r /= 2;
        }
        return sum;
    }
}

// 308. Range Sum Query 2D - Mutable
class NumMatrix {
    private int[][] colSums;
    private int[][] matrix;

    public NumMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return;
        }
        this.matrix = matrix;
        int m = matrix.length;
        int n = matrix[0].length;
        colSums = new int[m + 1][n];
        for (int i = 1; i <= m; i++) {
            for (int j = 0; j < n; j++) {
                colSums[i][j] = colSums[i - 1][j] + matrix[i - 1][j];
            }
        }
    }

    public void update(int row, int col, int val) {
        int diff = val - matrix[row][col];
        for (int i = row + 1; i < colSums.length; i++) {
            colSums[i][col] += diff;
        }
        matrix[row][col] = val;
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        int res = 0;
        for (int j = col1; j <= col2; j++) {
            res += colSums[row2 + 1][j] - colSums[row1][j];
        }
        return res;
    }
}
