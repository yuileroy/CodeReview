package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Solution700 {
    public int calculate(String s) {
        Queue<Character> que = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c != ' ')
                que.offer(c);
        }
        que.add(' '); // to process the last operation
        return cal(que);
    }

    private int cal(Queue<Character> q) {
        int num = 0, res = 0, pre = 0;
        char op = '+';
        while (!q.isEmpty()) {
            char c = q.remove();
            if (Character.isDigit(c))
                num = num * 10 + (c - '0');
            else if (c == '(')
                num = cal(q);
            else {
                switch (op) {
                case '+':
                    res += pre;
                    pre = num;
                    break;
                case '-':
                    res += pre;
                    pre = -num;
                    break;
                case '*':
                    pre *= num;
                    break;
                case '/':
                    pre /= num;
                    break;
                }
                if (c == ')')
                    break;
                num = 0;
                op = c;
            }
        }
        return res + pre;
    }

    /**
     * 763. Partition Labels
     * 
     * We want to partition this string into as many parts as possible so that each letter appears in at most one part
     */
    public List<Integer> partitionLabels(String S) {
        int[] last = new int[26];
        for (int i = 0; i < S.length(); i++) {
            last[S.charAt(i) - 'a'] = i;
        }

        int start = 0, end = 0;
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
}
