package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import org.junit.Test;

import keycode.util.TreeNode;

public class Solution500 {
    /**
     * 523. Continuous Subarray Sum
     * 
     * check if the array has a continuous subarray of size at least 2 that sums up to a multiple of k
     */
    public boolean checkSubarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        // put(0, -1), for range check
        map.put(0, -1);
        int sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (k != 0) {
                sum = sum % k;
            }
            if (map.containsKey(sum)) {
                if (i - map.get(sum) > 1) {
                    return true;
                }
            } else {
                map.put(sum, i);
            }
        }
        return false;
    }

    /**
     * 973. K Closest Points to Origin
     */
    public int[][] kClosest(int[][] points, int K) {
        int len = points.length, l = 0, r = len - 1;
        while (l < r) {
            int mid = quicksort(points, l, r);
            if (mid == K - 1)
                break;
            if (mid < K - 1) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return Arrays.copyOfRange(points, 0, K);
    }

    private int quicksort(int[][] points, int l, int r) {
        int p = l++;
        while (l <= r) {
            while (l <= r && compare(points[p], points[l]) >= 0) {
                l++;
            }
            while (l <= r && compare(points[p], points[r]) <= 0) {
                r--;
            }
            if (l < r) {
                swap(points, l++, r--);
            }
        }
        swap(points, p, --l);
        return l;
    }

    private int compare(int[] a, int[] b) {
        return a[0] * a[0] + a[1] * a[1] - b[0] * b[0] - b[1] * b[1];
    }

    private void swap(int[][] points, int l, int r) {
        int[] tmp = points[l];
        points[l] = points[r];
        points[r] = tmp;
    }

    public boolean isBipartite(int[][] graph) {
        boolean[] visited = new boolean[graph.length];
        boolean[] color = new boolean[graph.length];

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < graph.length; i++) {
            if (graph[i].length == 0 || visited[i]) {
                continue;
            }
            queue.add(i);
            visited[i] = true;
            color[i] = true;
            while (!queue.isEmpty()) {
                int u = queue.remove();
                for (int v : graph[u]) {
                    if (visited[v]) {
                        if (color[v] == color[u]) {
                            return false;
                        }
                    } else {
                        queue.add(v);
                        visited[v] = true;
                        color[v] = !color[u];
                    }
                }
            }
        }
        return true;
    }

    /**
     * 632. Smallest Range Covering Elements from K Lists
     */
    public int[] smallestRange(List<List<Integer>> nums) {
        int range = Integer.MAX_VALUE, right = Integer.MIN_VALUE;
        int[] res = new int[2];

        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[2] - b[2];
            }
        });
        for (int i = 0; i < nums.size(); i++) {
            int val = nums.get(i).get(0);
            pq.add(new int[] { i, 0, val });
            right = Math.max(right, val);
        }

        while (true) {
            int[] e = pq.remove();
            int len = right - e[2];
            if (len < range) {
                res[0] = e[2];
                res[1] = right;
                range = len;
            }
            if (nums.get(e[0]).size() == e[1] + 1) {
                break;
            }
            e[1]++;
            e[2] = nums.get(e[0]).get(e[1]);
            pq.add(e);
            right = Math.max(right, e[2]);
        }
        return res;
    }

    /**
     * 772. Basic Calculator III
     */
    public int calculate(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        char sign = '+';
        int num = 0;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            // both digit and '(' will do nothing but set num
            if (Character.isDigit(c)) {
                while (i < s.length() && Character.isDigit(s.charAt(i))) {
                    num = num * 10 + s.charAt(i++) - '0';
                }
                i--;
            } else if (c == '(') {
                int j = i + 1;
                int braces = 1;
                while (j < n) {
                    if (s.charAt(j) == '(')
                        braces++;
                    if (s.charAt(j) == ')')
                        braces--;
                    if (braces == 0)
                        break;
                    j++;
                }
                num = calculate(s.substring(i + 1, j));
                i = j;
            }
            // finish calulation of num with previous sign, reset num and sign
            if (c == '+' || c == '-' || c == '*' || c == '/' || i == n - 1) {
                switch (sign) {
                case '+':
                    stack.push(num);
                    break;
                case '-':
                    stack.push(-num);
                    break;
                case '*':
                    stack.push(stack.pop() * num);
                    break;
                case '/':
                    stack.push(stack.pop() / num);
                    break;
                }
                num = 0;
                sign = c;
            }
        }

        int res = 0;
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;
    }

    int find(int[] A) {
        // if 0, return middle element
        int diff = A[0] - A[A.length - 1];
        if (A[1] - A[0] > 0) {
            return (A.length - diff) / 2;
        } else {
            return (A.length + diff) / 2;
        }
    }

    int find2(int[] A) {
        if (A.length < 3) {
            return -1;
        }
        boolean peak = A[1] - A[0] > 0;
        int l = 1, r = A.length - 2;
        // l=1, r = 3, mid = 2
        // l=3, r=3
        while (l < r) {
            int mid = l + (r - l) / 2;
            // 1,2,3
            if (peak) {
                if (A[mid] - 1 < A[mid] && A[mid] < A[mid] + 1) {
                    l = mid + 1;
                } else if (A[mid] - 1 > A[mid] && A[mid] > A[mid] + 1) {
                    r = mid;
                } else {
                    return l;
                }
            }
            if (!peak) {
                if (A[mid] - 1 < A[mid] && A[mid] < A[mid] + 1) {
                    r = mid;
                } else if (A[mid] - 1 > A[mid] && A[mid] > A[mid] + 1) {
                    l = mid + 1;
                } else {
                    return l;
                }
            }
        }
        return l;
    }

    class Solution545 {
        List<Integer> nodes = new ArrayList<>();

        public List<Integer> boundaryOfBinaryTree(TreeNode root) {
            if (root == null) {
                return nodes;
            }
            nodes.add(root.val);
            leftBoundary(root.left);
            leaves(root);
            rightBoundary(root.right);
            return nodes;
        }

        private void leftBoundary(TreeNode root) {
            if (root == null)
                return;
            if (root.left == null && root.right == null)
                return;

            nodes.add(root.val);
            if (root.left != null) {
                leftBoundary(root.left);
            } else {
                leftBoundary(root.right);
            }
        }

        private void rightBoundary(TreeNode root) {
            if (root == null)
                return;
            if (root.left == null && root.right == null)
                return;

            if (root.right != null) {
                rightBoundary(root.right);
            } else {
                rightBoundary(root.left);
            }
            nodes.add(root.val);
        }

        private void leaves(TreeNode root) {
            if (root == null)
                return;
            if (root.left == null && root.right == null) {
                nodes.add(root.val);
            }
            leaves(root.left);
            leaves(root.right);
        }
    }



    @Test
    public void test() {

        System.out.println(find(new int[] { 2, 1, 0, 1 }));

        int[] nums = { 3, -4, 4 };
        // System.out.println(-4 % 3);
        System.out.println(checkSubarraySum(nums, 0));
        int[][] points = { { 3, 3 }, { 5, -1 }, { -2, 4 } };
        System.out.println(points[0][0]);
        kClosest(points, 2);
    }
}
