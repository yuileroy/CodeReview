package keycode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            // !wrong
            // if (A[c] != 0) {
            // start = A[c];
            // }
            res = Math.max(res, i - start + 1);
            // !-> (last index of char) + 1, so start=2 when i=3
            index[s.charAt(i)] = i + 1;
        }
        return res;
    }

    /**
     * 4. Median of Two Sorted Arrays
     */
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int mid = (m + n) / 2;
        if ((m + n) % 2 == 0) {
            return 0.5 * (kth(nums1, m, nums2, n, mid) + kth(nums1, m, nums2, n, mid + 1));
        }
        return (double) kth(nums1, m, nums2, n, mid + 1);
    }

    int kth(int arr1[], int m, int arr2[], int n, int k) {
        if (m > n)
            return kth(arr2, n, arr1, m, k);
        if (m == 0)
            return arr2[k - 1];
        if (k == 1)
            return Math.min(arr1[0], arr2[0]);

        // divide and conquer, n >= m >= 1, k >= 2
        int i = Math.min(m, k / 2);
        int j = Math.min(n, k / 2);
        // Now we need to find only k-j th element
        // since we have found out the lowest j
        if (arr1[i - 1] > arr2[j - 1]) {
            int temp[] = Arrays.copyOfRange(arr2, j, n);
            return kth(arr1, m, temp, n - j, k - j);
        }

        // Now we need to find only k-i th element
        // since we have found out the lowest i
        int temp[] = Arrays.copyOfRange(arr1, i, m);
        return kth(temp, m - i, arr2, n, k - i);
    }

    /**
     * 5. Longest Palindromic Substring
     */
    int start = 0, max = 0;

    public String longestPalindrome(String s) {
        if (s == null || s.length() < 2) {
            return s;
        }
        for (int i = 0; i < s.length() - 1; i++) {
            palindrome(s, i, i);
            palindrome(s, i, i + 1);
        }
        return s.substring(start, start + max);
    }

    void palindrome(String s, int i, int j) {
        while (i >= 0 && j < s.length() && s.charAt(i) == s.charAt(j)) {
            i--;
            j++;
        }
        int len = j - i - 1;
        if (len > max) {
            max = len;
            start = i + 1;
        }
    }

    String longestPalindrome2(String s) {
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
     * 8. String to Integer (atoi)
     * 
     * @category long - int cast
     */
    public int myAtoi(String str) {
        String s = str.trim();
        if (s.isEmpty()) {
            return 0;
        }
        int i = 0;
        long val = 0;
        boolean pos = true;
        if (s.charAt(0) == '+') {
            pos = true;
            i++;
        } else if (s.charAt(0) == '-') {
            pos = false;
            i++;
        }
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            val = val * 10 + s.charAt(i) - '0';
            i++; // ! forgot this line
            if (val > (long) Integer.MAX_VALUE + 1) {
                break; // Long.MAX_VALUE may also overflow
            }
        }
        if (!pos) {
            val = -val;
        }
        if (val > Integer.MAX_VALUE) {
            val = Integer.MAX_VALUE;
        }
        if (val < Integer.MIN_VALUE) {
            val = Integer.MIN_VALUE;
        }
        return (int) val; // ! cast
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
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        if (nums.length < 3) {
            return res;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0)
                break;
            if (i > 0 && nums[i] == nums[i - 1])
                continue;
            int sum = 0 - nums[i];
            int l = i + 1, r = nums.length - 1;
            while (l < r) {
                // wrong, [-4, 2, 2, 3]
                // while (l + 1 < r && nums[l + 1] == nums[l]) l++;
                if (nums[l] + nums[r] == sum) {
                    res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                    l++;
                    r--;
                    while (l < r && nums[l] == nums[l - 1]) {
                        l++;
                    }
                } else if (nums[l] + nums[r] < sum) {
                    l++;
                } else {
                    r--;
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
        for (int i = 0; i < nums.length - 2; i++) {
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
     * 18. 4Sum
     */
    public List<List<Integer>> fourSum(int[] num, int target) {
        Arrays.sort(num);
        List<List<Integer>> result = new ArrayList<>();

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
        int cur = 0;
        while (cur <= last) {
            if (nums[cur] == val) {
                nums[cur] = nums[last];
                last--;
            } else {
                cur++;
            }
        }
        return last + 1;
    }

    /**
     * 29. Divide Two Integers
     */
    public int divide(int dividend, int divisor) {
        if (divisor == 0 || (dividend == Integer.MIN_VALUE && divisor == -1)) {
            return Integer.MAX_VALUE;
        }
        boolean neg = (dividend < 0) ^ (divisor < 0);
        long a = Math.abs((long) dividend);
        long b = Math.abs((long) divisor);
        if (dividend == 0 || a < b) {
            return 0;
        }
        int res = 0;
        while (a >= b) {
            long tmp = b;
            long multiplier = 1;
            while ((tmp << 1) <= a) {
                tmp <<= 1;
                multiplier <<= 1;
            }
            a -= tmp;
            res += multiplier;
        }
        return neg ? -res : res;
    }

    /**
     * 30. Substring with Concatenation of All Words
     */
    public List<Integer> findSubstring(String S, String[] L) {
        List<Integer> res = new ArrayList<>();
        if (S == null || L == null || S.length() == 0 || L.length == 0)
            return res;
        int wordLen = L[0].length();

        Map<String, Integer> dict = new HashMap<>();
        for (String word : L) {
            dict.put(word, dict.getOrDefault(word, 0) + 1);
        }

        for (int i = 0; i < wordLen; i++) {
            int count = 0;
            int start = i;
            Map<String, Integer> curdict = new HashMap<>();
            // till the first letter of last word
            for (int j = i; j <= S.length() - wordLen; j += wordLen) {
                String curWord = S.substring(j, j + wordLen);
                // check each word to tell if it existes in give dictionary
                if (!dict.containsKey(curWord)) {
                    curdict.clear();
                    count = 0;
                    start = j + wordLen;
                    continue;
                }
                // form current dictionary
                curdict.put(curWord, curdict.getOrDefault(curWord, 0) + 1);
                if (curdict.get(curWord) <= dict.get(curWord)) {
                    count++;
                } else {
                    while (curdict.get(curWord) > dict.get(curWord)) {
                        String tmp = S.substring(start, start + wordLen);
                        curdict.put(tmp, curdict.get(tmp) - 1);
                        if (curdict.get(tmp) < dict.get(tmp)) {
                            count--;
                        }
                        start += wordLen;
                    }
                }

                // put into res and move index point to nextword
                // and update current dictionary as well as count
                if (count == L.length) {
                    res.add(start);
                    String tmp = S.substring(start, start + wordLen);
                    curdict.put(tmp, curdict.get(tmp) - 1);
                    start = start + wordLen;
                    count--;
                }
            }
        }
        return res;
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
        int start = 0, res = 0;
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                if (stack.isEmpty()) {
                    start = i + 1;
                    continue;
                }
                stack.pop();
                if (stack.isEmpty()) {
                    res = Math.max(res, i - start + 1);
                } else {
                    // "(()()"
                    res = Math.max(res, i - stack.peek());
                }
            }
        }
        return res;
    }

    // V2 tricky
    public int longestValidParentheses2(String s) {
        int maxans = 0;
        int dp[] = new int[s.length()];
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    dp[i] = (i >= 2 ? dp[i - 2] : 0) + 2;
                } else if (i - dp[i - 1] > 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    // "))" -> ( + dp[i-1] + )
                    dp[i] = dp[i - 1] + 2;
                    if (i - dp[i - 1] >= 2) {
                        // ()( + dp[i-1] + )
                        dp[i] += dp[i - dp[i - 1] - 2];
                    }
                }
                maxans = Math.max(maxans, dp[i]);
            }
        }
        return maxans;
    }

    // V3 good
    public int longestValidParenthesesV3(String s) {
        int left = 0, right = 0, maxlength = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            if (left == right) {
                maxlength = Math.max(maxlength, 2 * right);
            } else if (right > left) {
                left = right = 0;
            }
        }
        // "(()"
        left = right = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }
            if (left == right) {
                maxlength = Math.max(maxlength, 2 * left);
            } else if (left > right) {
                left = right = 0;
            }
        }
        return maxlength;
    }

    /**
     * 33. Search in Rotated Sorted Array
     * 
     * @category Binary Search
     */
    public int search33(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int start = 0, end = nums.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (target == nums[mid]) {
                return mid;
            }
            if (nums[start] <= nums[mid]) { // left sorted
                if (nums[start] <= target && target < nums[mid]) {
                    end = mid;
                } else {
                    start = mid + 1;
                }
            } else { // right sorted
                if (nums[mid] < target && target <= nums[end]) {
                    start = mid + 1;
                } else {
                    end = mid;
                }
            }
        }
        if (target == nums[start]) {
            return start;
        }
        return -1;
    }

    /**
     * 34. Find First and Last Position of Element in Sorted Array
     * 
     * @category Binary Search
     */
    public int[] searchRange(int[] nums, int target) {
        int[] res = { -1, -1 };
        if (nums == null || nums.length == 0) {
            return res;
        }
        // find first match
        int start = 0, end = nums.length - 1;
        while (start < end) {
            int mid = start + (end - start) / 2;
            if (nums[mid] < target) {
                start = mid + 1;
            } else {
                end = mid;
            }
        }
        if (nums[start] != target) {
            return res;
        }
        res[0] = start;
        // find last match
        end = nums.length - 1;
        while (start < end) {
            int mid = end - (end - start) / 2;
            if (nums[mid] > target) {
                end = mid - 1;
            } else {
                start = mid;
            }
        }
        res[1] = start;
        return res;
    }

    /**
     * 35. Search Insert Position
     */
    public int searchInsert(int[] nums, int target) {
        if (nums.length == 0) {
            return 0;
        }
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int mid = l + (r - l) / 2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        // ! if (l == nums.length && nums[l - 1] < target)
        if (l == nums.length - 1 && nums[l] < target) {
            return l + 1;
        }
        return l;
    }

    /**
     * 40. Combination Sum II
     */
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(candidates);
        helper(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private void helper(int[] c, int target, int start, List<Integer> item, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(item));
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

    // boolean version, @See 416. Partition Equal Subset Sum
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
     * 42. Trapping Rain Water
     */
    public int trap(int[] height) {
        if (height.length < 3) {
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        int res = 0;
        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
                int t = stack.pop();
                if (stack.isEmpty()) {
                    break;
                }
                int dis = i - stack.peek() - 1;
                int h = Math.min(height[i], height[stack.peek()]);
                res += dis * (h - height[t]);
            }
            stack.push(i);
        }
        return res;
    }

    // V2
    public int trap2(int[] height) {
        if (height.length < 3) {
            return 0;
        }
        int max = 0;
        for (int i = 0; i < height.length; i++) {
            if (height[i] > height[max]) {
                max = i;
            }
        }
        int water = 0, peak = 0;
        for (int i = 0; i < max; i++) {
            if (height[i] > peak) {
                peak = height[i];
            } else {
                water += peak - height[i];
            }
        }
        peak = 0;
        for (int i = height.length - 1; i > max; i--) {
            if (height[i] > peak) {
                peak = height[i];
            } else {
                water += peak - height[i];
            }
        }
        return water;
    }

    /**
     * 43. Multiply Strings
     */
    String multiply(String num1, String num2) {
        num1 = new StringBuilder(num1).reverse().toString();
        num2 = new StringBuilder(num2).reverse().toString();
        int[] d = new int[num1.length() + num2.length()];
        for (int i = 0; i < num1.length(); i++) {
            int a = num1.charAt(i) - '0';
            for (int j = 0; j < num2.length(); j++) {
                int b = num2.charAt(j) - '0';
                d[i + j] += a * b;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < d.length; i++) {
            int digit = d[i] % 10;
            int carry = d[i] / 10;
            sb.insert(0, digit);
            if (i + 1 < d.length) {
                d[i + 1] += carry;
            }
        }
        // trim starting zeros
        // 2 * 3 = 06, 56 * 0 = 000
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    /**
     * 45. Jump Game II
     */
    int jump(int A[]) {
        if (A.length == 1) {
            return 0;
        }
        int start = 0, right = 0, step = 0;
        // step++: calculate max(right) each time. all positions within can be reached
        while (start <= right) {
            step++;
            int end = right;
            for (int i = start; i <= end; i++) {
                right = Math.max(right, A[i] + i);
                if (right >= A.length - 1) {
                    return step;
                }
            }
            start = end + 1;
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
        List<List<Integer>> res = new ArrayList<>();
        if (num == null || num.length == 0) {
            return res;
        }
        List<Integer> first = new ArrayList<>();
        first.add(num[0]);
        res.add(first);
        // newRes: add one number to every position of every list {{1}} -> {{1, 2}, {2, 1}}
        // -> {{3, 1, 2}, {1, 3, 2}, {1, 2, 3}, ...}
        for (int i = 1; i < num.length; i++) {
            List<List<Integer>> newRes = new ArrayList<>();
            for (int j = 0; j < res.size(); j++) {
                List<Integer> cur = res.get(j);
                for (int k = 0; k < cur.size() + 1; k++) {
                    List<Integer> item = new ArrayList<>(cur);
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
        List<List<Integer>> res = new ArrayList<>();
        if (nums == null || nums.length == 0) {
            return res;
        }
        Arrays.sort(nums);
        helper47(nums, new boolean[nums.length], new ArrayList<>(), res);
        return res;
    }

    private void helper47(int[] nums, boolean[] used, List<Integer> item, List<List<Integer>> res) {
        if (item.size() == nums.length) {
            res.add(new ArrayList<>(item));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            // 1, 2, 2, 3 -> !used[i - 1] to keep the order
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }
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

    /**
     * 79. Word Search
     */
    public boolean exist(char[][] board, String word) {
        if (board == null || board.length == 0) {
            return false;
        }
        if (word.length() == 0) {
            return true;
        }
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (find(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean find(char[][] board, String word, int i, int j, int k) {
        if (k == word.length()) {
            return true;
        }
        if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word.charAt(k)) {
            return false;
        }
        // no need to use visited[]
        board[i][j] = '#';
        boolean res = find(board, word, i, j - 1, k + 1) || find(board, word, i, j + 1, k + 1)
                || find(board, word, i - 1, j, k + 1) || find(board, word, i + 1, j, k + 1);
        board[i][j] = word.charAt(k);
        return res;
    }

    /**
     * 81. Search in Rotated Sorted Array II
     */
    public boolean search(int[] A, int target) {
        int left = 0;
        int right = A.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (A[mid] == target) {
                return true;
            }

            // left sorted
            if (A[left] < A[mid] || A[right] < A[mid]) {
                if (A[left] <= target && target < A[mid]) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
                // right sorted
            } else if (A[left] > A[mid] || A[right] > A[mid]) {
                if (A[mid] < target && target <= A[right]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
                // left == mid == right
            } else {
                left++;
                right--;
            }
        }
        return false;
    }

    /**
     * 84. Largest Rectangle in Histogram
     */
    public int largestRectangleArea(int[] heights) {

        Stack<Integer> stack = new Stack<>();
        int maxArea = 0, i = 0;
        while (i < heights.length) {
            if (stack.isEmpty() || heights[i] >= heights[stack.peek()]) {
                stack.push(i++);
            } else {
                // i -> right bound, stack.peek() -> left bound, both lower than h[cur]
                int cur = stack.pop();
                int left = stack.isEmpty() ? -1 : stack.peek();
                maxArea = Math.max(maxArea, heights[cur] * (i - left - 1));
            }
        }
        while (!stack.isEmpty()) {
            int cur = stack.pop();
            int left = stack.isEmpty() ? -1 : stack.peek();
            maxArea = Math.max(maxArea, heights[cur] * (i - left - 1));
        }
        return maxArea;
    }

    // V2
    public int largestRectangleArea2(int[] heights) {
        // add zero to both side
        int[] h = new int[heights.length + 2];
        for (int i = 0; i < heights.length; i++) {
            h[i + 1] = heights[i];
        }
        int maxArea = 0, i = 1;
        Stack<Integer> idx = new Stack<>();
        idx.add(0);
        while (i < h.length) {
            if (h[idx.peek()] <= h[i]) {
                idx.push(i++);
            } else {
                // i -> right bound, stack.peek() -> left bound, both lower than h[t]
                int t = idx.pop();
                maxArea = Math.max(maxArea, h[t] * (i - idx.peek() - 1));
            }
        }
        return maxArea;
    }

    /**
     * 91. Decode Ways
     */
    public int numDecodings(String s) {
        if (s == null || s.isEmpty() || s.charAt(0) == '0') {
            return 0;
        }
        int a = 1, b = 1, cur = 1;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (s.charAt(i - 1) != '1' && s.charAt(i - 1) != '2') {
                    return 0;
                }
                cur = a;
            } else {
                cur = b;
                if (s.charAt(i - 1) != '0' && Integer.parseInt(s.substring(i - 1, i + 1)) <= 26) {
                    cur += a;
                }
            }
            a = b;
            b = cur;
        }
        return b;
    }

    @Test
    public void test() {
        largestRectangleArea(new int[] { 2, 1, 5, 6, 2, 3 });
    }

    @SuppressWarnings("unused")
    // @Test
    public void test0() {
        int[] A = {};
        int[] B = { 2 };
        // List<List<Integer>> res = permute(A);
        // combine(6, 2);
        System.out.println(kth(A, A.length, B, B.length, 1));

    }
}
