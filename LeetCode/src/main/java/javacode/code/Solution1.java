package javacode.code;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.Test;

public class Solution1 {

    public void flatten(TreeNode root) {
        TreeNode[] cur = { null };
        flatten(root, cur);
    }

    private void flatten(TreeNode root, TreeNode[] cur) {
        if (root == null) {
            return;
        }
        if (cur[0] == null) {
            cur[0] = root;
        } else {
            cur[0].right = root;
            cur[0] = root;
        }
        TreeNode r = root.right;
        flatten(root.left, cur);
        flatten(r, cur);
        root.left = null;
    }

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> res = new ArrayList<Integer>();
        Deque<TreeNode> stack = new ArrayDeque<TreeNode>();
        TreeNode cur = root;
        while (cur != null || !stack.isEmpty()) {
            if (cur != null) {
                stack.push(cur);
                cur = cur.left;
            } else {
                cur = stack.pop();
                res.add(cur.val);
                cur = cur.right;
            }
        }
        return res;
    }

    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        Deque<TreeNode> deque = new ArrayDeque<>();
        TreeNode cur = root;
        while (cur != null || !deque.isEmpty()) {
            if (cur != null) {
                result.add(cur.val);
                deque.addLast(cur);
                cur = cur.right;
            } else {
                cur = deque.removeLast();
                cur = cur.left;
            }
        }
        Collections.reverse(result);
        return result;
    }

    private static final String spliter = ",";
    private static final String NN = "X";

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        buildString(root, sb);
        return sb.toString();
    }

    private void buildString(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NN).append(spliter);
        } else {
            sb.append(node.val).append(spliter);
            buildString(node.left, sb);
            buildString(node.right, sb);
        }
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Deque<String> nodes = new ArrayDeque<>();
        nodes.addAll(Arrays.asList(data.split(spliter)));
        return buildTree(nodes);
    }

    private TreeNode buildTree(Deque<String> nodes) {
        String val = nodes.remove();
        if (val.equals(NN)) {
            return null;
        } else {
            TreeNode node = new TreeNode(Integer.valueOf(val));
            node.left = buildTree(nodes);
            node.right = buildTree(nodes);
            return node;
        }
    }
    
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        Comparator<ListNode> comp = new Comparator<ListNode>() {
            public int compare(ListNode o1, ListNode o2) {
                return o1.val - o2.val;
            }
        };
        PriorityQueue<ListNode> heap = new PriorityQueue<ListNode>(comp);
        for(ListNode li : lists) {
            if(li != null) {
                heap.add(li);
            }
        }
        while(heap.size() > 0) {
            //heap.peek();
            ListNode top = heap.remove();
            curr.next = top;
            curr = top;
            if(top.next != null) {
                heap.add(top.next);
            }
        }
        return dummy.next;
    }

    @Test
    public void test() {
        TreeNode t1 = new TreeNode(1);
        TreeNode t2 = new TreeNode(2);
        TreeNode t3 = new TreeNode(3);
        t1.left = t2;
        t1.right = t3;
        flatten(t1);
        System.out.println(t1.val);
        System.out.println(t1.right.val);
        System.out.println(t1.left);
        System.out.println(t2.right.val);
    }
}