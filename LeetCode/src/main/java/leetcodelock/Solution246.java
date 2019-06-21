package leetcodelock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leetcode.TreeNode;

public class Solution246 {
    // 246. Strobogrammatic Number
    public boolean isStrobogrammatic(String num) {
        int i = -1, j = num.length();
        while (++i <= --j) {
            if (num.charAt(i) == num.charAt(j)
                    && (num.charAt(i) == '1' || num.charAt(i) == '8' || num.charAt(i) == '0')) {

            } else if (num.charAt(i) == '6' && num.charAt(j) == '9' || num.charAt(i) == '9' && num.charAt(j) == '6') {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }
}

// 247. Strobogrammatic Number II
// A strobogrammatic number is a number that looks the same when rotated 180 degrees (looked at upside down).
// Find all strobogrammatic numbers that are of length = n.
class Solution247 {
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

}

// 249. Group Shifted Strings
// Given a string, we can "shift" each of its letter to its successive letter, for example: "abc" -> "bcd". We can
// keep "shifting" which forms the sequence:
class Solution249 {

    public List<List<String>> groupStrings(String[] strings) {
        Map<List<Integer>, List<String>> map = new HashMap<>();
        for (String s : strings) {
            List<Integer> key = convert(s);
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(convert(s)).add(s);
        }
        return new ArrayList<List<String>>(map.values());
    }

    private List<Integer> convert(String s) {
        List<Integer> li = new ArrayList<>(s.length());
        for (char c : s.toCharArray()) {
            int val = c - s.charAt(0);
            li.add(val >= 0 ? val : val + 26);
        }
        return li;
    }

    public List<List<String>> groupStrings2(String[] strings) {
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strings) {
            String key = getKey(s);
            if (map.containsKey(key)) {
                map.get(key).add(s);
            } else {
                List<String> list = new ArrayList<>();
                list.add(s);
                map.put(key, list);
            }
        }
        return new ArrayList<List<String>>(map.values());
    }

    private String getKey(String word) {
        StringBuilder sb = new StringBuilder();
        if (word.isEmpty()) {
            return sb.toString();
        }
        char c = word.charAt(0);
        for (int i = 0; i < word.length(); i++) {
            sb.append((char) ((word.charAt(i) + 26 - c) % 26));
        }
        return sb.toString();
    }
}

// 250. Count Univalue Subtrees
// Given a binary tree, count the number of uni-value subtrees.
// A Uni-value subtree means all nodes of the subtree have the same value.
class Solution250 {
    int res = 0;

    public int countUnivalSubtrees(TreeNode root) {
        if (root == null) {
            return 0;
        }
        isUni(root);
        return res;
    }

    boolean isUni(TreeNode root) {
        if (root.left == null && root.right == null) {
            res++;
            return true;
        }
        boolean bv = true;
        if (root.left != null) {
            bv = isUni(root.left) && root.left.val == root.val;
        }
        // don't shortcut
        // bv = bv && isUni(root.right) && root.right.val == root.val;
        if (root.right != null) {
            bv = isUni(root.right) && bv && root.right.val == root.val;
        }
        res += bv ? 1 : 0;
        return bv;
    }
}
