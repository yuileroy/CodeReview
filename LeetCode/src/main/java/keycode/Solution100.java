package keycode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import keycode.util.TreeNode;

public class Solution100 {

    /**
     * 114. Flatten Binary Tree to Linked List
     */
    class Solution {
        TreeNode pre = null;

        public void flatten(TreeNode root) {
            if (root == null) {
                return;
            }
            if (pre != null) {
                pre.right = root;
                pre.left = null;
            }
            pre = root;
            // ! realright
            TreeNode realright = root.right;
            flatten(root.left);
            flatten(realright);
        }
    }

    /**
     * 139. Word Break
     */
    public boolean wordBreak139(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) {
            return true;
        }
        Set<String> set = new HashSet<>(wordDict);
        boolean[] res = new boolean[s.length() + 1];
        res[0] = true;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j <= i; j++) {
                if (res[j] && set.contains(s.substring(j, i + 1))) {
                    res[i + 1] = true;
                    break;
                }
            }
        }
        return res[s.length()];
    }

    /**
     * 140. Word Break II
     */
    public List<String> wordBreak(String s, List<String> wordDict) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("", null);
        return dfs140(s, wordDict, map);
    }

    // dfs function returns an array including all substrings derived from s.
    List<String> dfs140(String s, List<String> wordDict, Map<String, List<String>> map) {
        if (map.containsKey(s)) {
            return map.get(s);
        }

        List<String> res = new ArrayList<>();
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                List<String> sublist = dfs140(s.substring(word.length()), wordDict, map);
                if (sublist == null) {
                    res.add(word);
                    continue;
                }
                for (String sub : sublist) {
                    res.add(word + " " + sub);
                }
            }
        }
        map.put(s, res);
        return res;
    }

    /**
     * 145. Binary Tree Postorder Traversal
     */
    // root -> right -> left, REVERSE
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                res.add(cur.val);
                stack.push(cur);
                cur = cur.right;
            } else {
                cur = stack.pop();
                cur = cur.left;
            }
        }
        Collections.reverse(res);
        return res;
    }

    /**
     * 166. Fraction to Recurring Decimal
     */
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        if ((numerator < 0) ^ (denominator < 0)) {
            sb.append("-");
        }

        // convert to long before abs() -- Integer.MIN_VALUE
        long a = Math.abs((long) numerator);
        long b = Math.abs((long) denominator);
        sb.append(a / b);

        long r = a % b;
        if (r == 0) {
            return sb.toString();
        }
        sb.append(".");

        // right-hand side of decimal point
        Map<Long, Integer> map = new HashMap<Long, Integer>();
        while (r != 0) {
            // if digits repeat, return result
            if (map.containsKey(r)) {
                int len = map.get(r);
                String part1 = sb.substring(0, len);
                String part2 = sb.substring(len);
                return part1 + "(" + part2 + ")";
            }
            map.put(r, sb.length());
            r = r * 10;
            sb.append(r / b);
            r = r % b;
        }
        return sb.toString();
    }

    /**
     * 179. Largest Number
     */
    public String largestNumber(int[] nums) {

        List<String> list = new ArrayList<>();
        for (int e : nums) {
            list.add(String.valueOf(e));
        }

        Collections.sort(list, (a, b) -> (b + a).compareTo(a + b));
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        while (sb.charAt(0) == '0' && sb.length() > 1) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    @Test
    public void test() {
        fractionToDecimal(5, 800);
    }
}
