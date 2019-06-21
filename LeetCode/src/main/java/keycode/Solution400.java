package keycode;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.LinkedList;

import org.junit.Test;

public class Solution400 {

    /**
     * 402. Remove K Digits
     */
    public String removeKdigits(String num, int k) {
        if (num.length() == k) {
            return "0";
        }
        k = num.length() - k;
        ArrayDeque<Character> qu = new ArrayDeque<>();
        for (int i = 0; i < num.length(); i++) {
            // also check total count of available char before delete
            while (qu.size() > 0 && qu.peekLast() > num.charAt(i) && qu.size() + num.length() - i > k) {
                qu.removeLast();
            }
            qu.add(num.charAt(i));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; i++) {
            sb.append(qu.removeFirst());
        }
        while (sb.length() > 0 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.length() == 0 ? "0" : sb.toString();
    }

    /**
     * 406. Queue Reconstruction by Height
     */
    public int[][] reconstructQueue(int[][] people) {
        // 7-0, 7-1, 6-1, 5-0, 5-2, 4-4
        // 5-0, 7-0, 5-2, 6-1, 4-4, 7-1,
        LinkedList<int[]> list = new LinkedList<int[]>();
        for (int[] e : people) {
            list.add(e);
        }
        Collections.sort(list, (a, b) -> a[0] == b[0] ? a[1] - b[1] : b[0] - a[0]);

        for (int i = 1; i < list.size(); i++) {
            int[] cur = list.remove(i);
            list.add(cur[1], cur);
        }
        int[][] res = new int[people.length][2];
        for (int i = 0; i < list.size(); i++) {
            int[] cur = list.get(i);
            res[i][0] = cur[0];
            res[i][1] = cur[1];
        }
        return res;
    }

    @Test
    public void test() {
        // System.out.println(maxNumber(new int[] { 3, 4, 6, 5 }, new int[] { 9, 1, 2, 5, 8, 3 }, 5));
        System.out.println("a".substring(0, 0));
    }
}
