package leetcodelock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import leetcode.TreeNode;

public class Solution311 {
    // 311. Sparse Matrix Multiplication
    // Given two sparse matrices A and B, return the result of AB.
    public int[][] multiply(int[][] A, int[][] B) {
        int x = A.length;
        int y = A[0].length;
        int z = B[0].length;
        int[][] answer = new int[x][z];

        List<List<Integer>> AList = new ArrayList<>();
        for (int j = 0; j < y; j++) {
            List<Integer> li = new ArrayList<>();
            for (int i = 0; i < x; i++) {
                if (A[i][j] != 0)
                    li.add(i);
            }
            AList.add(li);
        }

        List<List<Integer>> BList = new ArrayList<>();
        for (int j = 0; j < y; j++) {
            List<Integer> li = new ArrayList<>();
            for (int k = 0; k < z; k++) {
                if (B[j][k] != 0)
                    li.add(k);
            }
            BList.add(li);
        }

        for (int p = 0; p < y; p++) {
            List<Integer> list1 = AList.get(p);
            List<Integer> list2 = BList.get(p);
            for (int i : list1) {
                for (int j : list2) {
                    answer[i][j] += A[i][p] * B[p][j];
                }
            }
        }
        return answer;
    }

    public int[][] multiply2(int[][] A, int[][] B) {
        int m = A.length, n = A[0].length, nB = B[0].length;
        int[][] C = new int[m][nB];

        for (int i = 0; i < m; i++) {
            for (int k = 0; k < n; k++) {
                if (A[i][k] != 0) {
                    for (int j = 0; j < nB; j++) {
                        if (B[k][j] != 0) {
                            C[i][j] += A[i][k] * B[k][j];
                        }
                    }
                }
            }
        }
        return C;
    }
}

// 314. Binary Tree Vertical Order Traversal
// Given a binary tree, return the vertical order traversal of its nodes' values. (ie, from top to bottom, column by
// column).
class Solution314 {
    private int min = 0, max = 0;

    public List<List<Integer>> verticalOrder(TreeNode root) {
        List<List<Integer>> list = new ArrayList<>();
        if (root == null) {
            return list;
        }
        // find min and max
        traverse(root, 0);
        for (int i = min; i <= max; i++) {
            list.add(new ArrayList<>());
        }
        // bfs, level by level, root idx is -min
        Queue<TreeNode> q = new ArrayDeque<>();
        Queue<Integer> idx = new ArrayDeque<>();
        q.add(root);
        idx.add(-min);
        while (!q.isEmpty()) {
            TreeNode node = q.remove();
            int i = idx.remove();
            list.get(i).add(node.val);
            if (node.left != null) {
                q.add(node.left);
                idx.add(i - 1);
            }
            if (node.right != null) {
                q.add(node.right);
                idx.add(i + 1);
            }
        }
        return list;
    }

    private void traverse(TreeNode root, int idx) {
        if (root == null) {
            return;
        }
        min = Math.min(min, idx);
        max = Math.max(max, idx);
        traverse(root.left, idx - 1);
        traverse(root.right, idx + 1);
    }
}
