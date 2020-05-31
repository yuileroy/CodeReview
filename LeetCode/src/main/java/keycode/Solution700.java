package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.junit.Test;

public class Solution700 {

    /**
     * 736. Parse Lisp Expression
     */
    public int evaluate(String expression) {
        return eval(expression, new HashMap<String, Integer>());
    }

    private int eval(String expression, Map<String, Integer> parent) {
        if (expression.charAt(0) != '(') {
            if ((expression.charAt(0) >= '0' && expression.charAt(0) <= '9') || expression.charAt(0) == '-') {
                return Integer.parseInt(expression);
            }
            return parent.get(expression);
        }
        // expression = (*, if add, elseif mult, else let
        Map<String, Integer> scope = new HashMap<>();
        scope.putAll(parent);
        List<String> list = parse(expression.substring(expression.charAt(1) == 'm' ? 6 : 5, expression.length() - 1));
        if (expression.charAt(1) == 'a') {
            return eval(list.get(0), scope) + eval(list.get(1), scope);
        } else if (expression.charAt(1) == 'm') {
            return eval(list.get(0), scope) * eval(list.get(1), scope);
        } else {
            for (int i = 0; i < list.size() - 2; i += 2) {
                scope.put(list.get(i), eval(list.get(i + 1), scope));
            }
            return eval(list.get(list.size() - 1), scope);
        }
    }

    // add, mult : 2 parts, let : 2n parts
    private List<String> parse(String expression) {
        List<String> result = new ArrayList<>();
        int par = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                par--;
            }
            if (expression.charAt(i) == ')') {
                par++;
            }
            if (expression.charAt(i) == ' ' && par == 0) {
                result.add(sb.toString());
                sb = new StringBuilder();
            } else {
                sb.append(expression.charAt(i));
            }
        }
        if (sb.length() > 0) {
            result.add(sb.toString());
        }
        return result;
    }

    /**
     * 743. Network Delay Time
     */
    // Given times, a list of travel times as directed edges times[i] = (u, v, w), where u is the source node, v is the
    // target node, and w is the time it takes for a signal to travel from source to target.
    // Now, we send a signal from a certain node K. How long will it take for all nodes to receive the signal? If it is
    // impossible, return -1.
    public int networkDelayTime(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> graph = new HashMap<>();
        for (int[] e : times) {
            if (!graph.containsKey(e[0]))
                graph.put(e[0], new ArrayList<>());
            graph.get(e[0]).add(new int[] { e[1], e[2] });
        }
        // pq <timeToK, node>
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[0] - b[0]);
        pq.add(new int[] { 0, K });
        // result map <node, timeToK>
        Map<Integer, Integer> map = new HashMap<>();
        while (!pq.isEmpty()) {
            int[] e = pq.remove();
            int len = e[0], u = e[1];
            if (map.containsKey(u))
                continue;
            map.put(u, len);
            if (graph.containsKey(u)) {
                for (int[] edge : graph.get(u)) {
                    int v = edge[0], len2 = edge[1];
                    if (!map.containsKey(v)) {
                        pq.add(new int[] { len + len2, v });
                    }
                }
            }
        }

        if (map.size() != N)
            return -1;
        int res = 0;
        for (int e : map.values())
            res = Math.max(res, e);
        return res;
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

    /**
     * 772. Basic Calculator III
     */
    public int calculate(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        char sign = '+';
        int num = 0, pre = 0, res = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ("+-*/".indexOf(c) != -1) {
                sign = c;
                continue;
            }
            if (c == ' ')
                continue;
            if (Character.isDigit(c)) {
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + s.charAt(i++) - '0';
                }
                i--;
            } else if (c == '(') {
                int j = i + 1;
                int braces = 1;
                while (j < s.length()) {
                    if (s.charAt(j) == '(')
                        braces++;
                    if (s.charAt(j) == ')')
                        braces--;
                    if (braces == 0)
                        break;
                    j++;
                }
                num = calculate(s.substring(i + 1, j));
                i = j;
            }
            switch (sign) {
            case '+':
                res += num;
                pre = num;
                break;
            case '-':
                res -= num;
                pre = -num;
                break;
            case '*':
                res = res - pre + pre * num;
                pre = pre * num;
                break;
            case '/':
                res = res - pre + pre / num;
                pre = pre / num;
                break;
            }
            num = 0;
        }
        return res;
    }

    public int calculateV2(String s) {
        Queue<Character> que = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c != ' ')
                que.add(c);
        }
        que.add(' '); // to process the last operation
        return cal(que);
    }

    private int cal(Queue<Character> q) {
        int num = 0, res = 0, pre = 0;
        char op = '+';
        while (!q.isEmpty()) {
            char c = q.remove();
            if (Character.isDigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '(') {
                num = cal(q);
            } else { // c = ' ', '(', '+-*/'
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
     * 
     * 785. Is Graph Bipartite?
     * 
     * Input: [[1,2,3], [0,2], [0,1,3], [0,2]] Output: false
     */
    public boolean isBipartite(int[][] graph) {
        boolean[] visited = new boolean[graph.length];
        boolean[] color = new boolean[graph.length];

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < graph.length; i++) {
            if (graph[i].length == 0 || visited[i]) {
                continue;
            }
            queue.add(i);
            visited[i] = true;
            color[i] = true;
            while (!queue.isEmpty()) {
                int u = queue.remove();
                for (int v : graph[u]) {
                    if (visited[v]) {
                        if (color[v] == color[u]) {
                            return false;
                        }
                    } else {
                        queue.add(v);
                        visited[v] = true;
                        color[v] = !color[u];
                    }
                }
            }
        }
        return true;
    }

    @Test
    public void test() {
        evaluate("(mult 3 (add 2 3))");
        System.out.println(calculateV2("-15/3"));
        System.out.println("+-*/)".indexOf(')'));
    }
}
