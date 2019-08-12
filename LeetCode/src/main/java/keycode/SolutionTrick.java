package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;

public class SolutionTrick {

    /**
     * 11. Container With Most Water
     */
    public int maxArea(int[] height) {
        int start = 0;
        int end = height.length - 1;
        int max = 0;
        while (start < end) {
            int area = (end - start) * Math.min(height[start], height[end]);
            max = Math.max(max, area);
            if (height[start] < height[end]) {
                start++;
            } else {
                end--;
            }
        }
        return max;
    }

    /**
     * 13. Roman to Integer
     */
    private Map<Character, Integer> symbols = new HashMap<Character, Integer>();
    {
        symbols.put('M', 1000);
        symbols.put('D', 500);
        symbols.put('C', 100);
        symbols.put('L', 50);
        symbols.put('X', 10);
        symbols.put('V', 5);
        symbols.put('I', 1);
    }

    public int romanToInt(String s) {
        int num = symbols.get(s.charAt(s.length() - 1));

        for (int i = s.length() - 2; i >= 0; i--) {
            char c = s.charAt(i);
            char nextC = s.charAt(i + 1);
            if (symbols.get(c) < symbols.get(nextC)) {
                num -= symbols.get(c);
            } else {
                num += symbols.get(c);
            }
        }
        return num;
    }

    public String intToRoman(int num) {
        String M[] = { "", "M", "MM", "MMM" };
        String C[] = { "", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM" };
        String X[] = { "", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC" };
        String I[] = { "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX" };
        return M[num / 1000] + C[(num % 1000) / 100] + X[(num % 100) / 10] + I[num % 10];
    }

    // 273. Integer to English Words
    class Solution273 {
        private final String[] lessThan20 = { "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
                "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen",
                "Eighteen", "Nineteen" };
        private final String[] tens = { "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty",
                "Ninety" };
        private final String[] thousands = { "", "Thousand", "Million", "Billion" };

        public String numberToWords(int num) {
            if (num == 0) {
                return "Zero";
            }
            String result = "";
            int i = 0;
            while (num > 0) {
                if (num % 1000 != 0) {
                    result = helper(num % 1000) + thousands[i] + " " + result;
                }
                num /= 1000;
                i++;
            }
            return result.trim();
        }

        private String helper(int num) {
            if (num == 0) {
                return "";
            } else if (num < 20) {
                return lessThan20[num] + " ";
            } else if (num < 100) {
                return tens[num / 10] + " " + helper(num % 10);
            } else {
                return lessThan20[num / 100] + " Hundred " + helper(num % 100);
            }
        }
    }

    /**
     * 240. Search a 2D Matrix II
     */
    public boolean searchMatrix(int[][] matrix, int target) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }

        int i = 0, j = matrix[0].length - 1;

        while (i < matrix.length && j >= 0) {
            int x = matrix[i][j];
            if (target == x)
                return true;
            else if (target < x)
                j--;
            else
                i++;
        }
        return false;
    }

    /**
     * 279. Perfect Squares
     */
    public int numSquares(int n) {
        int dp[] = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 0; i <= n; i++) {
            for (int j = 1; i + j * j <= n; j++) {
                dp[i + j * j] = Math.min(dp[i] + 1, dp[i + j * j]);
            }
        }
        return dp[n];
    }

    /**
     * 282. Expression Add Operators
     */
    public List<String> addOperators(String num, int target) {
        List<String> list = new ArrayList<String>();
        fn282(list, num, target, new StringBuilder(), 0, 0, 0);
        "".toLowerCase();
        return list;
    }

    // dp, dfs, add a number to result each time
    private void fn282(List<String> list, String num, int target, StringBuilder path, int pos, long sum, long lastNum) {
        int len = num.length();
        if (pos == len && sum == target) {
            list.add(path.toString());
            return;
        }
        if (pos >= len) {
            return;
        }
        for (int i = pos; i < len; i++) {
            if (num.charAt(pos) == '0' && i > pos) {
                break;
            }
            long curNum = Long.parseLong(num.substring(pos, i + 1));
            int sb = path.length();
            if (pos == 0) {
                path.append(curNum);
                fn282(list, num, target, path, i + 1, curNum, curNum);
                path.setLength(sb);
            } else {
                path.append("+").append(curNum);
                fn282(list, num, target, path, i + 1, sum + curNum, curNum);
                path.setLength(sb);
                path.append("-").append(curNum);
                fn282(list, num, target, path, i + 1, sum - curNum, -curNum);
                path.setLength(sb);
                // sum - lastNum + lastNum * curNum
                path.append("*").append(curNum);
                fn282(list, num, target, path, i + 1, sum - lastNum + lastNum * curNum, lastNum * curNum);
                path.setLength(sb);
            }
        }
    }

    class Solution {
        public boolean waterJugProblem(int x, int y, int z) {
            if (x + y < z)
                return false;
            if (z == x || x == y || x == x + y)
                return true;

            if (x > y) {
                int tmp = x;
                x = y;
                y = tmp;
            }

            Queue<State> states = new ArrayDeque<>();
            Set<State> visited = new HashSet<>();

            // initial state
            State init = new State(0, 0);
            states.offer(init);
            visited.add(init);

            while (!states.isEmpty()) {
                State curr = states.poll();
                if (curr.a + curr.b == z)
                    return true;

                Queue<State> queue = new ArrayDeque<>();
                queue.offer(new State(x, curr.b)); // fill jug 1
                queue.offer(new State(0, curr.b)); // empty jug1
                queue.offer(new State(curr.a, y)); // fill jug 2
                queue.offer(new State(curr.a, 0)); // empty jug2
                // pour all water from jug2 to jug1
                queue.offer(new State(Math.min(curr.a + curr.b, x), curr.a + curr.b < x ? 0 : curr.b - (x - curr.a)));
                // pour all water from jug1 to jug2
                queue.offer(new State(curr.a + curr.b < y ? 0 : curr.a - (y - curr.b), Math.min(curr.a + curr.b, y)));

                for (State tmp : queue) {
                    if (visited.contains(tmp))
                        continue;
                    states.offer(tmp);
                    visited.add(tmp);
                }
            }
            return false;
        }

        class State {
            public int a, b;

            public State(int a, int b) {
                this.a = a;
                this.b = b;
            }

            public int hashCode() {
                return 31 * a + b;
            }

            public boolean equals(Object o) {
                State other = (State) o;
                return this.a == other.a && this.b == other.b;
            }
        }
    }

    public int dayOfYear(String date) {
        int[] M = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7));
        int day = Integer.parseInt(date.substring(8));
        if (year % 4 == 0) {
            M[1] = 29;
        }
        if (year == 1900) {
            M[1] = 28;
        }
        int res = 0, i = 0;
        while (i < month - 1) {
            res += M[i++];
        }
        res += day;
        return res;
    }

    public int numRollsToTarget2(int d, int f, int target) {
        int mod = 1000000007;
        long[] dp = new long[target + 1];
        dp[0] = 1;
        for (int i = 0; i < d; i++) {
            long[] ndp = new long[target + 1];
            for (int j = 0; j <= target; j++) {
                if (j + 1 < ndp.length)
                    ndp[j + 1] += dp[j];
                if (j + f + 1 < ndp.length)
                    ndp[j + f + 1] += mod - dp[j];
            }
            for (int j = 1; j <= target; j++) {
                ndp[j] += ndp[j - 1];
                ndp[j] %= mod;
            }
            dp = ndp;
        }
        return (int) dp[target];
    }

    public int numRollsToTarget(int d, int f, int target) {
        int mod = 1000000007;
        int[][] A = new int[d][target + 1];
        for (int j = 1; j <= f && j <= target; j++) {
            A[0][j] = 1;
        }
        for (int i = 1; i < d; i++) {
            for (int j = 1; j <= f; j++) {
                // j == 1, dice = 1
                for (int k = 1; k < target; k++) {
                    if (k + j <= target) {
                        A[i][k + j] = (A[i][k + j] + A[i - 1][k]) % mod;
                    }
                }
            }
        }
        return A[d - 1][target];
    }

    public int maxRepOpt1V1(String text) {
        List<List<int[]>> A = new ArrayList<>();
        int n = 26;
        while (n-- > 0) {
            A.add(new ArrayList<>());
        }
        int start = 0, res = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (i == text.length() - 1 || text.charAt(i + 1) != c) {
                A.get(c - 'a').add(new int[] { start, i });
                res = Math.max(res, i - start + 1);// one
                start = i + 1;
            }
        }

        for (int i = 0; i < 26; i++) {
            List<int[]> list = A.get(i);
            if (list.size() < 2) {
                continue;
            }
            int addOne = list.size() > 2 ? 1 : 0;
            for (int j = 0; j < list.size() - 1; j++) {
                res = Math.max(res, list.get(j)[1] - list.get(j)[0] + 2); // aabbaa -> 3
                if (list.get(j)[1] + 2 == list.get(j + 1)[0]) {
                    // aabaa -> 4
                    res = Math.max(res, list.get(j + 1)[1] - list.get(j)[0] + addOne);
                }
            }

        }
        return res;
    }

    // V2, works
    public int maxRepOpt1V2(String text) {
        char[] A = text.toCharArray();
        int res = 0;
        int cnt[] = new int[26];
        for (char c : A) {
            cnt[c - 'a']++;
        }

        for (char c = 'a'; c <= 'z'; c++) {
            int cur = 0;
            int pre = 0;
            for (int i = 0; i < A.length; i++) {
                if (A[i] == c) {
                    cur++;
                }
                while (i - pre - cur > 0) {
                    if (A[pre] == c) {
                        cur--;
                    }
                    pre++;
                }
                res = Math.max(res, Math.min(i - pre + 1, cnt[c - 'a']));
            }
        }
        return res;
    }

    // works
    public int maxRepOpt1V3(String text) {
        int result = 0;
        for (int i = 0; i < 26; i++) {
            char c = (char) ('a' + i);
            int index = 0;
            int localMax = 0;
            int numGroup = 0;
            boolean hasContatenate = false;
            int l1 = 0;
            int l2 = 0;
            while (index < text.length()) {
                if (text.charAt(index) == c) {
                    l1++;
                } else {
                    localMax = Math.max(localMax, l1 + l2);
                    if (l1 > 0) {
                        numGroup++;
                        if (l2 > 0) {
                            hasContatenate = true;
                        }
                    }
                    l2 = l1;
                    l1 = 0;
                }
                index++;
            }
            if (l1 > 0) {
                localMax = Math.max(localMax, l1 + l2);
                numGroup++;
                if (l2 > 0) {
                    hasContatenate = true;
                }
            }
            if (numGroup >= 3 || (numGroup == 2 && !hasContatenate)) {
                localMax++;
            }
            result = Math.max(result, localMax);
        }
        return result;
    }

    @Test
    public void test() {
        System.out.println(maxRepOpt1V1("aaabbaaa"));
        // System.out.println(numRollsToTarget(2, 6, 7));
    }
}
