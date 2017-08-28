package oca;

import org.junit.Test;

import leetcode.TreeNode;

public class Solution114 {
	TreeNode pre = null;
	TreeNode dummy;

	public void flattenPreOrder(TreeNode root) {
		if (root == null) {
			return;
		}
		flattenPreOrder(root.left);
		if (pre == null) {
			pre = root;
			dummy = root;
		} else {
			pre.right = root;
			pre = root;
		}
		flattenPreOrder(root.right);
		root.left = null;
	}

	TreeNode lastvisited = null;

	public void flatten(TreeNode root) {
		if (root == null) {
			return;
		}

		if (lastvisited == null) {
			lastvisited = root;
		} else {
			lastvisited.right = root;
			lastvisited = root;
		}
		TreeNode realright = root.right;
		flatten(root.left);
		flatten(realright);
		root.left = null;
	}

	@Test
	public void test() {
		TreeNode t1 = new TreeNode(1);
		TreeNode t2 = new TreeNode(2);
		TreeNode t3 = new TreeNode(3);
		TreeNode t4 = new TreeNode(4);
		TreeNode t5 = new TreeNode(5);
		TreeNode t6 = new TreeNode(6);
		t1.left = t2;
		t2.left = t3;
		t2.right = t4;
		t1.right = t5;
		t5.right = t6;
		flattenPreOrder(t1);
		while (dummy != null) {
			System.out.println(dummy.val);
			dummy = dummy.right;
		}
	}

	@Test
	public void test2() {
		TreeNode t1 = new TreeNode(1);
		TreeNode t2 = new TreeNode(2);
		TreeNode t3 = new TreeNode(3);
		TreeNode t4 = new TreeNode(4);
		TreeNode t5 = new TreeNode(5);
		TreeNode t6 = new TreeNode(6);
		t1.left = t2;
		t2.left = t3;
		t2.right = t4;
		t1.right = t5;
		t5.right = t6;
		flattenPreOrder(t1);
		while (dummy != null) {
			System.out.println(dummy.val);
			dummy = dummy.right;
		}
	}
}
