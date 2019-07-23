package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
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
        int prev = 0;
        for (int[] h : hList) {
            if (h[1] < 0) {
                pq.add(-h[1]);
            } else {
                pq.remove(h[1]);
            }
            int cur = pq.peek();
            // the current building removed, if the highest height is not
            // changed, then it's covered by other building.
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
        // one side will be full tree
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    /**
     * 224. Basic Calculator
     */
    public int calculate(String s) {
        int sign = 1, res = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                int sum = s.charAt(i) - '0';
                while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    sum = sum * 10 + s.charAt(i + 1) - '0';
                    i++;
                }
                res += sum * sign;
            } else if (s.charAt(i) == '+') {
                sign = 1;
            } else if (s.charAt(i) == '-') {
                sign = -1;
            } else if (s.charAt(i) == '(') {
                stack.push(res);
                stack.push(sign);
                res = 0;
                sign = 1;
            } else if (s.charAt(i) == ')') {
                res = res * stack.pop() + stack.pop();
            }
        }
        return res;
    }

    /**
     * 230. Kth Smallest Element in a BST
     */
    public int kthSmallest(TreeNode root, int k) {
        int res = 0;
        Deque<TreeNode> s = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !s.isEmpty()) {
            while (cur != null) {
                s.push(cur);
                cur = cur.left;
            }
            cur = s.pop();
            if (--k == 0) {
                res = cur.val;
                break;
            }
            cur = cur.right;
        }
        return res;
    }

    public int kthSmallest2(TreeNode root, int k) {
        int L = count(root.left);
        if (L == k - 1) {
            return root.val;
        } else if (L < k - 1) {
            return kthSmallest2(root.right, k - L - 1);
        } else {
            return kthSmallest2(root.left, k);
        }
    }

    private int count(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return count(root.left) + count(root.right) + 1;
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                res.add(root.val);
                root = root.left;
            } else {
                root = stack.pop();
                root = root.right;
            }
        }
        return res;
    }

    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        preorderTraversal2(root, res);
        return res;
    }

    private void preorderTraversal2(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preorderTraversal2(root.left, res);
        preorderTraversal2(root.right, res);
    }

    @Test
    public void test() {
        int[] A = { 3, 2, 1, 5, 6, 4 };
        System.out.println(findKthLargest(A, 2));
    }
}
