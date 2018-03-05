package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

    @Test
    public void test() {
        int[] A = new int[] { 3, 1, 4, 5 };
        System.out.println(isIdealPermutation(A));
    }
}