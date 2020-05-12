package javacode.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class Amazons {
    public static int bstDistance(int[] values, int n, int node1, int node2) {
        Node root = new Node(values[0]);
        List<Integer> tmp = null, list1 = new ArrayList<>(), list2 = new ArrayList<>();
        for (int i = 1; i < values.length; i++) {
            int val = values[i];
            Node cur = root, par = null;
            tmp = new ArrayList<>();

            while (cur != null) {
                par = cur;
                tmp.add(par.val);
                if (val < cur.val) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
            if (val < par.val) {
                par.left = new Node(val);
            } else {
                par.right = new Node(val);
            }
            tmp.add(val);
            if (val == node1) {
                list1 = tmp;
            }
            if (val == node2) {
                list2 = tmp;
            }
        }

        int i = 0;
        while (i < list1.size() && i < list2.size()) {
            if (list1.get(i).intValue() != list2.get(i).intValue()) {
                break;
            }
            i++;
        }

        return list1.size() + list2.size() - i * 2;
    }

    int getM(int numTotalAvailableCities, int numTotalAvailableRoads, List<List<Integer>> roadsAvailable,
            int numNewRoadsConstruct, List<List<Integer>> costNewRoadsConstruct) {
        if (numTotalAvailableCities < 2) {
            return 0;
        }
        int[] A = new int[numTotalAvailableCities + 1];
        for (int i = 1; i < A.length; i++) {
            A[i] = i;
        }
        for (List<Integer> e : roadsAvailable) {
            union(A, e.get(0), e.get(1));
        }
        int res = 0;
        Collections.sort(costNewRoadsConstruct, ((a, b) -> a.get(2) - b.get(2)));
        for (List<Integer> e : costNewRoadsConstruct) {
            int a = find(A, e.get(0));
            int b = find(A, e.get(1));
            if (a != b) {
                res += e.get(2);
            }
            union(A, a, b);
        }

        for (int i = 2; i < A.length; i++) {
            if (A[i] != A[1]) {
                return -1;
            }
        }
        return res;
    }

    int find(int[] A, int i) {
        if (i == A[i]) {
            return i;
        }
        A[i] = find(A, A[i]);
        return A[i];
    }

    void union(int[] A, int i, int j) {
        int a = find(A, i);
        int b = find(A, j);
        A[a] = b;
    }

    // METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
    public List<String> orderedJunctionBoxes(int numberOfBoxes, List<String> boxList) {
        // WRITE YOUR CODE HERE
        List<String> res = new ArrayList<>(), newV = new ArrayList<>();
        List<List<String>> oldV = new ArrayList<>();
        for (String s : boxList) {
            int idx = s.indexOf(" ");
            String s1 = s.substring(idx);
            String s2 = s.substring(idx + 1, s.length());
            boolean isDigit = true;
            for (char c : s2.toCharArray()) {
                if (!Character.isDigit(c) && c != ' ') {
                    isDigit = false;
                }
            }
            if (isDigit) {
                newV.add(s);
            } else {
                List<String> list = new ArrayList<>();
                list.add(s1);
                list.add(s2);
                s2.replaceAll(" ", "-");
                list.add(s);
                oldV.add(list);
            }
        }
        Collections.sort(oldV,
                ((a, b) -> (!a.get(1).equals(b.get(1)) ? a.get(1).compareTo(b.get(1)) : a.get(0).compareTo(b.get(0)))));
        class Cc implements Comparator<String> {

            @Override
            public int compare(String o1, String o2) {
                String[] v1 = o1.split(" ");
                String[] v2 = o2.split(" ");
                int i = 0;
                while (i < v1.length && i < v2.length && v1[i].equals(v2[i])) {
                    i++;
                }
                return 0;
            }

        }

        Collections.sort(oldV, new Comparator<List<String>>() {

            @Override
            public int compare(List<String> o1, List<String> o2) {
                // TODO Auto-generated method stub
                return 0;
            }

        });

        for (List<String> list : oldV) {
            res.add(list.get(2));
        }
        res.addAll(newV);
        return res;
    }

    List<List<Integer>> optimalUtilization(int maxTravelDist, List<List<Integer>> forwardRouteList,
            List<List<Integer>> returnRouteList) {

        Collections.sort(forwardRouteList, ((a, b) -> a.get(1) - b.get(1)));
        Collections.sort(returnRouteList, ((a, b) -> a.get(1) - b.get(1)));

        // if (forwardRouteList.get(0).get(1) + returnRouteList.get(0).get(1) >
        // maxTravelDist) {
        // return new ArrayList<>();
        // }

        Map<Integer, List<List<Integer>>> map = new HashMap<>();
        for (int i = 0; i < forwardRouteList.size(); i++) {
            int cur = 0;
            int v1 = forwardRouteList.get(i).get(1);
            while (cur < returnRouteList.size() && v1 + returnRouteList.get(cur).get(1) <= maxTravelDist) {
                cur++;
            }
            cur--;
            if (cur < 0) {
                continue;
            }
            int diff = maxTravelDist - v1 - returnRouteList.get(cur).get(1);
            if (!map.containsKey(diff)) {
                map.put(diff, new ArrayList<>());
            }
            List<Integer> item = new ArrayList<>();
            item.add(forwardRouteList.get(i).get(0));
            item.add(returnRouteList.get(cur).get(0));
            map.get(diff).add(item);
            while (cur > 0
                    && returnRouteList.get(cur).get(1).intValue() == returnRouteList.get(cur - 1).get(1).intValue()) {
                List<Integer> tmp = new ArrayList<>();
                tmp.add(forwardRouteList.get(i).get(0));
                tmp.add(returnRouteList.get(cur - 1).get(0));
                cur--;
                map.get(diff).add(tmp);
            }
        }
        if (map.isEmpty()) {
            return new ArrayList<>();
        }

        int minKey = Integer.MAX_VALUE;
        for (int key : map.keySet()) {
            if (key < minKey) {
                minKey = key;
            }
        }
        return map.get(minKey);
    }

    int resMap = 0;

    int getMaxLen(Map<String, List<String>> map) {
        for (String key : map.keySet()) {
            Set<String> visited = new HashSet<>();
            visited.add(key);
            dfsMap(key, 0, map, visited);
        }
        return resMap;
    }

    void dfsMap(String key, int cnt, Map<String, List<String>> map, Set<String> visited) {
        cnt++;
        resMap = Math.max(resMap, cnt);
        System.out.println(visited);
        if (!map.containsKey(key)) {
            return;
        }
        for (String s : map.get(key)) {
            if (!visited.contains(s)) {
                visited.add(s);
                dfsMap(s, cnt, map, visited);
                visited.remove(s);
            }
        }
    }

    /**
     * You are given an undirected connected graph. An articulation point (or cut vertex) is defined as a vertex which,
     * when removed along with associated edges, makes the graph disconnected (or more precisely, increases the number
     * of connected components in the graph). The task is to find all articulation points in the given graph.
     * 
     * Input: The input to the function/method consists of three arguments:
     * 
     * numNodes, an integer representing the number of nodes in the graph. numEdges, an integer representing the number
     * of edges in the graph. edges, the list of pair of integers - A, B representing an edge between the nodes A and B.
     * 
     */
    public List<Integer> getCriticalRouters(int numNodes, int numEdges, int[][] edges) {

        // construct graph
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int i = 0; i < numNodes; i++)
            graph.put(i, new HashSet<>());
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
        }

        List<Integer> result = new ArrayList<>();

        // calculate critical routers
        for (int nodeToRemove = 0; nodeToRemove < numNodes; nodeToRemove++) {

            // remove each node and its edges and check if entire graph is connected
            Set<Integer> nodeEdges = graph.get(nodeToRemove);
            int start = 0;
            for (int v : nodeEdges) {
                graph.get(v).remove(nodeToRemove);
                start = v;
            }

            HashSet<Integer> visited = new HashSet<>();
            dfs(graph, start, visited);

            if (!visited.containsAll(nodeEdges)) {
                // this node was a critical router
                result.add(nodeToRemove);
            }

            // add the edges back
            for (int v : nodeEdges)
                graph.get(v).add(nodeToRemove);
        }
        return result;
    }

    public void dfs(Map<Integer, Set<Integer>> graph, int u, Set<Integer> visited) {
        if (visited.contains(u))
            return;
        visited.add(u);
        for (int v : graph.get(u)) {
            dfs(graph, v, visited);
        }
    }

    class Solution1268 {
        public List<List<String>> suggestedProducts(String[] products, String searchWord) {
            Arrays.sort(products);
            Node root = new Node();
            for (String p : products) {
                fill(root, p, 0);
            }
            List<List<String>> res = new ArrayList<>();
            Node cur = root;
            for (char c : searchWord.toCharArray()) {
                cur = cur.child[c - 'a'];
                if (cur == null) {
                    res.add(new ArrayList<>());
                    break;
                } else {
                    res.add(cur.word);
                }
            }
            while (res.size() < searchWord.length()) {
                res.add(new ArrayList<>());
            }
            return res;
        }

        void fill(Node root, String p, int idx) {
            if (idx == p.length())
                return;
            Node cur = root.child[p.charAt(idx) - 'a'];
            if (cur == null) {
                cur = new Node();
                root.child[p.charAt(idx) - 'a'] = cur; // missed
            }
            if (cur.size < 3) {
                cur.size++;
                cur.word.add(p);
            }
            fill(cur, p, idx + 1);
        }

        class Node {
            Node[] child = new Node[26];
            int size = 0;
            List<String> word = new ArrayList<>();
        }
    }

    public String[] reorderLogFiles(String[] logs) {

        Arrays.sort(logs, (log1, log2) -> {
            String[] split1 = log1.split(" ", 2);
            String[] split2 = log2.split(" ", 2);
            boolean isDigit1 = Character.isDigit(split1[1].charAt(0));
            boolean isDigit2 = Character.isDigit(split2[1].charAt(0));
            if (isDigit1 && isDigit2) {
                return 0;
            }
            if (!isDigit1 && !isDigit2) {
                int cmp = split1[1].compareTo(split2[1]);
                return cmp != 0 ? cmp : split1[0].compareTo(split2[0]);
            }
            return isDigit1 ? 1 : -1;
        });
        return logs;
    }

    // Optimal Utilization
    public List<int[]> getPairs(List<int[]> a, List<int[]> b, int target) {
        Collections.sort(a, (i, j) -> i[1] - j[1]);
        Collections.sort(b, (i, j) -> i[1] - j[1]);
        List<int[]> result = new ArrayList<>();
        int max = Integer.MIN_VALUE;
        int m = a.size();
        int n = b.size();
        int i = 0;
        int j = n - 1;
        while (i < m && j >= 0) {
            int sum = a.get(i)[1] + b.get(j)[1];
            if (sum > target) {
                --j;
            } else {
                if (max <= sum) {
                    if (max < sum) {
                        max = sum;
                        result.clear();
                    }
                    result.add(new int[] { a.get(i)[0], b.get(j)[0] });
                    int index = j - 1;
                    while (index >= 0 && b.get(index)[1] == b.get(index + 1)[1]) {
                        result.add(new int[] { a.get(i)[0], b.get(index--)[0] });
                    }
                }
                ++i;
            }
        }
        return result;
    }

    // 542. 01 Matrix
    public int[][] updateMatrix(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;

        ArrayDeque<int[]> queue = new ArrayDeque<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    queue.add(new int[] { i, j });
                } else {
                    matrix[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] dirs = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        while (!queue.isEmpty()) {
            int[] cell = queue.remove();
            for (int[] d : dirs) {
                int r = cell[0] + d[0];
                int c = cell[1] + d[1];
                if (r < 0 || r >= m || c < 0 || c >= n || matrix[r][c] <= matrix[cell[0]][cell[1]] + 1)
                    continue;
                queue.add(new int[] { r, c });
                matrix[r][c] = matrix[cell[0]][cell[1]] + 1;
            }
        }

        return matrix;
    }

    List<String> fnword(String helpText, List<String> ext) {
        String[] words = helpText.toLowerCase().split("[\\d||\\W]+");
        if (words.length == 0) {
            return null;
        }
        return null;
    }

    @Test
    public void test2() {

        String[] words = " yd6uy.2c5".split("[\\d||\\W]+");
        String[] words2 = " ydf6uy.2c5".split("[^a-z]+");
        words.equals(words2);
        Solution1268 sol = new Solution1268();
        // sol.suggestedProducts(new String[] { "mobile", "mouse", "moneypot",
        // "monitor", "mousepad" }, "m");
        sol.suggestedProducts(new String[] { "mobile" }, "mobile");

        int numRouters1 = 7;
        int numLinks1 = 7;
        int[][] links1 = { { 0, 1 }, { 0, 2 }, { 1, 3 }, { 2, 3 }, { 2, 5 }, { 5, 6 }, { 3, 4 } };

        List<Integer> res = getCriticalRouters(numRouters1, numLinks1, links1);

        for (int i : res)
            System.out.print(i + " ");
        System.out.println();

        int numRouters2 = 5;
        int numLinks2 = 5;
        int[][] links2 = { { 1, 2 }, { 0, 1 }, { 2, 0 }, { 0, 3 }, { 3, 4 } };

        List<Integer> res2 = getCriticalRouters(numRouters2, numLinks2, links2);
        for (int i : res2)
            System.out.print(i + " ");
        System.out.println();

        int numRouters3 = 4;
        int numLinks3 = 4;
        int[][] links3 = { { 0, 1 }, { 1, 2 }, { 2, 3 } };

        List<Integer> res3 = getCriticalRouters(numRouters3, numLinks3, links3);
        for (int i : res3)
            System.out.print(i + " ");
        System.out.println();

        int numRouters4 = 7;
        int numLinks4 = 8;
        int[][] links4 = { { 0, 1 }, { 0, 2 }, { 1, 2 }, { 1, 3 }, { 1, 4 }, { 1, 6 }, { 3, 5 }, { 4, 5 } };

        List<Integer> res4 = getCriticalRouters(numRouters4, numLinks4, links4);
        for (int i : res4)
            System.out.print(i + " ");
        System.out.println();
    }

    // @Test
    public void test() {
        Map<String, List<String>> map = new HashMap<>();
        map.put("A", Arrays.asList("B", "D"));
        map.put("B", Arrays.asList("C"));
        map.put("C", Arrays.asList("A"));
        map.put("D", Arrays.asList("E"));
        System.out.println(getMaxLen(map));
        List<List<Integer>> forwardRouteList = new ArrayList<>();
        List<List<Integer>> returnRouteList = new ArrayList<>();
        List<Integer> li = new ArrayList<>();
        li.add(1);
        li.add(5);
        forwardRouteList.add(li);
        forwardRouteList.add(li);
        forwardRouteList.add(li);
        forwardRouteList.add(li);
        returnRouteList.add(li);
        returnRouteList.add(li);
        returnRouteList.add(li);
        System.out.println(optimalUtilization(10, forwardRouteList, returnRouteList));
    }

}

class Node {
    public int val;
    public Node left;
    public Node right;

    public Node(int x) {
        val = x;
    }

}
