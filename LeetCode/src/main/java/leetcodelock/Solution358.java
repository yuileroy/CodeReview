package leetcodelock;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Solution358 {
	// 358. Rearrange String k Distance Apart
	// Given a non-empty string s and an integer k, rearrange the string such that the same characters are at least distance k from each other.
	// All input strings are given in lowercase letters. If it is not possible to rearrange the string, return an empty string "".
	// Example 1:
	// Input: s = "aabbcc", k = 3
	// Output: "abcabc"
	// Explanation: The same letters are at least distance 3 from each other.

	public String rearrangeString(String s, int k) {
		if (k < 2 || s.length() < 2) {
			return s;
		}
		// greedy, fill max count char first
		// int[] valid to save first allowed index
		int[] count = new int[26];
		int[] valid = new int[26];
		int n = s.length();
		for (int i = 0; i < n; i++) {
			count[s.charAt(i) - 'a']++;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			int next = findNext(count, valid, i);
			if (next == -1)
				return "";
			sb.append((char) ('a' + next));
			count[next]--;
			valid[next] = i + k;
		}
		return sb.toString();
	}

	private int findNext(int[] count, int[] valid, int idx) {
		int next = -1, maxCnt = 0;
		for (int k = 0; k < count.length; k++) {
			if (count[k] > maxCnt && valid[k] <= idx) {
				next = k;
				maxCnt = count[k];
			}
		}
		return next;
	}

	public String rearrangeString2(String s, int k) {
		if (k < 2 || s.length() < 2) {
			return s;
		}
		int[] cnt = new int[26];
		for (char ch : s.toCharArray()) {
			cnt[ch - 'a']++;
		}

		// greedy, fill with max-count char first with batch size k
		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[1] == b[1] ? a[0] - b[0] : b[1] - a[1]);
		for (int i = 0; i < cnt.length; i++) {
			if (cnt[i] > 0) {
				pq.add(new int[] { i, cnt[i] });
			}
		}

		StringBuilder sb = new StringBuilder();
		int total = s.length();
		while (total > 0) {
			List<int[]> list = new ArrayList<>();
			int dist = 0;
			while (dist < k && total > 0) {
				// total > 0 but no valid char to fill in
				if (pq.isEmpty()) {
					return "";
				}
				int[] cur = pq.remove();
				sb.append((char) (cur[0] + 'a'));
				cur[1]--;
				total--;
				if (cur[1] > 0) {
					list.add(cur);
				}
				dist++;
			}
			// the order in this list remains same, k=2, abba will not happen
			pq.addAll(list);
		}
		return sb.toString();
	}
}
