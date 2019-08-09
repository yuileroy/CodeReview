package leetcodelock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leetcode.TreeNode;

public class Solution320 {
    // 320. Generalized Abbreviation
    // Input: "word", Output:
    // ["word", "1ord", "w1rd", "wo1d", "wor1", "2rd", "w2d",
    // "wo2", "1o1d", "1or1", "w1r1", "1o2", "2r1", "3d", "w3", "4"]
    List<String> res = new ArrayList<>();

    public List<String> generateAbbreviations(String word) {
        dfs(0, new StringBuilder(), word);
        // dfs(0, "", word);
        return res;
    }

    void dfs(int start, StringBuilder sb, String word) {
        if (start >= word.length()) {
            res.add(sb.toString());
            return;
        }

        for (int i = start; i < word.length(); i++) {
            int len = sb.length();
            sb.append(i - start + 1);
            if (i < word.length() - 1) {
                sb.append(word.charAt(i + 1));
            }
            // 1o + dfs, 2r + dfs, 3d + dfs, 4
            dfs(i + 2, sb, word);

            if (i == start) {
                sb.setLength(len);
                // w + dfs
                dfs(i + 1, sb.append(word.charAt(i)), word);
            }
            sb.setLength(len);
        }
    }

    void dfs(int start, String item, String word) {
        if (start >= word.length()) {
            res.add(item);
            return;
        }
        for (int i = start; i < word.length(); i++) {
            String item1 = item + (i - start + 1);
            if (i < word.length() - 1) {
                item1 += word.charAt(i + 1);
            }
            dfs(i + 2, item1, word);

            if (i == start) {
                dfs(i + 1, item + word.charAt(i), word);
            }
        }
    }

    // 323. Number of Connected Components in an Undirected Graph
    // write a function to find the number of connected components in an undirected graph.
    public int countComponents(int n, int[][] edges) {
        int[] roots = new int[n];
        for (int i = 0; i < n; i++) {
            roots[i] = i;
        }

        for (int[] e : edges) {
            int root1 = find(roots, e[0]);
            int root2 = find(roots, e[1]);
            if (root1 != root2) {
                roots[root2] = root1; // union
                n--;
            }
        }
        return n;
    }

    public int find(int[] roots, int id) {
        if (roots[id] == id) {
            return id;
        }
        roots[id] = find(roots, roots[id]);
        return roots[id];
    }

    public int find2(int[] roots, int id) {
        while (roots[id] != id) {
            roots[id] = roots[roots[id]]; // optional: path compression
            id = roots[id];
        }
        return id;
    }

    // 325. Maximum Size Subarray Sum Equals k
    public int maxSubArrayLen(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0, res = 0;
        map.put(0, -1); // sum, idx
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (map.containsKey(sum - k)) {
                res = Math.max(res, i - map.get(sum - k));
            }
            if (!map.containsKey(sum)) {
                map.put(sum, i);
            }
        }
        return res;
    }
}

// 333. Largest BST Subtree
// Given a binary tree, find the largest subtree which is a Binary Search Tree (BST)
class Solution333 {
    class Result {
        int size, lower, upper;

        Result(int size, int lower, int upper) {
            this.size = size;
            this.lower = lower;
            this.upper = upper;
        }
    }

    int max = 0;

    public int largestBSTSubtree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        traverse(root);
        return max;
    }

    private Result traverse(TreeNode root) {
        if (root == null) {
            return new Result(0, Integer.MAX_VALUE, Integer.MIN_VALUE);
        }
        Result left = traverse(root.left);
        Result right = traverse(root.right);
        if (left.size == -1 || right.size == -1 || root.val <= left.upper || root.val >= right.lower) {
            return new Result(-1, 0, 0);
        }
        int size = left.size + 1 + right.size;
        max = Math.max(size, max);
        // not just left.lower, cause left maybe null~Integer.MAX_VALUE
        return new Result(size, Math.min(left.lower, root.val), Math.max(right.upper, root.val));
    }

    interface NestedInteger {
        boolean isInteger();

        int getInteger();

        List<NestedInteger> getList();
    }

    // 339. Nested List Weight Sum
    class Solution339 {

        public int depthSum(List<NestedInteger> nestedList) {
            return depthSum(nestedList, 1);
        }

        int depthSum(List<NestedInteger> nestedList, int lv) {
            int res = 0;
            for (NestedInteger item : nestedList) {
                if (item.isInteger()) {
                    res += lv * item.getInteger();
                } else {
                    res += depthSum(item.getList(), lv + 1);
                }
            }
            return res;
        }
    }
}
