package leetcodelock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Solution266 {

	// 266. Palindrome Permutation

	public boolean canPermutePalindrome(String s) {
		int[] map = new int[128];
		for (int i = 0; i < s.length(); i++) {
			map[s.charAt(i)]++;
		}
		int count = 0;
		for (int key = 0; key < map.length && count <= 1; key++) {
			count += map[key] % 2;
		}
		return count <= 1;
	}

	public boolean canPermutePalindrome2(String s) {
		Set<Character> set = new HashSet<>();
		for (char c : s.toCharArray())
			if (set.contains(c))
				set.remove(c);
			else
				set.add(c);
		return set.size() <= 1;
	}

	/**
	 * 267. Palindrome Permutation II
	 */
	public List<String> generatePalindromes(String s) {
		int odd = 0;
		String mid = "";
		List<String> res = new ArrayList<>();
		List<Character> list = new ArrayList<>();
		Map<Character, Integer> map = new HashMap<>();
		// step 1. build character count map and count odds
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			map.put(c, map.containsKey(c) ? map.get(c) + 1 : 1);
			odd += map.get(c) % 2 != 0 ? 1 : -1;
		}
		// cannot form any palindromic string
		if (odd > 1)
			return res;
		// step 2. add half count of each character to list
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			char key = entry.getKey();
			int val = entry.getValue();
			if (val % 2 != 0)
				mid += key;
			for (int i = 0; i < val / 2; i++)
				list.add(key);
		}
		// step 3. generate all the permutations
		getPerm(list, mid, new boolean[list.size()], new StringBuilder(), res);
		return res;
	}

	void getPerm(List<Character> list, String mid, boolean[] used, StringBuilder sb, List<String> res) {
		if (sb.length() == list.size()) {
			res.add(sb.toString() + mid + sb.reverse().toString());
			sb.reverse();
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			// avoid duplication, ------!used[i - 1]------
			if (i > 0 && list.get(i) == list.get(i - 1) && !used[i - 1])
				continue;
			if (!used[i]) {
				used[i] = true;
				sb.append(list.get(i));
				getPerm(list, mid, used, sb, res);
				used[i] = false;
				sb.deleteCharAt(sb.length() - 1);
			}
		}
	}
}

/**
 * Given a collection of numbers that might contain duplicates, return all possible unique permutations.
 */
class Solution47 {
	public List<List<Integer>> permuteUnique(int[] nums) {
		List<List<Integer>> res = new ArrayList<>();
		if (nums == null || nums.length == 0) {
			return res;
		}
		Arrays.sort(nums);
		helper47(nums, new boolean[nums.length], new ArrayList<Integer>(), res);
		return res;
	}

	private void helper47(int[] nums, boolean[] used, List<Integer> item, List<List<Integer>> res) {
		if (item.size() == nums.length) {
			res.add(new ArrayList<Integer>(item));
			return;
		}
		for (int i = 0; i < nums.length; i++) {
			// 1, 2, 2, 3 -> skip second 2
			if (i > 0 && nums[i - 1] == nums[i] && !used[i - 1])
				continue;
			if (!used[i]) {
				used[i] = true;
				item.add(nums[i]);
				helper47(nums, used, item, res);
				item.remove(item.size() - 1);
				used[i] = false;
			}
		}
	}
}
