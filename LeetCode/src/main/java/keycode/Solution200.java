package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import leetcode.TreeNode;

public class Solution200 {

    /**
     * 224. Basic Calculator
     */
    public int calculate(String s) {
        int sign = 1, result = 0;
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                int sum = s.charAt(i) - '0';
                while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    sum = sum * 10 + s.charAt(i + 1) - '0';
                    i++;
                }
                result += sum * sign;
            } else if (s.charAt(i) == '+') {
                sign = 1;
            } else if (s.charAt(i) == '-') {
                sign = -1;
            } else if (s.charAt(i) == '(') {
                stack.push(result);
                stack.push(sign);
                result = 0;
                sign = 1;
            } else if (s.charAt(i) == ')') {
                result = result * stack.pop() + stack.pop();
            }
        }
        return result;
    }

    /**
     * 230. Kth Smallest Element in a BST
     */
    public int kthSmallest(TreeNode root, int k) {
        int res = 0;
        Deque<TreeNode> s = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !s.isEmpty()) {
            while (cur != null) {
                s.push(cur);
                cur = cur.left;
            }
            cur = s.pop();
            if (--k == 0) {
                res = cur.val;
                break;
            }
            cur = cur.right;
        }
        return res;
    }

    public int kthSmallest2(TreeNode root, int k) {
        int L = count(root.left);
        if (L == k - 1) {
            return root.val;
        } else if (L < k - 1) {
            return kthSmallest2(root.right, k - L - 1);
        } else {
            return kthSmallest2(root.left, k);
        }
    }

    private int count(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return count(root.left) + count(root.right) + 1;
    }

    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        if (root == null) {
            return res;
        }
        Stack<TreeNode> stack = new Stack<TreeNode>();
        while (root != null || !stack.isEmpty()) {
            if (root != null) {
                stack.push(root);
                res.add(root.val);
                root = root.left;
            } else {
                root = stack.pop();
                root = root.right;
            }
        }
        return res;
    }

    public List<Integer> preorderTraversal2(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        preorderTraversal2(root, res);
        return res;
    }

    private void preorderTraversal2(TreeNode root, List<Integer> res) {
        if (root == null) {
            return;
        }
        res.add(root.val);
        preorderTraversal2(root.left, res);
        preorderTraversal2(root.right, res);
    }

    @Test
    public void test() {
        // System.out.println(compress("aba"));
    }
}
