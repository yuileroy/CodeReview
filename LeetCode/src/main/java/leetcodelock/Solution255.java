package leetcodelock;

import java.util.Stack;

// 255. Verify Preorder Sequence in Binary Search Tree
// Input: [5,2,6,1,3]
// Output: false
// Input: [5,2,1,3,6]
// Output: true
public class Solution255 {

	// 1. modify array to avoid stack
	// 2. stack, pop() is inorder
	// 3. recursive, inefficient

	public boolean verifyPreorder(int[] preorder) {
		int low = Integer.MIN_VALUE, i = -1;
		for (int p : preorder) {
			if (p < low)
				return false;
			while (i > -1 && p > preorder[i])
				low = preorder[i--];
			preorder[++i] = p;
		}
		return true;
	}

	public boolean verifyPreorder2(int[] preorder) {
		Stack<Integer> stack = new Stack<>();
		int inorder = Integer.MIN_VALUE;

		for (int v : preorder) {
			if (v < inorder)
				return false;
			while (!stack.isEmpty() && v > stack.peek())
				inorder = stack.pop();
			stack.push(v);
		}
		return true;
	}

	public boolean verifyPreorder3(int[] preorder) {
		return valid(preorder, 0, preorder.length - 1);
	}

	private boolean valid(int[] preorder, int s, int e) {
		if (s + 1 >= e) {
			return true;
		}
		// at least 3 nodes
		int cur = s + 1;
		while (cur <= e && preorder[cur] < preorder[s]) {
			cur++;
		}
		for (int i = cur + 1; i <= e; i++) {
			if (preorder[i] < preorder[s]) {
				return false;
			}
		}
		return valid(preorder, s + 1, cur - 1) && valid(preorder, cur, e);
	}
}
