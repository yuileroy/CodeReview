package javacode.code;

import static org.junit.Assert.assertTrue;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;

public class JavaCode {

	void mergeArray(int[] a, int[] b) {
		int n = a.length;
		int cur = 2 * n - 1;
		int i, j;
		for (i = n - 1, j = n - 1; i >= 0 && j >= 0;) {
			if (a[i] >= b[j]) {
				b[cur] = a[i];
				i--;
			} else {
				b[cur] = b[j];
				j--;
			}
			cur--;
		}
		if (j == -1) {
			while (cur >= 0) {
				b[cur--] = a[i--];
			}
		}
		for (int e : b) {
			System.out.println(e);
		}
	}

	int[] findRange(int[] a, int val) {
		if (a == null) {
			throw new IllegalArgumentException("null");
		}
		int[] res = {-1, -1};
		int left = 0;
		int right = a.length - 1;
		while (left + 1 < right) {
			int mid = left + (right - left) / 2;
			if (a[mid] < val) {
				left = mid;
			} else {
				right = mid;
			}
		}
		if (a[right] == val) {
			res[0] = right;
		}
		if (a[left] == val) {
			res[0] = left;
		}
		left = 0;
		right = a.length - 1;
		while (left + 1 < right) {
			int mid = left + (right - left) / 2;
			if (a[mid] <= val) {
				left = mid;
			} else {
				right = mid;
			}
		}
		if (a[left] == val) {
			res[1] = left;
		}
		if (a[right] == val) {
			res[1] = right;
		}

		return res;
	}

	public String oDRes;

	public String originalDigits(String s) {
		int[] charArray = new int[26];
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			charArray[c - 'a']++;
		}
		String[] dict = {"zero", "one", "two", "three", "four", "five", "six",
				"seven", "eight", "night"};
		originalDigitsHelper("", dict, 0, charArray);
		return oDRes;
	}

	private void originalDigitsHelper(String pre, String[] dict, int digit,
			int[] charArray) {
		if (oDRes != null) {
			return;
		}
		if (digit == 10) {
			boolean found = true;
			for (int cnt : charArray) {
				if (cnt != 0) {
					found = false;
					break;
				}
			}
			if (found) {
				oDRes = pre;
			}
			return;
		}
		char[] curChar = dict[digit].toCharArray();
		boolean found = true;
		for (char c : curChar) {
			if (charArray[c - 'a'] == 0) {
				found = false;
				break;
			}
		}
		if (found) {
			String newPre = pre + digit;
			for (char c : curChar) {
				charArray[c - 'a']--;
			}
			originalDigitsHelper(newPre, dict, digit, charArray);
			for (char c : curChar) {
				charArray[c - 'a']++;
			}
		}
		if (!found) {
			originalDigitsHelper(pre, dict, digit + 1, charArray);
		}
	}

	public int characterReplacement0(String s, int k) {
		int len = s.length();
		int[] count = new int[26];
		int start = 0, maxCount = 0, maxLength = 0;
		for (int end = 0; end < len; end++) {
			maxCount = Math.max(maxCount, ++count[s.charAt(end) - 'A']);
			while (end - start + 1 - maxCount > k) {
				count[s.charAt(start) - 'A']--;
				start++;
			}
			maxLength = Math.max(maxLength, end - start + 1);
		}
		return maxLength;
	}

	public int characterReplacement(String s, int k) {
		int[] count = new int[26];
		int start = 0, maxCount = 0, res = 0;
		// find max length of strings end at index end.
		for (int end = 0; end < s.length(); end++) {
			maxCount = Math.max(maxCount, ++count[s.charAt(end) - 'A']);
			while (end - start + 1 - maxCount > k) {
				count[s.charAt(start) - 'A']--;
				start++;
				// don't need to decrease maxCount
			}
			res = Math.max(res, end - start + 1);
		}
		return res;
	}

	public int eraseOverlapIntervals(Interval[] intervals) {
		Arrays.sort(intervals, new Comparator<Interval>() {
			public int compare(Interval a, Interval b) {
				return a.end - b.end;
			}
		});
		Interval pre = intervals[0];
		int count = 1;
		for (int i = 1; i < intervals.length; i++) {
			Interval cur = intervals[i];
			if (cur.start >= pre.end) {
				pre = cur;
				count++;
			}
		}
		return intervals.length - count;
	}

	public int[] findRightInterval(Interval[] intervals) {
		int[] result = new int[intervals.length];
		TreeMap<Integer, Integer> intervalMap = new TreeMap<>();

		for (int i = 0; i < intervals.length; i++) {
			intervalMap.put(intervals[i].start, i);
		}
		for (int i = 0; i < intervals.length; i++) {
			Integer key = intervalMap.ceilingKey(intervals[i].end);
			result[i] = (key != null) ? intervalMap.get(key) : -1;
		}
		return result;
	}

	public int pathSum(TreeNode root, int sum) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, 1);
		return helper(root, 0, sum, map);
	}

	public int helper(TreeNode root, int currSum, int target,
			Map<Integer, Integer> map) {
		if (root == null) {
			return 0;
		}
		currSum += root.val;
		int res = map.getOrDefault(currSum - target, 0);
		map.put(currSum, map.getOrDefault(currSum, 0) + 1);

		res += helper(root.left, currSum, target, map)
				+ helper(root.right, currSum, target, map);
		map.put(currSum, map.get(currSum) - 1);
		return res;
	}

	public List<Integer> findAnagrams(String s, String p) {
		List<Integer> list = new ArrayList<>();
		if (s == null || s.isEmpty() || p == null || p.isEmpty()) {
			return list;
		}
		int[] hash = new int[256];
		for (char c : p.toCharArray()) {
			hash[c]++;
		}
		int left = 0, right = 0, count = p.length();
		while (right < s.length()) {
			if (right - left == p.length()) {
				if (hash[s.charAt(left)] >= 0) {
					count++;
				}
				hash[s.charAt(left)]++;
				left++;
			}

			if (hash[s.charAt(right)] >= 1) {
				count--;
			}
			hash[s.charAt(right)]--;

			if (count == 0) {
				list.add(left);
			}
			right++;
		}
		return list;
	}

	// when find a number i, flip the number at position i-1 to negative.
	// if the number at position i-1 is already negative, i is the number that
	// occurs twice.
	public List<Integer> findDuplicates(int[] nums) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			int index = Math.abs(nums[i]) - 1;
			if (nums[index] < 0) {
				res.add(Math.abs(index + 1));
			}
			nums[index] = -nums[index];
		}
		return res;
	}

	public int findKthNumber(int n, int k) {
		int curr = 1;
		k = k - 1;
		while (k > 0) {
			int steps = calSteps(n, curr, curr + 1);
			if (steps <= k) {
				curr += 1;
				k -= steps;
			} else {
				curr *= 10;
				k -= 1;
			}
		}
		return curr;
	}

	// use long in case of overflow
	public int calSteps(int n, long n1, long n2) {
		int steps = 0;
		while (n1 <= n) {
			steps += Math.min(n + 1, n2) - n1;
			n1 *= 10;
			n2 *= 10;
		}
		return steps;
	}

	public List<Integer> findDisappearedNumbers(int[] nums) {
		List<Integer> res = new ArrayList<>();
		for (int i = 0; i < nums.length; i++) {
			int idx = Math.abs(nums[i]) - 1;
			nums[idx] = 0 - Math.abs(nums[idx]);
		}
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] > 0) {
				res.add(i + 1);
			}
		}
		return res;
	}

	// Encodes a tree to a single string.
	public String serialize(TreeNode root) {
		if (root == null) {
			return "x,";
		}
		TreeNode dummy = new TreeNode(0);
		StringBuilder sb = new StringBuilder();
		ArrayDeque<TreeNode> deque = new ArrayDeque<>();
		deque.push(root);
		while (!deque.isEmpty()) {
			root = deque.pop();
			if (root == dummy) {
				sb.append("x,");
				continue;
			}
			sb.append(root.val).append(",");
			if (root.right != null) {
				deque.push(root.right);
			} else {
				deque.push(dummy);
			}
			if (root.left != null) {
				deque.push(root.left);
			} else {
				deque.push(dummy);
			}
		}
		return sb.toString();
	}

	// Decodes your encoded data to tree.
	public TreeNode deserialize(String data) {
		String[] token = data.split(",");
		TreeNode dummy = new TreeNode(0);
		TreeNode root = createNewNode(token[0], dummy);
		if (root == null) {
			return root;
		}
		ArrayDeque<TreeNode> deque = new ArrayDeque<>();
		deque.add(root);
		for (int i = 1; i < token.length; i++) {
			TreeNode cur = deque.peek();
			TreeNode newNode = createNewNode(token[i], dummy);
			if (cur.left == dummy) {
				cur.left = newNode;
			} else if (cur.right == dummy) {
				cur.right = newNode;
				deque.pop();
			}
			if (newNode != null) {
				deque.push(newNode);
			}
		}
		return root;
	}

	private TreeNode createNewNode(String s, TreeNode dummy) {
		if ("x".equals(s)) {
			return null;
		}
		TreeNode root = new TreeNode(Integer.parseInt(s));
		root.left = dummy;
		root.right = dummy;
		return root;
	}

	public TreeNode deleteNode0(TreeNode root, int key) {
		if (root == null) {
			return null;
		}
		TreeNode target = null;
		TreeNode cur = root;
		TreeNode pre = null;
		while (cur != null) {
			if (cur.val == key) {
				target = cur;
				break;
			} else if (cur.val < key) {
				pre = cur;
				cur = cur.right;
			} else {
				pre = cur;
				cur = cur.left;
			}
		}
		if (target == null) {
			return root;
		}
		// single node
		if (target == root && target.left == null && target.right == null) {
			return null;
		}
		// leaf node
		if (target.left == null && target.right == null) {
			if (pre.left == target) {
				pre.left = null;
			} else {
				pre.right = null;
			}
			return root;
		}
		// one side child node
		if (target.left == null) {
			return target.right;
		}
		if (target.right == null) {
			return target.left;
		}
		// to do
		return root;
	}

	public TreeNode deleteNode(TreeNode root, int key) {
		if (root == null) {
			return null;
		}
		if (key < root.val) {
			root.left = deleteNode(root.left, key);
		} else if (key > root.val) {
			root.right = deleteNode(root.right, key);
		} else {
			if (root.left == null) {
				return root.right;
			} else if (root.right == null) {
				return root.left;
			}
			// both left and right are not null

			// findMin() will run at most once, since root.val = minNode.val
			TreeNode minNode = findMin(root.right);
			root.val = minNode.val;
			root.right = deleteNode(root.right, root.val);
		}
		return root;
	}

	private TreeNode findMin(TreeNode node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	public String frequencySort(String s) {
		HashMap<Character, Integer> map = new HashMap<>();
		for (char key : s.toCharArray()) {
			if (map.containsKey(key)) {
				map.put(key, map.get(key) + 1);
			} else {
				map.put(key, 1);
			}
		}

		PriorityQueue<Map.Entry<Character, Integer>> queue = new PriorityQueue<>(
				new Comparator<Map.Entry<Character, Integer>>() {
					public int compare(Map.Entry<Character, Integer> e1,
							Map.Entry<Character, Integer> e2) {
						return e2.getValue() - e1.getValue();
					}
				});

		queue.addAll(map.entrySet());
		StringBuilder sb = new StringBuilder();
		while (!queue.isEmpty()) {
			sb.append(queue.remove().getKey());
		}
		return sb.toString();
	}

	public int findMinArrowShots(int[][] points) {
		Arrays.sort(points, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				return b[1] == a[1] ? a[0] - b[0] : a[1] - b[1];
			}
		});
		return -1;
	}

	public int minMoves(int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int min = nums[0];
		for (int n : nums)
			min = Math.min(min, n);
		int res = 0;
		for (int n : nums)
			res += n - min;
		return res;
	}

	public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
		Map<Integer, Integer> map = new HashMap<>();
		for (int a : A) {
			for (int b : B) {
				int sum = a + b;
				map.put(sum, map.getOrDefault(sum, 0) + 1);
			}
		}

		int res = 0;
		for (int c : C) {
			for (int d : D) {
				res += map.getOrDefault(0 - c - d, 0);
			}
		}
		return res;
	}

	public TreeNode sortedListToBST(ListNode head) {
		if (head == null) {
			return null;
		}
		int len = 0;
		ListNode tmp = head;
		while (tmp != null) {
			tmp = tmp.next;
			len++;
		}
		ListNode[] cur = {head};
		return sortedListToBST(0, len - 1, cur);
	}

	private TreeNode sortedListToBST(int start, int end, ListNode[] cur) {
		if (start > end) {
			return null;
		}
		int mid = start + (end - start) / 2;
		TreeNode left = sortedListToBST(start, mid - 1, cur);
		TreeNode root = new TreeNode(cur[0].val);
		root.left = left;
		cur[0] = cur[0].next;
		TreeNode right = sortedListToBST(mid + 1, end, cur);
		root.right = right;

		return root;
	}

	public void rotate(int[] nums, int k) {
		int length = nums.length;
		int[] temp = new int[length];
		for (int i = 0; i < length; i++) {
			int j = (i + k) % length;
			temp[j] = nums[i];
		}
		nums = Arrays.copyOf(temp, length);
		System.out.println(nums);
	}

	public boolean canComp1(String input, Set<String> dict) {
		if (input == null || input.isEmpty()) {
			return true;
		} else if (dict == null || dict.isEmpty()) {
			return false;
		}
		int n = input.length();
		boolean[][] B = new boolean[n + 1][n + 1];
		B[0][0] = true;
		for (int k = 1; k <= n; k++) {
			for (int i = 0; i + k <= n; i++) {
				if (dict.contains(input.substring(i, i + k))) {
					B[i][i + k] = true;
				} else {
					for (int j = i + 1; j < i + k; j++) {
						if (B[i][j] && B[j][i + k]) {
							B[i][i + k] = true;
							break;
						}
					}
				}
			}
		}
		return B[0][n];
	}

	public boolean canComp(String input, Set<String> dict) {
		if (input == null || input.isEmpty()) {
			return true;
		} else if (dict == null || dict.isEmpty()) {
			return false;
		}
		int n = input.length();
		boolean b[] = new boolean[n + 1];
		b[0] = true;
		for (int i = 0; i <= n; i++) {
			if (b[i]) {
				for (int j = i + 1; j <= n; j++) {
					if (dict.contains(input.substring(i, j))) {
						b[j] = true;
					}
				}
			}
			if (b[n]) {
				return true;
			}
		}
		return false;
	}

	public boolean find132pattern(int[] nums) {
		if (nums == null || nums.length < 3) {
			return false;
		}
		int[] minL = new int[nums.length];
		minL[0] = nums[0];
		for (int i = 1; i < nums.length - 1; i++) {
			minL[i] = Math.min(minL[i - 1], nums[i]);
		}

		ArrayDeque<Integer> queue = new ArrayDeque<>();
		queue.addLast(nums[nums.length - 1]);
		for (int i = nums.length - 2; i > 0; i--) {
			int maxR = Integer.MIN_VALUE;
			while (!queue.isEmpty() && queue.getLast() < nums[i]) {
				maxR = queue.removeLast();
			}
			if (minL[i] < maxR) {
				return true;
			}
			queue.addLast(nums[i]);
		}
		return false;
	}

	public int minMoves2(int[] nums) {
		int sum = 0;
		for (int e : nums) {
			sum += e;
		}
		int ave = sum / nums.length;
		if ((ave + 1) * nums.length - sum < sum - ave * nums.length) {
			ave += 1;
		}
		int res = 0;
		for (int e : nums) {
			res += Math.abs(ave - e);
		}
		return res;
	}

	public int hammingDistance(int x, int y) {
		int xor = x ^ y;
		int res = 0;
		for (int i = 0; i < 32; i++) {
			if (((xor >> i) & 1) == 1) {
				res++;
			}
		}
		return res;
	}

	public int getMaxRepetitions(String s1, int n1, String s2, int n2) {
		char[] array1 = s1.toCharArray(), array2 = s2.toCharArray();
		int count1 = 0, count2 = 0, i = 0, j = 0;

		while (count1 < n1) {
			if (array1[i] == array2[j]) {
				j++;
				if (j == array2.length) {
					j = 0;
					count2++;
				}
			}
			i++;
			if (i == array1.length) {
				i = 0;
				count1++;
			}
		}

		return count2 / n2;
	}

	public int findSubstringInWraproundString(String p) {
		if (p == null || p.length() == 0) {
			return 0;
		}

		int[] count = new int[26];
		int cur = 0;
		for (int i = 0; i < p.length(); i++) {
			if (i > 0 && (p.charAt(i) - p.charAt(i - 1) == 1
					|| p.charAt(i) - p.charAt(i - 1) == -25)) {
				cur++;
			} else {
				cur = 1;
			}
			int index = p.charAt(i) - 'a';
			count[index] = Math.max(count[index], cur);
		}

		int sum = 0;
		for (int cnt : count) {
			sum += cnt;
		}
		return sum;
	}

	public String validIPAddress(String IP) {
		if (IP.matches(
				"(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])")) {
			return "IPv4";
		}
		if (IP.matches("(([0-9a-fA-F]{1,4}):){7}([0-9a-fA-F]{1,4})")) {
			return "IPv6";
		}
		return "Neither";
	}

	public boolean makesquare(int[] nums) {
		if (nums == null || nums.length < 4) {
			return false;
		}
		int sum = 0;
		for (int num : nums) {
			sum += num;
		}
		if (sum % 4 != 0) {
			return false;
		}

		Arrays.sort(nums);
		reverse(nums);

		return dfs(nums, new int[4], 0, sum / 4);
	}

	private boolean dfs(int[] nums, int[] sums, int index, int target) {
		if (index == nums.length) {
			return true;
		}
		for (int i = 0; i < 4; i++) {
			if (sums[i] + nums[index] > target
					|| (i > 0 && sums[i] == sums[i - 1])) {
				continue;
			}
			sums[i] += nums[index];
			if (dfs(nums, sums, index + 1, target)) {
				return true;
			}
			sums[i] -= nums[index];
		}
		return false;
	}

	private void reverse(int[] nums) {
		int i = 0, j = nums.length - 1;
		while (i < j) {
			int temp = nums[i];
			nums[i] = nums[j];
			nums[j] = temp;
			i++;
			j--;
		}
	}

	public String[] findWords(String[] words) {
		return Stream.of(words)
				.filter(s -> s.toLowerCase()
						.matches("[qwertyuiop]*|[asdfghjkl]*|[zxcvbnm]*"))
				.toArray(String[]::new);
	}

	public int[] findMode(TreeNode root) {
		if (root == null) {
			return new int[0];
		}
		int cnt = 0;

		int[] res = new int[cnt];
		return res;
	}

	public List<Integer> inorderTraversal0(TreeNode root) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		if (root == null) {
			return res;
		}

		ArrayDeque<TreeNode> que = new ArrayDeque<TreeNode>();
		TreeNode cur = root;
		while (cur != null || !que.isEmpty()) {
			if (cur != null) {
				que.addLast(cur);
				cur = cur.left;
			} else {
				cur = que.removeLast();
				res.add(cur.val);
				cur = cur.right;
			}
		}
		return res;
	}

	public List<Integer> inorderTraversal(TreeNode root) {
		ArrayList<Integer> res = new ArrayList<Integer>();
		helperTra(root, res);
		return res;
	}

	private void helperTra(TreeNode root, ArrayList<Integer> res) {
		if (root == null)
			return;
		helperTra(root.left, res);
		res.add(root.val);
		helperTra(root.right, res);
	}

	private Map<Integer, Integer> mapFrequentTreeSum = new HashMap<>();

	public int[] findFrequentTreeSum(TreeNode root) {
		rootSum(root);
		int max = 0;
		for (int e : mapFrequentTreeSum.values()) {
			max = max < e ? e : max;
		}
		List<Integer> li = new ArrayList<>();
		for (int key : mapFrequentTreeSum.keySet()) {
			if (mapFrequentTreeSum.get(key) == max) {
				li.add(key);
			}
		}
		int[] res = new int[li.size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = li.get(i);
		}
		return res;
	}

	private int rootSum(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int res = root.val + rootSum(root.left) + rootSum(root.right);
		mapFrequentTreeSum.put(res,
				mapFrequentTreeSum.getOrDefault(res, 0) + 1);
		return res;
	}

	public String convertToBase7(int num) {
		StringBuilder res = new StringBuilder();
		boolean neg = false;
		if (num < 0) {
			num = -num;
			neg = true;
		}
		while (num > 0) {
			int remain = num % 7;
			res.append(remain);
			num = num / 7;
		}
		res.reverse();
		if (neg) {
			res.insert(0, "-");
		}
		return res.toString();
	}

	public int findBottomLeftValue(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int[] res = {root.val, 0};
		traverse(root, 0, res);
		return res[0];
	}

	private void traverse(TreeNode root, int h, int[] res) {
		if (root == null) {
			return;
		}
		if (root.left == null && root.right == null) {
			if (h > res[1]) {
				res[0] = root.val;
				res[1] = h;
			}
		}
		traverse(root.left, h + 1, res);
		traverse(root.right, h + 1, res);
	}

	public int longestPalindromeSubseq(String s) {
		int len = s.length();
		int[][] B = new int[len + 1][len + 1];
		for (int i = 0; i < len; i++) {
			B[i][i + 1] = 1;
		}
		for (int k = 2; k < len + 1; k++) {
			for (int i = 0; i + k < len + 1; i++) {
				// --! s.charAt(i + k - 1), not s.charAt(i + k)
				if (s.charAt(i) == s.charAt(i + k - 1)) {
					B[i][i + k] = B[i + 1][i + k - 1] + 2;
				} else {
					B[i][i + k] = Math.max(B[i + 1][i + k], B[i][i + k - 1]);
				}
			}
		}
		return B[0][len];
	}

	public int change(int amount, int[] coins) {
		int[] dp = new int[amount + 1];
		dp[0] = 1;

		for (int val : coins) {
			for (int i = 0; i <= amount; i++) {
				if (dp[i] == 0) {
					continue;
				}
				if (i + val <= amount) {
					dp[i + val] += dp[i];
				}
			}
		}

		return dp[amount];
	}

	public int change2(int amount, int[] coins) {
		int[] dp = new int[amount + 1];
		dp[0] = 1;

		for (int i = 0; i < amount + 1; i++) {
			if (dp[i] == 0) {
				continue;
			}
			for (int val : coins) {
				if (i + val <= amount) {
					dp[i + val] += dp[i];
				}
			}
		}
		return dp[amount];
	}

	public int[] findChange(int amount, int[] coins) {
		int[] dp = new int[amount + 1];
		dp[0] = 1;

		for (int val : coins) {
			for (int i = 0; i <= amount; i++) {
				if (dp[i] == 0) {
					continue;
				}
				if (i + val <= amount) {
					dp[i + val] += dp[i];
				}
			}
		}
		System.out.println("res:" + dp[amount]);
		if (dp[amount] == 0) {
			return new int[0];
		}
		int[] res = new int[coins.length];
		int idx = amount;
		while (idx > 0) {
			for (int i = 0; i < coins.length; i++) {
				if (idx - coins[i] >= 0 && dp[idx - coins[i]] > 0) {
					res[i]++;
					idx -= coins[i];
					break;
				}
			}
		}

		return res;
	}

	public String findLongestWord(String s, List<String> d) {
		if (s == null || s.isEmpty() || d == null || d.isEmpty()) {
			return "";
		}
		String res = "";
		for (String item : d) {
			if (isBefore(res, item) && isSubseq(s, item)) {
				res = item;
			}
		}
		return res;
	}

	private boolean isBefore(String s, String item) {
		if (item.length() == s.length()) {
			return item.compareTo(s) <= 0;
		} else {
			return item.length() > s.length();
		}
	}

	private boolean isSubseq(String s, String item) {
		int k = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == item.charAt(k)) {
				k++;
				if (k == item.length()) {
					return true;
				}
			}
		}
		return false;
	}

	public int findMaxLength(int[] nums) {
		if (nums == null) {
			throw new IllegalArgumentException("x");
		}
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == 0) {
				nums[i] = -1;
			}
		}
		int[] sum = new int[nums.length + 1];
		int res = 0;
		Map<Integer, Integer> map = new HashMap<>();
		// -! add index 0
		map.put(0, 0);

		for (int i = 1; i < sum.length; i++) {
			sum[i] = sum[i - 1] + nums[i - 1];
			int key = sum[i];
			if (map.containsKey(key)) {
				res = Math.max(res, i - map.get(key));
			} else {
				map.put(key, i);
			}
		}
		return res;
	}

	public char[][] updateBoard(char[][] board, int[] click) {
		int m = board.length, n = board[0].length;
		int row = click[0], col = click[1];

		if (board[row][col] == 'M') {
			board[row][col] = 'X';
		} else {
			int count = 0;
			for (int i = -1; i < 2; i++) {
				for (int j = -1; j < 2; j++) {
					if (i == 0 && j == 0)
						continue;
					int r = row + i, c = col + j;
					if (r < 0 || r >= m || c < 0 || c >= n)
						continue;
					if (board[r][c] == 'M' || board[r][c] == 'X')
						count++;
				}
			}
			if (count > 0) {
				board[row][col] = (char) (count + '0');
			} else {
				board[row][col] = 'B';
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (i == 0 && j == 0)
							continue;
						int r = row + i, c = col + j;
						if (r < 0 || r >= m || c < 0 || c >= n)
							continue;
						if (board[r][c] == 'E')
							updateBoard(board, new int[]{r, c});
					}
				}
			}
		}
		return board;
	}

	public int kthSmallest(TreeNode root, int k) {
		int res = 0;
		Stack<TreeNode> s = new Stack<>();
		TreeNode cur = root;
		while (cur != null || !s.isEmpty()) {
			while (cur != null) {
				s.push(cur);
				cur = cur.left;
			}
			cur = s.pop();
			if (--k == 0) {
				res = cur.val;
			}
			cur = cur.right;
		}

		return res;
	}

	public List<List<Integer>> levelOrderBottom(TreeNode root) {
		Deque<TreeNode> queue = new ArrayDeque<TreeNode>();
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (root == null) {
			return res;
		}

		queue.addLast(root);
		while (!queue.isEmpty()) {
			int levelNum = queue.size();
			List<Integer> subList = new ArrayList<Integer>();
			for (int i = 0; i < levelNum; i++) {
				TreeNode cur = queue.removeFirst();
				if (cur.left != null) {
					queue.addLast(cur.left);
				}
				if (cur.right != null) {
					queue.addLast(cur.right);
				}
				subList.add(cur.val);
			}
			res.add(0, subList);
		}
		return res;
	}

	public int findTargetSumWays(int[] nums, int Sum) {
		int sum = 0;
		for (int e : nums) {
			sum += e;
		}
		if ((Sum + sum) % 2 != 0) {
			return 0;
		}
		int target = (Sum + sum) / 2;
		int[] dp = new int[target + 1];
		dp[0] = 1;
		for (int e : nums) {
			for (int i = target; i - e >= 0; i--) {
				dp[i] += dp[i - e];
			}
		}
		return dp[target];
	}

	public int widthOfBinaryTree(TreeNode root) {
		if (root == null) {
			return 0;
		}
		List<TreeNode> li = new ArrayList<>();
		li.add(root.left);
		li.add(root.right);

		int res = 0;

		while (!li.isEmpty()) {
			long first = Long.MIN_VALUE;
			long last = Long.MIN_VALUE;
			int n = li.size();
			for (int i = 0; i < n; i++) {
				TreeNode cur = li.remove(0);
				if (cur != null) {
					if (first == Long.MIN_VALUE) {
						first = i;
					} else {
						last = i;
					}
					li.add(cur.left);
					li.add(cur.right);
				} else {
					li.add(null);
					li.add(null);
				}
			}
			if (first == Long.MIN_VALUE && last == Long.MIN_VALUE) {
				break;
			}
			if (first != Long.MIN_VALUE && last != Long.MIN_VALUE) {
				res = Math.max(res, (int) (last - first + 1));
			}
		}

		return res;
	}

	class Solution663 {
		boolean res = false;

		public boolean checkEqualTree(TreeNode root) {
			int max = sum(root);
			if (max % 2 != 0) {
				return false;
			}
			sum(root, max);
			return res;
		}

		private int sum(TreeNode root) {
			if (root == null) {
				return 0;
			}
			return root.val + sum(root.left) + sum(root.right);
		}

		private int sum(TreeNode root, int max) {
			if (res || root == null) {
				return 0;
			}
			int val = root.val + sum(root.left) + sum(root.right);
			if (val == max / 2) {
				res = true;
			}
			return val;
		}
	}

	public int strangePrinter(String s) {
		char[] c = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < c.length; i++) {
			if (i == 0 || c[i] != c[i - 1]) {
				sb.append(c[i]);
			}
		}
		c = sb.toString().toCharArray();
		int res = strangePrinterFn(c, 0, c.length - 1);
		return res;
	}

	private int strangePrinterFn(char[] c, int start, int end) {
		if (start > end || start < 0 || end >= c.length) {
			return 0;
		}
		if (start == end) {
			return 1;
		}
		Map<Character, List<Integer>> map = new HashMap<>();
		for (int i = start; i <= end; i++) {
			if (!map.containsKey(c[i])) {
				map.put(c[i], new ArrayList<>());
				map.get(c[i]).add(i);
				map.get(c[i]).add(i);
			} else {
				map.get(c[i]).set(1, i);
			}
		}
		int gap = -1;
		Map.Entry<Character, List<Integer>> target = null;
		for (Map.Entry<Character, List<Integer>> et : map.entrySet()) {
			if (et.getValue().get(1) - et.getValue().get(0) > gap) {
				target = et;
				gap = et.getValue().get(1) - et.getValue().get(0);
			}
		}
		int x1 = target.getValue().get(0);
		int x2 = target.getValue().get(1);
		return strangePrinterFn(c, start, x1 - 1)
				+ strangePrinterFn(c, x2 + 1, end)
				+ Math.min(strangePrinterFn(c, x1 + 1, x2),
						strangePrinterFn(c, x1, x2 - 1));
	}

	public int strangePrinter22(String s) {

		char[] c = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < c.length; i++) {
			if (i == 0 || c[i] != c[i - 1]) {
				sb.append(c[i]);
			}
		}
		char[] ch = sb.toString().toCharArray();

		int n = ch.length;
		if (n <= 2) {
			return n;
		}
		int[][] dp = new int[n][n];
		for (int[] row : dp) {
			Arrays.fill(row, Integer.MAX_VALUE);
		}
		for (int i = 0; i < n; i++) {
			dp[i][i] = 1;
		}
		for (int l = 1; l < n; l++) {
			for (int i = 0; i + l < n; i++) {
				int j = i + l;
				if (ch[i] == ch[j]) {
					dp[i][j] = Math.min(dp[i][j - 1], dp[i + 1][j]);
				} else {
					for (int k = i; k < j; k++) {
						dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j]);
					}
				}
			}
		}
		return dp[0][n - 1];
	}

	public List<Interval> merge(List<Interval> intervals) {
		if (intervals == null || intervals.size() == 0) {
			return intervals;
		}
		Collections.sort(intervals, new Comparator<Interval>() {
			public int compare(Interval o1, Interval o2) {
				return o1.start - o2.start;
			}
		});

		List<Interval> res = new ArrayList<>();
		Interval cur = intervals.get(0);
		for (int i = 1; i < intervals.size(); i++) {
			Interval tmp = intervals.get(i);
			if (tmp.start <= cur.end) {
				cur.end = Math.max(cur.end, tmp.end);
			} else {
				res.add(cur);
				cur = tmp;
			}
		}
		res.add(cur);
		return res;
	}

	public List<List<Integer>> permuteUnique(int[] num) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (num == null || num.length == 0) {
			return res;
		}
		ArrayList<Integer> first = new ArrayList<Integer>();
		first.add(num[0]);
		res.add(first);
		// newRes: add one number to every position of every list {{1}} -> {{1,
		// 2}, {2, 1}}
		// -> {{3, 1, 2}, {1, 3, 2}, {1, 2, 3}, ...}
		for (int i = 1; i < num.length; i++) {
			List<List<Integer>> newRes = new ArrayList<List<Integer>>();
			for (int j = 0; j < res.size(); j++) {
				List<Integer> cur = res.get(j);
				ArrayList<Integer> item0 = new ArrayList<Integer>(cur);
				item0.add(num[i]);
				newRes.add(item0);
				for (int k = cur.size() - 1; k >= 0
						&& num[i] != cur.get(k); k--) {
					ArrayList<Integer> item = new ArrayList<Integer>(cur);
					item.add(k, num[i]);
					newRes.add(item);
				}
			}
			res = newRes;
		}
		return res;
	}

	public int search(int[] nums, int target) {
		int start = 0, end = nums.length - 1;

		while (start + 1 < end) {
			int mid = start + (end - start) / 2;
			if (target == nums[mid]) {
				return mid;
			}
			if (nums[start] <= nums[mid]) {
				if (nums[start] <= target && target < nums[mid]) {
					end = mid - 1;
				} else {
					start = mid + 1;
				}
			} else {
				if (nums[mid] < target && target <= nums[end]) {
					start = mid + 1;
				} else {
					end = mid - 1;
				}
			}
		}
		if (nums[start] == target) {
			return start;
		}
		if (nums[end] == target) {
			return end;
		}
		return -1;
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
		for (ListNode li : lists) {
			if (li != null) {
				heap.add(li);
			}
		}
		while (heap.size() > 0) {
			ListNode top = heap.remove();
			curr.next = top;
			curr = top;
			if (top.next != null) {
				heap.add(top.next);
			}
		}
		return dummy.next;
	}

	public boolean isMatch(String s, String p) {
		if (s == null || p == null) {
			return false;
		}
		boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
		dp[0][0] = true;
		// p = a*, a*b*,
		for (int j = 1; j < p.length(); j++) {
			if (p.charAt(j) == '*' && dp[0][j - 1]) {
				dp[0][j + 1] = true;
			}
		}

		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < p.length(); j++) {
				if (p.charAt(j) == '.' || p.charAt(j) == s.charAt(i)) {
					dp[i + 1][j + 1] = dp[i][j];
				}
				if (j > 0 && p.charAt(j) == '*') {
					if (p.charAt(j - 1) != s.charAt(i)
							&& p.charAt(j - 1) != '.') {
						dp[i + 1][j + 1] = dp[i + 1][j - 1];
					} else {
						dp[i + 1][j + 1] = dp[i + 1][j] || dp[i][j + 1]
								|| dp[i + 1][j - 1];
					}
				}
			}
		}
		return dp[s.length()][p.length()];
	}

	public boolean isMatch44(String s, String p) {
		boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
		dp[0][0] = true;

		for (int j = 0; j < p.length(); j++) {
			if (p.charAt(j) == '*') {
				dp[0][j + 1] = true;
			} else {
				break;
			}
		}

		for (int i = 0; i < s.length(); i++) {
			for (int j = 0; j < p.length(); j++) {
				if (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i)) {
					dp[i + 1][j + 1] = dp[i][j];
				}
				if (p.charAt(j) == '*') {
					for (int k = i + 1; k >= 0; k--) {
						if (dp[k][j]) {
							dp[i + 1][j + 1] = true;
							break;
						}
					}
				}
			}
		}
		return dp[s.length()][p.length()];
	}

	public void testMap() {
		Map<Integer, Integer> map = new LinkedHashMap<>();
		map.put(0, 1);
		map.put(5, 1);
		map.put(2, 1);
		map.put(4112, 1);
		map.put(1213138, 1);
		map.put(80021, 1);
		map.values();
		for (Map.Entry<Integer, Integer> e : map.entrySet()) {
			System.out.println(e.getKey());
			e.getValue();
		}
		Iterator<Entry<Integer, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			System.out.println(it.next().getKey());
		}
		Iterator<Integer> it2 = map.keySet().iterator();
		while (it2.hasNext()) {
			System.out.println(it2.next());
		}
	}

	public List<Interval> insert(List<Interval> intervals,
			Interval newInterval) {
		Interval cur = null;
		Interval merge = new Interval(newInterval.start, newInterval.end);
		boolean merged = false;
		for (int i = 0; i < intervals.size(); i++) {
			cur = intervals.get(i);
			if (cur.end < newInterval.start) {
				// do nothing
			} else if (cur.start > newInterval.end) {
				if (!merged) {
					intervals.add(i, merge);
					merged = true;
					break;
				}
			} else {
				merge.start = Math.min(merge.start, cur.start);
				merge.end = Math.max(merge.end, cur.end);
				intervals.remove(i--);
			}
		}
		if (!merged) {
			intervals.add(merge);
		}
		return intervals;
	}

	public int numDecodings(String s) {
		if (s == null || s.isEmpty() || s.charAt(0) == '0') {
			return 0;
		}
		int[] A = new int[s.length() + 1];
		A[0] = 1;
		A[1] = 1;
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == '0') {
				if (s.charAt(i - 1) != '1' && s.charAt(i - 1) != '2') {
					return 0;
				}
				A[i + 1] = A[i - 1];
			} else {
				A[i + 1] = A[i];
				if (s.charAt(i - 1) != '0'
						&& Integer.parseInt(s.substring(i - 1, i + 1)) <= 26) {
					A[i + 1] += A[i - 1];
				}
			}
		}
		return A[s.length()];
	}

	public boolean isValidBST(TreeNode root) {
		if (root == null) {
			return true;
		}
		Deque<TreeNode> que = new ArrayDeque<>();
		TreeNode cur = root;
		TreeNode pre = null;
		while (cur != null || !que.isEmpty()) {
			if (cur != null) {
				que.addLast(cur);
				cur = cur.left;
			} else {
				cur = que.removeLast();
				if (pre == null) {
					pre = cur;
				} else {
					if (pre.val >= cur.val) {
						return false;
					}
					pre = cur;
				}
				cur = cur.right;
			}
		}
		return true;
	}

	public List<int[]> getSkyline(int[][] buildings) {
		List<int[]> result = new ArrayList<int[]>();
		if (buildings == null || buildings.length == 0) {
			return result;
		}
		List<int[]> hList = new ArrayList<>();
		for (int[] bd : buildings) {
			hList.add(new int[]{bd[0], -bd[2]});
			hList.add(new int[]{bd[1], bd[2]});
		}
		Collections.sort(hList,
				(a, b) -> a[0] != b[0] ? a[0] - b[0] : a[1] - b[1]);

		PriorityQueue<Integer> pq = new PriorityQueue<Integer>((a, b) -> b - a);
		pq.add(0);
		int prev = 0;
		for (int[] h : hList) {
			if (h[1] < 0) {
				pq.add(-h[1]);
			} else {
				pq.remove(h[1]);
			}
			int cur = pq.peek();
			// the current building removed, if the highest height is not
			// changed, then it's covered by other building.
			if (cur != prev) {
				result.add(new int[]{h[0], cur});
				prev = cur;
			}
		}
		return result;
	}

	public int maximalSquare(char[][] matrix) {

		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}
		int m = matrix.length;
		int n = matrix[0].length;
		int[][] t = new int[m][n];
		int max = 0;

		for (int i = 0; i < m; i++) {
			t[i][0] = matrix[i][0] - '0';
		}
		for (int j = 0; j < n; j++) {
			t[0][j] = matrix[0][j] - '0';
		}

		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (matrix[i][j] == '1') {
					int min = Math.min(t[i - 1][j], t[i][j - 1]);
					t[i][j] = Math.min(min, t[i - 1][j - 1]) + 1;
					max = Math.max(max, t[i][j]);
				} else {
					t[i][j] = 0;
				}
			}
		}
		return max * max;
	}

	public boolean canAttendMeetings(Interval[] intervals) {
		if (intervals.length < 2) {
			return true;
		}
		Arrays.sort(intervals, ((a, b) -> a.start - b.start));
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i].start < intervals[i - 1].end) {
				return false;
			}
		}
		return true;
	}

	public int minMeetingRooms(Interval[] intervals) {
		if (intervals.length < 2) {
			return intervals.length;
		}
		Arrays.sort(intervals, ((a, b) -> a.start - b.start));
		PriorityQueue<Interval> heap = new PriorityQueue<>(intervals.length,
				((a, b) -> a.end - b.end));

		heap.add(intervals[0]);
		for (int i = 1; i < intervals.length; i++) {
			Interval interval = heap.peek();
			if (intervals[i].start >= interval.end) {
				interval.end = intervals[i].end;
			} else {
				heap.add(intervals[i]);
			}
		}
		return heap.size();
	}

	public boolean checkSubarraySum(int[] nums, int k) {
		Map<Integer, Integer> map = new HashMap<>();
		map.put(0, -1);
		int sum = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (k != 0) {
				sum = sum % k;
			}
			if (map.containsKey(sum) && i - map.get(sum) > 1) {
				return true;
			}
		}
		return false;
	}

	public int totalHammingDistance(int[] nums) {
		int[] B = new int[32];
		for (int e : nums) {
			for (int i = 0; i < 32; i++) {
				B[i] += (e >> i) & 1;
			}
		}

		int[] B2 = new int[32];
		for (int i = 0; i < 32; i++) {
			B2[i] += (1 << i) & 4;
		}
		int res = 0;
		for (int cnt : B) {
			res += cnt * (nums.length - cnt);
		}
		return res;
	}

	public int minCostII(int[][] costs) {
		if (costs.length == 0) {
			return 0;
		}
		int preMin1 = 0, preMin2 = 0, preIdx1 = -1;
		for (int i = 0; i < costs.length; i++) {
			int m1 = Integer.MAX_VALUE, m2 = Integer.MAX_VALUE, idx1 = -1;
			for (int j = 0; j < costs[0].length; j++) {
				int cost = costs[i][j] + (j != preIdx1 ? preMin1 : preMin2);
				if (cost < m1) { // cost < m1 < m2
					m2 = m1;
					m1 = cost;
					idx1 = j;
				} else if (cost < m2) { // m1 < cost < m2
					m2 = cost;
				}
			}
			preMin1 = m1;
			preMin2 = m2;
			preIdx1 = idx1;
		}
		return preMin1;
	}

	public void wallsAndGates(int[][] rooms) {
		if (rooms.length == 0 || rooms[0].length == 0) {
			return;
		}
		for (int i = 0; i < rooms.length; i++) {
			for (int j = 0; j < rooms[0].length; j++) {
				if (rooms[i][j] == 0) {
					bfs(rooms, i, j);
				}
			}
		}
	}

	void bfs(int[][] rooms, int i, int j) {
		Deque<int[]> queue = new ArrayDeque<>();
		boolean[][] used = new boolean[rooms.length][rooms[0].length];
		int[][] B = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
		queue.add(new int[]{i, j});
		int step = 0;
		while (!queue.isEmpty()) {
			step++;
			int size = queue.size();
			while (size-- > 0) {
				int[] cur = queue.removeFirst();
				for (int[] b : B) {
					int x = cur[0] + b[0], y = cur[1] + b[1];
					if (x >= 0 && x < rooms.length && y >= 0
							&& y < rooms[0].length && rooms[x][y] > 0
							&& !used[x][y]) {
						used[x][y] = true;
						rooms[x][y] = Math.min(rooms[x][y], step);
						queue.addLast(new int[]{x, y});
					}
				}
			}
		}
	}

	public int lengthOfLongestSubstringTwoDistinct(String s) {
		if (s == null || s.isEmpty()) {
			return 0;
		}
		int[] arr = new int[256];
		int len = Integer.MIN_VALUE;

		int start = 0, end = -1;
		int count = 0;
		while (++end < s.length()) {
			if (arr[s.charAt(end)]++ == 0) {
				count++;
			}
			if (count > 2) {
				while (--arr[s.charAt(start++)] > 0);
				count--;
			}
			len = Math.max(len, end - start + 1);
		}
		return len;
	}

	public List<String> findMissingRanges(int[] nums, int lower, int upper) {
		List<String> res = new ArrayList<>();
		int cur = lower;
		boolean over = false;
		for (int i = 0; i < nums.length; i++) {
			if (nums[i] == cur) {
				cur++;
			} else if (nums[i] > cur) {
				res.add(getRange(cur, nums[i] - 1));
				cur = nums[i] + 1;
			}
			if (cur == Integer.MIN_VALUE) {
				over = true;
				break;
			}
		}
		if (!over && cur <= upper) {
			res.add(getRange(cur, upper));
		}
		return res;
	}

	private String getRange(int n1, int n2) {
		return (n1 == n2)
				? String.valueOf(n1)
				: String.format("%d->%d", n1, n2);
	}

	public List<List<String>> groupStrings(String[] strings) {
		Map<List<Integer>, List<String>> map = new HashMap<>();
		for (String s : strings) {
			List<Integer> key = convert(s);
			if (!map.containsKey(key)) {
				map.put(key, new ArrayList<>());
			}
			map.get(convert(s)).add(s);
		}
		return new ArrayList<List<String>>(map.values());
	}

	private List<Integer> convert(String s) {
		List<Integer> li = new ArrayList<>(s.length());
		for (char c : s.toCharArray()) {
			int val = c - s.charAt(0);
			li.add(val >= 0 ? val : val + 26);
		}
		return li;
	}

	public int threeSumSmaller(int[] nums, int target) {
		int count = 0;
		Arrays.sort(nums);
		int len = nums.length;
		for (int i = 0; i < len - 2; i++) {
			int left = i + 1, right = len - 1;
			while (left < right) {
				if (nums[i] + nums[left] + nums[right] < target) {
					count += right - left;
					left++;
				} else {
					right--;
				}
			}
		}
		return count;
	}

	public boolean canPermutePalindrome(String s) {
		Set<Character> set = new HashSet<Character>();
		for (char c : s.toCharArray())
			if (set.contains(c))
				set.remove(c);
			else
				set.add(c);
		return set.size() <= 1;
	}

	public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
		int n = nums.length;
		int[] sum = new int[n + 1], idxL = new int[n], idxR = new int[n],
				res = new int[3];

		for (int i = 0; i < n; i++) {
			sum[i + 1] = sum[i] + nums[i];
		}

		for (int i = k, max = sum[k] - sum[0]; i < n; i++) {
			if (sum[i + 1] - sum[i + 1 - k] > max) { // > max
				idxL[i] = i + 1 - k;
				max = sum[i + 1] - sum[i + 1 - k];
			} else {
				idxL[i] = idxL[i - 1];
			}
		}

		idxR[n - k] = n - k;
		for (int i = n - k - 1, max = sum[n] - sum[n - k]; i >= 0; i--) {
			if (sum[i + k] - sum[i] >= max) { // >= max
				idxR[i] = i;
				max = sum[i + k] - sum[i];
			} else {
				idxR[i] = idxR[i + 1];
			}
		}
		// test all possible middle interval
		int maxsum = 0;
		for (int i = k; i <= n - 2 * k; i++) {
			int l = idxL[i - 1], r = idxR[i + k];
			int val = (sum[i + k] - sum[i]) + (sum[l + k] - sum[l])
					+ (sum[r + k] - sum[r]);
			if (val > maxsum) {
				maxsum = val;
				res[0] = l;
				res[1] = i;
				res[2] = r;
			}
		}
		return res;
	}

	public List<Integer> closestKValues(TreeNode root, double target, int k) {
		List<Integer> res = new ArrayList<>();
		Stack<Integer> s1 = new Stack<>(); // val < target
		Stack<Integer> s2 = new Stack<>(); // val > target
		inorder(root, target, false, s1);
		inorder(root, target, true, s2);
		while (k-- > 0) {
			if (s1.isEmpty())
				res.add(s2.pop());
			else if (s2.isEmpty())
				res.add(s1.pop());
			else if (Math.abs(s1.peek() - target) < Math
					.abs(s2.peek() - target))
				res.add(s1.pop());
			else
				res.add(s2.pop());
		}
		return res;
	}

	// inorder traversal
	void inorder(TreeNode root, double target, boolean reverse,
			Stack<Integer> stack) {
		if (root == null)
			return;
		inorder(reverse ? root.right : root.left, target, reverse, stack);
		// early terminate, no need to traverse the whole tree
		if ((reverse && root.val <= target) || (!reverse && root.val > target))
			return;
		stack.push(root.val);
		inorder(reverse ? root.left : root.right, target, reverse, stack);
	}

	private double avg(int l, int r, int Sum[]) {
		return (double) (Sum[r] - Sum[l]) / (r - l);
	}

	// 644
	public double findMaxAverage(int[] A, int k) {
		int n = A.length;
		int[] Sum = new int[n + 1];
		int[] Q = new int[n + 1];

		Sum[0] = 0;
		for (int i = 1; i <= n; i++)
			Sum[i] = Sum[i - 1] + A[i - 1];

		double ret = Sum[n] / n;
		int l = 0, r = -1;
		for (int i = k; i <= n; i++) {
			int t = i - k;
			while (l < r && avg(Q[r], t, Sum) <= avg(Q[r - 1], t, Sum))
				r--;
			r++;
			Q[r] = t;
			while (l < r && avg(Q[l], i, Sum) <= avg(Q[l + 1], i, Sum))
				l++;
			double tmp = avg(Q[l], i, Sum);
			if (tmp > ret)
				ret = tmp;
		}
		return ret;
	}

	public int minimumDeleteSum(String s1, String s2) {
		int m = s1.length(), n = s2.length();
		int[][] B = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			B[i][0] = B[i - 1][0] + s1.charAt(i - 1);
		}
		for (int j = 1; j <= n; j++) {
			B[0][j] = B[0][j - 1] + s2.charAt(j - 1);
		}
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
					B[i][j] = B[i - 1][j - 1];
				} else {
					B[i][j] = Math.min(B[i][j - 1] + s2.charAt(j - 1),
							B[i - 1][j] + s1.charAt(i - 1));
				}
			}
		}
		return B[m][n];
	}

	public int[] findRedundantConnection(int[][] edges) {
		Map<Integer, List<Integer>> map = new HashMap<>();
		for (int i = 0; i < edges.length; i++) {
			int u = edges[i][0], v = edges[i][1];
			if (!map.containsKey(u)) {
				map.put(u, new ArrayList<Integer>());
			}
			if (!map.containsKey(v)) {
				map.put(v, new ArrayList<Integer>());
			}
			map.get(u).add(i);
			map.get(v).add(i);
		}
		while (true) {
			int count = 0;
			for (int k = 1; k <= edges.length; k++) {
				if (map.containsKey(k) && map.get(k).size() == 1) {
					int idx = map.get(k).get(0);
					int v = edges[idx][0] == k ? edges[idx][1] : edges[idx][0];
					map.get(v).remove(Integer.valueOf(idx));
					map.remove(k);
					count++;
				}
			}
			if (count == 0) {
				break;
			}
		}
		int res = -1;
		for (List<Integer> li : map.values()) {
			for (int idx : li) {
				res = Math.max(idx, res);
			}
		}
		return edges[res];
	}

	public int findLength(int[] A, int[] B) {
		int res = 0;
		int[][] L = new int[A.length + 1][B.length + 1];
		for (int i = 1; i <= A.length; i++) {
			for (int j = 1; j <= B.length; j++) {
				L[i][j] = A[i - 1] == B[j - 1] ? L[i - 1][j - 1] + 1 : 0;
				res = Math.max(res, L[i][j]);
			}
		}

		return res;
	}

	public int smallestDistancePair(int[] nums, int k) {
		Arrays.sort(nums);
		int low = 0, hi = nums[nums.length - 1] - nums[0];
		while (low < hi) {
			int cnt = 0, j = 0, mid = (low + hi) / 2;
			for (int i = 0; i < nums.length; i++) {
				while (j < nums.length && nums[j] - nums[i] <= mid) {
					j++;
				}
				cnt += j - i - 1;
			}
			if (cnt >= k)
				hi = mid;

			else
				low = mid + 1;
		}
		return low;
	}

	public int kEmptySlots(int[] flowers, int k) {
		// days[i] is the blooming day of the flower in position i+1
		int[] days = new int[flowers.length];
		for (int i = 0; i < flowers.length; i++) {
			days[flowers[i] - 1] = i + 1;
		}
		int left = 0, right = k + 1, res = Integer.MAX_VALUE;
		for (int i = 0; right < days.length; i++) {
			if (days[i] < days[left] || days[i] <= days[right]) {
				if (i == right) {
					res = Math.min(res, Math.max(days[left], days[right]));
				}
				left = i;
				right = k + 1 + i;
			}
		}
		return (res == Integer.MAX_VALUE) ? -1 : res;
	}

	public String longestWord(String[] words) {
		String res = "";
		Arrays.sort(words);
		Set<String> set = new HashSet<>();
		for (String s : words) {
			if (s.length() == 1
					|| set.contains(s.substring(0, s.length() - 1))) {
				set.add(s);
				if (s.length() > res.length()) {
					res = s;
				}
			}
		}
		return res;
	}

	@Test
	public void test() {
		//System.out.println(false && false || true);
		//System.out.println(true || false && false);
		//System.out.println(minimumDeleteSum("delete", "leet"));
	}

	// @Test
	public void test1() {
		int[] Aa = {536, 493};
		int[] res = findChange(62878, Aa);
		for (int e : res) {
			System.out.println(e);
		}
		int[] A2 = {1, 2};
		rotate(A2, 1);
		A2.toString();
		Arrays.asList(1, 2, 3);
		// System.out.println("a".substring(1,1));
		Set<String> dict = new HashSet<>();
		dict.add("a");
		dict.add("cc");
		dict.add("df");
		dict.add("acdfcca");
		assertTrue(canComp("acca", dict));
		assertTrue(!canComp("acdfca", dict));
		assertTrue(canComp("accaaccaaccaa", dict));
		assertTrue(canComp("df", dict));
		assertTrue(canComp("aaadfdf", dict));

		System.out.println("x,".split(",").length);
		int[] A = {1, 5, 5, 5, 5};
		int[] out = findRange(A, 9);
		System.out.println(out[0]);
		System.out.println(out[1]);
		System.out.println(originalDigits("owoztneoerowoztneoer"));
	}

	@SuppressWarnings("unused")
	public void test0() {
		System.out.println("7,x,x,,4".split("[,]+").length);
		String s = new String();
		assertTrue(s.equals(""));
		s.concat("a");
		String t1 = "abb";
		String t2 = "ab" + "b";
		String t3 = "ab".concat("b");
		String s4 = t2.substring(0, 2);
		assertTrue(t1 == t2);
		// System.out.println(findAnagrams("acb", "ab"));
		String ss = "href=\"/account/registerBorrowerInit.actionyu\">";
		ss.substring(0);
		// String s = "abcedefg";

		// String line = "This order was placed for QT3000! OK?";
		// String pattern = "(.*)(\\d+)(.*)";

		Pattern pattern = Pattern.compile("href=\"(.*)\\.action");
		Matcher m = pattern.matcher(ss);
		while (m.find()) {
			System.out.println("Found " + m.group());
			System.out.println("  starting at index " + m.start()
					+ " and ending at index " + m.end());
			System.out.println();
		}
	}
}
