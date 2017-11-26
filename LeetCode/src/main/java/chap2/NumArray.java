package chap2;

public class NumArray {

    private int[] total;
    private Node top;
    private int depth;

    public NumArray(int[] nums) {
        depth = (int) Math.ceil(Math.log(nums.length) / Math.log(2));
        total = new int[(int) Math.pow(2, depth) + 1];
        for (int i = 0; i < nums.length; i++) {
            total[i + 1] = total[i] + nums[i];
        }
        top = buildTree(0, (int) Math.pow(2, depth) - 1);
    }

    void update(int i, int val) {
        updateChild(top, i, val);
    }

    public int sumRange(int i, int j) {
        return sumRange(top, i, j, depth - 1);
    }

    private class Node {
        int index;
        int val;
        Node left;
        Node right;

        Node(int index, int val) {
            this.index = index;
            this.val = val;
        }
    }

    private Node buildTree(int start, int end) {
        int mid = (start + end) / 2;
        Node root = new Node(mid, total[end + 1] - total[start]);
        if (start == end) {
            return root;
        }
        root.left = buildTree(start, mid);
        root.right = buildTree(mid + 1, end);
        return root;
    }

    private void updateChild(Node root, int i, int val) {
        root.val += val;
        if (root.left == null) {
            return;
        }
        if (i <= root.index) {
            updateChild(root.left, i, val);
        } else {
            updateChild(root.right, i, val);
        }
    }

    private int sumRange(Node root, int i, int j, int level) {
        int res = 0;
        if (level == -1) {
            return root.val;
        }
        int left = root.index - (int) Math.pow(2, level) + 1;
        int right = root.index + (int) Math.pow(2, level);
        if (i == left && j == right) {
            res += root.val;
        } else if (i > root.index) {
            res += sumRange(root.right, i, j, level - 1);
        } else if (j <= root.index) {
            res += sumRange(root.left, i, j, level - 1);
        } else {
            res += sumRange(root.left, i, root.index, level - 1) + sumRange(root.right, root.index + 1, j, level - 1);
        }
        return res;
    }
}
