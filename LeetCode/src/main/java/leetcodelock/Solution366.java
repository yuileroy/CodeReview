package leetcodelock;

import java.util.ArrayList;
import java.util.List;

import keycode.util.ListNode;
import leetcode.TreeNode;

// KEY_CODE
public class Solution366 {

	// 366. Find Leaves of Binary Tree
	// Given a binary tree, collect a tree's nodes as if you were doing this: Collect and remove all leaves, repeat until the tree is empty.
	public List<List<Integer>> findLeaves(TreeNode root) {
		List<List<Integer>> res = new ArrayList<>();
		depth(root, res);
		return res;
	}

	int depth(TreeNode root, List<List<Integer>> res) {
		if (root == null) {
			return -1;
		}
		int lv = 1 + Math.max(depth(root.left, res), depth(root.right, res));
		if (res.size() == lv) {
			res.add(new ArrayList<>());
		}
		res.get(lv).add(root.val);
		return lv;
	}

	// v2
	public List<List<Integer>> findLeaves2(TreeNode root) {
		List<List<Integer>> res = new ArrayList<>();
		while (root != null) {
			List<Integer> temp = new ArrayList<>();
			root = removeLeaves(root, temp);
			res.add(temp);
		}
		return res;
	}

	private TreeNode removeLeaves(TreeNode root, List<Integer> temp) {
		if (root == null)
			return null;
		if (root.left == null && root.right == null) {
			temp.add(root.val);
			return null;
		}
		root.left = removeLeaves(root.left, temp);
		root.right = removeLeaves(root.right, temp);
		return root;
	}

	// 369. Plus One Linked List
	public ListNode plusOne(ListNode head) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		// nNine is the last node.val != 9
		ListNode cur = dummy, nNine = dummy;
		while (cur.next != null) {
			cur = cur.next;
			if (cur.val != 9) {
				nNine = cur;
			}
		}
		// 7->9->9 <=> 8->0->0
		nNine.val += 1;
		cur = nNine;
		while (cur.next != null) {
			cur = cur.next;
			cur.val = 0;
		}
		return dummy == nNine ? dummy : dummy.next;
	}

	// v2
	public ListNode plusOne2(ListNode head) {
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		// nNine is a node val!=9 and every following node val=9
		ListNode cur = dummy, nNine = null;
		while (cur.next != null) {
			if (cur.next.val == 9 && nNine == null) {
				nNine = cur;
			}
			if (cur.next.val != 9) {
				nNine = null;
			}
			cur = cur.next;
		}
		// cur is now last node
		if (cur.val != 9) {
			cur.val += 1;
			return dummy.next;
		}
		// 7->9->9 <=> 8->0->0
		nNine.val += 1;
		cur = nNine;
		while (cur.next != null) {
			cur = cur.next;
			cur.val = 0;
		}
		return dummy == nNine ? dummy : dummy.next;
	}

	// KEY_CODE
	// 370. Range Addition
	// Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
	// Output: [-2,0,3,5,3]
	public int[] getModifiedArray(int length, int[][] updates) {
		int[] A = new int[length];
		// +=[start], -=[end + 1]
		for (int[] e : updates) {
			A[e[0]] += e[2];
			if (e[1] + 1 < length) {
				A[e[1] + 1] -= e[2];
			}
		}
		int diff = 0;
		for (int i = 0; i < length; i++) {
			int tmp = diff;
			diff += A[i];
			A[i] += tmp;
		}
		return A;
	}
}
