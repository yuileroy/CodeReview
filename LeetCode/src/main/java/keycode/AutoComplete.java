package keycode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class AutoComplete {
    class Node {
        String name = null;
        Node[] child = new Node[26];
    }

    class Obj {
        int rank;
        String source;
    }

    Node root = new Node();

    List<Obj> getJson() {
        TreeSet<Obj> set = new TreeSet<>((a, b) -> a.rank - b.rank);
        Obj o1 = new Obj();
        o1.rank = 2;
        o1.source = "s1";
        set.add(o1);
        Obj o2 = new Obj();
        o2.rank = 1;
        o2.source = "s2";
        set.add(o2);
        return new ArrayList<>(set);
    }

    List<String> getPrefix(String s) {
        List<String> res = new ArrayList<>();
        Node cur = root;
        for (char c : s.toCharArray()) {
            if (cur.child[c - 'a'] == null) {
                return res;
            }
            cur = cur.child[c - 'a'];
        }
        dfs(cur, res);
        return res;
    }

    void dfs(Node cur, List<String> res) {
        if (cur.name != null) {
            res.add(cur.name);
        }
        for (Node node : cur.child) {
            if (node != null) {
                dfs(node, res);
            }
        }
    }

    void buildTrie(Set<String> names) {
        for (String s : names) {
            Node cur = root;
            for (char c : s.toCharArray()) {
                if (cur.child[c - 'a'] == null) {
                    cur.child[c - 'a'] = new Node();
                }
                cur = cur.child[c - 'a'];
            }
            cur.name = s;
        }
    }

    @Test
    public void test() {
        Set<String> names = new HashSet<>(Arrays.asList("a", "abc", "abde", "abcd"));
        buildTrie(names);
        System.out.println(getPrefix("ab"));

        Solution sol = new Solution();
        sol.setCell("A1", "=B1+B2"); // 15
        sol.setCell("B1", "5");
        sol.setCell("B2", "10");
        sol.setCell("C2", "=A1+B1"); // 20 //=B1+B2+B1
        System.out.println(sol.getValue("C2"));
        sol.setCell("B2", "12");
        System.out.println(sol.getValue("C2"));
        //AssertTrue("a" == "a");
    }

    class Solution {

        // "12", "A1+B1"

        Map<String, String> map = new HashMap<>();
        Map<String, String> map2 = new HashMap<>();

        void setCell(String key, String value) {
            // if (value.charAt(0) == '=') {
            // map.put(key, value.subtring(1));
            // } else {
            // map.put(key, value);
            // }
            map.put(key, value);
            if (value.charAt(0) != '=') {
                value = key;
            }
            map2.put(key, value);
        }

        String simplify(String key) {
            String s = map2.get(key);
            if (s.charAt(0) != '=') {
                return s;
            }
            String[] token = s.substring(1).split("\\+");
            String res = "";
            for (String item : token) {
                res += "+" + simplify(item);
            }
            return res.substring(1);
        }

        int getValue(String key) {
            String s = map.get(key);
            if (s.charAt(0) != '=') {
                return Integer.parseInt(s);
            }
            s = simplify(key);
            System.out.println(s);
            int res = 0;
            String[] token = s.split("\\+");
            // String[] token = s.substring(1).split("\\+");
            for (String item : token) {
                res += getValue(item);
            }
            return res;
        }
    }

}
