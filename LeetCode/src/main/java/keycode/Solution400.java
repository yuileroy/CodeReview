package keycode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

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
        ArrayDeque<Character> qu = new ArrayDeque<>();
        for (int i = 0; i < num.length(); i++) {
            // also check total count of available char before delete
            while (qu.size() > 0 && qu.peekLast() > num.charAt(i) && qu.size() + num.length() - i > k) {
                qu.removeLast();
            }
            qu.add(num.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append(qu.removeFirst());
        }
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    public boolean canCross(int[] stones) {
        for (int i = 3; i < stones.length; i++) {
            if (stones[i] > stones[i - 1] * 2) {
                return false;
            }
        }
        Set<Integer> set = new HashSet<>();
        for (int s : stones) {
            set.add(s);
        }
        Stack<Integer> position = new Stack<>();
        Stack<Integer> jump = new Stack<>();
        position.add(0);
        jump.add(0);

        while (!position.isEmpty()) {
            int pos = position.pop();
            int j = jump.pop();

            for (int i = j - 1; i <= j + 1; i++) {
                if (i <= 0) {
                    continue;
                }
                int nextPos = pos + i;
                if (nextPos == stones[stones.length - 1]) {
                    return true;
                }
                if (set.contains(nextPos)) {
                    position.push(nextPos);
                    jump.push(i);
                }
            }

        }
        return false;
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
     * to minimize the largest sum among these m subarrays.
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
            if (!countCopyFit(nums, mid, m)) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        return start;
    }

    boolean countCopyFit(int[] nums, int minSum, int m) {
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

    @Test
    public void test() {
        // System.out.println(maxNumber(new int[] { 3, 4, 6, 5 }, new int[] { 9, 1, 2, 5, 8, 3 }, 5));
        System.out.println("a".substring(0, 0));
    }
}
