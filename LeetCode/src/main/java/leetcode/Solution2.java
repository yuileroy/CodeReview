package leetcode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution2 {
	class Node {
		public int val;
		public Node prev;
		public Node next;
		public Node child;

		public Node() {
		}

		public Node(int _val, Node _prev, Node _next, Node _child) {
			val = _val;
			prev = _prev;
			next = _next;
			child = _child;
		}
	}

	class Solution {
		public Node flatten(Node head) {
			fn(head);
			return head;
		}

		// return the last node
		Node fn(Node head) {
			Node cur = head;
			while (cur != null && cur.next != null) {
				if (cur.child != null) {
					Node oNext = cur.next;
					Node end = handle(cur);
					end.next = oNext;
					oNext.prev = end;
					cur = oNext;
				} else {
					cur = cur.next;
				}
			}
			if (cur.next == null && cur.child != null) {
				Node end = handle(cur);
				return end;
			}
			return cur;
		}

		// handle child link
		Node handle(Node head) {
			Node c = head.child;
			head.next = c;
			c.prev = head;
			head.child = null;
			return fn(c);
		}
	}

	public int minMutation(String start, String end, String[] bank) {
		Set<String> set = new HashSet<>(Arrays.asList(bank));
		if (!set.contains(end)) {
			return -1;
		}
		int res = 0;
		char[] ge = new char[] { 'A', 'C', 'G', 'T' };
		Set<String> curSet = new HashSet<>();
		curSet.add(start);
		while (curSet.size() != 0) {
			res++;
			curSet = getMutations(curSet, set, ge);
			if (curSet.contains(end)) {
				return res;
			}
		}
		return -1;
	}

	Set<String> getMutations(Set<String> curSet, Set<String> set, char[] ge) {
		Set<String> res = new HashSet<>();
		for (String start : curSet) {
			char[] A = start.toCharArray();
			for (int i = 0; i < A.length; i++) {
				char c = A[i];
				for (char r : ge) {
					if (r != c) {
						A[i] = r;
					}
					String s = String.valueOf(A);
					if (set.contains(s)) {
						res.add(s);
						set.remove(s);
					}
				}
				A[i] = c;
			}
		}
		return res;
	}
}
