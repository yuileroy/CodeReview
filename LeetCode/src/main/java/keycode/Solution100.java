package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import keycode.util.TreeNode;

public class Solution100 {

    /**
     * 114. Flatten Binary Tree to Linked List
     */
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

    /**
     * 123. Best Time to Buy and Sell Stock III
     */
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        int buy1 = Integer.MIN_VALUE, buy2 = Integer.MIN_VALUE;
        int sell1 = 0, sell2 = 0;
        for (int p : prices) {
            buy1 = Math.max(-p, buy1);
            sell1 = Math.max(buy1 + p, sell1);
            buy2 = Math.max(sell1 - p, buy2);
            sell2 = Math.max(buy2 + p, sell2);
        }
        return sell2;
    }

    // V2 at most n transactions
    public int maxProfitV2(int[] prices) {
        if (prices == null || prices.length == 0) {
            return 0;
        }
        int n = 2;
        int max = 0, len = prices.length;
        int[][] dp = new int[n + 1][len];
        // i-th sell profit at price index j
        for (int j = 1; j < len; j++) {
            for (int i = 1; i <= n; i++) {
                int cur = dp[i][j - 1];
                for (int k = 0; k <= j; k++) {
                    cur = Math.max(cur, dp[i - 1][k] + prices[j] - prices[k]);
                }
                max = Math.max(max, cur);
                dp[i][j] = cur;
            }
        }
        return max;
    }

    /**
     * 124. Binary Tree Maximum Path Sum
     */
    public int maxPathSum(TreeNode root) {
        int[] res = { root.val };
        maxRootSum(root, res);
        return res[0];
    }

    private int maxRootSum(TreeNode root, int[] res) {
        if (root == null) {
            return 0;
        }
        int left = maxRootSum(root.left, res);
        int right = maxRootSum(root.right, res);
        int rootMax = Math.max(root.val, Math.max(left, right) + root.val);
        int pathMax = Math.max(rootMax, left + right + root.val);
        res[0] = Math.max(res[0], pathMax);
        return rootMax;
    }

    /**
     * 126. Word Ladder II
     */
    class Solution126 {

        public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordL) {
            Set<String> wordList = new HashSet<>(wordL);
            List<List<String>> res = new ArrayList<>();
            // wordList.add(endWord);

            Node begin = new Node(beginWord, new ArrayList<>());
            List<Node> cur = new ArrayList<>();
            cur.add(begin);

            boolean found = false;
            while (!found) {
                List<Node> level = build(cur, wordList);
                if (level.isEmpty()) {
                    break;
                }
                for (Node node : level) {
                    if (endWord.equals(node.val)) {
                        found = true;
                        List<String> list = new ArrayList<>(node.prev);
                        list.add(node.val);
                        res.add(list);
                    }
                }
                cur = level;
            }
            return res;
        }

        private List<Node> build(List<Node> level, Set<String> wordList) {
            List<Node> res = new ArrayList<>();
            for (Node cur : level) {
                List<String> prev = new ArrayList<>(cur.prev);
                prev.add(cur.val);
                for (String s : findNext(cur.val, wordList)) {
                    Node newNode = new Node(s, prev);
                    res.add(newNode);
                }
            }
            // remove after for loop
            for (Node node : res) {
                wordList.remove(node.val);
            }
            return res;
        }

        private List<String> findNext(String s, Set<String> wordList) {
            List<String> res = new ArrayList<>();
            char[] chars = s.toCharArray();
            for (int i = 0; i < s.length(); i++) {
                char old = chars[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    if (c == old) {
                        continue;
                    }
                    chars[i] = c;
                    String newstr = String.valueOf(chars);
                    if (wordList.contains(newstr)) {
                        res.add(newstr);
                    }
                }
                chars[i] = old;
            }
            return res;
        }

        private class Node {
            String val;
            List<String> prev;

            Node(String val, List<String> prev) {
                this.val = val;
                this.prev = prev;
            }
        }
    }

    class SolutionV2 {
        List<List<String>> res = new ArrayList<>();
        Map<String, List<String>> parents = new HashMap<>();

        public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
            Set<String> unvisited = new HashSet<>(wordList);
            if (!unvisited.contains(endWord)) {
                return res;
            }
            Queue<String> queue = new ArrayDeque<>();
            queue.add(beginWord);

            boolean found = false;
            while (!found && !queue.isEmpty()) {
                int size = queue.size();
                Set<String> visited = new HashSet<>();
                while (size-- > 0) {
                    String word = queue.remove();
                    char[] chars = word.toCharArray();
                    for (int i = 0; i < word.length(); i++) {
                        char old = chars[i];
                        for (char j = 'a'; j <= 'z'; j++) {
                            if (j == old)
                                continue;
                            chars[i] = j;
                            String newWord = new String(chars);
                            if (unvisited.contains(newWord)) {
                                if (visited.add(newWord)) {
                                    queue.add(newWord);
                                }
                                if (parents.containsKey(newWord)) {
                                    parents.get(newWord).add(word);
                                } else {
                                    List<String> list = new ArrayList<>();
                                    list.add(word);
                                    parents.put(newWord, list);
                                }
                                if (newWord.equals(endWord)) {
                                    found = true;
                                }
                            }
                        }
                        chars[i] = old;
                    }
                }
                unvisited.removeAll(visited);
            }
            dfs(new ArrayList<>(), endWord, beginWord);
            return res;
        }

        // generate result from parent map
        private void dfs(List<String> item, String word, String beginWord) {
            if (word.equals(beginWord)) {
                item.add(beginWord);
                List<String> tmp = new ArrayList<>(item);
                Collections.reverse(tmp);
                res.add(tmp);
                item.remove(item.size() - 1);
                return;
            }
            item.add(word);
            if (parents.containsKey(word)) {
                for (String s : parents.get(word)) {
                    dfs(item, s, beginWord);
                }
            }
            item.remove(item.size() - 1);
        }
    }

    /**
     * 132. Palindrome Partitioning II
     */
    public int minCut(String s) {
        int n = s.length();
        boolean B[][] = new boolean[n][n];
        int[] A = new int[n + 1];
        for (int i = 0; i < n; i++) {
            A[i] = n - i;
        }
        // A[i] is min cut of s.substring(i)
        // A[0] = n, A[n - 1] = 1, A[n] = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (s.charAt(i) == s.charAt(j) && (j - i < 2 || B[i + 1][j - 1])) {
                    B[i][j] = true;
                    A[i] = Math.min(A[i], A[j + 1] + 1);
                }
            }
        }
        return A[0] - 1;
    }

    /**
     * 135. Candy
     */
    public int candy(int[] ratings) {
        int[] A = new int[ratings.length];
        A[0] = 1;
        for (int i = 1; i < ratings.length; i++) {
            if (ratings[i] > ratings[i - 1]) {
                A[i] = A[i - 1] + 1;
            } else {
                A[i] = 1;
            }
        }
        for (int i = ratings.length - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                A[i] = Math.max(A[i], A[i + 1] + 1);
            }
        }
        // [1,2,4,2,2,1] -> 1, 2, 3, 1, 2, 1
        int res = 0;
        for (int e : A) {
            res += e;
        }
        return res;
    }

    /**
     * 139. Word Break
     */
    public boolean wordBreak139(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) {
            return true;
        }
        Set<String> set = new HashSet<>(wordDict);
        // res[j] means s.substring(0, j) is valid
        boolean[] res = new boolean[s.length() + 1];
        res[0] = true;
        for (int j = 0; j < s.length(); j++) {
            for (int i = 0; i <= j; i++) {
                if (res[i] && set.contains(s.substring(i, j + 1))) {
                    res[j + 1] = true;
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
        return dfs140(s, wordDict, map);
    }

    List<String> dfs140(String s, List<String> wordDict, Map<String, List<String>> map) {
        if (map.containsKey(s)) {
            return map.get(s);
        }
        List<String> res = new ArrayList<>();
        for (String word : wordDict) {
            // ! check equals()
            if (s.equals(word)) {
                res.add(word);
            } else if (s.startsWith(word)) {
                List<String> sublist = dfs140(s.substring(word.length()), wordDict, map);
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
     * 152. Maximum Product Subarray
     */
    public int maxProduct(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        int min = nums[0], max = nums[0], res = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int a = nums[i] * min, b = nums[i] * max;
            max = Math.max(Math.max(a, b), nums[i]);
            min = Math.min(Math.min(a, b), nums[i]);
            res = Math.max(res, max);
        }
        return res;
    }

    /**
     * 154. Find Minimum in Rotated Sorted Array II
     * 
     * @see 81
     */

    public int findMin153(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[right]) { // right side sorted, and all are greater than nums[mid]
                right = mid;
            } else { // left side sorted, and all are greater than nums[right]
                left = mid + 1;
            }
        }
        return nums[left];
    }

    public int findMin(int[] nums) {
        int left = 0, right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            // do not use while, just run once
            if (left != mid && nums[left] == nums[mid]) { // left != mid
                left++;
            } else if (nums[right] == nums[mid]) {
                right--;
            } else {
                if (nums[mid] < nums[right]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
        }
        return nums[left];
    }

    /**
     * 163. Missing Ranges
     */
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> res = new ArrayList<>();
        int cur = lower;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == cur) {
                cur++;
            } else if (nums[i] > cur) { // nums[i] < cur for [1, 1]
                res.add(getRange(cur, nums[i] - 1));
                cur = nums[i] + 1;
            }
            if (cur == Integer.MIN_VALUE) {
                return res;
            }
        }
        if (cur <= upper) {
            res.add(getRange(cur, upper));
        }
        return res;
    }

    private String getRange(int n1, int n2) {
        return (n1 == n2 ? String.valueOf(n1) : n1 + "->" + n2);
    }

    /**
     * 
     */
    public int maximumGap(int[] num) {
        if (num == null || num.length < 2)
            return 0;
        int min = num[0];
        int max = num[0];
        for (int i : num) {
            min = Math.min(min, i);
            max = Math.max(max, i);
        }
        // the minimum possibale gap, ceiling of the integer division
        int gap = (int) Math.ceil((max - min) / (num.length - 1.0));
        int[] bucketsMIN = new int[num.length - 1];
        int[] bucketsMAX = new int[num.length - 1];
        Arrays.fill(bucketsMIN, Integer.MAX_VALUE);
        Arrays.fill(bucketsMAX, Integer.MIN_VALUE);
        // put numbers into buckets
        for (int i : num) {
            if (i == min || i == max)
                continue;
            int idx = (i - min) / gap; // index of the right position in the buckets
            bucketsMIN[idx] = Math.min(i, bucketsMIN[idx]);
            bucketsMAX[idx] = Math.max(i, bucketsMAX[idx]);
        }
        // scan the buckets for the max gap
        int maxGap = 0;
        int preMax = min;
        for (int i = 0; i < num.length - 1; i++) {
            if (bucketsMIN[i] == Integer.MAX_VALUE) {
                // empty bucket
                continue;
            }
            maxGap = Math.max(maxGap, bucketsMIN[i] - preMax);
            preMax = bucketsMAX[i];
        }
        maxGap = Math.max(maxGap, max - preMax); // updata the final max value gap
        return maxGap;
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
     * 168. Excel Sheet Column Title
     */
    public String convertToTitle(int n) {
        StringBuilder sb = new StringBuilder();
        int mod = 0;
        while (n > 26) {
            mod = n % 26;
            n = n / 26;
            if (mod != 0) {
                sb.insert(0, (char) (mod - 1 + 'A'));
            } else {
                // 52 -> AZ, mod = 0, n = 2
                sb.insert(0, 'Z');
                n--;
            }
        }
        sb.insert(0, (char) (n - 1 + 'A'));
        return sb.toString();
    }

    /**
     * 179. Largest Number
     * 
     * Given a list of non negative integers, arrange them such that they form the largest number.
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
        String s = "catsanddog";
        List<String> wordDict = Arrays.asList("cat", "cats", "and", "sand", "dog");
        System.out.println(wordBreak(s, wordDict));
        fractionToDecimal(5, 800);
    }
}
