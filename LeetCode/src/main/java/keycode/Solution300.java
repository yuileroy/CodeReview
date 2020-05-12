package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;

import org.junit.Test;

import keycode.util.ListNode;
import keycode.util.NestedInteger;
import leetcode.TreeNode;

public class Solution300 {

    /**
     * 300. Longest Increasing Subsequence
     */
    public int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        // {2, 6, 8, 3, 9} -> {2, 3, 8} -> {2, 3, 8, 9}
        List<Integer> li = new ArrayList<>();
        for (int e : nums) {
            if (li.isEmpty() || e > li.get(li.size() - 1)) {
                li.add(e);
                continue;
            }
            int start = 0, end = li.size() - 1;
            while (start < end) {
                int mid = start + (end - start) / 2;
                if (li.get(mid) >= e) {
                    end = mid;
                } else {
                    start = mid + 1;
                }
            }
            li.set(start, e);
        }
        return li.size();
    }

    /**
     * 305. Number of Islands II
     * 
     */
    // A 2d grid map of m rows and n columns is initially filled with water. We may perform an addLand operation which
    // turns the water at position (row, col) into a land. Given a list of positions to operate, count the number of
    // islands after each addLand operation.
    public List<Integer> numIslands2(int m, int n, int[][] positions) {

        int[][] dir = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int[] A = new int[m * n];
        // for (int i = 0; i < U.length; i++) U[i] = i;
        // set U[i] = i to indicate it's an island, -1 as water
        Arrays.fill(A, -1);
        List<Integer> res = new ArrayList<>();
        int cnt = 0;

        for (int[] e : positions) {
            int idx = e[0] * n + e[1];
            if (A[idx] != -1) {
                // already an island, skip
                res.add(cnt);
                continue;
            }
            A[idx] = idx;
            cnt++;
            for (int[] b : dir) {
                int nx = e[0] + b[0];
                int ny = e[1] + b[1];
                int idx2 = nx * n + ny;
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || A[idx2] == -1) {
                    continue;
                }
                // union
                int v1 = find(A, idx);
                int v2 = find(A, idx2);
                A[v1] = v2;
                if (v1 != v2) {
                    cnt--;
                }
            }
            res.add(cnt);
        }
        return res;
    }

    private int find(int[] A, int i) {
        if (A[i] == i) {
            return i;
        }
        A[i] = find(A, A[i]);
        return A[i];
    }

    /**
     * 307. Range Sum Query - Mutable
     */
    class NumArray {
        int[] tree;
        int n;

        public NumArray(int[] nums) {
            if (nums.length > 0) {
                n = nums.length;
                tree = new int[n * 2];
                buildTree(nums);
            }
        }

        // k -> (2k, 2k + 1) -> (even, odd)
        // [_, 10, 3, 7, 1, 2, 3, 4]
        // [_, 15, 10, 5, 9, 1, 2, 3, 4, 5]
        private void buildTree(int[] nums) {
            for (int i = n, j = 0; i < 2 * n; i++, j++)
                tree[i] = nums[j];
            for (int i = n - 1; i > 0; i--)
                tree[i] = tree[i * 2] + tree[i * 2 + 1];
        }

        public void update(int pos, int val) {
            pos += n;
            int diff = val - tree[pos];
            tree[pos] = val;
            while (pos > 0) {
                pos /= 2;
                tree[pos] += diff;
            }
        }

        public int sumRange(int l, int r) {
            l += n;
            r += n;
            int sum = 0;
            while (l <= r) {
                if ((l % 2) == 1) {
                    sum += tree[l];
                    l++;
                }
                if ((r % 2) == 0) {
                    sum += tree[r];
                    r--;
                }
                l /= 2;
                r /= 2;
            }
            return sum;
        }
    }

    /**
     * 309. Best Time to Buy and Sell Stock with Cooldown
     */
    public int maxProfit(int[] prices) {
        if (prices.length < 2) {
            return 0;
        }

        int[] keep = new int[prices.length];
        int[] clear = new int[prices.length];

        keep[0] = -prices[0];
        keep[1] = Math.max(keep[0], -prices[1]);
        clear[0] = 0;
        clear[1] = Math.max(clear[0], keep[0] + prices[1]);

        for (int i = 2; i < prices.length; i++) {
            // keep stock today: not buy today vs buy today
            keep[i] = Math.max(keep[i - 1], clear[i - 2] - prices[i]);
            // don't keep stock today: not sell today vs sell today
            clear[i] = Math.max(clear[i - 1], keep[i - 1] + prices[i]);
        }

        return clear[prices.length - 1];

    }

    /**
     * 310. Minimum Height Trees
     * 
     * Among all possible rooted trees, those with minimum height are called minimum height trees
     * 
     * The graph contains n nodes which are labeled from 0 to n - 1
     */
    public List<Integer> findMinHeightTrees(final int n, final int[][] edges) {
        if (n == 1) {
            return Collections.singletonList(0);
        }
        @SuppressWarnings("unchecked")
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        // topological
        int[] degree = new int[n];
        for (int i = 0; i < n; i++) {
            degree[i] = graph[i].size();
        }
        Queue<Integer> queue = new ArrayDeque<>(n);
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                queue.add(i);
            }
        }

        int count = n;
        while (count > 2) {
            int size = queue.size();
            count -= size;
            while (size-- > 0) {
                int cur = queue.remove();
                for (int next : graph[cur]) {
                    if (--degree[next] == 1) {
                        queue.add(next);
                    }
                }
            }
        }
        return new ArrayList<>(queue);
    }

    public List<Integer> findMinHeightTreesV2(int n, int[][] edges) {
        List<Integer> result = new ArrayList<>();
        if (n == 1) {
            result.add(0);
            return result;
        }

        // construct the graph
        List<Set<Integer>> adjList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adjList.add(new HashSet<>());
        }
        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }

        // remove leaf nodes
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (adjList.get(i).size() == 1) {
                leaves.add(i);
            }
        }
        while (n > 2) {
            n -= leaves.size();
            List<Integer> newLeaves = new ArrayList<>();
            for (int leaf : leaves) {
                int neighbor = adjList.get(leaf).iterator().next();
                adjList.get(neighbor).remove(leaf); // or use extra degree[i]--
                if (adjList.get(neighbor).size() == 1) {
                    newLeaves.add(neighbor);
                }
            }
            leaves = newLeaves;
        }
        return leaves;
    }

    /**
     * 312. Burst Balloons
     */

    public int maxCoins(int[] iNums) {
        int n = iNums.length;
        int[] nums = new int[n + 2];
        for (int i = 0; i < n; i++) {
            nums[i + 1] = iNums[i];
        }
        nums[0] = nums[n + 1] = 1;
        int[][] dp = new int[n + 2][n + 2];
        return fn(1, n, nums, dp);
    }

    private int fn(int i, int j, int[] nums, int[][] dp) {
        if (i > j) {
            return 0;
        }
        if (dp[i][j] > 0) {
            return dp[i][j];
        }
        // max { last balloon value = nums[i - 1] * nums[x] * nums[j + 1] }
        for (int x = i; x <= j; x++) {
            dp[i][j] = Math.max(dp[i][j],
                    fn(i, x - 1, nums, dp) + nums[i - 1] * nums[x] * nums[j + 1] + fn(x + 1, j, nums, dp));
        }
        return dp[i][j];
    }

    /**
     * 313. Super Ugly Number
     */
    public int nthSuperUglyNumber(int n, int[] primes) {
        int size = primes.length;
        int[] res = new int[n], index = new int[size], vals = new int[size];
        res[0] = 1;
        // when calc next value, we must choose one Number from primes
        // each Number multiple a min value it doesn't use before(from res)
        // compare result, find min( will be two items, update both)
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < size; j++) {
                vals[j] = res[index[j]] * primes[j];
            }
            res[i] = findMin(vals);
            for (int j = 0; j < size; j++) {
                if (vals[j] == res[i]) {
                    index[j] += 1;
                }
            }
        }
        return res[n - 1];
    }

    private int findMin(int[] nums) {
        int min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            min = Math.min(min, nums[i]);
        }
        return min;
    }

    /**
     * 315. Count of Smaller Numbers After Self
     */
    public List<Integer> countSmaller(int[] nums) {
        List<Integer> res = new ArrayList<>();
        if (nums.length == 0) {
            return res;
        }
        Node root = new Node(nums[nums.length - 1]);
        res.add(0);
        for (int i = nums.length - 2; i >= 0; i--) {
            int value = nums[i];
            res.add(insertTree(root, value));
        }
        Collections.reverse(res);
        return res;
    }

    class Node {
        int val, leftCnt, cnt;
        Node left, right;

        Node(int val) {
            this.val = val;
            cnt = 1;
        }
    }

    int insertTree(Node root, int value) {
        if (root == null) {
            return 0;
        }
        if (root.val == value) {
            root.cnt++;
            return root.leftCnt;
        } else if (root.val > value) {
            root.leftCnt++; // ! don't forget to add
            if (root.left == null) { // don't forget to link
                root.left = new Node(value);
                return 0;
            }
            root = root.left;
            return insertTree(root, value);
        } else {
            int sum = root.cnt + root.leftCnt;
            if (root.right == null) {
                root.right = new Node(value);
                return sum;
            }
            root = root.right;
            return sum + insertTree(root, value);
        }
    }

    // V2
    int insertIdx(List<Integer> list, int value) {
        if (list.size() == 0) {
            list.add(value);
            return 0;
        }
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (list.get(m) >= value) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        int idx = list.get(l) < value ? l + 1 : l;
        list.add(idx, value);
        return idx;
    }

    /**
     * 316. Remove Duplicate Letters
     */
    // Given a string which contains only lowercase letters, remove duplicate letters so that every letter appears once
    // and only once. You must make sure your result is the smallest in lexicographical order among all possible
    // results.
    public String removeDuplicateLetters(String s) {
        int[] count = new int[128];
        for (int i = 0; i < s.length(); i++) {
            count[s.charAt(i)]++;
        }

        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            int k = s.charAt(i);
            count[k]--;

            if (stack.contains(k)) {
                continue;
            }
            // keep track of count : if count == 0, don't remove
            // last char is larger than k and it appears after current i
            while (!stack.isEmpty() && stack.peek() > k && count[stack.peek()] > 0) {
                stack.pop();
            }
            stack.push(k);
        }
        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            sb.append((char) (int) stack.pop());
        }
        return sb.reverse().toString();
    }

    /**
     * 320. Generalized Abbreviation
     */
    // Input: "word", Output:
    // ["word", "1ord", "w1rd", "wo1d", "wor1", "2rd", "w2d",
    // "wo2", "1o1d", "1or1", "w1r1", "1o2", "2r1", "3d", "w3", "4"]
    public List<String> generateAbbreviations(String word) {
        List<String> res = new ArrayList<>();
        dfs(0, "", word, res);
        return res;
    }

    private void dfs(int start, String item, String word, List<String> res) {
        if (start >= word.length()) {
            res.add(item);
            return;
        }
        for (int i = start; i < word.length(); i++) {
            if (i == start) {
                // w + dfs
                dfs(i + 1, item + word.charAt(i), word, res);
            }
            String item1 = item + (i - start + 1);
            if (i < word.length() - 1) {
                item1 += word.charAt(i + 1);
            }
            // 1o + dfs, 2r + dfs, 3d + dfs, 4
            dfs(i + 2, item1, word, res);
        }
    }

    /**
     * 321. Create Maximum Number
     * 
     * Create the maximum number of length k <= m + n from digits of the two. The relative order of the digits from the
     * same array must be preserved. Return an array of the k digits.
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int[] res = new int[k];
        for (int i = 0; i <= Math.min(nums1.length, k); i++) {
            if (i + nums2.length < k) {
                continue;
            }
            int[] res1 = largestDigits(nums1, i);
            int[] res2 = largestDigits(nums2, k - i);
            int[] item = new int[k];
            int pos1 = 0, pos2 = 0, idx = 0;
            while (pos1 < res1.length || pos2 < res2.length) {
                item[idx++] = greater(res1, pos1, res2, pos2) ? res1[pos1++] : res2[pos2++];
            }
            if (greater(item, 0, res, 0)) {
                res = item;
            }
        }
        return res;
    }

    // nums1 = [9, 8], num2 = [9, 8, 3] -> false
    // nums1 = [6, 7], num2 = [6, 0, 4] -> true
    private boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }

    // find largest k sequence, acted as stack, Compare 300. Longest Increasing Subsequence
    // nums = [4, 5, 3, 9], k = 2 -> [5, 9]
    private int[] largestDigits(int[] nums, int k) {
        int[] res = new int[k];
        if (k == 0) {
            return res;
        }
        int cnt = 0; // count of selected digits
        for (int i = 0; i < nums.length; i++) {
            while (cnt > 0 && cnt + nums.length - i > k && nums[i] > res[cnt - 1]) {
                cnt--;
            }
            if (cnt < res.length) { // remember (cnt < res.length)
                res[cnt++] = nums[i]; // remember cnt++
            }
        }
        return res;
    }

    /**
     * 330. Patching Array
     */
    // [1 5 10], 20
    public int minPatches(int[] nums, int n) {
        long miss = 1;
        int i = 0, res = 0;
        while (miss <= n) {
            if (i < nums.length && nums[i] <= miss) {
                miss += nums[i++];
            } else {
                // patch miss and miss = miss + miss;
                miss += miss;
                res++;
            }
        }
        // miss = 1, i = 0, res = 0;
        // miss = 2, i = 1, res = 0;
        // miss = 4, i = 1, res = 1;
        // miss = 8, i = 1, res = 2;
        // miss = 13, i = 2; res = 2;
        // miss = 23, i = 3
        return res;
    }

    /**
     * 332. Reconstruct Itinerary
     */
    class Solution332 {

        LinkedList<String> res = new LinkedList<>();
        Map<String, PriorityQueue<String>> map = new HashMap<>();

        public List<String> findItinerary(List<List<String>> tickets) {
            if (tickets == null || tickets.size() == 0) {
                return res;
            }
            for (List<String> ticket : tickets) {
                if (!map.containsKey(ticket.get(0))) {
                    map.put(ticket.get(0), new PriorityQueue<>());
                }
                map.get(ticket.get(0)).add(ticket.get(1));
            }
            dfs("JFK");
            return res;
        }

        // [A, B] [A, C] [C, A]
        // res = [B], [C, B], [A, C, B]
        private void dfs(String cur) {
            // use while, run again means previous run didn't traverse every flight
            while (map.containsKey(cur) && !map.get(cur).isEmpty()) {
                dfs(map.get(cur).remove());
            }
            res.addFirst(cur);
        }
    }

    /**
     * 333. Largest BST Subtree
     * 
     * Given a binary tree, find the largest subtree which is a Binary Search Tree
     */
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
    }

    /**
     * 336. Palindrome Pairs
     */
    class Solution336 {
        class TrieNode {
            public int index = -1;
            public TrieNode[] next = new TrieNode[26];
            public List<Integer> list = new ArrayList<>();
        }

        public List<List<Integer>> palindromePairs(String[] words) {
            List<List<Integer>> res = new ArrayList<>();
            TrieNode root = new TrieNode();
            for (int i = 0; i < words.length; i++) {
                buildTrie(root, words[i], i);
            }
            for (int i = 0; i < words.length; i++) {
                search(root, words[i], i, res);
            }
            return res;
        }

        public void buildTrie(TrieNode root, String s, int index) {
            for (int i = s.length() - 1; i >= 0; i--) {
                char c = s.charAt(i);
                if (root.next[c - 'a'] == null) {
                    root.next[c - 'a'] = new TrieNode();
                }
                if (isPalindrome(s, 0, i)) {
                    root.list.add(index);
                }
                root = root.next[c - 'a'];
            }
            root.index = index;
            root.list.add(index); // prefix("") is palindorme
        }

        // [a, ab, ba]
        public void search(TrieNode root, String s, int i, List<List<Integer>> res) {
            for (int j = 0; j < s.length(); j++) {
                // first to handle empty string e.g. ["a",""], we will miss [0,1]
                if (root.index != -1 && root.index != i && isPalindrome(s, j, s.length() - 1)) {
                    // [ab, a] added here, i's prefix matches index's string
                    res.add(Arrays.asList(i, root.index));
                }
                root = root.next[s.charAt(j) - 'a'];
                if (root == null) {
                    return;
                }
            }
            // [a, ba] [ab, ba] [ba, ab] added here, i's string matches e's suffix
            for (int e : root.list) {
                if (i != e) {
                    res.add(Arrays.asList(i, e));
                }
            }
        }

        public boolean isPalindrome(String s, int i, int j) {
            while (i < j) {
                if (s.charAt(i++) != s.charAt(j--)) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 347. Top K Frequent Elements
     * 
     * Given a non-empty array of integers, return the k most frequent elements.
     * 
     * Your algorithm's time complexity must be better than O(n log n)
     */
    public List<Integer> topKFrequent(int[] nums, int k) {
        @SuppressWarnings("unchecked")
        List<Integer>[] buckets = new List[nums.length + 1];
        Map<Integer, Integer> map = new HashMap<>();
        for (int e : nums) {
            map.put(e, map.getOrDefault(e, 0) + 1);
        }

        for (int key : map.keySet()) {
            int cnt = map.get(key);
            if (buckets[cnt] == null) {
                buckets[cnt] = new ArrayList<>();
            }
            buckets[cnt].add(key);
        }

        List<Integer> res = new ArrayList<>();
        for (int i = buckets.length - 1; i >= 0 && k > 0; i--) {
            if (buckets[i] != null) {
                for (int j = 0; j < buckets[i].size() && k > 0; j++) {
                    res.add(buckets[i].get(j));
                    k--;
                }
            }
        }
        return res;
    }

    /**
     * 352. Data Stream as Disjoint Intervals
     */
    // Given a data stream input of non-negative integers a1, a2, ..., an, ..., summarize the numbers seen so far as a
    // list of disjoint intervals.
    class SummaryRanges {
        TreeMap<Integer, int[]> tree;

        public SummaryRanges() {
            tree = new TreeMap<>();
        }

        public void addNum(int val) {
            if (tree.containsKey(val))
                return;
            Integer l = tree.lowerKey(val);
            Integer h = tree.higherKey(val);
            if (l != null && h != null && tree.get(l)[1] + 1 == val && h == val + 1) {
                tree.get(l)[1] = tree.get(h)[1];
                tree.remove(h);
            } else if (l != null && tree.get(l)[1] + 1 >= val) {
                tree.get(l)[1] = Math.max(tree.get(l)[1], val);
            } else if (h != null && h == val + 1) {
                tree.put(val, new int[] { val, tree.get(h)[1] });
                tree.remove(h);
            } else {
                tree.put(val, new int[] { val, val });
            }
        }

        public int[][] getIntervals() {
            int[][] res = new int[tree.size()][2];
            int idx = 0;
            for (Map.Entry<Integer, int[]> e : tree.entrySet()) {
                res[idx++] = e.getValue();
            }
            return res;
        }
    }

    /**
     * 354. Russian Doll Envelopes
     */
    public int maxEnvelopes(int[][] envelopes) {
        if (envelopes.length == 0) {
            return 0;
        }
        // Arrays.sort(envelopes, (a, b) -> a[0] != b[0] ? a[0] - b[0] : b[1] - a[1]);
        // Comparator 10ms vs Lambda 41ms
        Arrays.sort(envelopes, new Comparator<int[]>() {
            public int compare(int[] env1, int[] env2) {
                if (env1[0] == env2[0])
                    return env2[1] - env1[1];
                else
                    return env1[0] - env2[0];
            }
        });
        // [5,4],[6,7],[6,5],[6,4],[7,6] ->
        // [5,4] -> [5,4],[6,7] -> [5,4],[6,5] ->[5,4],[6,5],[7,6]
        // store height
        int[] A = new int[envelopes.length];
        int cnt = 0;
        for (int[] envelope : envelopes) {
            int left = 0, right = cnt;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (A[mid] < envelope[1]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }

            // all height before left is less than envelope[1]
            // decrease A[left] if exists, or it is added
            // [5,6],[6,7],[6,3],[7,4],[8,5] -> after 3rd run, A=[3, 7], cnt=2
            // [5,6] -> [5,6],[6,7]
            A[left] = envelope[1];
            if (left == cnt) {
                // height > last one's, insert at idx left
                // and width > last one's already
                cnt++;
            }
        }
        return cnt;
    }

    /**
     * 355. Design Twitter
     */
    class Twitter {
        private int timeStamp = 0;

        // easy to find if user exist
        private Map<Integer, User> userMap;

        // Tweet link to next Tweet so that we can save a lot of time
        // when we execute getNewsFeed(userId)
        private class Tweet {
            public int id;
            public int time;
            public Tweet next;

            public Tweet(int id) {
                this.id = id;
                time = timeStamp++;
                next = null;
            }
        }

        // User can follow, unfollow and post itself
        public class User {
            public int id;
            public Set<Integer> followed;
            public Tweet tweet_head;

            public User(int id) {
                this.id = id;
                followed = new HashSet<>();
                follow(id); // first follow itself
                tweet_head = null;
            }

            public void follow(int id) {
                followed.add(id);
            }

            public void unfollow(int id) {
                followed.remove(id);
            }

            // everytime user post a new tweet, add it to the head of tweet list.
            public void post(int id) {
                Tweet t = new Tweet(id);
                t.next = tweet_head;
                tweet_head = t;
            }
        }

        /** Initialize your data structure here. */
        public Twitter() {
            userMap = new HashMap<Integer, User>();
        }

        /** Compose a new tweet. */
        public void postTweet(int userId, int tweetId) {
            if (!userMap.containsKey(userId)) {
                User u = new User(userId);
                userMap.put(userId, u);
            }
            userMap.get(userId).post(tweetId);

        }

        public List<Integer> getNewsFeed(int userId) {
            List<Integer> res = new LinkedList<>();
            if (!userMap.containsKey(userId))
                return res;

            Set<Integer> users = userMap.get(userId).followed;
            PriorityQueue<Tweet> q = new PriorityQueue<Tweet>(users.size(), (a, b) -> (b.time - a.time));
            for (int user : users) {
                Tweet t = userMap.get(user).tweet_head;
                // very imporant! If we add null to the head we are screwed.
                if (t != null) {
                    q.add(t);
                }
            }
            int n = 0;
            while (!q.isEmpty() && n < 10) {
                Tweet t = q.poll();
                res.add(t.id);
                n++;
                if (t.next != null)
                    q.add(t.next);
            }
            return res;
        }

        /** Follower follows a followee. If the operation is invalid, it should be a no-op. */
        public void follow(int followerId, int followeeId) {
            if (!userMap.containsKey(followerId)) {
                User u = new User(followerId);
                userMap.put(followerId, u);
            }
            if (!userMap.containsKey(followeeId)) {
                User u = new User(followeeId);
                userMap.put(followeeId, u);
            }
            userMap.get(followerId).follow(followeeId);
        }

        /** Follower unfollows a followee. If the operation is invalid, it should be a no-op. */
        public void unfollow(int followerId, int followeeId) {
            if (!userMap.containsKey(followerId) || followerId == followeeId)
                return;
            userMap.get(followerId).unfollow(followeeId);
        }
    }

    /**
     * 358. Rearrange String k Distance Apart
     */
    // Input: s = "aabbcc", k = 3, Output: "abcabc"
    public String rearrangeString2(String s, int k) {
        if (k < 2 || s.length() < 2) {
            return s;
        }
        int[] cnt = new int[26];
        for (char ch : s.toCharArray()) {
            cnt[ch - 'a']++;
        }

        // greedy, fill with max-count char first with batch size k
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[1] == b[1] ? a[0] - b[0] : b[1] - a[1]);
        for (int i = 0; i < cnt.length; i++) {
            if (cnt[i] > 0) {
                pq.add(new int[] { i, cnt[i] });
            }
        }

        StringBuilder sb = new StringBuilder();
        int total = s.length();
        while (total > 0) {
            List<int[]> list = new ArrayList<>();
            int batch = k;
            while (batch-- > 0 && total > 0) {
                // no valid char to fill in
                if (pq.isEmpty()) {
                    return "";
                }
                int[] cur = pq.remove();
                sb.append((char) (cur[0] + 'a'));
                cur[1]--;
                total--;
                if (cur[1] > 0) {
                    list.add(cur);
                }
            }
            // the order in this list remains same, k=2, abba will not happen
            pq.addAll(list);
        }
        return sb.toString();
    }

    /**
     * 368. Largest Divisible Subset
     */
    public List<Integer> largestDivisibleSubset(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new ArrayList<>();
        }
        Arrays.sort(nums);
        // DFS is TLE, then consider DP
        int[] DP = new int[nums.length];
        int[] pre = new int[nums.length];
        int max = 1;
        int maxIndex = 0;
        for (int j = 0; j < nums.length; j++) {
            DP[j] = 1;
            pre[j] = -1;
            for (int i = 0; i < j; i++) {
                if (nums[j] % nums[i] == 0) {
                    if (DP[i] + 1 > DP[j]) {
                        DP[j] = DP[i] + 1;
                        pre[j] = i;
                    }
                    if (DP[j] > max) {
                        max = DP[j];
                        maxIndex = j;
                    }
                }
            }
        }
        List<Integer> res = new ArrayList<>();
        while (maxIndex != -1) {
            res.add(nums[maxIndex]);
            maxIndex = pre[maxIndex];
        }
        Collections.reverse(res);
        return res;
    }

    /**
     * 370. Range Addition
     */
    // [startIndex, endIndex, inc]
    // Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
    // Output: [-2,0,3,5,3]
    public int[] getModifiedArray(int length, int[][] updates) {
        int[] A = new int[length];
        // +=[start], -=[end + 1]
        for (int[] e : updates) {
            A[e[0]] += e[2];
            if (e[1] + 1 < length) {
                A[e[1] + 1] -= e[2];
            }
        }
        int diff = 0;
        for (int i = 0; i < length; i++) {
            int cur = diff;
            diff += A[i];
            A[i] += cur;
        }
        return A;
    }

    /**
     * 372. Super Pow
     */
    private int mod = 1337;

    public int superPow(int a, int[] b) {
        int res = 1;
        for (int i = b.length - 1; i >= 0; i--) {
            // *= is wrong
            res = res * quickPow(a, b[i]) % mod;
            a = quickPow(a, 10);
        }
        return res;
    }

    // treat b as binary, a = a * a
    int quickPow(int a, int b) {
        int res = 1;
        a %= mod;
        while (b > 0) {
            if ((b & 1) != 0) {
                res = res * a % mod;
            }
            a = a * a % mod;
            b >>= 1;
        }
        return res;
    }

    /**
     * 376. Wiggle Subsequence
     * 
     * Given a sequence of integers, return the length of the longest subsequence that is a wiggle sequence.
     */
    public int wiggleMaxLength(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int p = 1, n = 1;
        // [i] depends only on [i - 1]
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < nums[i - 1]) {
                n = p + 1;
            } else if (nums[i] > nums[i - 1]) {
                p = n + 1;
            }
        }
        return Math.max(n, p);
    }

    // V2 [i] compares with every previous
    public int wiggleMaxLength2(int[] nums) {
        int[] P = new int[nums.length];
        int[] N = new int[nums.length];
        P[0] = N[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            P[i] = N[i] = 1;
            for (int k = i - 1; k >= 0; k--) {
                if (nums[k] < nums[i]) {
                    P[i] = Math.max(P[i], N[k] + 1);
                }
                if (nums[k] > nums[i]) {
                    N[i] = Math.max(N[i], P[k] + 1);
                }
            }
        }
        return Math.max(P[nums.length - 1], N[nums.length - 1]);
    }

    /**
     * 378. Kth Smallest Element in a Sorted Matrix
     */
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int start = matrix[0][0], end = matrix[n - 1][n - 1];
        while (start < end) {
            int mid = start + (end - start) / 2;
            int tmp = smallEqual(matrix, mid);
            // !-> if (tmp == k) return mid; -> end = mid;
            if (tmp < k) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return start;
    }

    private int smallEqual(int[][] matrix, int x) {
        int i = 0, j = matrix.length - 1, cnt = 0;
        while (i < matrix.length && j >= 0) {
            if (matrix[i][j] <= x) {
                i++;
                cnt += j + 1;
            } else {
                j--; // discard column
            }
        }
        return cnt;
    }

    /**
     * 382. Linked List Random Node
     *
     */
    class Solution382 {

        ListNode head;
        Random random;

        public Solution382(ListNode head) {
            this.head = head;
            random = new Random();
        }

        /** Returns a random node's value. */
        public int getRandom() {
            ListNode cur = head;
            int res = cur.val, i = 0;
            while (cur.next != null) {
                i++;
                cur = cur.next;
                if (random.nextInt(i + 1) == i) {
                    res = cur.val;
                }
            }
            return res;
        }
    }

    /**
     * 385. Mini Parser
     */
    public NestedInteger deserialize(String s) {
        NestedInteger res = new NestedInteger();
        if (s.equals("[]")) {
            return res;
        }
        if (s.charAt(0) != '[') {
            res.setInteger(Integer.parseInt(s));
            return res;
        }

        int count = 0, lastComma = 0;
        for (int i = 1; i < s.length() - 1; i++) {
            char cur = s.charAt(i);
            if (cur == '[') {
                count++;
            } else if (cur == ']') {
                count--;
            } else if (count == 0 && cur == ',') {
                res.add(deserialize(s.substring(lastComma + 1, i)));
                lastComma = i;
            }
        }
        res.add(deserialize(s.substring(lastComma + 1, s.length() - 1)));
        return res;
    }

    /**
     * 386. Lexicographical Numbers
     * 
     * For example, given 12, return: [1,10,11,12,2,3,4,5,6,7,8,9].
     */
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        // need to start from 1, can't use dfs(res, 0, n)
        for (int i = 1; i <= 9 && i <= n; i++) {
            dfs(res, i, n);
        }
        return res;
    }

    void dfs(List<Integer> res, int i, int n) {
        if (i > n) {
            return;
        }
        res.add(i);
        for (int j = 0; j <= 9; j++) {
            if (i * 10 + j > n) {
                break;
            }
            dfs(res, i * 10 + j, n);
        }
    }

    /**
     * 388. Longest Absolute File Path
     */
    public int lengthLongestPath(String input) {
        int res = 0, path = 0;
        Stack<Integer> stack = new Stack<>();

        for (String s : input.split("\n")) {
            int level = s.lastIndexOf("\t") + 1;
            int len = s.length() - level;
            // stack stores folder(append \), remove folders of same or larger level
            while (stack.size() > level) {
                path -= stack.pop();
            }
            if (!s.contains(".")) {
                path += len + 1;
                stack.add(len + 1);
            } else {
                res = Math.max(path + len, res);
            }
        }
        return res;
    }

    /**
     * 390. Elimination Game
     */
    public int lastRemaining(int n) {
        boolean left = true;
        int remaining = n;
        int step = 1;
        int head = 1;
        while (remaining > 1) {
            if (left || remaining % 2 == 1) {
                head = head + step;
            }
            remaining = remaining / 2;
            step = step * 2;
            left = !left;
        }
        return head;
    }

    /**
     * 394. Decode String
     */
    public String decodeString(String s) {
        Stack<Integer> cntStack = new Stack<>();
        Stack<String> stack = new Stack<>();
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                int start = i;
                while (s.charAt(i + 1) != '[') {
                    i++;
                }
                int val = Integer.parseInt(s.substring(start, i + 1));
                cntStack.push(val);
                stack.push(res);
            } else if (c == '[') {
                res = "";
            } else if (c == ']') {
                res = stack.pop() + dup(cntStack.pop(), res);
            } else {
                res += c;
            }
        }
        return res;
    }

    private String dup(int val, String s) {
        StringBuilder sb = new StringBuilder();
        while (val-- > 0) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 395. Longest Substring with At Least K Repeating Characters
     * 
     * every character in T appears no less than k times.
     */
    public int longestSubstring(String s, int k) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int n = s.length();
        if (k <= 1) {
            return n;
        }
        int counter[] = new int[26];
        boolean valid[] = new boolean[26];

        char ss[] = s.toCharArray();
        for (int i = 0; i < n; i++) {
            counter[ss[i] - 'a']++;
        }
        boolean fullValid = true;
        for (int i = 0; i < 26; i++) {
            if (counter[i] > 0 && counter[i] < k) {
                valid[i] = false;
                fullValid = false;
            } else {
                valid[i] = true;
            }
        }
        if (fullValid) {
            return s.length();
        }
        int max = 0, start = 0;
        for (int i = 0; i <= n; i++) {
            if (i == n || !valid[ss[i] - 'a']) {
                max = Math.max(max, longestSubstring(s.substring(start, i), k));
                start = i + 1;
            }
        }
        return max;
    }

    /**
     * 399. Evaluate Division
     */
    class Solution399 {
        Map<String, String> rootMap = new HashMap<>();
        Map<String, Double> valMap = new HashMap<>();

        public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
            // union find
            for (int i = 0; i < equations.size(); i++) {
                String x1 = equations.get(i).get(0), x2 = equations.get(i).get(1);
                rootMap.putIfAbsent(x1, x1);
                rootMap.putIfAbsent(x2, x2);
                valMap.putIfAbsent(x1, 1.0);
                valMap.putIfAbsent(x2, 1.0);

                String r1 = find(x1);
                String r2 = find(x2);
                rootMap.put(r2, r1);
                valMap.put(r2, valMap.get(x1) * values[i] / valMap.get(x2));
            }

            double[] res = new double[queries.size()];
            for (int i = 0; i < queries.size(); i++) {
                res[i] = -1.0;
                String x1 = queries.get(i).get(0), x2 = queries.get(i).get(1);
                if (!rootMap.containsKey(x1) || !rootMap.containsKey(x2))
                    continue;
                String r1 = find(x1);
                String r2 = find(x2);
                if (r1.equals(r2)) {
                    res[i] = getValue(x2) / getValue(x1);
                }
            }
            return res;
        }

        private String find(String item) {
            if (rootMap.get(item).equals(item))
                return item;
            String root = find(rootMap.get(item));
            // can't shortcut
            // rootMap.put(item, root);
            return root;
        }

        // { { "a", "b" }, { "c", "d" }, { "b", "c" } }, { 2.0, 3.0, 5.0 }
        // {a=a, b=a, c=a, d=c}
        // {a=1.0, b=2.0, c=10.0, d=3.0} -> get(d) = 3.0 * get(c) = 3.0 * 10.0 * get(a) = 30.0

        // { { "a", "b" }, { "c", "d" }, { "b", "d" } }, { 2.0, 3.0, 5.0 }
        // {a=a, b=a, c=a, d=c}
        // {a=1.0, b=2.0, c=3.3333333333333335, d=3.0}
        private double getValue(String item) {
            double res = valMap.get(item);
            String r = rootMap.get(item);
            if (r.equals(item)) {
                return res;
            }
            return res * getValue(r);
        }
    }

    class Solution399V2 {
        public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
            Map<String, Map<String, Double>> graph = new HashMap<>();
            for (int i = 0; i < equations.length; i++) {
                addArc(graph, equations[i][0], equations[i][1], values[i]);
                addArc(graph, equations[i][1], equations[i][0], 1 / values[i]);
            }

            double[] answer = new double[queries.length];
            for (int i = 0; i < answer.length; i++) {
                answer[i] = getValue(graph, queries[i][0], queries[i][1]);
            }
            return answer;
        }

        public void addArc(Map<String, Map<String, Double>> graph, String vexStart, String vexEnd, double value) {
            if (!graph.containsKey(vexStart)) {
                graph.put(vexStart, new HashMap<>());
            }
            Map<String, Double> arcMap = graph.get(vexStart);
            arcMap.put(vexEnd, value);
        }

        public double getValue(Map<String, Map<String, Double>> graph, String vexStart, String vexEnd) {
            if (graph.get(vexStart) == null || graph.get(vexEnd) == null) {
                return -1;
            }
            Queue<String> queue = new LinkedList<>();
            Map<String, Double> value = new HashMap<>();
            queue.add(vexStart);
            value.put(vexStart, 1.0);

            String currentNode, nextNode;
            while (!queue.isEmpty()) {
                currentNode = queue.remove();
                for (Map.Entry<String, Double> arc : graph.get(currentNode).entrySet()) {
                    nextNode = arc.getKey();
                    value.put(nextNode, value.get(currentNode) * arc.getValue());
                    if (nextNode.equals(vexEnd)) {
                        return value.get(vexEnd);
                    } else if (!value.containsKey(nextNode)) {
                        queue.add(nextNode);
                    }
                }
            }
            return -1;
        }
    }

    public String solution(long[] arr) {
        // Type your solution here
        if (arr.length == 0)
            return "";
        long left = 0, right = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == -1)
                continue;
            int j = i; // ! don't change i
            while (j > 2) {
                j = (j - 1) / 2;
            }
            if (j == 1) {
                left += arr[i];
            } else {
                right += arr[i];
            }
        }
        if (left == right)
            return "";
        return left > right ? "Left" : "Right";
    }

    @Test
    public void test0() {
        solution(new long[] { 3, 6, 2, 9, -1, 10 });
        countSmaller(new int[] { 5, 2, 6, 1 });
        System.out.println(maxNumber(new int[] { 3, 4, 6, 5 }, new int[] { 9, 1, 2, 5, 8, 3 }, 5));
        Solution399V2 s = new Solution399V2();
        s.calcEquation(new String[][] { { "a", "b" }, { "c", "d" }, { "b", "d" } }, new double[] { 2.0, 3.0, 5.0 },
                new String[][] { { "a", "b" }, { "a", "c" }, { "c", "a" } });
        int[] A = { 9, 1, 2, 5, 8, 3 };
        largestDigits(A, 3);
    }

    // @Test
    public void test() {
        minPatches(new int[] { 1, 5, 10 }, 20);
    }
}
