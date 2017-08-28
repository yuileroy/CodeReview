package chap2;

import java.util.Stack;

import org.junit.Test;

public class LVP {

	public int longestValidParentheses(String s) {
		if (s.isEmpty()) {
			return 0;
		}
		char[] c = s.toCharArray();
		int max = 0;
		int left = 0;
		int right = 0;
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '(') {
				left++;
			} else {
				right++;
				if (left >= right) {
					int r = 0;
					int l = 0;
					for (int j = i; j > i - 2 * right; j--) {
						if (c[j] == ')') {
							r++;
						} else {
							l++;
							if (l > r) {
								break;
							} else {
								max = Math.max(max, 2 * l);
							}
						}
					}
				} else {
					left = 0;
					right = 0;
				}
			}
		}
		return max;
	}

	public int longestValidParentheses2(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}

		int start = 0;
		int max = 0;
		Stack<Integer> stack = new Stack<Integer>();

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				stack.push(i);
			} else {
				if (stack.isEmpty()) {
					// record the position before first left parenthesis
					start = i + 1;
				} else {
					stack.pop();
					// if stack is empty mean the positon before the valid left
					// parenthesis is "last"
					if (stack.isEmpty()) {
						max = Math.max(i - start + 1, max);
					} else {
						// if stack is not empty, then for current i the longest
						// valid parenthesis length is
						// i-stack.peek()
						max = Math.max(i - stack.peek(), max);
					}
				}
			}
		}
		return max;
	}

	@Test
	public void test() {
		System.out.println(longestValidParentheses("()(())"));
		System.out.println(longestValidParentheses2("()(())"));
	}
}
