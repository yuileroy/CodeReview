package leetcodelock;

import java.util.LinkedList;
import java.util.List;

public class Solution340 {

    // 340. Longest Substring with At Most K Distinct Characters
    // Given a string, find the length of the longest substring T that contains at most k distinct characters.
    // Input: s = "eceba", k = 2
    // Output: 3
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.isEmpty() || k == 0) {
            return 0;
        }
        int[] arr = new int[256];
        int len = Integer.MIN_VALUE;

        int start = 0, end = -1;
        int count = 0;
        while (++end < s.length()) {
            if (arr[s.charAt(end)]++ == 0) {
                count++;
            }
            if (count > k) {
                while (--arr[s.charAt(start++)] > 0)
                    ;
                count--;
            }
            len = Math.max(len, end - start + 1);
        }
        return len;
    }
}

class MovingAverage {

    /** Initialize your data structure here. */
    List<Integer> list;
    double sum = 0;
    int size;

    public MovingAverage(int size) {
        list = new LinkedList<>();
        this.size = size;
    }

    public double next(int val) {
        list.add(val);
        sum += val;
        if (list.size() > size) {
            sum -= list.remove(0);
        }
        return sum / list.size();
    }
}
