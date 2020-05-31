package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import keycode.util.TreeNode;

public class Solution400 {

    /**
     * 402. Remove K Digits
     * 
     * remove k digits from the number so that the new number is the smallest possible
     */
    public String removeKdigits(String num, int k) {
        if (num.length() == k) {
            return "0";
        }
        k = num.length() - k;
        ArrayDeque<Character> que = new ArrayDeque<>();
        for (int i = 0; i < num.length(); i++) {
            // also check total count of available char before delete
            while (que.size() > 0 && que.peekLast() > num.charAt(i) && que.size() + num.length() - i > k) {
                que.removeLast();
            }
            que.add(num.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append(que.remove());
        }
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    /**
     * 403. Frog Jump
     * 
     * If the frog's last jump was k units, then its next jump must be either k - 1, k, or k + 1 units. Note that the
     * frog can only jump in the forward direction.
     */
    public boolean canCross(int[] stones) {
        Set<Integer> set = new HashSet<>();
        for (int s : stones) {
            set.add(s);
        }
        Stack<int[]> position = new Stack<>();
        position.push(new int[] { 0, 0 });

        while (!position.isEmpty()) {
            int[] e = position.pop();

            for (int move = e[1] - 1; move <= e[1] + 1; move++) {
                if (move <= 0) {
                    continue;
                }
                int idx = e[0] + move;
                if (idx == stones[stones.length - 1]) {
                    return true;
                }
                if (set.contains(idx)) {
                    position.push(new int[] { idx, move });
                }
            }

        }
        return false;
    }

    public boolean canCrossV2(int[] stones) {
        if (stones[1] != 1) {
            return false;
        }
        for (int i = 3; i < stones.length; i++) {
            if (stones[i] > stones[i - 1] * 2)
                return false;
        }
        Map<Integer, Integer> map = new HashMap<>();
        List<Set<Integer>> list = new ArrayList<>(stones.length);
        for (int i = 0; i < stones.length; i++) {
            map.put(stones[i], i);
            list.add(new HashSet<Integer>());
        }
        list.get(1).add(1);
        for (int i = 1; i < stones.length; i++) {
            Set<Integer> nextPosition = new HashSet<>();
            for (int preJump : list.get(i)) {
                nextPosition.add(stones[i] + preJump);
                if (preJump > 1) {
                    nextPosition.add(stones[i] + preJump - 1);
                }
                nextPosition.add(stones[i] + preJump + 1);
            }
            // end early
            if (nextPosition.contains(stones[stones.length - 1])) {
                return true;
            }
            for (int pos : nextPosition) {
                if (map.containsKey(pos)) {
                    list.get(map.get(pos)).add(pos - stones[i]);
                }
            }
        }

        return !list.get(stones.length - 1).isEmpty();
    }

    /**
     * 406. Queue Reconstruction by Height
     */
    public int[][] reconstructQueue(int[][] people) {
        // 7-0, 7-1, 6-1, 5-0, 5-2, 4-4
        // 5-0, 7-0, 5-2, 6-1, 4-4, 7-1,
        LinkedList<int[]> list = new LinkedList<int[]>();
        for (int[] e : people) {
            list.add(e);
        }
        Collections.sort(list, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);

        for (int i = 1; i < list.size(); i++) {
            int[] cur = list.remove(i);
            list.add(cur[1], cur);
        }
        int[][] res = new int[people.length][2];
        for (int i = 0; i < list.size(); i++) {
            int[] cur = list.get(i);
            res[i][0] = cur[0];
            res[i][1] = cur[1];
        }
        return res;
    }

    /**
     * 410. Split Array Largest Sum
     * 
     * split the array into m non-empty continuous subarrays to minimize the largest sum among these
     */
    public int splitArray(int[] nums, int m) {
        int total = 0;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; i++) {
            total += nums[i];
            if (nums[i] > max) {
                max = nums[i];
            }
        }

        // use binary search
        int start = max, end = total;
        while (start < end) {
            int mid = (end - start) / 2 + start;
            if (!countFit(nums, mid, m)) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return start;
    }

    private boolean countFit(int[] nums, int minSum, int m) {
        int sum = 0, count = 1;
        // count number of copies under this minSum limit
        for (int i = 0; i < nums.length; i++) {
            if (sum + nums[i] > minSum) {
                count++;
                sum = 0;
            }
            sum += nums[i];
            if (count > m) {
                return false;
            }
        }
        return true;
    }

    // V2, f[i][j] is first i nums form j subarrays
    public int splitArrayV2(int[] nums, int m) {
        int n = nums.length;
        int[][] f = new int[n + 1][m + 1];
        int[] sub = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                f[i][j] = Integer.MAX_VALUE;
            }
        }
        for (int i = 0; i < n; i++) {
            sub[i + 1] = sub[i] + nums[i];
        }
        f[0][0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                for (int k = 0; k < i; k++) {
                    f[i][j] = Math.min(f[i][j], Math.max(f[k][j - 1], sub[i] - sub[k]));
                }
            }
        }
        return f[n][m];
    }

    /**
     * 416. Partition Equal Subset Sum
     * 
     * find if the array can be partitioned into two subsets such that the sum of elements in both subsets is equal
     */
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int e : nums) {
            sum += e;
        }
        if (sum % 2 != 0) {
            return false;
        }
        sum /= 2;
        Arrays.sort(nums);
        return helper(nums, sum, 0);
    }

    // @See 40. Combination Sum II
    boolean helper(int[] nums, int remain, int start) {
        if (remain == 0) {
            return true;
        }

        for (int i = start; i < nums.length; i++) {
            if (nums[i] > remain) {
                return false;
            }
            if (i > start && nums[i] == nums[i - 1]) {
                continue;
            }
            if (helper(nums, remain - nums[i], i + 1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 421. Maximum XOR of Two Numbers in an Array
     */
    public int findMaximumXOR(int[] nums) {

        Object[] root = { null, null };
        for (int num : nums) {
            Object[] cur = root;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (cur[bit] == null) {
                    cur[bit] = new Object[] { null, null };
                }
                cur = (Object[]) cur[bit];
            }
        }
        int max = 0;
        for (int num : nums) {
            Object[] cur = root;
            int sum = 0;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (cur[bit ^ 1] != null) {
                    sum += (1 << i);
                    cur = (Object[]) cur[bit ^ 1];
                } else {
                    cur = (Object[]) cur[bit];
                }
            }
            max = Math.max(sum, max);
        }
        return max;
    }

    /**
     * 424. Longest Repeating Character Replacement
     * 
     * perform at most k replaces on a string, find longest repeating letters
     */
    public int characterReplacement(String s, int k) {
        int[] count = new int[26];
        int start = 0, maxCount = 0, res = 0;
        // find max length string ends at index end
        for (int end = 0; end < s.length(); end++) {
            maxCount = Math.max(maxCount, ++count[s.charAt(end) - 'A']);
            if (end - start + 1 - maxCount > k) {
                // can't make it same with k replaces, discard start
                count[s.charAt(start) - 'A']--;
                start++;
                // don't need to decrease maxCount
            }
            res = Math.max(res, end - start + 1);
        }
        return res;
    }

    /**
     * 498. Diagonal Traverse
     */
    public int[] findDiagonalOrder(int[][] matrix) {
        int row = matrix.length;
        if (row == 0)
            return (new int[0]); // if empty matrix
        int col = matrix[0].length;
        int[] ans = new int[row * col];
        int index = 0;
        int i = 0, j = 0;

        while (i < row && j < col) {
            while (i >= 0 && j < col) { // moving up
                ans[index++] = matrix[i][j];
                i--;
                j++;
            }
            i++;
            if (j == col) { // reach beyond column
                i++;
                j--;
            }
            while (j >= 0 && i < row) { // moving down
                ans[index++] = matrix[i][j];
                i++;
                j--;
            }
            j++;
            if (i == row) { // reach beyond row
                i--;
                j++;
            }
        }
        return ans;
    }

    /**
     * 450. Delete Node in a BST
     */
    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) {
            return null;
        }
        if (key < root.val) {
            root.left = deleteNode(root.left, key);
        } else if (key > root.val) {
            root.right = deleteNode(root.right, key);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            TreeNode tmp = root.right;
            while (tmp.left != null) {
                tmp = tmp.left;
            }
            tmp.left = root.left;
            return root.right;
        }
        return root;
    }

    @Test
    public void test() {
        // System.out.println(maxNumber(new int[] { 3, 4, 6, 5 }, new int[] { 9, 1, 2, 5, 8, 3 }, 5));
        System.out.println("a".substring(0, 0));
    }
}
