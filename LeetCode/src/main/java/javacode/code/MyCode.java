package javacode.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class MyCode {
    TreeNode2 t1 = new TreeNode2(1);
    TreeNode2 t2 = new TreeNode2(2);
    TreeNode2 t3 = new TreeNode2(3);

    public List<Integer> postorderTraversal(TreeNode2 root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode2> stack = new ArrayDeque<>();
        TreeNode2 p = root;
        while (!stack.isEmpty() || p != null) {
            if (p != null) {
                stack.push(p);
                result.add(p.val);
                p = p.right;
            } else {
                TreeNode2 node = stack.pop();
                p = node.left;
            }
        }
        Collections.reverse(result);
        return result;
    }

    public static void main(String[] args) throws java.lang.Exception {

        MyCode my = new MyCode();
        my.t1.left = my.t2;
        my.t1.right = my.t3;
        System.out.println("Hello Java");
        System.out.println(my.postorderTraversal(my.t1));
    }

    class TreeNode2 {
        public int val;
        public TreeNode2 left;
        public TreeNode2 right;

        public TreeNode2(int x) {
            val = x;
        }
    }
}
