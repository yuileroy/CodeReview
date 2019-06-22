package leetcodelock;

import leetcode.TreeNode;

// 285. Inorder Successor in BST
// The successor of a node p is the node with the smallest key greater than p.val.
public class Solution285 {

	// ! see diff with inorderSuccessor2()
	public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
		TreeNode res = null, cur = root;
		while (cur != null) {
			if (cur.val <= p.val) {
				cur = cur.right;
			} else {
				res = cur;
				cur = cur.left;
			}
		}
		return res;
	}

	public TreeNode inorderSuccessor2(TreeNode root, TreeNode p) {
		TreeNode res = null, cur = root;

		while (cur != null) {
			if (cur == p) {
				cur = cur.right;
				while (cur != null) {
					res = cur;
					cur = cur.left;
				}
				return res;
			} else if (cur.val < p.val) {
				cur = cur.right;
			} else {
				res = cur;
				cur = cur.left;
			}
		}
		return res;
	}

	class Solution {
		TreeNode pre = null, res = null;

		public TreeNode inorderSuccessor(TreeNode root, TreeNode p) {
			if (root == null) {
				return null;
			}
			inOrder(root, p);
			return res;
		}

		// inOrder reverse actually
		void inOrder(TreeNode root, TreeNode p) {
			if (root == null) {
				return;
			}
			inOrder(root.right, p);
			if (root == p) {
				res = pre;
				return;
			}
			pre = root;
			inOrder(root.left, p);
		}
	}
}
