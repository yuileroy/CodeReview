package javacode.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class UnionFind {
	public List<List<String>> accountsMerge(List<List<String>> acts) {
		Map<String, String> owner = new HashMap<>();
		Map<String, String> parents = new HashMap<>();
		Map<String, TreeSet<String>> unions = new HashMap<>();
		for (List<String> a : acts) {
			for (int i = 1; i < a.size(); i++) {
				parents.put(a.get(i), a.get(i));
				owner.put(a.get(i), a.get(0));
			}
		}
		for (List<String> a : acts) {
			String p = find(a.get(1), parents);
			for (int i = 2; i < a.size(); i++) {
				parents.put(find(a.get(i), parents), p);
			}
		}

		for (String s : parents.keySet()) {
			String p = find(s, parents);
			if (!unions.containsKey(p)) {
				unions.put(p, new TreeSet<>());
			}
			unions.get(p).add(s);
		}

		List<List<String>> res = new ArrayList<>();
		for (String p : unions.keySet()) {
			List<String> emails = new ArrayList<>(unions.get(p));
			emails.add(0, owner.get(p));
			res.add(emails);
		}
		return res;
	}

	private String find(String s, Map<String, String> p) {
		return p.get(s) == s ? s : find(p.get(s), p);
	}
}
