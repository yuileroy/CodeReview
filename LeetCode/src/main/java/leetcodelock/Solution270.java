package leetcodelock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import leetcode.TreeNode;

// 270. Closest Binary Search Tree Value
public class Solution270 {
	public int closestValue(TreeNode root, double target) {
		int res = root.val;
		while (root != null) {
			if (Math.abs(target - root.val) < Math.abs(target - res)) {
				res = root.val;
			}
			if (target < root.val) {
				root = root.left;
			} else {
				root = root.right;
			}
		}
		return res;
	}
}

// 272. Closest Binary Search Tree Value II
class Solution272 {
	public List<Integer> closestKValues(TreeNode root, double target, int k) {
		List<Integer> res = new LinkedList<>();
		boolean[] found = new boolean[1];
		helper(root, target, k, res, found);
		return res;
	}

	private void helper(TreeNode root, double target, int k, List<Integer> res, boolean[] found) {
		if (root == null || found[0]) {
			return;
		}
		helper(root.left, target, k, res, found);
		if (res.size() == k) {
			if (Math.abs(root.val - target) < Math.abs(res.get(0) - target)) {
				res.remove(0);
				res.add(root.val);
			} else {
				found[0] = true;
				return;
			}
		} else {
			res.add(root.val);
		}
		helper(root.right, target, k, res, found);
	}
}

// 271. Encode and Decode Strings
class Codec {

	// Encodes a list of strings to a single string.
	public String encode(List<String> strs) {
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			sb.append(s.length());
			sb.append('#');
			sb.append(s);
		}
		return sb.toString();
	}

	// Decodes a single string to a list of strings.
	public List<String> decode(String s) {
		List<String> list = new ArrayList<>();
		if (s.length() == 0) {
			return list;
		}
		int i = 0;
		while (i < s.length()) {
			int len = 0;
			while (s.charAt(i) != '#') {
				len = len * 10 + s.charAt(i) - '0';
				i++;
			}
			i++;
			list.add(s.substring(i, i + len));
			i += len;
		}
		return list;
	}

}
