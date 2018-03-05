package keycode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution100 {
    /**
     * 128. Longest Consecutive Sequence
     * 
     * @deprecated
     */
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        HashSet<Integer> set = new HashSet<>();
        for (int val : nums) {
            set.add(val);
        }
        int max = 1;
        for (int val : nums) {
            int left = val - 1, right = val + 1, count = 1;

            set.remove(val);
            while (set.contains(left)) {
                count++;
                set.remove(left);
                left--;
            }
            while (set.contains(right)) {
                count++;
                set.remove(right);
                right++;
            }
            max = Math.max(count, max);
        }
        return max;
    }

    /**
     * 139. Word Break
     */
    public boolean wordBreak139(String s, List<String> wordDict) {
        if (s == null || s.length() == 0) {
            return true;
        }
        Set<String> set = new HashSet<>(wordDict);
        boolean[] res = new boolean[s.length() + 1];
        res[0] = true;
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j <= i; j++) {
                if (res[j] && set.contains(s.substring(j, i + 1))) {
                    res[i + 1] = true;
                    break;
                }
            }
        }
        return res[s.length()];
    }

    /**
     * 140. Word Break II
     */
    public List<String> wordBreak(String s, List<String> wordDict) {
        return dfs140(s, wordDict, new HashMap<String, List<String>>());
    }

    // dfs function returns an array including all substrings derived from s.
    List<String> dfs140(String s, List<String> wordDict, Map<String, List<String>> map) {
        if (map.containsKey(s)) {
            return map.get(s);
        }

        List<String> res = new ArrayList<String>();
        if ("".equals(s)) {
            res.add("");
            return res;
        }
        for (String word : wordDict) {
            if (s.startsWith(word)) {
                List<String> sublist = dfs140(s.substring(word.length()), wordDict, map);
                for (String sub : sublist) {
                    res.add("".equals(sub) ? word : word + " " + sub);
                }
            }
        }
        map.put(s, res);
        return res;
    }
}
