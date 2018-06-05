package keycode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

public class Solution000 {

    /**
     * 3. Longest Substring Without Repeating Characters
     * 
     * @category SliceWindow
     */
    // abcbba
    public int lengthOfLongestSubstring(String s) {
        int[] index = new int[128];
        int start = 0, res = 0;
        for (int i = 0; i < s.length(); i++) {
            start = Math.max(index[s.charAt(i)], start);
            res = Math.max(res, i - start + 1);
            // !-> (last index of char) + 1, so start=2 when i=3
            index[s.charAt(i)] = i + 1;
        }
        return res;
    }

    /**
     * 5. Longest Palindromic Substring
     * 
     * @category DP
     */
    String longestPalindrome(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }

        int n = s.length();
        boolean[][] B = new boolean[n][n];
        int start = 0, max = 1;
        for (int i = 0; i < n; i++) {
            B[i][i] = true;
            for (int j = 0; j < i; j++) {
                if (s.charAt(i) == s.charAt(j) && (i - j <= 2 || B[j + 1][i - 1])) {
                    B[j][i] = true;
                    if (max < i - j + 1) {
                        max = i - j + 1;
                        start = j;
                    }
                }
            }
        }
        return s.substring(start, start + max);
    }

    /**
     * 6. ZigZag Conversion
     */
    public String convert(String s, int nRows) {
        char[] c = s.toCharArray();
        int len = c.length;
        StringBuilder[] sb = new StringBuilder[nRows];
        for (int i = 0; i < sb.length; i++) {
            sb[i] = new StringBuilder();
        }
        int i = 0;
        while (i < len) {
            for (int idx = 0; idx < nRows && i < len; idx++) // vertical down
                sb[idx].append(c[i++]);
            for (int idx = nRows - 2; idx >= 1 && i < len; idx--) // slide up
                sb[idx].append(c[i++]);
        }

        for (int idx = 1; idx < sb.length; idx++)
            sb[0].append(sb[idx]);
        return sb[0].toString();
    }

    /**
     * 7. Reverse Integer
     * 
     * @category Overflow
     */
    // 120 -> 21
    public int reverse(int x) {
        int result = 0;
        while (x != 0) {
            int tail = x % 10;
            int newResult = result * 10 + tail;
            // !-> If overflow exists, the new result will not equal previous one.
            if ((newResult - tail) / 10 != result) {
                return 0;
            }
            result = newResult;
            x = x / 10;
        }
        return result;
    }

    /**
     * 9. Palindrome Number
     * 
     * @category Overflow
     */
    public boolean isPalindrome(int x) {
        if (x < 0)
            return false;
        int origin = x;
        int reverse = 0;
        while (origin != 0) {
            reverse = reverse * 10 + origin % 10;
            origin /= 10;
        }
        return x == reverse;
    }

    /**
     * 10. Regular Expression Matching
     * 
     * @category DP
     */
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        // p = a*, a*b*,
        for (int j = 1; j < p.length(); j++) {
            if (p.charAt(j) == '*' && dp[0][j - 1]) {
                dp[0][j + 1] = true;
            }
        }
        // s[0]-j[0] is dp[1][1]
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                if (p.charAt(j) == '.' || p.charAt(j) == s.charAt(i)) {
                    dp[i + 1][j + 1] = dp[i][j];
                }
                if (j > 0 && p.charAt(j) == '*') {
                    if (p.charAt(j - 1) != s.charAt(i) && p.charAt(j - 1) != '.') {
                        // "a, ax*" -> "a, a"
                        dp[i + 1][j + 1] = dp[i + 1][j - 1];
                    } else {
                        // "ax, ax*" -> "ax, ax" || "a, ax*" || "ax, a"
                        dp[i + 1][j + 1] = dp[i + 1][j] || dp[i][j + 1] || dp[i + 1][j - 1];
                    }
                }
            }
        }
        return dp[s.length()][p.length()];
    }

    /**
     * 15. 3Sum
     */
    public List<List<Integer>> threeSum(int[] num) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        Arrays.sort(num);
        for (int i = 0; i < num.length - 2 && num[i] <= 0; i++) {
            if (i > 0 && num[i] == num[i - 1]) { // skip same result
                continue;
            }
            int twosum = 0 - num[i];
            int l = i + 1, r = num.length - 1;
            while (l < r) {
                int sum = num[l] + num[r];
                if (sum < twosum) {
                    l++;
                } else if (sum > twosum) {
                    r--;
                } else {
                    res.add(Arrays.asList(num[i], num[l], num[r]));
                    l++;
                    r--;
                    while (l < r && num[l] == num[l - 1]) { // skip same result
                        l++;
                    }
                    while (l < r && num[r] == num[r + 1]) { // skip same result
                        r--;
                    }
                }
            }
        }
        return res;
    }

    /**
     * 16. 3Sum Closest
     */
    public int threeSumClosest(int[] nums, int target) {
        if (nums == null || nums.length < 3) {
            return 0;
        }

        Arrays.sort(nums);
        int min = Integer.MAX_VALUE, res = 0;
        for (int i = 0; i <= nums.length - 3; i++) {
            int low = i + 1;
            int high = nums.length - 1;
            while (low < high) {
                int sum = nums[i] + nums[low] + nums[high];
                if (Math.abs(target - sum) < min) {
                    min = Math.abs(target - sum);
                    res = sum;
                }
                if (sum == target) {
                    return sum;
                } else if (sum < target) {
                    low++;
                } else {
                    high--;
                }
            }
        }
        return res;
    }

    /**
     * 17. Letter Combinations of a Phone Number
     * 
     * @category DFS
     */
    private String[] dict = new String[] { "0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz" };

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits == null || digits.isEmpty()) {
            return res;
        }
        helper17(digits, 0, new StringBuilder(), res);
        return res;
    }

    private void helper17(String digits, int start, StringBuilder sb, List<String> res) {
        if (start == digits.length()) {
            res.add(sb.toString());
            return;
        }
        char digit = digits.charAt(start);
        for (char c : dict[digit - '0'].toCharArray()) { // digit - '0' , .toCharArray()
            sb.append(c);
            helper17(digits, start + 1, sb, res);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    /**
     * 22. Generate Parentheses
     * 
     * @category DFS
     */
    public List<String> generateParenthesis(int n) {
        List<String> result = new ArrayList<String>();
        dfs22("", 0, 0, n, result);

        return result;
    }

    void dfs22(String pre, int left, int right, int n, List<String> result) {
        if (left == n && right == n) {
            result.add(pre);
            return;
        }
        if (left < n) {
            dfs22(pre + "(", left + 1, right, n, result);
        }
        if (right < left) {
            dfs22(pre + ")", left, right + 1, n, result);
        }
    }

    /**
     * 18. 4Sum
     */
    public List<List<Integer>> fourSum(int[] num, int target) {
        Arrays.sort(num);
        List<List<Integer>> result = new ArrayList<List<Integer>>();

        for (int i = 0; i < num.length - 3; i++) {
            if (i > 0 && num[i] == num[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < num.length - 2; j++) {
                if (j > i + 1 && num[j] == num[j - 1]) {
                    continue;
                }
                int k = j + 1;
                int l = num.length - 1;
                while (k < l) {
                    int sum = num[i] + num[j] + num[k] + num[l];
                    if (sum > target) {
                        l--;
                    } else if (sum < target) {
                        k++;
                    } else {
                        result.add(Arrays.asList(num[i], num[j], num[k], num[l]));
                        // add while to remove duplicate
                        k++;
                        l--;
                        while (k < l && num[k] == num[k - 1])
                            k++;
                        while (k < l && num[l] == num[l + 1])
                            l--;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 27. Remove Element
     */
    // [3,2,4,3] , 3 -> [4,2,3,3]
    public int removeElement(int[] nums, int val) {
        int last = nums.length - 1;
        int curr = 0;
        while (curr <= last) {
            if (nums[curr] == val) {
                nums[curr] = nums[last];
                last--;
            } else {
                curr++;
            }
        }
        return last + 1;
    }

    public int divide(int dividend, int divisor) {
        if (divisor == 0 || (dividend == Integer.MIN_VALUE && divisor == -1)) {
            return Integer.MAX_VALUE;
        }
        long lDividend = Math.abs((long) dividend);
        long lDivisor = Math.abs((long) divisor);
        if (dividend == 0 || lDividend < lDivisor) {
            return 0;
        }
        int q = 0;
        boolean diffSign = false;
        if (dividend < 0 && divisor > 0 || dividend > 0 && divisor < 0) {
            diffSign = true;
        }
        while (lDividend >= lDivisor) {
            long temp = lDivisor;
            long multiplier = 1;
            while (lDividend >= temp << 1) {
                temp <<= 1;
                multiplier <<= 1;
            }
            lDividend -= temp;
            q += multiplier;
        }
        if (diffSign) {
            return q * (-1);
        } else {
            return q;
        }
    }

    /**
     * 31. Next Permutation
     */
    public void nextPermutation(int[] nums) {
        int last = nums.length - 1;
        int k = last;
        while (k > 0 && nums[k - 1] >= nums[k]) {
            k--;
        }
        // 6 3 4 9 8 7 1
        // k = 3, val = 9, replace 4 with 7
        if (k > 0) {
            int next = last;
            while (next > 0 && nums[next] <= nums[k - 1]) {
                next--;
            }
            swap(k - 1, next, nums);
        }
        // 6 3 7 9 8 4 1 -> 6 3 7 1 4 8 9
        for (int i = k, j = last; i < j; i++, j--) {
            swap(i, j, nums);
        }
    }

    private void swap(int i, int j, int[] nums) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    /**
     * 32. Longest Valid Parentheses
     */
    public int longestValidParentheses(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        int start = 0, max = 0;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                if (stack.isEmpty()) { // meet ')' but stack is empty, only in this situation reset start
                    start = i + 1;
                } else {
                    // !stack.isEmpty(), s.charAt(i)=')'
                    stack.pop();
                    if (stack.isEmpty()) { // )()()(), start=1
                        max = Math.max(i - start + 1, max);
                    } else { // )(()()(), peek()=1
                        max = Math.max(i - stack.peek(), max);
                    }
                }
            }
        }
        return max;
    }

    /**
     * 40. Combination Sum II
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        Arrays.sort(candidates);
        helper(candidates, target, 0, new ArrayList<Integer>(), res);
        return res;
    }

    private void helper(int[] c, int target, int start, List<Integer> item, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<Integer>(item));
            return;
        }
        for (int i = start; i < c.length; i++) {
            if (c[i] > target) {
                break;
            }
            // [1,1,1,5], 7
            if (i > start && c[i] == c[i - 1]) {
                continue;
            }
            item.add(c[i]);
            helper(c, target - c[i], i + 1, item, res);
            item.remove(item.size() - 1);
        }
    }

    /**
     * 45. Jump Game II
     */
    int jump(int A[]) {
        if (A.length == 1) {
            return 0;
        }
        int left = 0, right = 0, step = 0;
        // step++: calculate max(right) each time. all positions within can be reached
        while (left <= right) {
            step++;
            int last = right;
            for (int i = left; i <= last; i++) {
                right = Math.max(right, A[i] + i);
                if (right >= A.length - 1) {
                    return step;
                }
            }
            left = last + 1;
        }
        return -1;
    }

    /**
     * 46. Permutations DFS
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        helper46(nums, new boolean[nums.length], new ArrayList<Integer>(), res);
        return res;
    }

    private void helper46(int[] nums, boolean[] used, List<Integer> item, List<List<Integer>> res) {
        if (item.size() == nums.length) {
            res.add(new ArrayList<Integer>(item));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (!used[i]) {
                used[i] = true;
                item.add(nums[i]);
                helper46(nums, used, item, res);
                item.remove(item.size() - 1);
                used[i] = false;
            }
        }
    }

    public List<List<Integer>> permute2(int[] num) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (num == null || num.length == 0) {
            return res;
        }
        List<Integer> first = new ArrayList<Integer>();
        first.add(num[0]);
        res.add(first);
        // newRes: add one number to every position of every list {{1}} -> {{1, 2}, {2, 1}}
        // -> {{3, 1, 2}, {1, 3, 2}, {1, 2, 3}, ...}
        for (int i = 1; i < num.length; i++) {
            List<List<Integer>> newRes = new ArrayList<List<Integer>>();
            for (int j = 0; j < res.size(); j++) {
                List<Integer> cur = res.get(j);
                for (int k = 0; k < cur.size() + 1; k++) {
                    ArrayList<Integer> item = new ArrayList<Integer>(cur);
                    item.add(k, num[i]);
                    newRes.add(item);
                }
            }
            res = newRes;
        }
        return res;
    }

    /**
     * 47. Permutations II
     */

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        Arrays.sort(nums);
        helper47(nums, new boolean[nums.length], new ArrayList<Integer>(), res);
        return res;
    }

    private void helper47(int[] nums, boolean[] used, List<Integer> item, List<List<Integer>> res) {
        if (item.size() == nums.length) {
            res.add(new ArrayList<Integer>(item));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // 1, 2, 2, 3 -> skip second 2
            if (i > 0 && nums[i - 1] == nums[i] && !used[i - 1]) // !-> 40. if(i > start && c[i] == c[i-1]) {
                continue;
            if (!used[i]) {
                used[i] = true;
                item.add(nums[i]);
                helper47(nums, used, item, res);
                item.remove(item.size() - 1);
                used[i] = false;
            }
        }
    }

    /**
     * 53. Maximum Subarray
     */
    public int maxSubArray(int[] nums) {
        int right = 0, cur = 0, max = nums[0];
        while (right < nums.length) {
            cur += nums[right];
            max = Math.max(cur, max);
            // no need to track left
            if (cur < 0) {
                cur = 0;
            }
            right++;
        }
        return max;
    }

    /**
     * 60. Permutation Sequence
     */
    public String getPermutation(int n, int k) {
        StringBuilder sb = new StringBuilder();
        List<Integer> num = new ArrayList<Integer>();
        int fact = 1;
        for (int i = 1; i <= n; i++) {
            num.add(i);
            fact *= i;
        }

        // n = 4, k = 23
        // fact = 6, index = 3, cnt = 22 - 18 = 4
        // fact = 2, index = 2, cnt = 0
        // fact = 1, index = 0, cnt = 0
        // fact = 1, index = 0, cnt = 0
        int cnt = k - 1;
        for (int i = 0; i < n; i++) {
            fact /= (n - i);
            int index = (cnt / fact);
            sb.append(num.remove(index));
            cnt -= index * fact;
        }
        return sb.toString();
    }

    public int climbStairs(int n) {
        int a = 1, b = 1;
        while (n-- > 1) {
            int tmp = b;
            b = a + b;
            a = tmp;
        }
        return b;
    }

    /**
     * 63. Unique Paths II
     */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {

        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        if (m == 0 || n == 0) {
            return 0;
        }
        // handle i=0, j=0
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1)
                    obstacleGrid[i][j] = 0;
                else if (i == 0 && j == 0)
                    obstacleGrid[i][j] = 1;
                else if (i == 0)
                    obstacleGrid[i][j] = obstacleGrid[i][j - 1];
                else if (j == 0)
                    obstacleGrid[i][j] = obstacleGrid[i - 1][j];
                else
                    obstacleGrid[i][j] = obstacleGrid[i - 1][j] + obstacleGrid[i][j - 1];
            }
        }
        return obstacleGrid[m - 1][n - 1];
    }

    /**
     * 77. Combinations
     */
    // Given two integers n and k, return all possible combinations of k numbers out of 1 ... n.
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> res = new ArrayList<List<Integer>>();
        fn77(1, k, n, new ArrayList<Integer>(), res);
        return res;
    }

    void fn77(int start, int k, int n, ArrayList<Integer> item, List<List<Integer>> res) {
        if (k == 0) {
            res.add(new ArrayList<Integer>(item));
            return;
        }
        for (int i = start; i <= n - k + 1; i++) {
            item.add(i);
            fn77(i + 1, k - 1, n, item, res);
            // fn77(i, k - 1, n, item, res); -> dice combination
            item.remove(item.size() - 1);
        }
    }

    @Test
    public void test() {
        getPermutation(4, 23);
    }

    @SuppressWarnings("unused")
    @Test
    public void test0() {
        int[] A = { 1, 2, 3, 4 };
        // List<List<Integer>> res = permute(A);
        combine(6, 2);
    }
}
