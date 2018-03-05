package javacode.code;

import java.util.PriorityQueue;

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

    @Test
    public void test() {
        // String s = reorganizeString("aaabbbcccc");
        System.out.println(maxChunksToSorted(new int[] { 1, 4, 0, 2, 3, 5 }));
    }

}
