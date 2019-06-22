package leetcode;

import java.util.Stack;

public class LockedQ {

	public TreeNode upsideDownBinaryTree(TreeNode root) {
		if (root == null || root.left == null) {
			return root;
		}
		Stack<TreeNode> stack = new Stack<>();
		TreeNode cur = root;
		while (cur != null) {
			stack.push(cur);
			cur = cur.left;
		}

		TreeNode res = stack.pop();
		cur = res;
		while (!stack.isEmpty()) {
			TreeNode pre = stack.pop();
			cur.left = pre.right;
			cur.right = pre;
			cur = pre;
		}
		cur.left = null;
		cur.right = null;
		return res;
	}

	public TreeNode upsideDownBinaryTree2(TreeNode root) {
		if (root == null || root.left == null) {
			return root;
		}
		return dfs156(root);
	}

	public TreeNode dfs156(TreeNode root) {
		if (root.left == null)
			return root;
		// return the newRoot from bottom of the tree to the top
		TreeNode newRoot = dfs156(root.left);
		root.left.left = root.right;
		root.left.right = root;
		root.right = null;
		root.left = null;
		return newRoot;
	}

	/*
	 * The read4 API is defined in the parent class Reader4. int read4(char[] buf);
	 */

	class Reader4 {
		int read4(char[] tmp) {
			return 0;
		}
	}

	public class Solution157 extends Reader4 {

		public int read(char[] buf, int n) {
			int res = 0;
			boolean end = false;
			char[] tmp = new char[4];
			while (!end && res < n) {
				int val = read4(tmp);
				if (val < 4) {
					end = true;
				}
				int items = Math.min(n - res, val);
				for (int i = 0; i < items; i++) {
					buf[res + i] = tmp[i];
				}
				res += items;
			}
			return res;
		}
	}

	public class Solution158 extends Reader4 {

		private char[] tmp = new char[4];
		private int preIdx = 0;
		private int preVal = 0;

		public int read(char[] buf, int n) {
			int res = 0;
			while (res < n) {
				if (preIdx < preVal) {
					buf[res++] = tmp[preIdx++];
				} else {
					preVal = read4(tmp);
					preIdx = 0;
					if (preVal == 0) {
						break;
					}
				}
			}
			return res;
		}
	}
}
