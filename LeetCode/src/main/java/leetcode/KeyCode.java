package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.junit.Test;

public class KeyCode {

	public void throwsMethod(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
			// unreachable code
			// throw new IllegalArgumentException("n < 0");
		} else if (n == 0) {
			throw new IllegalArgumentException("n = 0");
		}
		n++;
	}

	// binary
	public int countDigits(int num) {
		int result = 0;
		while (num > 0) {
			if ((num & 1) == 1) { // (num & 1)
				result++;
			}
			num >>= 1; // num >>= 1;
		}
		return result;
	}

	// int to hex
	public String toHex(int num) {
		char[] charMap = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		StringBuilder sb = new StringBuilder();
		do {
			sb.append(charMap[num & 15]);
			num >>>= 4;
		} while (num != 0);
		return sb.reverse().toString();
	}

	//
	public List<List<Integer>> combine(int n, int k) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		combineHelper(1, k, n, new ArrayList<Integer>(), res);
		return res;
	}

	void combineHelper(int start, int k, int n, ArrayList<Integer> item, List<List<Integer>> res) {
		if (k == 0) {
			res.add(new ArrayList<Integer>(item));
			return;
		}
		for (int i = start; i <= n - k + 1; i++) {
			item.add(i);
			combineHelper(i + 1, k - 1, n, item, res);
			item.remove(item.size() - 1);
		}
	}

	void combineSum(int start, int k, int n, int sum, List<Integer> res) {
		if (k == 0) {
			res.add(sum);
			return; // remember to return here
		}
		for (int i = start; i <= n - k + 1; i++) {
			sum += i;
			combineSum(i + 1, k - 1, n, sum, res);
		}
	}

	public int[] searchRange(int[] nums, int target) {
		int[] res = { -1, -1 };
		if (nums == null || nums.length == 0) {
			return res;
		}
		int s = 0, e = nums.length - 1;
		// find the first number that >= target
		while (s < e) {
			int mid = s + (e - s) / 2;
			if (nums[mid] < target) {
				s = mid + 1;
			} else {
				e = mid;
			}
		}
		if (nums[s] > target) {
			return res;
		}
		int left = nums[s] == target ? s : s + 1;

		s = 0;
		e = nums.length - 1;
		while (s < e) {
			int mid = e - (e - s) / 2;
			if (nums[mid] > target) {
				e = mid - 1;
			} else {
				s = mid;
			}
		}
		if (nums[s] < target) {
			return res;
		}
		int right = nums[s] == target ? s : s - 1;
		res[0] = left;
		res[1] = right;
		return res;
	}

	public int findKthLargest(int[] nums, int k) {
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(k);
		for (int i : nums) {
			if (q.size() < k) {
				q.add(i);
			} else {
				if (i > q.peek()) {
					q.remove();
					q.add(i);
				}
			}
		}
		return q.peek();
	}

	public int findKthLargest2(int[] nums, int k) {
		return findKthLargest2(nums, nums.length - k, 0, nums.length - 1);
	}

	private int findKthLargest2(int[] nums, int k, int i, int j) {
		if (i >= j) {
			return nums[i];
		}
		int m = i;
		for (int n = i + 1; n <= j; n++) {
			if (nums[n] < nums[i]) {
				swap(nums, ++m, n);
			}
		}
		swap(nums, i, m);
		if (m == k) {
			return nums[m];
		} else if (m < k) {
			return findKthLargest2(nums, k, m + 1, j);
		} else {
			return findKthLargest2(nums, k, i, m - 1);
		}
	}

	private void swap(int[] nums, int i, int j) {
		int temp = nums[i];
		nums[i] = nums[j];
		nums[j] = temp;
	}

	public void quickSort(int[] arr, int low, int high) {
		if (arr == null || arr.length == 0)
			return;
		if (low >= high)
			return;
		int middle = low + (high - low) / 2;
		int pivot = arr[middle];
		int i = low, j = high;
		while (i <= j) {
			// here i and j will not cross pivot, so no need to do boundary
			// check
			while (arr[i] < pivot)
				i++;
			while (arr[j] > pivot)
				j--;
			if (i <= j) {
				int temp = arr[i];
				arr[i] = arr[j];
				arr[j] = temp;
				i++;
				j--;
			}
		}
		if (low < j)
			quickSort(arr, low, j);
		if (high > i)
			quickSort(arr, i, high);
	}

	// ==400. Nth Digit
	public int findNthDigit(int n) {
		int num = 1;
		// use long here
		long count = 9;
		long curr = num * count;
		while (n > curr) {
			n -= curr;
			num++;
			count *= 10;
			curr = num * count;
		}
		// parse int back
		int value = (int) (count / 9) + (n - 1) / num;
		int digit = (n - 1) % num;
		char c = ("" + value).charAt(digit);
		return c - '0';
	}

	// 79. Word Search
	public boolean wordSearch(char[][] board, String word) {
		if (board == null || board.length == 0) {
			return false;
		}
		if (word.length() == 0) {
			return true;
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (wordSearchHelper(board, word, i, j, 0)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean wordSearchHelper(char[][] board, String word, int i, int j, int k) {
		if (k == word.length()) {
			return true;
		}
		// if use m = board.length, n = board[0].length in exist(), here can't
		// use directly
		if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word.charAt(k)) {
			return false;
		}
		board[i][j] = '#';
		boolean res = wordSearchHelper(board, word, i, j - 1, k + 1) || wordSearchHelper(board, word, i, j + 1, k + 1)
				|| wordSearchHelper(board, word, i - 1, j, k + 1) || wordSearchHelper(board, word, i + 1, j, k + 1);
		board[i][j] = word.charAt(k);
		return res;
	}

	// -->Recursion 01 each char appears at least k times
	public int longestSubstring(String s, int k) {
		if (s == null || s.isEmpty()) {
			return 0;
		}
		int n = s.length();
		if (k <= 1) {
			return n;
		}
		int counter[] = new int[26];
		boolean valid[] = new boolean[26];

		char ss[] = s.toCharArray();
		for (int i = 0; i < n; i++) {
			counter[ss[i] - 'a']++;
		}
		boolean fullValid = true;
		for (int i = 0; i < 26; i++) {
			if (counter[i] > 0 && counter[i] < k) {
				valid[i] = false;
				fullValid = false;
			} else {
				valid[i] = true;
			}
		}
		if (fullValid) {
			return s.length();
		}
		int max = 0;
		int lastStart = 0;
		for (int i = 0; i < n; i++) {
			if (!valid[ss[i] - 'a']) {
				max = Math.max(max, longestSubstring(s.substring(lastStart, i), k));
				lastStart = i + 1;
			}
		}
		max = Math.max(max, longestSubstring(s.substring(lastStart, n), k));
		return max;
	}

	// 300. Longest Increasing Subsequence
	public int lengthOfLIS(int[] nums) {
		if (nums.length == 0) {
			return 0;
		}
		// res[i] stores min value that make LIS = i
		// {2, 8, 9, 3, 4, 5} -> {2, 8, 9} -> {2, 3, 9} -> {2, 3, 4} -> {2, 3,
		// 4, 5}
		List<Integer> res = new ArrayList<>();
		res.add(nums[0]);
		for (int a : nums) {
			if (a < res.get(0)) {
				res.set(0, a);
			} else if (a > res.get(res.size() - 1)) {
				res.add(a);
			} else {
				int left = 0, right = res.size() - 1;
				while (left < right) {
					int mid = left + (right - left) / 2;
					if (res.get(mid) < a) {
						left = mid + 1;
					} else {
						right = mid;
					}
				}
				res.set(right, a);
			}
		}
		return res.size();
	}

	// 402. Remove K Digits
	public String removeKdigits(String num, int k) {
		if (num.length() == k) {
			return "0";
		}
		k = num.length() - k;
		ArrayDeque<Character> qu = new ArrayDeque<>();
		for (int i = 0; i < num.length(); i++) {
			// also check total count of available char before delete
			while (qu.size() > 0 && qu.peekLast() > num.charAt(i) && qu.size() + num.length() - i > k) {
				qu.removeLast();
			}
			qu.add(num.charAt(i));
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < k; i++) {
			sb.append(qu.removeFirst());
		}
		while (sb.length() > 0 && sb.charAt(0) == '0') {
			sb.deleteCharAt(0);
		}
		if (sb.length() == 0) {
			return "0";
		}
		return sb.toString();
	}

	// -->Recursion 02
	public boolean canFound(String s) {
		if (s.isEmpty()) {
			return true;
		}
		for (int i = 1; i <= s.length(); i++) {
			if (isValid(s.substring(0, i))) {
				if (canFound(s.substring(i))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isValid(String s) {
		return s.equals("aa") || s.equals("b");
	}

	public boolean canFound2(String s) {
		int n = s.length();
		boolean[][] A = new boolean[n + 1][n + 1];
		for (int j = 1; j <= n; j++) {
			for (int i = 0; i + j <= n; i++) {
				if (isValid(s.substring(i, i + j))) {
					A[i][i + j] = true;
				} else {
					for (int k = i + 1; k < i + j; k++) {
						if (A[i][k] && A[k][i + j]) {
							A[i][i + j] = true;
							break;
						}
					}
				}
			}
		}
		return A[0][n];
	}

	public int[] findDiagonalOrder(int[][] matrix) {
		int n = matrix.length;
		int[] res = new int[n];
		int cur = -1;
		boolean fromZero = true;
		for (int sum = 0; sum < 2 * n - 1; sum++) {
			fromZero = !fromZero;
			int start = sum >= n ? sum - n + 1 : 0;
			if (fromZero) {
				for (int i = start; i < n && sum - i < n; i++) {
					cur++;
					res[cur] = matrix[i][sum - i];
				}
			} else {
				for (int i = start; i < n && sum - i < n; i++) {
					cur++;
					res[cur] = matrix[sum - i][i];
				}
			}
		}
		return res;
	}
	
	public boolean isPossible(int[] nums) {
		List<Integer> list = new LinkedList<>();
		for (int e : nums) {
			list.add(e);
		}
		return dfs(list);
	}

	private boolean dfs(List<Integer> list) {
		if (list.size() == 0) {
			return true;
		}
		if (list.size() == 1 || list.size() == 2) {
			return false;
		}
		List<Integer> cur = new LinkedList<>(list);
		int first = cur.get(0);
		cur.remove(0);
		int idx2 = Collections.binarySearch(cur, first + 1);
		if (idx2 >= 0) {
			cur.remove(idx2);
		} else {
			return false;
		}
		int idx3 = Collections.binarySearch(cur, first + 2);
		if (idx3 >= 0) {
			cur.remove(idx3);
		} else {
			return false;
		}
		if (dfs(cur)) {
			return true;
		}
		boolean res = false;
		for (int k = first + 3;; k++) {
			int idxk = Collections.binarySearch(cur, k);
			if (idxk >= 0) {
				cur.remove(idxk);
				if (dfs(cur)) {
					res = true;
					break;
				}
			} else {
				break;
			}
		}
		return res;
	}

	@Test
	public void test() {
		int[] A2 = { 1, 2, 3, 4, 6, 7};
		System.out.println(isPossible(A2));
		
		//String canFound = "b";
		//System.out.println(canFound(canFound));
		//System.out.println(canFound2(canFound));
	}

	@SuppressWarnings("unused")
	// @Test
	public void testBack() {
		int[] A3 = { -1, 3, -4, 5, 1, -6, 2, 1 };
		int[] A = { 3, 7, 8, 5, 2, 1, 9, 5, 4 };
		int[] A2 = { 1, 1, 1, 5 };
		Integer[] IN = { 3, 7, 8 };
		quickSort(A, 0, A.length - 1);
		System.out.println(Arrays.asList(A)); // [[I@2cdf8d8a]
		System.out.println(A);// [I@2cdf8d8a
		System.out.println(IN);
		System.out.println(Arrays.asList(IN));
		System.out.println(combine(6, 2));
		System.out.println(findKthLargest(A, 2));
	}
}
