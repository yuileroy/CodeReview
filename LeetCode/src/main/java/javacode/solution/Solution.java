package javacode.solution;

import java.util.LinkedList;
import java.util.List;

class Solution {
    int diff = 0x3f3f3f3f;
    String result = "";
    int h;
    int m;

    public String nextClosestTime(String time) {
        int[] digit = new int[4];
        int tot = 0;
        String[] val = time.split(":");
        int hour = Integer.parseInt(val[0]);
        int minu = Integer.parseInt(val[1]);
        digit[tot++] = hour / 10;
        digit[tot++] = hour % 10;
        digit[tot++] = minu / 10;
        digit[tot++] = minu % 10;

        h = hour;
        m = minu;

        dfs(digit, 0, new int[4]);

        return result;
    }

    void dfs(int[] digit, int i, int[] ans) {
        if (i == 4) {
            int hour = 10 * ans[0] + ans[1];
            int minu = 10 * ans[2] + ans[3];
            int df = diff(hour, minu);
            if (df < diff) {
                diff = df;
                result = valid(hour) + ":" + valid(minu);
            }
        } else {
            for (int j = 0; j < 4; ++j) {
                ans[i] = digit[j];
                if (i == 1) {
                    int hour = 10 * ans[0] + ans[1];
                    if (hour >= 0 && hour <= 23)
                        dfs(digit, i + 1, ans);
                } else if (i == 3) {
                    int minu = 10 * ans[2] + ans[3];
                    if (minu >= 0 && minu <= 59)
                        dfs(digit, i + 1, ans);
                } else {
                    dfs(digit, i + 1, ans);
                }
            }
        }
    }

    int diff(int hour, int minu) {
        int c2o = 60 * 60 - h * 60 - m;
        int n2o = 60 * 60 - hour * 60 - minu;
        return n2o < c2o ? c2o - n2o : c2o - n2o + 3600;
    }

    public String valid(int time) {
        if (time >= 0 && time <= 9)
            return "0" + time;
        else
            return time + "";
    }

    public int maxSubArray(int[] nums) {
        int right = 0, cur = 0, max = Integer.MIN_VALUE;
        while (right++ < nums.length) {
            cur += nums[right];
            max = Math.max(cur, max);
            if (cur < 0) {
                cur = 0;
            }
        }
        return max;
    }

    // Decodes a single string to a list of strings.
    public List<String> decode(String s) {
        List<String> res = new LinkedList<String>();
        int start = 0;
        while (start < s.length()) {
            if (s.charAt(start) == '#') {
                res.add(null);
                start += 2;
                continue;
            }
            int idx = s.indexOf('#', start);
            int size = Integer.parseInt(s.substring(start, idx));
            res.add(s.substring(idx + 1, idx + size + 1));
            start = idx + size + 1;
        }
        return res;
    }
}
