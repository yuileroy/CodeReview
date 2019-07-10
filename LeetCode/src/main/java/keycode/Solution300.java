package keycode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import keycode.util.ListNode;

public class Solution300 {
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
     */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
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
                adjList.get(neighbor).remove(leaf);
                if (adjList.get(neighbor).size() == 1) {
                    newLeaves.add(neighbor);
                }
            }
            leaves = newLeaves;
        }
        return leaves;
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

    public int findMin(int[] nums) {
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
            root = new Node(value);
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
    int insertIdx(List<Integer> sort, int value) {
        if (sort.size() == 0) {
            sort.add(value);
            return 0;
        }
        int l = 0, r = sort.size() - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (sort.get(m) >= value) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        int idx = sort.get(l) < value ? l + 1 : l;
        sort.add(idx, value);
        return idx;
    }

    /**
     * 316. Remove Duplicate Letters
     */
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
     * 321. Create Maximum Number
     */
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int[] ans = new int[k];
        // pick from two array to form k
        for (int i = Math.max(k - nums2.length, 0); i <= Math.min(nums1.length, k); i++) {
            int[] res1 = largestDigits(nums1, i);
            int[] res2 = largestDigits(nums2, k - i);
            int[] res = new int[k];
            int pos1 = 0, pos2 = 0, tpos = 0;
            while (pos1 < res1.length || pos2 < res2.length) {
                res[tpos++] = greater(res1, pos1, res2, pos2) ? res1[pos1++] : res2[pos2++];
            }

            if (!greater(ans, 0, res, 0)) {
                ans = res;
            }
        }
        return ans;
    }

    // nums1 = [9, 8], num2 = [9, 8, 3] -> false, ans = [9, 9, 8, 8, 3]
    // nums1 = [6, 7], num2 = [6, 0, 4] -> true, ans = [6, 7, 6, 0, 4]
    private boolean greater(int[] nums1, int i, int[] nums2, int j) {
        while (i < nums1.length && j < nums2.length && nums1[i] == nums2[j]) {
            i++;
            j++;
        }
        return j == nums2.length || (i < nums1.length && nums1[i] > nums2[j]);
    }

    // find largest number from selected digit.
    // nums2 = [5, 4, 3, 9], k = 2 -> [5, 9]
    private int[] largestDigits(int[] nums, int k) {
        int[] res = new int[k];
        int top = 0;
        int remove = nums.length - k;
        for (int i = 0; i < nums.length; i++) {
            while (remove > 0 && top > 0 && res[top - 1] < nums[i]) {
                top--;
                remove--;
            }
            // !-> res is still full, discard nums[i]
            if (top == k) {
                remove--;
            }
            if (top < k) {
                res[top++] = nums[i];
            }
        }
        return res;
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
                j--;
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
     * 388. Longest Absolute File Path
     */
    public int lengthLongestPath(String input) {
        int result = 0, curLen = 0;
        Stack<Integer> stack = new Stack<>();

        for (String s : input.split("\n")) {
            int level = s.lastIndexOf("\t") + 1;
            int len = s.length() - level;
            // if current directory/file depth is lower that the top directory/file on the stack, pop from stack
            while (stack.size() > level) {
                curLen -= stack.pop();
            }
            if (s.contains(".")) {
                int val = curLen + len;
                result = val > result ? val : result;
            } else {
                // add end /
                curLen += len + 1;
                stack.add(len + 1);
            }
        }
        return result;
    }

    /**
     * 394. Decode String
     */
    public String decodeString(String s) {
        String res = "";
        Stack<Integer> counts = new Stack<>();
        Stack<String> strs = new Stack<>();
        int i = 0;
        while (i < s.length()) {
            if (Character.isDigit(s.charAt(i))) {
                int num = 0;
                while (Character.isDigit(s.charAt(i))) {
                    num = num * 10 + (s.charAt(i) - '0');
                    i++;
                }
                counts.push(num);
                continue;
            }
            if (s.charAt(i) == '[') {
                strs.push(res);
                res = "";
            } else if (s.charAt(i) == ']') {
                StringBuilder sb = new StringBuilder(strs.pop());
                int count = counts.pop();
                for (int j = 0; j < count; j++) {
                    sb.append(res);
                }
                res = sb.toString();
            } else {
                res += s.charAt(i);
            }
            i++;
        }
        return res;
    }

    /**
     * 399. Evaluate Division
     */
    class Solution399 {
        Map<String, String> rootMap = new HashMap<>();
        Map<String, Double> valMap = new HashMap<>();

        public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
            if (equations == null || equations.length == 0) {
                return new double[] {};
            }

            for (int i = 0; i < equations.length; i++) {
                String x1 = equations[i][0], x2 = equations[i][1];
                rootMap.putIfAbsent(x1, x1);
                rootMap.putIfAbsent(x2, x2);
                valMap.putIfAbsent(x1, 1.0);
                valMap.putIfAbsent(x2, 1.0);

                String r1 = find(x1);
                String r2 = find(x2);
                rootMap.put(r2, r1);
                valMap.put(r2, valMap.get(x1) * values[i] / valMap.get(x2));
            }

            double[] res = new double[queries.length];
            for (int i = 0; i < queries.length; i++) {
                res[i] = -1.0;
                String x1 = queries[i][0], x2 = queries[i][1];
                if (!rootMap.containsKey(x1) || !rootMap.containsKey(x2))
                    continue;
                String r1 = find(x1);
                String r2 = find(x2);
                if (r1.equals(r2))
                    res[i] = get(x2) / get(x1);
            }
            return res;
        }

        private String find(String item) {
            if (rootMap.get(item).equals(item))
                return item;
            return find(rootMap.get(item));
        }

        // { { "a", "b" }, { "c", "d" }, { "b", "c" } }, { 2.0, 3.0, 5.0 }
        // {a=a, b=a, c=a, d=c}
        // {a=1.0, b=2.0, c=10.0, d=3.0} -> get(d) = 3.0 * get(c) = 3.0 * 10.0 * get(a) = 30.0

        // { { "a", "b" }, { "c", "d" }, { "b", "d" } }, { 2.0, 3.0, 5.0 }
        // {a=a, b=a, c=a, d=c}
        // {a=1.0, b=2.0, c=3.3333333333333335, d=3.0}
        private double get(String item) {
            double res = valMap.get(item);
            String r = rootMap.get(item);
            if (r.equals(item)) {
                return res;
            }
            return res * get(r);
        }
    }

    class Solution399v2 {
        public double[] calcEquation(String[][] equations, double[] values, String[][] queries) {
            Map<String, Map<String, Double>> graph = new HashMap<>();
            for (int i = 0; i < equations.length; i++) {
                // add arcs for both directions
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
                graph.put(vexStart, new HashMap<String, Double>());
            }
            Map<String, Double> arcMap = graph.get(vexStart);
            arcMap.put(vexEnd, value);
        }

        public double getValue(Map<String, Map<String, Double>> graph, String vexStart, String vexEnd) {
            if (graph.get(vexStart) == null || graph.get(vexEnd) == null) {
                return -1;
            }
            // queue uesd for bfs
            Queue<String> queue = new LinkedList<>();
            // distance from vexStart
            Map<String, Double> value = new HashMap<>();
            // check if the vertex has been in the queue
            Set<String> validation = new HashSet<>();
            // init
            queue.add(vexStart);
            validation.add(vexStart);
            value.put(vexStart, 1d);

            String currentNode, nextNode;
            while (!queue.isEmpty()) {
                currentNode = queue.remove();
                for (Map.Entry<String, Double> arc : graph.get(currentNode).entrySet()) {
                    nextNode = arc.getKey();
                    value.put(nextNode, value.get(currentNode) * arc.getValue());
                    if (nextNode.equals(vexEnd)) {
                        return value.get(vexEnd);
                    } else if (!validation.contains(nextNode)) {
                        queue.add(nextNode);
                        validation.add(nextNode);
                    }
                }
            }
            return -1;
        }
    }

    @Test
    public void test() {
        countSmaller(new int[] { 5, 2, 6, 1 });
        System.out.println(maxNumber(new int[] { 3, 4, 6, 5 }, new int[] { 9, 1, 2, 5, 8, 3 }, 5));
        Solution399 s = new Solution399();
        s.calcEquation(new String[][] { { "a", "b" }, { "c", "d" }, { "b", "d" } }, new double[] { 2.0, 3.0, 5.0 },
                new String[][] { { "a", "d" }, { "a", "c" }, { "c", "a" } });
    }
}
