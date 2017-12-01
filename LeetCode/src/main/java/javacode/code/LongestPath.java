package javacode.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class LongestPath {
    // 1
    /// \
    // 2 3
    /// \
    // 4 5

    // 1
    ///
    // 2
    ///
    // 4

    int maxLen = 0;
    List<Integer> maxList = null;

    public void longPath(TreeNode root) {
        if (root == null) {
            return;
        }
        int leftLen = getLength(root.left);
        int rightLen = getLength(root.right);
        if (1 + leftLen + rightLen > maxLen) {
            maxLen = 1 + leftLen + rightLen;
            List<Integer> cur = new ArrayList<>();
            cur.addAll(getList(root.left));
            cur.add(root.val);
            cur.addAll(getList(root.right));
            maxList = cur;
        }
        longPath(root.left);
        longPath(root.right);
    }

    // maxLen, N nodes, 2N * (getLength() _> O(h)) O(N) 2,4... (2^h), O(N) =
    // O(2^h)

    private int getLength(TreeNode root) { // O(1) * N = O(N)
        if (root == null) {
            return 0;
        }
        if (map.containsKey(root)) {
            return map.get(root);
        }
        int val = 1 + Math.max(getLength(root.left), getLength(root.right));
        map.put(root, val);
        return val;
    }

    private List<Integer> getList(TreeNode root) { // O(1) * N = O(N)
        if (root == null) {
            return new ArrayList<>();
        }
        if (mapOfList.containsKey(root)) {
            return mapOfList.get(root);
        }
        int leftLen = getLength(root.left);
        int rightLen = getLength(root.right);

        List<Integer> res = new ArrayList<>();
        res.add(root.val);

        if (leftLen > rightLen) {
            res.addAll(getList(root.left));
        } else {
            res.addAll(getList(root.right));
        }

        mapOfList.put(root, res);
        return res;
    }

    Map<TreeNode, Integer> map = new HashMap<>();
    Map<TreeNode, List<Integer>> mapOfList = new HashMap<>();

    public void longPath2(TreeNode root) {
        if (root == null) {
            return;
        }
        getLength2(root);
    }

    int maxLen2 = 0;

    private int getLength2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int l = getLength2(root.left);
        int r = getLength2(root.right);
        int val = 1 + Math.max(l, r);
        maxLen2 = Math.max(maxLen2, 1 + l + r);
        return val;
    }

    public int widthOfBinaryTree(TreeNode root) {
        List<Integer> leftli = new ArrayList<Integer>();
        int[] res = new int[1];
        dfs(root, 1, 0, leftli, res);
        return res[0];
    }

    private void dfs(TreeNode node, int idx, int depth, List<Integer> leftli, int[] res) {
        if (node == null)
            return;
        if (depth == leftli.size()) {
            leftli.add(idx);
        }
        res[0] = Integer.max(res[0], idx + 1 - leftli.get(depth));
        dfs(node.left, idx * 2, depth + 1, leftli, res);
        dfs(node.right, idx * 2 + 1, depth + 1, leftli, res);
    }

    public boolean isPossible(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>(), backMap = new HashMap<>();
        for (int i : nums) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }
        for (int i : nums) {
            if (map.get(i) == 0) {
                continue;
            }
            if (backMap.getOrDefault(i, 0) > 0) {
                backMap.put(i, backMap.get(i) - 1);
                backMap.put(i + 1, backMap.getOrDefault(i + 1, 0) + 1);
            } else if (map.getOrDefault(i + 1, 0) > 0 && map.getOrDefault(i + 2, 0) > 0) {
                map.put(i + 1, map.get(i + 1) - 1);
                map.put(i + 2, map.get(i + 2) - 1);
                backMap.put(i + 3, backMap.getOrDefault(i + 3, 0) + 1);
            } else {
                return false;
            }
            map.put(i, map.get(i) - 1);
        }
        return true;
    }

    public int flipLights(int n, int m) {
        if (m == 0)
            return 1;
        if (n == 1)
            return 2;
        if (n == 2 && m == 1)
            return 3;
        if (n == 2)
            return 4;
        if (m == 1)
            return 4;
        if (m == 2)
            return 7;
        if (m >= 3)
            return 8;
        return 8;
    }

    public List<String> findStrobogrammatic(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        if (n == 1) {
            return new ArrayList<>(Arrays.asList("0", "1", "8"));
        }
        List<String> res = new ArrayList<>();
        char[] arr = new char[n];
        char[][] pairs = { { '0', '0' }, { '1', '1' }, { '8', '8' }, { '6', '9' }, { '9', '6' } };
        dfs(arr, 0, n - 1, res, pairs);
        return res;
    }

    private void dfs(char[] arr, int s, int e, List<String> res, char[][] pairs) {
        if (s > e) {
            res.add(String.valueOf(arr));
            return;
        }

        for (int i = (s == 0 ? 1 : 0); i < (s == e ? 3 : 5); i++) {
            arr[s] = pairs[i][0];
            arr[e] = pairs[i][1];
            dfs(arr, s + 1, e - 1, res, pairs);
            // no need to set value back
        }
    }

    @Test
    public void test() {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        TreeNode t4 = new TreeNode(4);
        TreeNode t5 = new TreeNode(5);
        t1.left = t2;
        t1.right = t3;
        t3.left = t4;
        t4.right = t5;
        longPath(t1);
        System.out.println(maxLen);
        System.out.println(maxList);
        longPath2(t1);
        System.out.println(maxLen2);
    }
}
