package leetcodelock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//243. Shortest Word Distance
// Given a list of words and two words word1 and word2, return the shortest distance between these two words in the list.
public class Solution243 {
    public int shortestDistance(String[] words, String word1, String word2) {
        int p1 = -1, p2 = -1, res = Integer.MAX_VALUE;
        for (int i = 0; i < words.length; i++) {
            if (word1.equals(words[i])) {
                p1 = i;
                if (p1 != -1 && p2 != -1) {
                    res = Math.min(res, p1 - p2);
                }
            }
            if (word2.equals(words[i])) {
                p2 = i;
                if (p1 != -1 && p2 != -1) {
                    res = Math.min(res, p2 - p1);
                }
            }
        }
        return res;
    }

    // 245. Shortest Word Distance III
    public int shortestWordDistance(String[] words, String word1, String word2) {
        int p1 = -1, p2 = -1, res = Integer.MAX_VALUE;
        for (int i = 0; i < words.length; i++) {
            if (word1.equals(words[i])) {
                if (word1.equals(word2)) {
                    if (p1 != -1) {
                        res = Math.min(res, i - p1);
                    }
                    p1 = i;
                } else {
                    p1 = i;
                    if (p1 != -1 && p2 != -1) {
                        res = Math.min(res, p1 - p2);
                    }
                }
            } else if (word2.equals(words[i])) {
                p2 = i;
                if (p1 != -1 && p2 != -1) {
                    res = Math.min(res, p2 - p1);
                }
            }
        }
        return res;
    }
}

// 244. Shortest Word Distance II
// Your method will be called repeatedly many times with different parameters.
class WordDistance {

    HashMap<String, List<Integer>> map;

    public WordDistance(String[] words) {
        map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (!map.containsKey(words[i])) {
                map.put(words[i], new ArrayList<Integer>());
            }
            map.get(words[i]).add(i);
        }
    }

    public int shortest(String word1, String word2) {
        List<Integer> loc1, loc2;
        loc1 = map.get(word1);
        loc2 = map.get(word2);

        int l1 = 0, l2 = 0, res = Integer.MAX_VALUE;
        while (l1 < loc1.size() && l2 < loc2.size()) {
            res = Math.min(res, Math.abs(loc1.get(l1) - loc2.get(l2)));
            if (loc1.get(l1) < loc2.get(l2)) {
                l1++;
            } else {
                l2++;
            }
        }
        return res;
    }
}
