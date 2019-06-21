package javacode.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class LinkedinCode {

    public boolean isNumber(String toTest) {
        // implementation here
        // check negtive -,
        // 1.2. -> false
        // 12. -> true
        // 00.12 -> true
        // -3-4 -> false
        if (toTest == null || toTest.length() == 0) {
            return false;
        }
        boolean dot = false;
        for (int i = 0; i < toTest.length(); i++) {
            char ch = toTest.charAt(i);
            if (i > 0 && ch == '-') {
                return false;
            }
            if (i == 0 && ch == '.') {
                return false;
            }
            // .6 invliad, 0.7, -8
            if (!Character.isDigit(ch) && ch != '.' && ch != '-') {
                return false;
            }
            // more dot
            if (ch == '.' && dot) {
                return false;
            }
            if (ch == '.') {
                dot = true;
            }
        }
        return true;
    }

    // public int maxSum(List<Integer> input)
    // (1, 2, -4, 1, 3, -2, 3, -1) -> 5

    // cur: 1, 3, -1, 1, 4, 2, 5, 4
    // res: 1, 3, 3, 3, 4, 4, 5, 5

    public int maxSum(List<Integer> input) {
        int res = Integer.MIN_VALUE, cur = 0;
        for (int i : input) {
            cur += i;
            res = Math.max(res, cur);
            if (cur < 0) {
                cur = 0;
            }
        }

        return res;
    }

    // 2, -4, 3, -2
    // cur: 2, -8, -24, 48
    // pos: 2, 2, 2, 48
    // neg: 0, -8, -24, -24

    // 2, -4, -3, 2
    // res: 2,
    // pos: 2, -4, 24, 48
    // neg: 2, -8, -3, -6
    public int maxSum2(List<Integer> input) {
        int res = input.get(0);
        int maxPos = input.get(0), maxNeg = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            int v = input.get(i);
            int tmp = maxPos;
            // 12, 24
            maxPos = Math.max(Math.max(tmp * v, maxNeg * v), v);
            maxNeg = Math.min(Math.min(tmp * v, maxNeg * v), v);
            res = Math.max(res, maxPos);
        }

        return res;
    }

    public int findCircleNum(int[][] M) {
        int count = 0;
        for (int i = 0; i < M.length; i++) {
            count += sink(M, i);
        }
        return count;
    }

    private int sink(int[][] M, int i) {
        if (M[i][i] == 0) {
            return 0;
        }
        M[i][i] = 0;
        for (int j = i + 1; j < M.length; j++) {
            if (M[i][j] != 0) {
                M[i][j] = 0;
                M[j][i] = 0;
                sink(M, j);
            }
        }
        return 1;
    }

    class Solution730 {
        public int countPalindromicSubsequences(String S) {
            int n = S.length();
            int mod = 1000000007;
            int[][][] dp = new int[4][n][n];

            for (int i = n - 1; i >= 0; i--) {
                for (int j = i; j < n; j++) {
                    for (int k = 0; k < 4; k++) {
                        char ch = (char) ('a' + k);
                        if (j == i) {
                            if (S.charAt(i) == ch)
                                dp[k][i][j] = 1;
                            else
                                dp[k][i][j] = 0;
                        } else { // j > i
                            if (S.charAt(i) != ch)
                                dp[k][i][j] = dp[k][i + 1][j];
                            else if (S.charAt(j) != ch)
                                dp[k][i][j] = dp[k][i][j - 1];
                            else { // S[i] == S[j] == c
                                if (j == i + 1)
                                    dp[k][i][j] = 2; // "aa" : {"a", "aa"}
                                else { // length is > 2
                                    dp[k][i][j] = 2;
                                    for (int m = 0; m < 4; m++) { // count each one within subwindows [i+1][j-1]
                                        dp[k][i][j] += dp[m][i + 1][j - 1];
                                        dp[k][i][j] %= mod;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            int ans = 0;
            for (int k = 0; k < 4; ++k) {
                ans += dp[k][0][n - 1];
                ans %= mod;
            }

            return ans;
        }

        class RangeModule {
            TreeSet<Interval> ranges;

            public RangeModule() {
                ranges = new TreeSet<>();
            }

            public void addRange(int left, int right) {
                Iterator<Interval> itr = ranges.tailSet(new Interval(0, left - 1)).iterator();
                while (itr.hasNext()) {
                    Interval item = itr.next();
                    if (right < item.left) {
                        break;
                    }
                    left = Math.min(left, item.left);
                    right = Math.max(right, item.right);
                    itr.remove();
                }
                ranges.add(new Interval(left, right));
            }

            public boolean queryRange(int left, int right) {
                Interval item = ranges.higher(new Interval(0, left));
                return (item != null && item.left <= left && right <= item.right);
            }

            public void removeRange(int left, int right) {
                Iterator<Interval> itr = ranges.tailSet(new Interval(0, left)).iterator();
                ArrayList<Interval> todo = new ArrayList<>();
                while (itr.hasNext()) {
                    Interval item = itr.next();
                    if (right < item.left)
                        break;
                    if (item.left < left)
                        todo.add(new Interval(item.left, left));
                    if (right < item.right)
                        todo.add(new Interval(right, item.right));
                    itr.remove();
                }
                for (Interval item : todo) {
                    ranges.add(item);
                }
            }
        }

        class Interval implements Comparable<Interval> {
            int left;
            int right;

            public Interval(int left, int right) {
                this.left = left;
                this.right = right;
            }

            public int compareTo(Interval e) {
                return this.right == e.right ? this.left - e.left : this.right - e.right;
            }
        }
    }

    class Solution {
        int[][] grid;
        Set<Integer> shape;

        public void explore(int r, int c, int r0, int c0) {
            if (0 <= r && r < grid.length && 0 <= c && c < grid[0].length && grid[r][c] == 1) {
                grid[r][c] = 2;
                shape.add((r - r0) * 2 * grid[0].length + (c - c0));
                explore(r + 1, c, r0, c0);
                explore(r - 1, c, r0, c0);
                explore(r, c + 1, r0, c0);
                explore(r, c - 1, r0, c0);
            }
        }

        public int numDistinctIslands(int[][] grid) {
            this.grid = grid;
            Set<Set<Integer>> shapes = new HashSet<>();

            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[0].length; c++) {
                    shape = new HashSet<>();
                    explore(r, c, r, c);
                    if (!shape.isEmpty()) {
                        shapes.add(shape);
                    }
                }
            }

            return shapes.size();
        }

        public boolean rotateString(String A, String B) {
            return A.length() == B.length() && (A + A).contains(B);
        }
    }

    class Codec {
        String NN = "X";
        String spliter = ",";

        // Encodes a tree to a single string.
        public String serialize(Node root) {
            StringBuilder sb = new StringBuilder();
            buildString(root, sb);
            return sb.toString();
        }

        private void buildString(Node node, StringBuilder sb) {
            if (node == null) {
                sb.append(NN);
                sb.append(spliter);
            } else {
                sb.append(node.val);
                sb.append(spliter);
                sb.append(node.children.size());
                sb.append(spliter);
                for (Node child : node.children) {
                    buildString(child, sb);
                }
            }
        }

        // Decodes your encoded data to tree.
        public Node deserialize(String data) {
            Deque<String> deque = new ArrayDeque<>(Arrays.asList(data.split(spliter)));
            return buildTree(deque);
        }

        private Node buildTree(Deque<String> deque) {
            String s1 = deque.removeFirst();
            if (s1.equals(NN))
                return null;

            int rootVal = Integer.valueOf(s1);
            int childrenNumber = Integer.valueOf(deque.removeFirst());

            Node root = new Node(rootVal, new ArrayList<>());
            for (int i = 0; i < childrenNumber; i++) {
                root.children.add(buildTree(deque));
            }
            return root;
        }

        class Node {
            public int val;
            public List<Node> children;

            public Node() {
            }

            public Node(int _val, List<Node> _children) {
                val = _val;
                children = _children;
            }
        }
    }

    public int profitableSchemes(int G, int P, int[] group, int[] profit) {
        int mod = (int) 1e9 + 7;
        int[][] dp = new int[G + 1][P + 1];
        dp[0][0] = 1;
        for (int k = 1; k <= group.length; k++) {
            int g = group[k - 1];
            int p = profit[k - 1];
            for (int i = G; i >= g; i--) {
                for (int j = P; j >= 0; j--) {
                    dp[i][j] = (dp[i][j] + dp[i - g][Math.max(0, j - p)]) % mod;
                }
            }
        }
        int sum = 0;
        for (int i = 0; i <= G; i++) {
            sum = (sum + dp[i][P]) % mod;
        }
        return sum;
    }

}
