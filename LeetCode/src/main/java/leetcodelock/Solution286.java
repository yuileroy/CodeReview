package leetcodelock;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 286. Walls and Gates
// -1 - A wall or an obstacle.
// 0 - A gate.
public class Solution286 {
    // DFS
    public void wallsAndGates(int[][] rooms) {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) {
                    dfs(rooms, i, j, 0);
                }
            }
        }
    }

    private void dfs(int[][] rooms, int x, int y, int d) {
        if (x < 0 || x >= rooms.length || y < 0 || y >= rooms[0].length || rooms[x][y] < d) {
            // -1, 0, or less than d
            return;
        }
        rooms[x][y] = d;
        dfs(rooms, x + 1, y, d + 1);
        dfs(rooms, x - 1, y, d + 1);
        dfs(rooms, x, y + 1, d + 1);
        dfs(rooms, x, y - 1, d + 1);
    }

    // BFS
    public void wallsAndGates2(int[][] rooms) {
        if (rooms.length == 0 || rooms[0].length == 0) {
            return;
        }
        Deque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) {
                    queue.add(new int[] { i, j });
                }
            }
        }
        bfs(rooms, queue);
    }

    void bfs(int[][] rooms, Deque<int[]> queue) {
        int[][] B = new int[][] { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
        int step = 0;
        while (!queue.isEmpty()) {
            step++;
            int size = queue.size();
            while (size-- > 0) {
                int[] cur = queue.removeFirst();
                for (int[] b : B) {
                    int x = cur[0] + b[0], y = cur[1] + b[1];
                    if (x >= 0 && x < rooms.length && y >= 0 && y < rooms[0].length
                            && rooms[x][y] == Integer.MAX_VALUE) {
                        rooms[x][y] = step;
                        queue.addLast(new int[] { x, y });
                    }
                }
            }
        }
    }

    // 288. Unique Word Abbreviation
    class ValidWordAbbr {
        Map<String, String> map = new HashMap<>();

        public ValidWordAbbr(String[] dictionary) {
            for (String str : dictionary) {
                String key = getKey(str);
                if (map.containsKey(key)) {
                    if (!map.get(key).equals(str)) {
                        // mark as invalid
                        map.put(key, "");
                    }
                } else {
                    map.put(key, str);
                }
            }
        }

        public boolean isUnique(String word) {
            String key = getKey(word);
            return !map.containsKey(key) || map.get(key).equals(word);
        }

        private String getKey(String str) {
            if (str.length() <= 2) {
                return str;
            }
            return str.charAt(0) + Integer.toString(str.length() - 2) + str.charAt(str.length() - 1);
        }
    }

    // 290. Word Pattern
    public boolean wordPattern(String pattern, String str) {
        String[] strs = str.split(" ");
        if (pattern.length() != strs.length) {
            return false;
        }
        Map<Character, String> map = new HashMap<>();
        // use Set to check one-to-one mapping
        Set<String> set = new HashSet<>();

        for (int i = 0; i < pattern.length(); i++) {
            char x = pattern.charAt(i);
            if (!map.containsKey(x)) {
                if (!set.add(strs[i])) {
                    return false;
                }
                map.put(x, strs[i]);
            } else {
                if (!strs[i].equals(map.get(x))) {
                    return false;
                }
            }
        }
        return true;
    }

    // 291. Word Pattern II
    // Given a pattern and a string str, find if str follows the same pattern.
    // Input: pattern = "abab", str = "redblueredblue"
    // Output: true
    class Solution291 {
        public boolean wordPatternMatch(String pattern, String str) {
            HashMap<Character, String> map = new HashMap<>();
            return dfs(map, pattern, str, 0, 0);
        }

        public boolean dfs(HashMap<Character, String> map, String pattern, String str, int pidx, int sidx) {
            int n = pattern.length();
            int m = str.length();
            if (pidx == n && sidx == m)
                return true;
            if (pidx == n || sidx == m)
                return false;
            boolean res = false;

            for (int i = sidx; i < str.length(); i++) {
                String cur = str.substring(sidx, i + 1);
                char c = pattern.charAt(pidx);
                if (m - i + 1 < n - pidx + 1)
                    continue; // the number of str character left should more than the number of pattern left
                if (map.containsKey(c)) {
                    if (!map.get(c).equals(cur))
                        continue;
                    res |= dfs(map, pattern, str, pidx + 1, i + 1);
                } else {
                    if (map.containsValue(cur))
                        continue;
                    map.put(c, cur);
                    res |= dfs(map, pattern, str, pidx + 1, i + 1);
                    map.remove(c);
                }
            }
            return res;
        }
    }
}
