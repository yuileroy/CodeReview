package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;

import org.junit.Test;

import leetcode.TreeNode;

public class Solution200 {

    /**
     * 205. Isomorphic Strings
     */
    public boolean isIsomorphic(String s, String t) {
        int[] A = new int[128];
        int[] B = new int[128];
        for (int i = 0; i < s.length(); i++) {
            if (A[s.charAt(i)] != B[t.charAt(i)]) {
                return false;
            }
            A[s.charAt(i)] = i + 1;
            B[t.charAt(i)] = i + 1;
        }
        return true;
    }

    /**
     * 207. Course Schedule
     * 
     * to take course 0 you have to first take course 1: [0,1]
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[] D = new int[numCourses];
        List<List<Integer>> list = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<>());
        }
        for (int[] e : prerequisites) {
            D[e[0]]++;
            list.get(e[1]).add(e[0]);
        }
        Deque<Integer> queue = new ArrayDeque<>();
        int cnt = 0;
        for (int i = 0; i < D.length; i++) {
            if (D[i] == 0) {
                queue.addLast(i);
            }
        }
        while (!queue.isEmpty()) {
            int cur = queue.removeFirst();
            cnt++;
            for (int e : list.get(cur)) {
                if (--D[e] == 0) {
                    queue.addLast(e);
                }
            }
        }
        return cnt == numCourses;
    }

    /**
     * 211. Add and Search Word - Data structure design
     * 
     * addWord(bad), search(".ad") -> true, search("b..") -> true
     */
    class WordDictionary {
        Node root = new Node();

        public WordDictionary() {
        }

        public void addWord(String word) {
            Node cur = root;
            for (char c : word.toCharArray()) {
                if (cur.child[c - 'a'] == null) {
                    cur.child[c - 'a'] = new Node();
                }
                cur = cur.child[c - 'a'];
            }
            cur.item = true;
        }

        public boolean search(String word) {
            return search(word, 0, root);
        }

        boolean search(String word, int idx, Node cur) {
            if (cur == null) {
                return false;
            }
            if (idx == word.length()) {
                return cur.item;
            }
            char c = word.charAt(idx);
            if (c != '.') {
                return search(word, idx + 1, cur.child[c - 'a']);
            }
            for (Node node : cur.child) {
                if (search(word, idx + 1, node)) {
                    return true;
                }
            }
            return false;
        }
    }

    class Node {
        Node[] child = new Node[26];
        boolean item = false;
    }

    /**
     * 214. Shortest Palindrome
     */
    public String shortestPalindrome(String s) {
        int left = 0;
        for (int right = s.length() - 1; right >= 0; right--) {
            if (s.charAt(right) == s.charAt(left)) {
                left++;
            }
        }

        if (left == s.length()) { // palindrome
            return s;
        }
        // ab -> b+a+b
        // s=abecea, suffix=ecea || s=abacea, suffix=cea
        String suffix = s.substring(left);
        System.out.println(suffix);

        String prefix = new StringBuilder(suffix).reverse().toString();
        String mid = shortestPalindrome(s.substring(0, left));
        return prefix + mid + suffix;
    }

    /**
     * 215. Kth Largest Element in an Array
     */
    public int findKthLargest(int[] nums, int k) {
        return fn(nums, nums.length - k, 0, nums.length - 1);
    }

    int fn(int[] nums, int k, int i, int j) {
        if (i == j) {
            return nums[i];
        }
        int m = i;
        for (int n = i + 1; n <= j; n++) {
            if (nums[n] < nums[i]) {
                swap(nums, ++m, n);
            }
        }
        swap(nums, i, m);
        if (m == k) {
            return nums[m];
        } else if (m < k) {
            return fn(nums, k, m + 1, j);
        } else {
            return fn(nums, k, i, m - 1);
        }
    }

    // V2
    int fn2(int[] nums, int k, int i, int j) {
        if (i == j) {
            return nums[i];
        }
        int m = i + 1, n = j;
        while (m <= n) {
            while (m <= n && nums[m] <= nums[i]) {
                m++;
            }
            while (m <= n && nums[n] > nums[i]) {
                n--;
            }
            if (m < n) {
                swap(nums, m, n);
                m++;
                n--;
            }
        }
        m--;
        swap(nums, i, m);
        if (m == k) {
            return nums[m];
        } else if (m < k) {
            return fn(nums, k, m + 1, j);
        } else {
            return fn(nums, k, i, m - 1);
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * 218. The Skyline Problem
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> res = new ArrayList<>();
        if (buildings == null || buildings.length == 0) {
            return res;
        }
        List<int[]> hList = new ArrayList<>();
        for (int[] bd : buildings) {
            hList.add(new int[] { bd[0], -bd[2] });
            hList.add(new int[] { bd[1], bd[2] });
        }
        Collections.sort(hList, (a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

        PriorityQueue<Integer> pq = new PriorityQueue<Integer>((a, b) -> b - a);
        pq.add(0);
        int prev = 0; // to compare with peek() after each step
        for (int[] h : hList) {
            // 1. in add, out remove
            if (h[1] < 0) {
                pq.add(-h[1]);
            } else {
                pq.remove(h[1]);
            }
            // 2. if the highest height peek() changed, it's an edge
            int cur = pq.peek();
            if (cur != prev) {
                res.add(Arrays.asList(h[0], cur));
                prev = cur;
            }
        }
        return res;
    }

    /**
     * 220. Contains Duplicate III
     * 
     * Given an array of integers, find out whether there are two distinct indices i and j in the array such that the
     * absolute difference between nums[i] and nums[j] is at most t and the absolute difference between i and j is at
     * most k
     */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        int len = nums.length;
        if (k == 0 || len == 0 || t < 0) {
            return false;
        }
        Map<Long, Long> map = new HashMap<>();
        long dis = (long) t + 1;
        for (int i = 0; i < len; i++) {
            long cur = bucket(nums[i], dis);
            if (map.containsKey(cur))
                return true;
            if (map.containsKey(cur - 1) && nums[i] - map.get(cur - 1) < dis)
                return true;
            if (map.containsKey(cur + 1) && map.get(cur + 1) - nums[i] < dis)
                return true;

            map.put(cur, (long) nums[i]);
            if (i >= k) {
                map.remove(bucket(nums[i - k], dis));
            }
        }
        return false;
    }

    private long bucket(long x, long dis) {
        // (0, dis - 1) -> 0, (-dis, -1) -> -1
        return x >= 0 ? x / dis : (x + 1) / dis - 1;
    }

    // V2 slow
    public boolean containsNearbyAlmostDuplicateV2(int[] nums, int k, int t) {
        if (k < 1 || t < 0) {
            return false;
        }
        TreeSet<Long> set = new TreeSet<Long>();

        for (int j = 0; j < nums.length; j++) {
            long leftBoundary = (long) nums[j] - t;
            long rightBoundary = (long) nums[j] + t + 1;
            // Returns a view of the portion of this set whose elements
            // range from fromElement, inclusive, to toElement,exclusive
            if (!set.subSet(leftBoundary, rightBoundary).isEmpty()) {
                return true;
            }
            set.add((long) nums[j]);
            if (j >= k) {
                set.remove((long) nums[j - k]);
            }
        }
        return false;
    }

    /**
     * 221. Maximal Square
     * 
     * Given a 2D binary matrix filled with 0's and 1's, find the largest square containing only 1's and return its
     * area.
     */
    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int m = matrix.length, n = matrix[0].length;
        int[][] t = new int[m][n];
        int max = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') {
                    if (i == 0 || j == 0) {
                        t[i][j] = 1;
                    } else {
                        int min = Math.min(t[i - 1][j], t[i][j - 1]);
                        t[i][j] = Math.min(min, t[i - 1][j - 1]) + 1;
                    }
                    max = Math.max(max, t[i][j]);
                }
            }
        }
        return max * max;
    }

    /**
     * 222. Count Complete Tree Nodes
     */
    int max = 0;

    public int countNodes(TreeNode root) {
        countNodes(root, 1);
        return max;
    }

    void countNodes(TreeNode root, int id) {
        if (root == null) {
            return;
        }
        max = Math.max(max, id);
        countNodes(root.left, id * 2);
        countNodes(root.right, id * 2 + 1);
    }

    public int countNodesV2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // **speed up START
        TreeNode left = root, right = root;
        int height = 0;
        while (right != null) {
            height++;
            left = left.left;
            right = right.right;
        }
        if (left == null) {
            return (1 << height) - 1;
        }
        // **speed up END
        // one side will be full tree
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    /**
     * 224. Basic Calculator
     */
    public int calculate(String s) {
        int cur = 0, sign = 1; // init
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                int val = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    val = val * 10 + s.charAt(i) - '0';
                    i++;
                }
                i--;
                cur += val * sign;
            } else if (s.charAt(i) == '+') {
                sign = 1;
            } else if (s.charAt(i) == '-') {
                sign = -1;
            } else if (s.charAt(i) == '(') {
                stack.push(cur);
                stack.push(sign);
                // init
                cur = 0;
                sign = 1;
            } else if (s.charAt(i) == ')') {
                cur = cur * stack.pop() + stack.pop();
            }
        }
        return cur;
    }

    /**
     * 227. Basic Calculator II
     * 
     * The expression string contains only non-negative integers, +, -, *, / operators and empty spaces
     */
    public int calculate227(String s) {
        char mark = '+';
        int res = 0, pre = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                continue;
            }
            if (Character.isDigit(c)) {
                int val = 0;
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    val = val * 10 + s.charAt(i++) - '0';
                }
                i--;
                if (mark == '+') {
                    pre = val;
                } else if (mark == '-') {
                    pre = -val;
                } else if (mark == '*') {
                    res -= pre;
                    pre *= val;
                } else if (mark == '/') {
                    res -= pre;
                    pre /= val;
                }
                res += pre;
            } else {
                mark = c;
            }
        }
        return res;
    }

    /**
     * 230. Kth Smallest Element in a BST
     */

    int cnt = 0, res = 0;

    public int kthSmallest(TreeNode root, int k) {
        kth(root, k);
        return res;
    }

    public void kth(TreeNode root, int k) {
        if (root == null || cnt == k) {
            return;
        }
        kth(root.left, k);
        if (++cnt == k) { // --k WRONG
            res = root.val;
        }
        kth(root.right, k);
    }

    public int kthSmallestV2(TreeNode root, int k) {
        int res = 0;
        Deque<TreeNode> que = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !que.isEmpty()) {
            while (cur != null) {
                que.push(cur);
                cur = cur.left;
            }
            cur = que.pop();
            if (--k == 0) {
                res = cur.val;
                break;
            }
            cur = cur.right;
        }
        return res;
    }

    /**
     * 236. Lowest Common Ancestor of a Binary Tree
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return null;
        }
        if (root == p || root == q) {
            return root;
        }
        TreeNode leftN = lowestCommonAncestor(root.left, p, q);
        TreeNode rightN = lowestCommonAncestor(root.right, p, q);
        // both null return null
        if (leftN != null && rightN != null) {
            return root;
        } else if (leftN == null) {
            return rightN;
        } else {
            return leftN;
        }
    }

    /**
     * Find distance between two nodes of a Binary Tree (236)
     */
    int find = 0;

    public int lowest(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null) {
            return -1;
        }
        int leftN = lowest(root.left, p, q);
        int rightN = lowest(root.right, p, q);
        if (root == p || root == q) {
            find++;
            return leftN == -1 ? rightN + 1 : leftN + 1;
        }
        if (leftN != -1 && rightN != -1) {
            return leftN + rightN + 2;
        } else if (leftN == -1 && rightN == -1) {
            return -1;
        } else if (leftN != -1) {
            return find == 2 ? leftN : leftN + 1;
        } else {
            return find == 2 ? rightN : rightN + 1;
        }
    }

    /**
     * 239. Sliding Window Maximum
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums.length == 0 || k == 1) {
            return nums;
        }
        int[] res = new int[nums.length - k + 1];
        ArrayDeque<Integer> que = new ArrayDeque<>();
        for (int i = 0; i < k - 1; i++) {
            addNext(que, nums[i]);
        }

        for (int i = 0; i <= nums.length - k; i++) {
            addNext(que, nums[i + k - 1]);
            res[i] = que.peekFirst();
            if (nums[i] == res[i]) {
                // otherwise is removed already
                que.removeFirst();
            }
        }
        return res;
    }

    private void addNext(ArrayDeque<Integer> que, int val) {
        while (!que.isEmpty() && que.peekLast() < val) {
            que.removeLast();
        }
        que.addLast(val);
    }

    /**
     * 240. Search a 2D Matrix II
     */
    // [1, 4, 7, 11, 15]
    // [2, 5, 8, 12, 19]
    // [3, 6, 9, 16, 22]
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int i = 0, j = matrix[0].length - 1;
        while (i < matrix.length && j >= 0) {
            int x = matrix[i][j];
            if (target == x)
                return true;
            else if (target < x)
                j--;
            else
                i++;
        }
        return false;
    }

    /**
     * 241. Different Ways to Add Parentheses
     * 
     */
    // Input: "2*3-4*5"
    // Output: [-34, -14, -10, -10, 10]
    // Explanation:
    // (2*(3-(4*5))) = -34
    // ((2*3)-(4*5)) = -14
    // ((2*(3-4))*5) = -10
    // (2*((3-4)*5)) = -10
    // (((2*3)-4)*5) = 10
    public List<Integer> diffWaysToCompute(String input) {
        List<Integer> res = new ArrayList<>();
        if (input == null || input.length() == 0) {
            return res;
        }
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != '+' && c != '-' && c != '*') {
                continue;
            }
            List<Integer> left = diffWaysToCompute(input.substring(0, i));
            List<Integer> right = diffWaysToCompute(input.substring(i + 1));
            for (int m : left) {
                for (int n : right) {
                    if (c == '+') {
                        res.add(m + n);
                    } else if (c == '-') {
                        res.add(m - n);
                    } else if (c == '*') {
                        res.add(m * n);
                    }
                }
            }
        }

        if (res.isEmpty()) {
            res.add(Integer.parseInt(input));
        }
        return res;
    }

    /**
     * 250. Count Univalue Subtrees
     * 
     * Given a binary tree, count the number of uni-value subtrees.A Uni-value subtree means all nodes of the subtree
     * have the same value.
     */
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
                bv = isUni(root.right) && root.right.val == root.val && bv;
            }
            res += bv ? 1 : 0;
            return bv;
        }
    }

    /**
     * 254. Factor Combinations
     *
     * Write a function that takes an integer n and return all possible combinations of its factors.
     */
    class Solution254 {
        public List<List<Integer>> getFactors(int n) {
            List<List<Integer>> res = new ArrayList<>();
            // dfs(2, n, new ArrayList<>(), res);
            build(2, n, new ArrayList<>(), res);
            return res;
        }

        void build(int start, int n, List<Integer> item, List<List<Integer>> res) {
            // if (n == 1) {
            // res.add(new ArrayList<>(item));
            // return;
            // }
            for (int i = start; i <= n / i; i++) {
                if (n % i == 0) {
                    List<Integer> tmp = new ArrayList<>(item);
                    tmp.add(i);
                    tmp.add(n / i);
                    res.add(new ArrayList<>(tmp));
                    item.add(i);
                    // build(start, n / i, item, res);
                    build(i, n / i, item, res);
                    item.remove(item.size() - 1);
                }
            }
        }

        void dfs(int start, int n, List<Integer> item, List<List<Integer>> res) {
            // ! correct but inefficient
            if (n == 1 && item.size() > 1) {
                res.add(new ArrayList<>(item));
                return;
            }
            for (int i = start; i <= n; i++) {
                if (n % i == 0) {
                    item.add(i);
                    dfs(i, n / i, item, res);
                    item.remove(item.size() - 1);
                }
            }
        }
    }

    /**
     * 255. Verify Preorder Sequence in Binary Search Tree
     */
    // Input: [5,2,1,3,6], Output: true
    // 1. modify array to act as stack
    // 2. stack, pop() is inorder
    // 3. recursive, inefficient
    public boolean verifyPreorder(int[] preorder) {
        int inorder = Integer.MIN_VALUE, end = -1;
        // index 0 to end, act as a stack
        for (int val : preorder) {
            if (val < inorder) {
                return false;
            }
            while (end > -1 && val > preorder[end]) {
                inorder = preorder[end--];
            }
            preorder[++end] = val;
        }
        return true;
    }

    // V2
    public boolean verifyPreorder2(int[] preorder) {
        Stack<Integer> stack = new Stack<>();
        int inorder = Integer.MIN_VALUE;

        for (int v : preorder) {
            if (v < inorder) {
                return false;
            }
            while (!stack.isEmpty() && v > stack.peek()) {
                inorder = stack.pop();
            }
            stack.push(v);
        }
        return true;
    }

    /**
     * 264. Ugly Number II
     */
    public int nthUglyNumber(int n) {

        int[] res = new int[n];
        res[0] = 1;

        int idx2 = 0, idx3 = 0, idx5 = 0;
        int cur = 1;
        while (cur < n) {
            int min = minOf(res[idx2] * 2, res[idx3] * 3, res[idx5] * 5);
            if (min == res[idx2] * 2) {
                idx2++;
            }
            if (min == res[idx3] * 3) {
                idx3++;
            }
            if (min == res[idx5] * 5) {
                idx5++;
            }
            res[cur] = min;
            cur++;
        }
        return res[n - 1];
    }

    private int minOf(int a, int b, int c) {
        int d = a < b ? a : b;
        return d < c ? d : c;
    }

    /**
     * @ 269. Alien Dictionary
     */

    public String alienOrder(String[] words) {
        @SuppressWarnings("unchecked")
        HashSet<Integer>[] adj = new HashSet[26];
        // Arrays.fill(adj, new HashSet<>()); // it's shared
        for (int i = 0; i < 26; i++) {
            adj[i] = new HashSet<>();
        }
        int[] degree = new int[26];
        Arrays.fill(degree, -1);
        for (int i = 0; i < words.length; i++) {
            for (char c : words[i].toCharArray()) {
                if (degree[c - 'a'] < 0) { // missed, degree[c2]++ may duplicate
                    degree[c - 'a'] = 0;
                }
            }
            if (i == 0) {
                continue;
            }
            String w1 = words[i - 1], w2 = words[i];
            for (int j = 0; j < w1.length() && j < w2.length(); j++) {
                int c1 = w1.charAt(j) - 'a', c2 = w2.charAt(j) - 'a';
                if (c1 != c2) {
                    if (!adj[c1].contains(c2)) { // missed
                        adj[c1].add(c2);
                        degree[c2]++;
                    }
                    break;
                }
                // "abc" -> "ab"
                if (j == w2.length() - 1 && w1.length() > w2.length()) {
                    return "";
                }
            }
        }
        ArrayDeque<Integer> que = new ArrayDeque<>();
        for (int i = 0; i < degree.length; i++) {
            if (degree[i] == 0) {
                que.add(i);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!que.isEmpty()) {
            int u = que.remove();
            sb.append((char) ('a' + u));
            for (int v : adj[u]) {
                if (--degree[v] == 0) {
                    que.add(v);
                }
            }
        }
        // need to check every degree, not que.isEmpty()
        for (int d : degree) {
            if (d > 0) {
                return "";
            }
        }
        return sb.toString();
    }

    /**
     * 272. Closest Binary Search Tree Value II
     */
    public List<Integer> closestKValues(TreeNode root, double target, int k) {
        List<Integer> res = new LinkedList<>();
        helper(root, target, k, res);
        return res;
    }

    private void helper(TreeNode root, double target, int k, List<Integer> res) {
        if (root == null) {
            return;
        }
        helper(root.left, target, k, res);
        if (res.size() == k) {
            if (Math.abs(root.val - target) < Math.abs(res.get(0) - target)) {
                res.remove(0);
                res.add(root.val);
            } else {
                return;
            }
        } else {
            res.add(root.val);
        }
        helper(root.right, target, k, res);
    }

    /**
     * 282. Expression Add Operators
     */
    public List<String> addOperators(String num, int target) {
        List<String> list = new ArrayList<String>();
        fn(list, num, target, new StringBuilder(), 0, 0, 0);
        return list;
    }

    // dp, dfs, add a number to result each time
    private void fn(List<String> list, String num, int target, StringBuilder sb, int start, long sum, long lastNum) {
        if (start == num.length() && sum == target) {
            list.add(sb.toString());
            return;
        }
        if (start >= num.length()) {
            return;
        }
        for (int i = start; i < num.length(); i++) {
            if (num.charAt(start) == '0' && i > start) {
                break; // "0x"
            }
            long curNum = Long.parseLong(num.substring(start, i + 1));
            int len = sb.length();
            if (start == 0) {
                sb.append(curNum);
                fn(list, num, target, sb, i + 1, curNum, curNum);
                sb.setLength(len);
            } else {
                sb.append("+").append(curNum);
                fn(list, num, target, sb, i + 1, sum + curNum, curNum);
                sb.setLength(len);
                sb.append("-").append(curNum);
                fn(list, num, target, sb, i + 1, sum - curNum, -curNum);
                sb.setLength(len);
                // sum - lastNum + lastNum * curNum
                sb.append("*").append(curNum);
                fn(list, num, target, sb, i + 1, sum - lastNum + lastNum * curNum, lastNum * curNum);
                sb.setLength(len);
            }
        }
    }

    /**
     * 285. Inorder Successor in BST
     */
    public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
        TreeNode cur = root, res = null;
        while (cur != null) {
            if (cur.val > p.val) {
                res = cur;
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return res;
    }

    public TreeNode inorderPre(TreeNode root, TreeNode p) {
        TreeNode cur = root, res = null;
        while (cur != null) {
            if (cur.val < p.val) {
                res = cur;
                cur = cur.right;
            } else {
                cur = cur.left;
            }
        }
        return res;
    }

    // V2
    TreeNode pre = null, res285 = null;

    public TreeNode inorderSuccessorV2(TreeNode root, TreeNode p) {
        if (root == null) {
            return null;
        }
        inOrder(root, p);
        return res285;
    }

    private void inOrder(TreeNode root, TreeNode p) {
        if (root == null) {
            return;
        }
        inOrder(root.right, p);
        if (root == p) {
            res285 = pre;
            return;
        }
        pre = root;
        inOrder(root.left, p);
    }

    @Test
    public void test() {
        System.out.println("a");
        // int[] A = { 3, 2, 1, 5, 6, 4 };
        // System.out.println(findKthLargest(A, 2));
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(2);
        System.out.println(kthSmallest(root, 1));
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(5);
        pq.add(3);
        pq.remove(5);
    }
}
