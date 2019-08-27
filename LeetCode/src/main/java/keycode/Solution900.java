package keycode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.junit.Test;

import keycode.util.TreeNode;

public class Solution900 {

    /**
     * 909. Snakes and Ladders
     */
    public int snakesAndLadders(int[][] board) {
        int N = board.length;
        int[] B = new int[N * N + 1];
        for (int num = 1; num <= N * N; num++) {
            B[num] = getValue(board, num);
        }

        // int[] A = new int[N * N + 1]; BFS Don't need to store steps
        boolean[] visited = new boolean[N * N + 1];
        ArrayDeque<Integer> que = new ArrayDeque<>();
        que.add(1);
        visited[1] = true;

        int step = 0;
        while (!que.isEmpty()) {
            step++;
            int size = que.size();
            while (size-- > 0) {
                int start = que.remove();
                // visited[start] = true;
                for (int i = 1; i <= 6; i++) {
                    int num = start + i;
                    if (num > N * N) {
                        continue;
                    }
                    if (B[num] != -1) {
                        num = B[num];
                    }
                    if (num == N * N) {
                        return step;
                    }
                    if (!visited[num]) {
                        que.add(num);
                        // SET visited[num] = true here to run faster
                        visited[num] = true;
                    }
                }
            }
        }
        return -1;
    }

    int getValue(int[][] board, int num) {
        int N = board.length;
        int i = N - 1 - (num - 1) / N, j = 0;
        boolean inc = (N - i) % 2 != 0;
        if (inc) {
            j = num - N * (N - i - 1) - 1;
        } else {
            j = N * (N - i) - num;
        }
        return board[i][j];
    }

    /**
     * 957. Prison Cells After N Days
     */
    public int[] prisonAfterNDays(int[] cells, int N) {
        Map<Integer, Integer> map = new HashMap<>();
        // put state(0) before while
        map.put(state(cells), 0);
        int i = 0;
        while (++i <= N) {
            cells = change(cells);
            int key = state(cells);
            if (map.containsKey(key)) {
                int len = i - map.get(key);
                // can't reset i = 0 and N %= len
                // [0,1,2,3,4,5,3,4] -> N = 7, len = 3, res is not N(1)=1, N(7)=N(4)!=N(1)
                // move i ahead of len
                i = i + (N - i) / len * len;
            } else {
                map.put(key, i);
            }
        }
        return cells;
    }

    int state(int[] cells) {
        int v = 0;
        for (int i = 0; i < cells.length; i++) {
            v += cells[i] << i;
        }
        return v;
    }

    int[] change(int[] cells) {
        int[] res = new int[cells.length];
        for (int i = 1; i < cells.length - 1; i++) {
            if (cells[i - 1] == cells[i + 1]) {
                res[i] = 1;
            }
        }
        return res;
    }

    /**
     * 863. All Nodes Distance K in Binary Tree
     */
    List<Integer> list = new ArrayList<>();

    public List<Integer> distanceK(TreeNode root, TreeNode target, int K) {
        getDistance(root, target, K);
        addToList(target, 0, K);
        return list;
    }

    // res != -1 means distance above target, go up only
    int getDistance(TreeNode root, TreeNode target, int K) {
        int res = -1;
        if (root == null) {
            return res;
        }
        if (root == target) {
            res = 0;
            return res;
        }
        int left = getDistance(root.left, target, K);
        int right = getDistance(root.right, target, K);
        if (left != -1) {
            res = left + 1;
            if (res == K) {
                list.add(root.val);
            } else if (res < K) {
                addToList(root.right, 1, K - res);
            }
        } else if (right != -1) {
            res = right + 1;
            if (res == K) {
                list.add(root.val);
            } else if (res < K) {
                addToList(root.left, 1, K - res);
            }
        }
        return res;
    }

    void addToList(TreeNode root, int cur, int k) {
        if (root == null) {
            return;
        }
        if (cur == k) {
            list.add(root.val);
            return;
        }
        addToList(root.left, cur + 1, k);
        addToList(root.right, cur + 1, k);
    }

    /**
     * 733. Flood Fill
     */
    int[][] D = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
    // int[] D

    public int[][] floodFill(int[][] image, int sr, int sc, int newColor) {
        int color = image[sr][sc];
        if (color == newColor) {
            return image;
        }
        dfs(image, sr, sc, color, newColor);
        return image;
    }

    void dfs(int[][] image, int i, int j, int color, int newColor) {
        image[i][j] = newColor;
        for (int[] d : D) {
            int r = i + d[0], c = j + d[1];
            if (r < 0 || r >= image.length || c < 0 || c >= image[0].length || image[r][c] != color) {
                continue;
            }
            // image[r][c] = newColor; // May miss [sr][sc]
            dfs(image, r, c, color, newColor);
        }
    }

    class FileSystem {
        class Dir {
            HashMap<String, Dir> dirs = new HashMap<>();
            HashMap<String, String> files = new HashMap<>();
        }

        Dir root;

        public FileSystem() {
            root = new Dir();
        }

        public List<String> ls(String path) {
            Dir t = root;
            List<String> files = new ArrayList<>();
            if (!path.equals("/")) {
                String[] d = path.split("/");
                // path = "/a", d = ["", "a"], d[d.length - 1] = "a"
                for (int i = 1; i < d.length - 1; i++) {
                    t = t.dirs.get(d[i]);
                }
                // if a is file, return it
                if (t.files.containsKey(d[d.length - 1])) {
                    files.add(d[d.length - 1]);
                    return files;
                } else {
                    t = t.dirs.get(d[d.length - 1]);
                }
            }
            // a is a Dir
            files.addAll(new ArrayList<>(t.dirs.keySet()));
            files.addAll(new ArrayList<>(t.files.keySet()));
            Collections.sort(files);
            return files;
        }

        public void mkdir(String path) {
            Dir t = root;
            String[] d = path.split("/");
            for (int i = 1; i < d.length; i++) {
                if (!t.dirs.containsKey(d[i]))
                    t.dirs.put(d[i], new Dir());
                t = t.dirs.get(d[i]);
            }
        }

        public void addContentToFile(String filePath, String content) {
            Dir t = root;
            String[] d = filePath.split("/");
            for (int i = 1; i < d.length - 1; i++) {
                t = t.dirs.get(d[i]);
            }
            t.files.put(d[d.length - 1], t.files.getOrDefault(d[d.length - 1], "") + content);
        }

        public String readContentFromFile(String filePath) {
            Dir t = root;
            String[] d = filePath.split("/");
            for (int i = 1; i < d.length - 1; i++) {
                t = t.dirs.get(d[i]);
            }
            return t.files.get(d[d.length - 1]);
        }
    }

    /**
     * 1000. Minimum Cost to Merge Stones
     */
    public int mergeStones(int[] stones, int K) {
        if ((stones.length - 1) % (K - 1) != 0) {
            return -1;
        }
        int n = stones.length;
        int[][] dp = new int[n][n];
        // dp sum up merged value only
        // dp[0][4] = min(6, 9), dp[0][5] = min(6, 9, 12) + 15
        // { 1, 2, 3, 4, 5 }, 3
        // [0, 0, 6, 6, 21]
        // [0, 0, 0, 9, 9 ]
        // [0, 0, 0, 0, 12]
        for (int j = 1; j < n; j++) {
            for (int i = j - 1; i >= 0; i--) {
                int res = Integer.MAX_VALUE;
                for (int k = i; k < j; k += K - 1) {
                    // k += K - 1, make sure dp[i][k] is valid
                    res = Math.min(res, dp[i][k] + dp[k + 1][j]);
                }
                if ((j - i) % (K - 1) == 0) {
                    for (int k = i; k <= j; k++) {
                        res += stones[k];
                    }
                }
                dp[i][j] = res;
            }
        }
        return dp[0][n - 1];
    }

    public int mergeStonesV2(int[] stones, int K) {
        int n = stones.length;
        if ((n - 1) % (K - 1) != 0) {
            return -1;
        }
        int[][] A = new int[n][n];
        int[] S = new int[n + 1];
        for (int i = 0; i < n; i++) {
            S[i + 1] = S[i] + stones[i];
        }

        for (int len = K; len <= n; len++) {
            for (int left = 0; left + len <= n; left++) {
                int right = left + len - 1;
                A[left][right] = Integer.MAX_VALUE;
                System.out.println();
                System.out.println("left: " + left + ", right: " + right + ", len: " + len);
                for (int split = left; split < right; split += (K - 1)) {
                    System.out.println("split: " + split);
                    A[left][right] = Math.min(A[left][right], A[left][split] + A[split + 1][right]);
                }
                if ((left - right) % (K - 1) == 0) {
                    A[left][right] += (S[right + 1] - S[left]);
                }
            }
        }
        return A[0][n - 1];
    }

    public boolean reachingPoints(int a, int b, int x, int y) {
        while (x >= a && y >= b) {
            if (x == y)
                break;
            if (x > y) {
                if (y > b)
                    x %= y;
                else
                    return (x - a) % y == 0;
            } else {
                if (x > a)
                    y %= x;
                else
                    return (y - b) % x == 0;
            }
        }
        return (x == a && y == b);
    }

    /**
     * 540. Single Element in a Sorted Array
     */
    public int singleNonDuplicate(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        int l = 0, r = nums.length - 1;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (nums[m] != nums[m - 1] && nums[m] != nums[m + 1]) {
                return nums[m];
            }
            // need to check length
            if (nums[m] == nums[m - 1]) {
                if (m % 2 == 0) {
                    r = m - 2;
                } else {
                    l = m + 1;
                }
            } else {
                if (m % 2 == 0) {
                    l = m + 2;
                } else {
                    r = m - 1;
                }
            }
        }
        return nums[l];
    }

    public List<List<String>> findDuplicate(String[] paths) {
        Map<String, List<String>> map = new HashMap<>();
        for (String path : paths) {
            String[] values = path.split(" ");
            for (int i = 1; i < values.length; i++) {
                String s = values[i];
                int left = s.indexOf('(');
                String key = s.substring(left + 1, s.length() - 1);
                if (!map.containsKey(key)) {
                    map.put(key, new ArrayList<String>());
                }
                map.get(key).add(values[0] + "/" + s.substring(left));
            }
        }
        List<List<String>> res = new ArrayList<>();
        for (String key : map.keySet()) {
            if (map.get(key).size() > 1) {
                res.add(map.get(key));
            }
        }
        return res;
    }

    public TreeNode str2tree(String s) {
        if (s.charAt(s.length() - 1) != ')') {
            return new TreeNode(Integer.parseInt(s));
        }
        int idx = s.indexOf('(');
        TreeNode root = new TreeNode(Integer.parseInt(s.substring(0, idx)));
        int cnt = 1;
        for (int i = idx + 1; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                cnt++;
            } else if (s.charAt(i) == ')') {
                cnt--;
                if (cnt == 0) {
                    root.left = str2tree(s.substring(idx + 1, i));
                    if (i != s.length() - 1) {
                        root.right = str2tree(s.substring(i + 2, s.length() - 1));
                        break; // need to break
                    }
                }
            }
        }
        return root;
    }

    // 854
    class Solution854 {
        int res = Integer.MAX_VALUE;

        public int kSimilarity(String A, String B) {
            char[] ch1 = A.toCharArray();
            char[] ch2 = B.toCharArray();
            int pre = preProcess(ch1, ch2);
            dfs(ch1, ch2, 0, 0);
            return res + pre;
        }

        // bfs() is for each step get a list of String by swap() once
        // if(s.charAt(j) == b.charAt(j) || s.charAt(i) != b.charAt(j)) continue;

        // dfs() with pre process
        void dfs(char[] ch1, char[] ch2, int start, int step) {
            if (start == ch1.length) {
                res = Math.min(res, step);
                return;
            }
            if (ch1[start] == ch2[start]) {
                // ! start++;
                dfs(ch1, ch2, start + 1, step);
            } else {
                for (int idx = start + 1; idx < ch1.length; idx++) {
                    if (ch1[idx] == ch2[start]) {
                        swap(ch1, start, idx);
                        dfs(ch1, ch2, start + 1, step + 1);
                        swap(ch1, start, idx);
                    }
                }
            }
        }

        private int preProcess(char[] arrA, char[] arrB) {
            int count = 0;
            for (int i = 0; i < arrA.length; ++i) {
                if (arrA[i] != arrB[i]) {
                    for (int j = i + 1; j < arrA.length; ++j) {
                        if (arrA[i] == arrB[j] && arrA[j] == arrB[i]) {
                            swap(arrA, i, j);
                            count++;
                            break;
                        }
                    }
                }
            }
            return count;
        }

        // List<Integer>[] arrayA = new ArrayList[26];
        // for (int i = 0; i < ch1.length; i++) {
        // if (arrayA[ch1[i] - 'a'] == null) {
        // arrayA[ch1[i] - 'a'] = new ArrayList<>();
        // }
        // arrayA[ch1[i] - 'a'].add(i);
        // }
        // void dfs(char[] ch1, char[] ch2, int start, int step) {
        // if (start == ch1.length) {
        // res = Math.min(res, step);
        // return;
        // }
        // if (ch1[start] == ch2[start]) {
        // start++; //! didn't call dfs()
        // } else {
        // for (int idx : arrayA[ch2[start] - 'a']) {
        // if (idx > start) {
        // swap(ch1, start, idx);
        // dfs(ch1, ch2, start + 1, step + 1);
        // swap(ch1, start, idx);
        // }
        // }
        // }
        // }

        void swap(char[] c, int i, int j) {
            char t = c[i];
            c[i] = c[j];
            c[j] = t;
        }
    }

    public int networkDelayTime(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> map = new HashMap<>();
        for (int[] e : times) {
            if (!map.containsKey(e[0]))
                map.put(e[0], new ArrayList<>());
            map.get(e[0]).add(new int[] { e[1], e[2] });
        }
        PriorityQueue<int[]> pq = new PriorityQueue<int[]>((e1, e2) -> e1[0] - e2[0]);
        pq.add(new int[] { 0, K });

        Map<Integer, Integer> dist = new HashMap<>();

        while (!pq.isEmpty()) {
            int[] e = pq.remove();
            int len = e[0], u = e[1];
            if (dist.containsKey(u))
                continue;
            dist.put(u, len);
            if (map.containsKey(u))
                for (int[] edge : map.get(u)) {
                    int v = edge[0], len2 = edge[1];
                    if (!dist.containsKey(v))
                        pq.add(new int[] { len + len2, v });
                }
        }

        if (dist.size() != N)
            return -1;
        int res = 0;
        for (int e : dist.values())
            res = Math.max(res, e);
        return res;
    }

    @Test
    public void test() {
        Solution854 sol = new Solution854();
        sol.kSimilarity("bccaba", "abacbc");
        "a".substring(1);
    }

    public void test0() {
        str2tree("4(2(3)(1))(6(5))");
        mergeStones(new int[] { 1, 2, 3, 4, 5 }, 3);
        FileSystem fs = new FileSystem();
        fs.mkdir("/a/b");
        System.out.println(fs.ls("/a"));
        prisonAfterNDays(new int[] { 1, 1, 0, 1, 1, 0, 1, 1 }, 6);
        int[][] A = { { 1, 1, -1 }, { 1, 1, 1 }, { -1, 1, 1 } };
        snakesAndLadders(A);
    }
}
