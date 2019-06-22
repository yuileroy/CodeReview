package leetcodelock;

import java.util.Stack;

import leetcode.TreeNode;

//Given a binary tree where all the right nodes are either leaf nodes with a sibling 
//(a left node that shares the same parent node) or empty, 
//flip it upside down and turn it into a tree where the original right nodes turned into left leaf nodes. Return the new root.

public class Solution156 {
    public TreeNode upsideDownBinaryTree(TreeNode root) {
        if (root == null || root.left == null) {
            return root;
        }
        TreeNode nRoot = upsideDownBinaryTree(root.left);
        root.left.left = root.right;
        root.left.right = root;
        root.left = null;
        root.right = null;
        return nRoot;

    }

    public TreeNode upsideDownBinaryTree2(TreeNode root) {
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

}
