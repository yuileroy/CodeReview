package leetcodelock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leetcode.TreeNode;

public class Solution293 {
    // 293. Flip Game
    // Write a function to compute all possible states of the string after one valid move.
    public List<String> generatePossibleNextMoves(String s) {
        List<String> res = new ArrayList<>();
        if (s == null || s.length() < 2) {
            return res;
        }
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '+' && s.charAt(i + 1) == '+') {
                res.add(s.substring(0, i) + "--" + s.substring(i + 2, s.length()));
            }
        }
        return res;
    }

    // 294. Flip Game II
    // Write a function to determine if the starting player can guarantee a win.

    Map<String, Boolean> map = new HashMap<>();

    public boolean canWin(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '+' && s.charAt(i + 1) == '+') {
                String flip = s.substring(0, i) + "--" + s.substring(i + 2);
                if ((map.containsKey(flip) && !map.get(flip)) || (!map.containsKey(flip) && !canWin(flip))) {
                    map.put(s, true);
                    return true;
                }
            }
        }
        map.put(s, false);
        return false;
    }

    // V2
    public boolean canWin2(String s) {
        char[] ch = s.toCharArray();
        return canWin(ch);
    }

    boolean canWin(char[] ch) {
        for (int i = 0; i < ch.length - 1; i++) {
            if (ch[i] == '+' && ch[i + 1] == '+') {
                ch[i] = '-';
                ch[i + 1] = '-';
                if (!canWin(ch)) {
                    return true;
                }
                ch[i] = '+';
                ch[i + 1] = '+';
            }
        }
        return false;
    }

    // 296. Best Meeting Point
    public int minTotalDistance(int[][] grid) {
        List<Integer> rows = new ArrayList<>();
        List<Integer> cols = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == 1) {
                    rows.add(row);
                    cols.add(col);
                }
            }
        }
        int row = rows.get(rows.size() / 2);
        Collections.sort(cols);
        int col = cols.get(cols.size() / 2);
        return minDistance1D(rows, row) + minDistance1D(cols, col);
    }

    private int minDistance1D(List<Integer> points, int origin) {
        int distance = 0;
        for (int point : points) {
            distance += Math.abs(point - origin);
        }
        return distance;
    }

    // 298. Binary Tree Longest Consecutive Sequence
    // Given a binary tree, find the length of the longest consecutive sequence path.
    // 2->3->4
    class Solution298 {
        int res = 0;

        public int longestConsecutive(TreeNode root) {
            if (root == null) {
                return 0;
            }
            res = 1;
            fn(root, root.val, 1);
            return res;
        }

        private void fn(TreeNode root, int pre, int len) {
            if (root == null) {
                return;
            }

            if (root.val != pre + 1) {
                len = 1;
            } else {
                len++;
                res = Math.max(res, len);
            }

            fn(root.left, root.val, len);
            fn(root.right, root.val, len);
        }
    }
}
