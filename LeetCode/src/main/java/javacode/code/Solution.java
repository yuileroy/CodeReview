package javacode.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.junit.Test;

public class Solution {

    public String reorganizeString(String S) {
        int[] map = new int[128];
        for (char c : S.toCharArray()) {
            map[c]++;
            if (map[c] > (S.length() + 1) / 2) {
                return "";
            }
        }
        // Greedy: fetch char of max count as next char in the result.
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        for (int i = 0 + 'a'; i <= 0 + 'z'; i++) {
            if (map[i] > 0) {
                pq.add(new int[] { i, map[i] });
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            int[] first = pq.remove();
            if (sb.length() == 0 || first[0] != sb.charAt(sb.length() - 1)) {
                sb.append((char) first[0]);
                if (--first[1] > 0) {
                    pq.add(first);
                }
            } else {
                int[] second = pq.remove();
                sb.append((char) second[0]);
                if (--second[1] > 0) {
                    pq.add(second);
                }
                pq.add(first);
            }
        }
        return sb.toString();
    }

    public int maxChunksToSorted(int[] arr) {
        int n = arr.length;
        int[] maxL = new int[n];
        int[] minR = new int[n];

        maxL[0] = arr[0];
        for (int i = 1; i < n; i++) {
            maxL[i] = Math.max(maxL[i - 1], arr[i]);
        }

        minR[n - 1] = arr[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            minR[i] = Math.min(minR[i + 1], arr[i]);
        }

        int res = 0;
        for (int i = 0; i < n - 1; i++) {
            if (maxL[i] <= minR[i + 1]) {
                res++;
            }
        }
        return res + 1;
    }

    List<String> getList(String text, List<String> excludeWords) {
        List<String> res = new ArrayList<>();
        Map<String, Integer> map = new HashMap<>();
        int start = 0;
        Set<String> exSet = new HashSet<>(excludeWords);
        for (int i = 0; i < text.length(); i++) {
            if (Character.isAlphabetic(text.charAt(i))) {
                start = i;
                while (++i < text.length() && Character.isAlphabetic(text.charAt(i))) {
                }
                String item = text.substring(start, i);

                if (!exSet.contains(item)) {
                    map.put(item, map.getOrDefault(item, 0) + 1);
                }
            }
        }
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > cnt) {
                cnt = entry.getValue();
                res.clear();
                res.add(entry.getKey());
            }
        }
        return res;
    }

    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    List<String> retrieveMostFrequentlyUsedWords(String literatureText, List<String> wordsToExclude) {
        // WRITE YOUR CODE HERE
        List<String> res = new ArrayList<>();
        if (literatureText == null || wordsToExclude == null) {
            return res;
        }
        Map<String, Integer> map = new HashMap<>();
        Set<String> exSet = new HashSet<>(wordsToExclude);
        int start = 0;
        for (int i = 0; i < literatureText.length(); i++) {
            if (Character.isAlphabetic(literatureText.charAt(i))) {
                start = i;
                while (++i < literatureText.length() && Character.isAlphabetic(literatureText.charAt(i))) {
                }
                String item = literatureText.substring(start, i);
                if (!exSet.contains(item)) {
                    map.put(item, map.getOrDefault(item, 0) + 1);
                }
            }
        }
        int cnt = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > cnt) {
                cnt = entry.getValue();
                res.clear();
                res.add(entry.getKey());
            }
        }
        return res;
    }
    // METHOD SIGNATURE ENDS

    List<String> orderLogs(List<String> logs) {
        List<String> res = new ArrayList<>();
        if (logs == null) {
            return res;
        }
        List<String[]> wList = new ArrayList<>();
        List<String> dList = new ArrayList<>();
        for (String s : logs) {
            if (Character.isDigit(s.charAt(s.length() - 1))) {
                String[] token = s.split(" ");
                wList.add(token);
            } else {
                dList.add(s);
            }
        }
        Comparator<String[]> c1 = new Comparator<String[]>() {
            public int compare(String[] a, String[] b) {
                int i = 1, j = 1;
                while (i < a.length && j < b.length && a[i].equals(b[j])) {
                    i++;
                    j++;
                }
                if (i == a.length && j == b.length) {
                    return a[0].compareTo(b[0]);
                }
                if (i == a.length) {
                    return 1;
                } else if (j == b.length) {
                    return -1;
                } else {
                    return a[i].compareTo(b[j]);
                }
            }
        };

        Comparator<String> c2 = new Comparator<String>() {
            public int compare(String a, String b) {
                int i = 0, j = 0;
                while (i < a.length() && j < b.length() && a.charAt(i) == b.charAt(j)) {
                    i++;
                    j++;
                }
                if (i == a.length()) {
                    return 1;
                } else if (j == b.length()) {
                    return -1;
                } else {
                    return a.charAt(i) - b.charAt(j);
                }
            }
        };

        Comparator<String> c = new Comparator<String>() {
            public int compare(String a, String b) {
                int i = a.indexOf(" ");
                String a1 = a.substring(0, i), a2 = a.substring(i + 1);
                i = b.indexOf(" ");
                String b1 = b.substring(0, i), b2 = b.substring(i + 1);
                return a2.equals(b2) ? a1.compareTo(b1) : a2.compareTo(b2);
            }
        };

        Collections.sort(wList, c1);
        for (String[] s : wList) {
            res.add(String.join(" ", s));
        }
        for (String s : dList) {
            res.add(s);
        }
        return res;
    }

    public List<String> reorderLines(int logFileSize, List<String> logLines) {
        // WRITE YOUR CODE HERE
        List<String> res = new ArrayList<>(logFileSize);
        if (logFileSize <= 0 || logLines == null) {
            return res;
        }
        List<String> wList = new ArrayList<>();
        List<String> dList = new ArrayList<>();
        for (String s : logLines) {
            if (Character.isDigit(s.charAt(s.length() - 1))) {
                dList.add(s);
            } else {
                wList.add(s);
            }
        }
        Comparator<String> c = new Comparator<String>() {
            public int compare(String a, String b) {
                int i = a.indexOf(" ");
                String a1 = a.substring(0, i), a2 = a.substring(i + 1);
                i = b.indexOf(" ");
                String b1 = b.substring(0, i), b2 = b.substring(i + 1);
                return a2.equals(b2) ? a1.compareTo(b1) : a2.compareTo(b2);
            }
        };
        Collections.sort(wList, c);
        for (String s : wList) {
            res.add(s);
        }
        for (String s : dList) {
            res.add(s);
        }
        return res;
    }

    public boolean canIWin(int maxChoosableInteger, int desiredTotal) {

        if (desiredTotal <= maxChoosableInteger) {
            return true;
        }
        if ((1 + maxChoosableInteger) / 2 * maxChoosableInteger < desiredTotal) {
            return false;
        }

        Map<Integer, Boolean> map = new HashMap<>();
        return dfs(maxChoosableInteger, desiredTotal, new boolean[maxChoosableInteger + 1], map);
    }

    private boolean dfs(int maxCI, int cur, boolean[] chosen, Map<Integer, Boolean> map) {
        if (cur <= 0) {
            return false;
        }
        int item = toInt(chosen);
        if (map.containsKey(item)) {
            return map.get(item);
        }

        for (int i = 1; i <= maxCI; i++) {
            if (chosen[i]) {
                continue;
            }
            chosen[i] = true;
            if (!dfs(maxCI, cur - i, chosen, map)) {
                map.put(item, true);
                chosen[i] = false;
                return true;
            }
            chosen[i] = false;
        }
        map.put(item, false);
        return false;
    }

    private int toInt(boolean[] chosen) {
        int res = 0;
        for (boolean b : chosen) {
            res <<= 1;
            if (b) {
                res |= 1;
            }
        }
        return res;
    }

    public double findMedianSortedArrays(int[] A, int[] B) {
        int m = A.length, n = B.length;
        if (m > n) { // to ensure m <= n
            return findMedianSortedArrays(B, A);
        }
        int iMin = 0, iMax = m, kth = (m + n + 1) / 2;
        // find i such that A[i - 1] <= B[j] && B[j - 1] <= A[i]
        // then A[i - 1] or B[j - 1] with be the kth
        while (iMin <= iMax) {
            int i = iMin + (iMax - iMin) / 2;
            int j = kth - i;
            if (i < iMax && B[j - 1] > A[i]) {
                iMin = i + 1; // i is too small
            } else if (i > iMin && A[i - 1] > B[j]) {
                iMax = i - 1; // i is too big
            } else { // i is perfect
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = B[j - 1];
                } else if (j == 0) {
                    maxLeft = A[i - 1];
                } else {
                    maxLeft = Math.max(A[i - 1], B[j - 1]);
                }
                if ((m + n) % 2 == 1) {
                    return maxLeft;
                }

                int minRight = 0;
                if (i == m) {
                    minRight = B[j];
                } else if (j == n) {
                    minRight = A[i];
                } else {
                    minRight = Math.min(B[j], A[i]);
                }
                return (maxLeft + minRight) / 2.0;
            }
        }
        return 0.0;
    }

    String sep(String s) {
        String[] c = s.split("\\.|\\?|!");
        return Arrays.asList(c).toString();
    }

    public boolean validWordAbbreviation(String word, String abbr) {
        int idx = 0;
        for (int i = 0; i < abbr.length(); i++) {
            // word reached end but abbr doesn't
            if (idx >= word.length()) {
                return false;
            }
            char c = abbr.charAt(i);
            if (isDigit(c)) {
                int n = 0;
                while (i < abbr.length() && isDigit(abbr.charAt(i))) {
                    n = n * 10 + abbr.charAt(i) - '0';
                    i++;
                }
                i--;
                idx += n;
                System.out.println(i + " idx: " + idx);
            } else {
                if (word.charAt(idx) != abbr.charAt(i)) {
                    return false;
                }
                idx += 1;
            }
        }
        return idx == word.length();
    }

    boolean isDigit(char c) {
        return '0' <= c && c <= '9';
    }

    @Test
    public void test() {
        System.out.println(sep("a.b?dadw !e"));
        System.out.println(-9 % 4); // -1
        System.out.println(validWordAbbreviation("abz", "a2"));
        System.out.println(validWordAbbreviation("international", "i12"));
        if (2 % 2 == 0) {
            return;
        }
        // String s = reorganizeString("aaabbbcccc");
        System.out.println(getList("f1 ,...>2.1.112 1 adad 3 3 esd2r r r  3f", new ArrayList<>()));
        for (char c = '0'; c <= '9'; c++) {
            System.out.println((int) c);
            // 0-9 : 48-57
            // a-z : 106-122
            // A-Z : 74-90
        }
        List<String> li = new ArrayList<>();
        li.add("1a");
        li.add("a1");
        li.add("a");
        Collections.sort(li);
        System.out.println(li);
        Math.sqrt(3);
    }

}
