package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

public class Solution {

    public boolean isIdealPermutation(int[] A) {
        Stack<Integer> s = new Stack<>();
        int max1 = A[0], max2 = -1;
        for (int i = 1; i < A.length; i++) {
            if (A[i] > max1 && max2 == -1) {
                max2 = A[i];
            } else if (A[i] > max2 && max2 != -1) {
                max1 = max2;
                max2 = A[i];
            } else {
                if (A[i] < s.peek()) {
                    // {1, 2, 0}
                    int tmp = s.pop();
                    if (!s.isEmpty() && A[i] < s.peek()) {
                        return false;
                    }
                    s.push(tmp);
                    // {2, 0, 1}
                    if (A[i] < s.peek() && A[i - 1] != s.peek()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public boolean isIdealPermutation0(int[] A) {
        Stack<Integer> s = new Stack<>();
        s.push(A[0]);
        for (int i = 1; i < A.length; i++) {
            if (A[i] > s.peek()) {
                s.push(A[i]);
            } else {
                if (A[i] < s.peek()) {
                    // {1, 2, 0}
                    int tmp = s.pop();
                    if (!s.isEmpty() && A[i] < s.peek()) {
                        return false;
                    }
                    s.push(tmp);
                    // {2, 0, 1}
                    if (A[i] < s.peek() && A[i - 1] != s.peek()) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public int openLock2(String[] deadends, String target) {
        ArrayDeque<String> q = new ArrayDeque<>();
        Set<String> deads = new HashSet<>(Arrays.asList(deadends));
        Set<String> visited = new HashSet<>();
        q.addLast("0000");
        visited.add("0000");
        int level = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            while (size-- > 0) {
                String s = q.removeFirst();
                if (deads.contains(s)) {
                    continue;
                }
                if (s.equals(target)) {
                    return level;
                }

                for (int i = 0; i < 4; i++) {
                    char c = s.charAt(i);
                    String pre = s.substring(0, i), suf = s.substring(i + 1);
                    String s1 = pre + (c == '9' ? 0 : c - '0' + 1) + suf;
                    String s2 = pre + (c == '0' ? 9 : c - '0' - 1) + suf;
                    if (!visited.contains(s1) && !deads.contains(s1)) {
                        q.addLast(s1);
                        visited.add(s1);
                    }
                    if (!visited.contains(s2) && !deads.contains(s2)) {
                        q.addLast(s2);
                        visited.add(s2);
                    }
                }
            }
            level++;
        }
        return -1;
    }

    public int openLock(String[] deadends, String target) {
        Set<String> begin = new HashSet<>();
        Set<String> end = new HashSet<>();
        Set<String> deads = new HashSet<>(Arrays.asList(deadends));
        begin.add("0000");
        end.add(target);
        int level = 0;
        while (!begin.isEmpty() && !end.isEmpty()) {
            Set<String> temp = new HashSet<>();
            for (String s : begin) {
                if (deads.contains(s)) {
                    continue;
                }
                if (end.contains(s)) {
                    return level;
                }
                deads.add(s);

                for (int i = 0; i < 4; i++) {
                    char c = s.charAt(i);
                    String pre = s.substring(0, i), suf = s.substring(i + 1);
                    String s1 = pre + (c == '9' ? 0 : c - '0' + 1) + suf;
                    String s2 = pre + (c == '0' ? 9 : c - '0' - 1) + suf;
                    if (!deads.contains(s1))
                        temp.add(s1);
                    if (!deads.contains(s2))
                        temp.add(s2);
                }
            }
            level++;
            begin = end;
            end = temp;
        }
        return -1;
    }

    public List<Integer> partitionLabels(String S) {
        int[] last = new int[26];
        for (int i = 0; i < S.length(); i++) {
            last[S.charAt(i) - 'a'] = i;
        }

        int end = 0, start = 0;
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < S.length(); i++) {
            end = Math.max(end, last[S.charAt(i) - 'a']);
            if (i == end) {
                res.add(i - start + 1);
                start = i + 1;
            }
        }
        return res;
    }

    public int orderOfLargestPlusSign(int N, int[][] mines) {
        int[][] grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(grid[i], N);
        }
        for (int[] m : mines) {
            grid[m[0]][m[1]] = 0;
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0, k = N - 1, l = 0, r = 0, u = 0, d = 0; j < N; j++, k--) {
                grid[i][j] = Math.min(grid[i][j], l = (grid[i][j] == 0 ? 0 : l + 1));
                grid[i][k] = Math.min(grid[i][k], r = (grid[i][k] == 0 ? 0 : r + 1));
                grid[j][i] = Math.min(grid[j][i], u = (grid[j][i] == 0 ? 0 : u + 1));
                grid[k][i] = Math.min(grid[k][i], d = (grid[k][i] == 0 ? 0 : d + 1));
            }
        }

        int res = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                res = Math.max(res, grid[i][j]);
            }
        }
        return res;
    }

    public int countPrimeSetBits(int L, int R) {
        int cnt = 0;
        Set<Integer> listPrimes = new HashSet<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19, 23, 29));
        int[] res = countBits(R);
        for (int i = L; i <= R; i++) {
            if (listPrimes.contains(res[i])) {
                cnt++;
                Integer.bitCount(cnt);
            }
        }
        return cnt;
    }

    public int[] countBits(int num) {
        if (num == 0)
            return new int[1];
        int[] dp = new int[num + 1];

        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= num; i++) {
            dp[i] = dp[i >> 1] + dp[i & 1];
        }
        return dp;
    }

    public void duplicateZeros(int[] arr) {
        int[] t = new int[arr.length];
        int cur = 0;
        for (int i = 0; i < arr.length && cur < arr.length; i++) {
            int v = arr[i];
            t[cur++] = v;
            if (v == 0 && cur < arr.length) {
                t[cur++] = v;
            }
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = t[i];
        }
    }

    public int largestValsFromLabels(int[] values, int[] labels, int num_wanted, int use_limit) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            int key = labels[i];
            if (!map.containsKey(key)) {
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(values[i]);
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        for (List<Integer> li : map.values()) {
            Collections.sort(li, Collections.reverseOrder());
            int lim = 0;
            while (lim < use_limit && lim < li.size()) {
                pq.add(li.get(lim++));
            }
        }
        int res = 0;
        while (num_wanted-- > 0 && !pq.isEmpty()) {
            res += pq.remove();
        }
        return res;
    }

    int[][] D = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 }, { 1, 1 }, { 1, -1 }, { -1, -1 }, { -1, 1 } };

    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] == 1) {
            return -1;
        }
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        ArrayDeque<int[]> que = new ArrayDeque<>();
        que.add(new int[] { 0, 0 });
        visited[0][0] = true;
        int path = 1, size = que.size();
        while (size > 0) {
            path++;
            while (size-- > 0) {
                int[] cur = que.removeFirst();
                for (int[] d : D) {
                    int[] nb = new int[2];
                    nb[0] = cur[0] + d[0];
                    nb[1] = cur[1] + d[1];
                    if (0 <= nb[0] && nb[0] < grid.length && 0 <= nb[1] && nb[1] < grid[0].length
                            && !visited[nb[0]][nb[1]]) {
                        if (grid[nb[0]][nb[1]] == 0) {
                            que.add(nb);
                            visited[nb[0]][nb[1]] = true;
                            if (nb[0] == grid.length - 1 && nb[1] == grid[0].length - 1) {
                                return path;
                            }
                        }
                    }
                }
            }
            size = que.size();
        }
        return -1;
    }

    public String shortestCommonSupersequence(String str1, String str2) {
        int[][] L = new int[str1.length() + 1][str2.length() + 1];
        shortest(str1, str2, L);
        StringBuilder sb = new StringBuilder();
        int i = 0, j = 0;
        while (!(i == str1.length() && j == str2.length())) {
            if (i == str1.length()) {
                sb.append(str2.substring(j));
                break;
            }
            if (j == str2.length()) {
                sb.append(str1.substring(i));
                break;
            }
            if (str1.charAt(i) == str2.charAt(j)) {
                sb.append(str1.charAt(i));
                i++;
                j++;
            } else if (L[i][j] == 1 + L[i + 1][j]) {
                sb.append(str1.charAt(i++));
            } else {
                sb.append(str2.charAt(j++));
            }
        }
        return sb.toString();
    }

    public void shortest(String str1, String str2, int[][] L) {
        for (int i = str1.length(); i >= 0; i--) {
            for (int j = str2.length(); j >= 0; j--) {
                if (j == str2.length()) {
                    L[i][j] = (i == str1.length() ? 0 : L[i + 1][j] + 1);
                } else if (i == str1.length()) {
                    L[i][j] = 1 + L[i][j + 1];
                    continue;
                } else {
                    if (str1.charAt(i) == str2.charAt(j)) {
                        L[i][j] = 1 + L[i + 1][j + 1];
                    } else {
                        L[i][j] = 1 + Math.min(L[i + 1][j], L[i][j + 1]);
                    }
                }
            }
        }
    }

    @Test
    public void test() {
        int[] A = new int[] { 0, 0, 0 };
        duplicateZeros(A);
    }
}

/**
 * 30. Substring with Concatenation of All Words
 */
class Solution30 {
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
}