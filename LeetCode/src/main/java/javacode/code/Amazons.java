package javacode.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

// 23280666774745 
//23280666774745
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

        // if (forwardRouteList.get(0).get(1) + returnRouteList.get(0).get(1) > maxTravelDist) {
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

    @Test
    public void test() {
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
