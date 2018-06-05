package ocp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassLoaderCode {

    public static void main(String[] args) {

        System.out.println("class loader for HashMap: " + java.util.HashMap.class.getClassLoader());
        // System.out.println("class loader for DNSNameService: "
        // + sun.net.spi.nameservice.dns.DNSNameService.class.getClassLoader());
        System.out.println("class loader for this class: " + ClassLoaderCode.class.getClassLoader());
        System.out.println(com.mysql.jdbc.Blob.class.getClassLoader());

        // class loader for HashMap: null
        // class loader for DNSNameService: sun.misc.Launcher$ExtClassLoader@3d4eac69
        // class loader for this class: sun.misc.Launcher$AppClassLoader@2a139a55
        // sun.misc.Launcher$AppClassLoader@2a139a55
    }
}

// For example, in this diagram, 3 is a child of 1 and 2, and 5 is a child of 4:

// 1 2 4
// \ / / \
// 3 5 8
// \ / \ \
// 6 7 9

// Write a function that, for two given individuals in our dataset, returns true if and only if they share at least one
// ancestor.

// Sample input and output:
// parentChildPairs, 3, 8 => false
// parentChildPairs, 5, 8 => true
// parentChildPairs, 6, 8 => true

class Solution {
    public static void main(String[] args) {
        // Java
        int[][] parentChildPairs = new int[][] { { 1, 3 }, { 2, 3 }, { 3, 6 }, { 5, 6 }, { 5, 7 }, { 4, 5 }, { 4, 8 },
                { 8, 9 } };
        System.out.println(parentChildPairs(6, 9, parentChildPairs));
    }

    public static Set<Integer> getParents(int child, Map<Integer, Set<Integer>> cMap) {
        Set<Integer> res = new HashSet<>();
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(child);
        while (!q.isEmpty()) {
            int size = q.size();
            while (size-- > 0) {
                int e = q.removeFirst();
                res.add(e);
                if (cMap.containsKey(e)) {
                    for (int p : cMap.get(e)) {
                        q.addLast(p);
                    }
                }
            }
        }
        return res;
    }

    public static boolean parentChildPairs(int c1, int c2, int[][] input) {
        Map<Integer, Set<Integer>> pMap = new HashMap<>();
        Map<Integer, Set<Integer>> cMap = new HashMap<>();

        for (int[] e : input) {
            if (!pMap.containsKey(e[0])) {
                pMap.put(e[0], new HashSet<>());
            }
            pMap.get(e[0]).add(e[1]);

            if (!cMap.containsKey(e[1])) {
                cMap.put(e[1], new HashSet<>());
            }
            cMap.get(e[1]).add(e[0]);
        }

        Set<Integer> set1 = getParents(c1, cMap);
        Set<Integer> set2 = getParents(c2, cMap);
        System.out.println(set1);
        System.out.println(set2);
        int size1 = set1.size();
        set1.removeAll(set2);
        return size1 != set1.size();
    }

    public static List<List<Integer>> getData(int[][] input) {
        Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> pMap = new HashMap<>();
        Map<Integer, Integer> cMap = new HashMap<>();

        for (int[] e : input) {
            pMap.put(e[0], pMap.getOrDefault(e[0], 0) + 1);
            cMap.put(e[1], cMap.getOrDefault(e[1], 0) + 1);
            set.add(e[0]);
            set.add(e[1]);
        }

        Set<Integer> set1 = new HashSet<>();
        Set<Integer> set2 = new HashSet<>();

        //
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> li1 = new ArrayList<>();
        List<Integer> li2 = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : cMap.entrySet()) {
            if (entry.getValue() == 1) {
                li2.add(entry.getKey());
            }
        }
        set.removeAll(cMap.keySet());
        li1.addAll(set);

        res.add(li1);
        res.add(li2);
        return res;
    }
}
