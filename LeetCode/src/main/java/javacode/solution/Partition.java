package javacode.solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class Partition {
    public List<List<String>> partition(String s) {
        List<String> item = new LinkedList<String>();
        List<List<String>> res = new ArrayList<List<String>>();

        if (s == null || s.length() == 0) {
            return res;
        }

        boolean[][] A = getValue(s);

        dfs(s, 0, item, res, A);
        return res;
    }

    private void dfs(String s, int start, List<String> item, List<List<String>> res, boolean[][] A) {
        if (start == s.length()) {
            res.add(new ArrayList<String>(item));
            return;
        }

        for (int i = start; i < s.length(); i++) {
            if (A[start][i]) {
                item.add(s.substring(start, i + 1));
                dfs(s, i + 1, item, res, A);
                item.remove(item.size() - 1);
            }
        }
    }

    private boolean[][] getValue(String s) {
        int n = s.length();
        boolean[][] A = new boolean[n][n];

        for (int i = n - 1; i >= 0; i--) {
            for (int j = i; j < n; j++) {
                if (j == i) {
                    A[i][j] = true;
                } else if (j == i + 1) {
                    A[i][j] = s.charAt(i) == s.charAt(j);
                } else {
                    A[i][j] = s.charAt(i) == s.charAt(j) && A[i + 1][j - 1];
                }
            }
        }
        return A;
    }

    boolean isPalindrome(String s) {
        int low = 0;
        int high = s.length() - 1;
        while (low < high) {
            if (s.charAt(low) != s.charAt(high)) {
                return false;
            }
            low++;
            high--;
        }
        return true;
    }

    @Test
    public void test() {
        partition("abbac");
    }
}
