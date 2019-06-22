package leetcodelock;

import java.util.ArrayList;
import java.util.List;

public class Solution422 {

	public boolean validWordSquare(List<String> words) {
		if (words == null || words.size() == 0) {
			return true;
		}
		int n = words.size();
		for (int i = 0; i < n; i++) {
			String s = words.get(i);
			for (int j = 0; j < s.length(); j++) {
				if (j >= n || i >= words.get(j).length() || words.get(j).charAt(i) != s.charAt(j))
					return false;
			}
		}
		return true;
	}

	public boolean validWordSquare2(List<String> words) {
		if (words == null || words.size() == 0) {
			return true;
		}
		int n = words.size();
		// valid length first
		for (int i = 0; i < n; i++) {
			for (int j = i + 1; j < words.get(i).length(); j++) {
				if (j >= n || i >= words.get(j).length())
					return false;
			}
		}
		for (int i = 0; i < n - 1; i++) {
			String s = words.get(i);
			for (int j = 1; j < s.length(); j++) {
				if (words.get(j).charAt(i) != s.charAt(j))
					return false;
			}
		}
		return true;
	}

	// 425. Word Squares
	class Node {
		List<String> list = new ArrayList<>();
		Node[] child = new Node[26];
	}

	List<List<String>> res = new ArrayList<>();

	public List<List<String>> wordSquares(String[] words) {
		Node root = new Node();
		for (String word : words) {
			Node cur = root;
			for (Character ch : word.toCharArray()) {
				cur.list.add(word);
				if (cur.child[ch - 'a'] == null) {
					cur.child[ch - 'a'] = new Node();
				}
				cur = cur.child[ch - 'a'];
			}
		}
		dfs(new ArrayList<>(), root, words[0].length());
		return res;
	}

	void dfs(List<String> item, Node root, int n) {
		// System.out.println("item: " + item);
		if (item.size() == n) {
			res.add(new ArrayList<>(item));
			return;
		}
		List<String> valid = findPre(root, item);
		// System.out.println("valid: " + valid);
		for (String s : valid) {
			item.add(s);
			dfs(item, root, n);
			item.remove(item.size() - 1);
		}
	}

	List<String> findPre(Node root, List<String> item) {
		Node cur = root;
		int size = item.size();
		for (String s : item) {
			int idx = s.charAt(size) - 'a';
			if (cur.child[idx] == null) {
				return new ArrayList<String>();
			}
			cur = cur.child[idx];
		}
		return cur.list;
	}
}
