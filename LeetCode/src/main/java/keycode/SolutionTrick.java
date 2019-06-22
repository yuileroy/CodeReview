package keycode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
