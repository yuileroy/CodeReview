package leetcodelock;

import java.util.ArrayList;
import java.util.List;

// 159. Longest Substring with At Most Two Distinct Characters
// Given a string s , find the length of the longest substring t  that contains at most 2 distinct characters.
public class Solution159 {
    public int lengthOfLongestSubstringTwoDistinct(String s) {
        if (s == null || s.isEmpty()) {
            return 0;
        }
        int[] A = new int[256];
        int len = Integer.MIN_VALUE;

        int start = 0, end = -1, count = 0;
        while (++end < s.length()) {
            if (A[s.charAt(end)]++ == 0) {
                count++;
            }
            if (count > 2) {
                // while (--A[s.charAt(start++)] > 0);
                // end of while
                while (--A[s.charAt(start)] > 0) {
                    start++;
                }
                start++;
                count--;
            }
            len = Math.max(len, end - start + 1);
        }
        return len;
    }
}

// 161. One Edit Distance
// Given two strings s and t, determine if they are both one edit distance apart.
class Solution161 {
    public boolean isOneEditDistance(String s, String t) {
        if (s.length() > t.length()) {
            return isOneEditDistance(t, s);
        }
        int len1 = s.length(), len2 = t.length();
        // exclude equals
        if (len2 - len1 > 1 || s.equals(t)) {
            return false;
        }

        int i = 0;
        while (i < len1 && s.charAt(i) == t.charAt(i)) {
            i++;
        }
        if (len1 != len2) {
            return s.substring(i).equals(t.substring(i + 1));
        } else {
            return s.substring(i + 1).equals(t.substring(i + 1));
        }
    }
}

// 163. Missing Ranges
// Given a sorted integer array nums, where the range of elements are in the inclusive range [lower, upper], return its
// missing ranges.

// Example:

// Input: nums = [0, 1, 3, 50, 75], lower = 0 and upper = 99,
// Output: ["2", "4->49", "51->74", "76->99"]
class Solution163 {
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

    // Input:[1,1,1] 1 1
    // Output:
    // ["2->0","2->0"]
    // Expected:
    // []
    public List<String> findMissingRanges2Error(int[] nums, int lower, int upper) {
        List<String> res = new ArrayList<>();
        int cur = lower;

        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == cur) {
                cur++;
                continue;
            }
            res.add(getRange(cur, nums[i] - 1));
            cur = nums[i] + 1;
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
        return (n1 == n2) ? String.valueOf(n1) : String.format("%d->%d", n1, n2);
    }
}