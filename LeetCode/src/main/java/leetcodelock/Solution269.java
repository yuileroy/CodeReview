package leetcodelock;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

//269. Alien Dictionary

class Solution269 {
	private final int N = 26;

	public String alienOrder(String[] words) {
		boolean[][] adj = new boolean[N][N];
		int[] visited = new int[N];
		buildGraph(words, adj, visited);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < N; i++) {
			if (visited[i] == 0) { // unvisited, set value to 2 after each run
				if (!dfs(adj, visited, sb, i))
					return "";
			}
		}
		return sb.reverse().toString();
	}

	public void buildGraph(String[] words, boolean[][] adj, int[] visited) {
		Arrays.fill(visited, -1); // -1 = not even existed
		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray())
				visited[c - 'a'] = 0;

			if (i > 0) {
				String w1 = words[i - 1], w2 = words[i];
				int len = Math.min(w1.length(), w2.length());
				for (int j = 0; j < len; j++) {
					char c1 = w1.charAt(j), c2 = w2.charAt(j);
					if (c1 != c2) {
						adj[c1 - 'a'][c2 - 'a'] = true;
						break;
					}
				}
			}
		}
	}

	public boolean dfs(boolean[][] adj, int[] visited, StringBuilder sb, int i) {
		visited[i] = 1; // 1 = visiting
		for (int j = 0; j < N; j++) {
			if (adj[i][j]) { // connected
				if (visited[j] == 1) // 1 => 1, cycle
					return false;
				if (visited[j] == 0) { // 0 = unvisited
					if (!dfs(adj, visited, sb, j))
						return false;
				}
			}
		}
		/**
		 * 
		 */
		visited[i] = 2; // 2 = visited
		sb.append((char) (i + 'a'));
		return true;
	}
}

class Solution269V2 {
	public String alienOrder(String[] words) {
		List<Set<Integer>> adj = new ArrayList<>();
		for (int i = 0; i < 26; i++) {
			adj.add(new HashSet<Integer>());
		}
		int[] degree = new int[26];
		Arrays.fill(degree, -1);

		for (int i = 0; i < words.length; i++) {
			for (char c : words[i].toCharArray()) {
				if (degree[c - 'a'] < 0) {
					degree[c - 'a'] = 0;
				}
			}
			if (i > 0) {
				String w1 = words[i - 1], w2 = words[i];
				int len = Math.min(w1.length(), w2.length());
				for (int j = 0; j < len; j++) {
					int c1 = w1.charAt(j) - 'a', c2 = w2.charAt(j) - 'a';
					if (c1 != c2) {
						if (!adj.get(c1).contains(c2)) {
							adj.get(c1).add(c2);
							degree[c2]++;
						}
						break;
					}
					// "abcd"->"ab"
					if (j == w2.length() - 1 && w1.length() > w2.length()) {
						return "";
					}
				}
			}
		}

		Queue<Integer> q = new ArrayDeque<>();
		for (int i = 0; i < degree.length; i++) {
			if (degree[i] == 0) {
				q.add(i);
			}
		}
		StringBuilder sb = new StringBuilder();
		while (!q.isEmpty()) {
			int i = q.remove();
			sb.append((char) ('a' + i));
			for (int j : adj.get(i)) {
				degree[j]--;
				if (degree[j] == 0) {
					q.add(j);
				}
			}
		}
		for (int d : degree) {
			if (d > 0) {
				return "";
			}
		}
		return sb.toString();
	}
}
