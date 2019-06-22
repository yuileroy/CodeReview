package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordAbbreviation {

	// 320. Generalized Abbreviation
	List<String> res = new ArrayList<>();

	public List<String> generateAbbreviations(String word) {
		// dfs(0, new StringBuilder(), word);
		// dfs(0, "", word);
		backtrack(0, "", word);
		return res;
	}

	public void backtrack(int start, String item, String word) {
		for (int i = start; i < word.length(); i++) {
			String abbr = i == start ? "" : i - start + "";
			backtrack(i + 1, item + abbr + word.charAt(i), word);
		}
		res.add(item + (word.length() == start ? "" : word.length() - start));
	}

	// V1
	void dfs(int start, StringBuilder sb, String word) {
		if (start >= word.length()) {
			res.add(sb.toString());
			return;
		}

		for (int i = start; i < word.length(); i++) {
			int len = sb.length();
			// 1o + dfs, 2r + dfs, 3d + dfs, 4
			sb.append(i - start + 1);
			if (i < word.length() - 1) {
				sb.append(word.charAt(i + 1));
			}
			dfs(i + 2, sb, word);
			// w + dfs
			if (i == start) {
				sb.setLength(len);
				dfs(i + 1, sb.append(word.charAt(i)), word);
			}
			sb.setLength(len);
		}
	}

	// V2
	void dfs(int start, String item, String word) {
		if (start >= word.length()) {
			res.add(item);
			return;
		}
		for (int i = start; i < word.length(); i++) {
			String item1 = item + (i - start + 1);
			if (i < word.length() - 1) {
				item1 += word.charAt(i + 1);
			}
			dfs(i + 2, item1, word);

			if (i == start) {
				dfs(i + 1, item + word.charAt(i), word);
			}
		}
	}

	// 408. Valid Word Abbreviation
	public boolean validWordAbbreviation(String word, String abbr) {
		int i = 0, j = 0;

		while (i < word.length() && j < abbr.length()) {
			if (!Character.isDigit(abbr.charAt(j))) {
				if (word.charAt(i) != abbr.charAt(j)) {
					return false;
				}
				i++;
				j++;
			} else {
				if (abbr.charAt(j) == '0') {
					return false;
				}
				int n = 0;
				while (j < abbr.length() && Character.isDigit(abbr.charAt(j))) {
					n = n * 10 + abbr.charAt(j) - '0';
					j++;
				}
				i += n;
			}
		}
		return i == word.length() && j == abbr.length();
	}

	// V2
	public boolean validWordAbbreviation2(String word, String abbr) {
		int idx = 0;
		for (int i = 0; i < abbr.length(); i++) {
			// word reached end but abbr doesn't
			if (idx >= word.length()) {
				return false;
			}
			char c = abbr.charAt(i);
			if (c == '0') {
				return false;
			}
			if (Character.isDigit(c)) {
				int n = 0;
				while (i < abbr.length() && Character.isDigit(abbr.charAt(i))) {
					// n = n * 10 + c - '0';
					n = n * 10 + abbr.charAt(i) - '0';
					i++;
				}
				i--;
				idx += n;
			} else {
				if (word.charAt(idx) != abbr.charAt(i)) {
					return false;
				}
				idx += 1;
			}
		}
		return idx == word.length();
	}
}

// KEY_CODE
// 527. Word Abbreviation
class Solution527 {
	// util class and method
	class TrieNode {
		TrieNode[] children;
		int count;

		TrieNode() {
			children = new TrieNode[26];
			count = 0;
		}
	}

	class IndexedWord {
		String word;
		int index;

		IndexedWord(String w, int i) {
			word = w;
			index = i;
		}
	}

	public String abbrev(String word, int i) {
		int N = word.length();
		if (N - i <= 3)
			return word;
		return word.substring(0, i + 1) + (N - i - 2) + word.charAt(N - 1);
	}

	public int longestCommonPrefix(String word1, String word2) {
		int i = 0;
		while (i < word1.length() && i < word2.length() && word1.charAt(i) == word2.charAt(i))
			i++;
		return i;
	}

	// V1, Trie tree
	public List<String> wordsAbbreviation(List<String> words) {
		Map<String, List<IndexedWord>> groups = new HashMap<>();
		String[] ans = new String[words.size()];

		int index = 0;
		for (String word : words) {
			String ab = abbrev(word, 0);
			if (!groups.containsKey(ab))
				groups.put(ab, new ArrayList<>());
			groups.get(ab).add(new IndexedWord(word, index));
			index++;
		}

		for (List<IndexedWord> group : groups.values()) {
			TrieNode trie = new TrieNode();
			for (IndexedWord iw : group) {
				TrieNode cur = trie;
				for (char letter : iw.word.substring(1).toCharArray()) {
					if (cur.children[letter - 'a'] == null)
						cur.children[letter - 'a'] = new TrieNode();
					cur.count++;
					cur = cur.children[letter - 'a'];
				}
			}

			for (IndexedWord iw : group) {
				TrieNode cur = trie;
				int i = 1;
				for (char letter : iw.word.substring(1).toCharArray()) {
					if (cur.count == 1)
						break;
					cur = cur.children[letter - 'a'];
					i++;
				}
				ans[iw.index] = abbrev(iw.word, i - 1);
			}
		}
		return Arrays.asList(ans);
	}

	// V2, map to store dup count
	public List<String> wordsAbbreviation2(List<String> dict) {

		int n = dict.size();
		String[] res = new String[n];
		Map<String, Integer> map = new HashMap<>();

		for (int i = 0; i < n; i++) {
			res[i] = abbrev(dict.get(i), 0);
			map.put(res[i], map.getOrDefault(res[i], 0) + 1);
		}

		int round = 0;
		while (true) {
			boolean hasDup = false;
			round++;
			for (int i = 0; i < n; i++) {
				if (map.get(res[i]) > 1) {
					res[i] = abbrev(dict.get(i), round);
					map.put(res[i], map.getOrDefault(res[i], 0) + 1);
					hasDup = true;
				}
			}
			if (!hasDup) {
				break;
			}
		}
		return Arrays.asList(res);
	}

	// V3, sort and longestCommonPrefix
	public List<String> wordsAbbreviation3(List<String> words) {
		Map<String, List<IndexedWord>> groups = new HashMap<>();
		String[] ans = new String[words.size()];

		int index = 0;
		for (String word : words) {
			String ab = abbrev(word, 0);
			if (!groups.containsKey(ab))
				groups.put(ab, new ArrayList<>());
			groups.get(ab).add(new IndexedWord(word, index));
			index++;
		}

		// each group has same length()
		for (List<IndexedWord> group : groups.values()) {
			Collections.sort(group, (a, b) -> a.word.compareTo(b.word));

			int[] lcp = new int[group.size()];
			for (int i = 1; i < group.size(); ++i) {
				int p = longestCommonPrefix(group.get(i - 1).word, group.get(i).word);
				lcp[i] = p;
				lcp[i - 1] = Math.max(lcp[i - 1], p);
			}

			for (int i = 0; i < group.size(); ++i)
				ans[group.get(i).index] = abbrev(group.get(i).word, lcp[i]);
		}

		return Arrays.asList(ans);
	}
}

// 411. Minimum Unique Word Abbreviation
class Solution411 {
	private int minL;// Min length of the result
	private int result;// Result int
	private Set<Integer> set;

	public String minAbbreviation(String target, String[] dictionary) {
		set = new HashSet<>();
		// Generate diff-numbers
		for (String s : dictionary) {
			if (s.length() == target.length()) {
				set.add(getBitMask(s, target));
			}
		}
		// return if all words are of different length
		if (set.size() == 0)
			return target.length() + "";

		minL = target.length() + 1;
		result = -1;
		// generate all possible abbreviation
		backTrack(0, target.length(), 0, 0);
		// Construct result string using result int
		int number = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < target.length(); i++) {
			if (((result >> (target.length() - i - 1)) & 1) == 1) {
				sb.append(number > 0 ? number : "").append(target.charAt(i));
				number = 0;
			} else
				number++;
		}
		return sb.append(number > 0 ? number : "").toString();
	}

	// curL is to keep track of the length of current abbreviation
	public void backTrack(int cur, int l, int res, int curL) {
		if (cur == l) {
			if (curL < minL) {
				// check whether the abbr is valid
				for (int n : set) {
					if ((res & n) == 0)
						return;
				}
				minL = curL;
				result = res;
			}
		} else {
			if ((res & 1) == 1 || cur == 0)
				backTrack(cur + 1, l, res << 1, curL + 1);
			else
				backTrack(cur + 1, l, res << 1, curL);
			backTrack(cur + 1, l, (res << 1) + 1, curL + 1);
		}
	}

	public int getBitMask(String s, String t) {
		int mask = 0;
		for (int i = 0; i < s.length(); i++) {
			mask = mask << 1;
			if (s.charAt(i) != t.charAt(i))
				mask += 1;
		}
		return mask;
	}
}
