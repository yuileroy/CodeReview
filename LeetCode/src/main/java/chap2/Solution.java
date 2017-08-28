package chap2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import org.junit.Test;

import leetcode.TreeNode;

public class Solution {

	public TreeNode constructMaximumBinaryTree(int[] nums) {
		return fn(nums, 0, nums.length - 1);
	}

	private TreeNode fn(int[] nums, int start, int end) {
		if (start > end) {
			return null;
		}
		int max = Integer.MIN_VALUE;
		int ridx = -1;
		for (int i = start; i <= end; i++) {
			if (nums[i] >= max) {
				ridx = i;
			}
		}
		TreeNode root = new TreeNode(nums[ridx]);
		root.left = fn(nums, start, ridx - 1);
		root.right = fn(nums, ridx + 1, end);
		return root;
	}

	public int findSmaller(List<Integer> li, int val) {
		int start = 0, end = li.size() - 1;
		while (start < end) {
			int mid = start + (end - start) / 2;
			if (li.get(mid) <= val) {
				start = mid + 1;
			} else {
				end = mid - 1;
			}
		}
		if (li.get(start) > val) {
			start--;
		}
		return start;
	}

	public class SummaryRanges {
		List<Integer> li;

		/** Initialize your data structure here. */
		public SummaryRanges() {
			li = new LinkedList<>();
		}

		public void addNum(int val) {
			if (li.isEmpty()) {
				li.add(val);
				li.add(val);
				return;
			}
			int start = 0, end = li.size() - 1;
			while (start < end) {
				int mid = start + (end - start) / 2;
				if (li.get(mid) == val) {
					return;
				} else if (li.get(mid) < val) {
					start = mid + 1;
				} else {
					end = mid - 1;
				}
			}
			if (li.get(start) > val) {
				start--;
			}
			if (start == -1) {
				if (li.get(0) == val + 1) {
					li.set(0, val);
				} else {
					li.add(0, val);
					li.add(0, val);
				}
			} else if (start == li.size() - 1) {
				if (li.get(start) == val - 1) {
					li.set(li.size() - 1, val);
				} else {
					li.add(val);
					li.add(val);
				}
			} else if (start % 2 == 0) {
			} else {
				if (li.get(start) == val - 1 && li.get(start + 1) == val + 1) {
					li.remove(start);
					li.remove(start);
				} else if (li.get(start) == val - 1 && li.get(start + 1) > val + 1) {
					li.set(start, val);
				} else if (li.get(start) < val - 1 && li.get(start + 1) == val + 1) {
					li.set(start + 1, val);
				} else {
					li.add(start + 1, val);
					li.add(start + 1, val);
				}
			}
		}

		public List<Interval> getIntervals() {
			List<Interval> res = new ArrayList<>();
			for (int i = 0; i < li.size(); i += 2) {
				Interval e = new Interval(li.get(i), li.get(i + 1));
				res.add(e);
			}
			return res;
		}
	}

	class Interval {
		int start;
		int end;

		Interval() {
			start = 0;
			end = 0;
		}

		Interval(int s, int e) {
			start = s;
			end = e;
		}
	}

	class Envelope {
		int width;
		int height;

		Envelope(int width, int height) {
			this.width = width;
			this.height = height;
		}
	}

	public int maxEnvelopes(int[][] envelopes) {
		int length = envelopes.length;
		Envelope[] sortedEnvelopes = new Envelope[length];
		int index = 0;
		for (int[] num : envelopes) {
			sortedEnvelopes[index++] = new Envelope(num[0], num[1]);
		}
		Arrays.sort(sortedEnvelopes, new Comparator<Envelope>() {
			public int compare(Envelope e1, Envelope e2) {
				if (e1.width != e2.width)
					return e1.width - e2.width;
				return e1.height - e2.height;
			}
		});
		int[] count = new int[length];
		int maxCount = 0;
		for (int i = 0; i < length; i++) {
			Envelope cur = sortedEnvelopes[i];
			count[i] = 1;
			for (int j = i - 1; j >= 0; j--) {
				Envelope tmp = sortedEnvelopes[j];
				if (tmp.width < cur.width && tmp.height < cur.height && count[j] + 1 > count[i]) {
					count[i] = count[j] + 1;
				}
			}
			if (count[i] > maxCount)
				maxCount = count[i];
		}
		return maxCount;
	}

	public int largestRectangleArea(int[] height) {
		Stack<Integer> stack = new Stack<Integer>();
		int i = 0;
		int maxArea = 0;
		// add a zero to the end to calculate last increasing group
		int[] h = Arrays.copyOf(height, height.length + 1);
		while (i < h.length) {
			if (stack.isEmpty() || h[stack.peek()] <= h[i]) {
				stack.push(i++);
			} else {
				int t = stack.pop();
				maxArea = Math.max(maxArea, h[t] * (stack.isEmpty() ? i : i - stack.peek() - 1));
			}
		}
		return maxArea;
	}

	public List<int[]> getSkyline(int[][] buildings) {
		List<int[]> result = new ArrayList<int[]>();
		if (buildings == null || buildings.length == 0) {
			return result;
		}

		List<Height> heights = new ArrayList<Height>();
		for (int[] building : buildings) {
			heights.add(new Height(building[0], -building[2]));
			heights.add(new Height(building[1], building[2]));
		}
		Collections.sort(heights, new Comparator<Height>() {
			public int compare(Height h1, Height h2) {
				return h1.index != h2.index ? h1.index - h2.index : h1.height - h2.height;
			}
		});

		// sort desc Height by index, height
		// if height < 0, add(); else remove()
		// compare prev and peek()
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(Collections.reverseOrder());
		pq.add(0);
		int prev = 0;
		for (Height h : heights) {
			if (h.height < 0) {
				pq.add(-h.height);
			} else {
				pq.remove(h.height);
			}
			int cur = pq.peek();
			// the current building removed, if the highest height is not
			// changed, then it's covered by other
			// building.
			if (cur != prev) {
				result.add(new int[] { h.index, cur });
				prev = cur;
			}
		}

		return result;
	}

	class Height {
		int index;
		int height;

		Height(int index, int height) {
			this.index = index;
			this.height = height;
		}
	}

	public int minPatches(int[] nums, int n) {
		// use long, miss > Integer.MAX_VALUE
		long miss = 1;
		int i = 0;
		int res = 0;
		while (miss <= n) {
			if (i < nums.length && nums[i] <= miss) {
				miss += nums[i++];
			} else {
				// add miss to array and miss = miss + miss;
				miss += miss;
				res++;
			}
			System.out.println("miss: " + miss + " res  " + res + " i " + i);
		}
		return res;
	}

	public int maxEnvelopes0(int[][] envelopes) {
		for (int[] ar : envelopes) {
			if (ar[0] > ar[1]) {
				int tmp = ar[0];
				ar[0] = ar[1];
				ar[1] = tmp;
			}
		}

		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> track = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < envelopes.length; i++) {
			map.put(i, new ArrayList<Integer>());
			track.put(i, new ArrayList<Integer>());
		}

		for (int i = 0; i < envelopes.length; i++) {
			for (int j = i + 1; j < envelopes.length; j++) {
				if (envelopes[i][0] < envelopes[j][0] && envelopes[i][1] < envelopes[j][1]) {
					map.get(j).add(i);
					track.get(i).add(j);
				} else if (envelopes[i][0] > envelopes[j][0] && envelopes[i][1] > envelopes[j][1]) {
					map.get(i).add(j);
					track.get(j).add(i);
				}
			}
		}

		int res = 0;
		List<Integer> li = new ArrayList<Integer>();
		while (!map.isEmpty()) {
			li.clear();
			for (Integer key : map.keySet()) {
				if (map.get(key).isEmpty()) {
					li.add(key);
				}
			}
			for (Integer key : map.keySet()) {
				if (li.contains(key)) {
					for (Integer back : track.get(key)) {
						map.get(back).remove(key);
						System.out.println(key);
					}
				}
			}
			for (Integer key : li) {
				map.remove(key);
			}

			// ConcurrentModificationException
			/*
			 * for(Integer key : map.keySet()) { if(map.get(key).isEmpty()) {
			 * map.remove(key); } for(Integer back : track.get(key)) {
			 * map.get(back).remove(new Integer(key)); } }
			 */
			res++;
		}

		return res;
	}

	public int[] productExceptSelf(int[] nums) {

		int len = nums.length;
		int p = 1;
		int[] arr = new int[len];
		arr[0] = 1;

		for (int i = 1; i < len; i++) {
			p = p * nums[i - 1];
			arr[i] = p;
		}

		p = 1;
		for (int i = len - 2; i >= 0; i--) {
			p = p * nums[i + 1];
			arr[i] *= p;
		}

		return arr;
	}

	public List<List<Integer>> combinationSum3(int k, int n) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		List<Integer> list = new ArrayList<Integer>();
		dfs(result, 1, n, list, k);
		return result;
	}

	public void dfs(List<List<Integer>> result, int start, int sum, List<Integer> list, int k) {
		if (sum == 0 && list.size() == k) {
			List<Integer> temp = new ArrayList<Integer>();
			temp.addAll(list);
			result.add(temp);
		}

		for (int i = start; i <= 9; i++) {
			if (sum - i < 0 || list.size() > k) {
				break;
			}
			list.add(i);
			dfs(result, i + 1, sum - i, list, k);
			list.remove(list.size() - 1);
		}
	}

	public int threeSumClosest(int[] nums, int target) {
		if (nums == null || nums.length < 3) {
			return 0;
		}

		int min = Integer.MAX_VALUE;
		int val = 0;
		Arrays.sort(nums);
		for (int i = 0; i <= nums.length - 3; i++) {
			int low = i + 1;
			int high = nums.length - 1;
			while (low < high) {
				int sum = nums[i] + nums[low] + nums[high];
				if (Math.abs(target - sum) < min) {
					min = Math.abs(target - sum);
					val = sum;
				}

				if (target == sum) {
					return val;
				} else if (target > sum) {
					low++;
				} else {
					high--;
				}
			}
		}
		return val;
	}

	public List<List<Integer>> combinationSum2(int[] candidates, int target) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (candidates == null || candidates.length == 0) {
			return res;
		}
		Arrays.sort(candidates);
		helper(candidates, 0, target, new ArrayList<Integer>(), res);
		return res;
	}

	private void helper(int[] candidates, int start, int target, List<Integer> item, List<List<Integer>> res) {
		if (target == 0) {
			res.add(new ArrayList<Integer>(item));
			return;
		}
		if (target < 0 || start >= candidates.length) {
			return;
		}
		for (int i = start; i < candidates.length; i++) {
			if (i > start && candidates[i] == candidates[i - 1])
				continue;
			item.add(candidates[i]);
			helper(candidates, i + 1, target - candidates[i], item, res);
			item.remove(item.size() - 1);
		}
	}

	public int minSubArrayLen(int s, int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		int left = 0, right = 0, sum = 0, len = nums.length, res = len + 1;
		while (right < len) {
			while (sum < s && right < len) {
				sum += nums[right++];
			}
			while (sum >= s) {
				res = Math.min(res, right - left);
				sum -= nums[left++];
			}
		}
		return res == len + 1 ? 0 : res;
	}

	public int countNodes(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int l = getLeft(root) + 1;
		int r = getRight(root) + 1;

		if (l == r) {
			return (2 << (l - 1)) - 1;
		} else {
			return countNodes(root.left) + countNodes(root.right) + 1;
		}
	}

	private int getLeft(TreeNode root) {
		int count = 0;
		while (root.left != null) {
			root = root.left;
			count++;
		}
		return count;
	}

	private int getRight(TreeNode root) {
		int count = 0;
		while (root.right != null) {
			root = root.right;
			count++;
		}
		return count;
	}

	public List<List<Integer>> permute7(int[] nums) {
		List<List<Integer>> res = new ArrayList<List<Integer>>();
		if (nums == null || nums.length == 0)
			return res;
		helper(nums, new boolean[nums.length], new ArrayList<Integer>(), res);
		return res;
	}

	private void helper(int[] nums, boolean[] used, List<Integer> item, List<List<Integer>> res) {
		if (item.size() == nums.length) {
			res.add(new ArrayList<Integer>(item));
			return;
		}
		for (int i = 0; i < nums.length; i++) {
			if (!used[i]) {
				used[i] = true;
				item.add(nums[i]);
				helper(nums, used, item, res);
				item.remove(item.size() - 1);
				used[i] = false;
			}
		}
	}

	// use LinkedList if insert to the start/end only
	LinkedList<String> res;
	// only need to select the least one
	Map<String, PriorityQueue<String>> map;

	public List<String> findItinerary(String[][] tickets) {
		if (tickets == null || tickets.length == 0) {
			return new LinkedList<String>();
		}
		res = new LinkedList<String>();
		map = new HashMap<String, PriorityQueue<String>>();
		for (String[] ticket : tickets) {
			if (!map.containsKey(ticket[0])) {
				map.put(ticket[0], new PriorityQueue<String>());
			}
			map.get(ticket[0]).add(ticket[1]);
		}
		dfs("A");
		System.out.println(res);
		return res;
	}

	public void dfs(String cur) {
		while (map.containsKey(cur) && !map.get(cur).isEmpty()) {
			dfs(map.get(cur).remove());
		}
		res.addFirst(cur);
	}

	public boolean isUgly(int num) {
		while (num / 2 * 2 == num) {
			num = num / 2;
		}
		while (num / 3 * 3 == num) {
			num = num / 3;
		}
		while (num / 5 * 5 == num) {
			num = num / 5;
		}
		return num == 1;
	}

	class MyQueue {

		Stack<Integer> temp = new Stack<Integer>();
		Stack<Integer> value = new Stack<Integer>();

		// Push element x to the back of queue.
		public void push(int x) {
			if (value.isEmpty()) {
				value.push(x);
			} else {
				while (!value.isEmpty()) {
					temp.push(value.pop());
				}
				value.push(x);
				while (!temp.isEmpty()) {
					value.push(temp.pop());
				}
			}
		}

		// Removes the element from in front of queue.
		public void pop() {
			value.pop();
		}

		// Get the front element.
		public int peek() {
			return value.peek();
		}

		// Return whether the queue is empty.
		public boolean empty() {
			return value.isEmpty();
		}
	}

	class MyQueue2 {
		// Push element x to the back of queue.
		Stack<Integer> s1 = new Stack<>();
		Stack<Integer> s2 = new Stack<>();

		public void push(int x) {
			s1.push(x);
		}

		// Removes the element from in front of queue.
		public void pop() {
			if (!s2.isEmpty()) {
				s2.pop();
			} else {
				while (!s1.isEmpty()) {
					s2.push(s1.pop());
				}
				s2.pop();
			}
		}

		// Get the front element.
		public int peek() {
			if (!s2.isEmpty()) {
				return s2.peek();
			} else {
				while (!s1.isEmpty()) {
					s2.push(s1.pop());
				}
				return s2.peek();
			}
		}

		// Return whether the queue is empty.
		public boolean empty() {
			return s1.isEmpty() && s2.isEmpty();
		}
	}

	class MyStack {

		Queue<Integer> q1 = new ArrayDeque<>();
		Queue<Integer> q2 = new ArrayDeque<>();
		boolean item1 = true;

		// Push element x onto stack.
		public void push(int x) {
			if (q1.isEmpty()) {
				q1.add(x);
				item1 = true;
			} else if (q2.isEmpty()) {
				q2.add(x);
				item1 = false;
			} else {
				if (item1) {
					while (!q2.isEmpty()) {
						q1.add(q2.remove());
					}
					q2.add(x);
					item1 = false;
				} else {
					while (!q1.isEmpty()) {
						q2.add(q1.remove());
					}
					q1.add(x);
					item1 = true;
				}
			}
		}

		// Removes the element on top of the stack.
		public void pop() {
			if (q1.isEmpty()) {
				q2.remove();
			} else if (q2.isEmpty()) {
				q1.remove();
			} else {
				if (item1) {
					q1.remove();
				} else {
					q2.remove();
				}
			}
		}

		// Get the top element.
		public int top() {
			if (q1.isEmpty()) {
				return q2.peek();
			} else if (q2.isEmpty()) {
				return q1.peek();
			} else {
				return item1 ? q1.peek() : q2.peek();
			}
		}

		// Return whether the stack is empty.
		public boolean empty() {
			return q1.isEmpty() && q2.isEmpty();
		}
	}

	public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
		if (C < E || G < A)
			return (G - E) * (H - F) + (C - A) * (D - B);

		if (D < F || H < B)
			return (G - E) * (H - F) + (C - A) * (D - B);

		int right = Math.min(C, G);
		int left = Math.max(A, E);
		int top = Math.min(H, D);
		int bottom = Math.max(F, B);

		return (G - E) * (H - F) + (C - A) * (D - B) - (right - left) * (top - bottom);
	}

	public boolean containsNearbyDuplicate(int[] nums, int k) {
		Map<Integer, Integer> map = new HashMap<>();

		for (int i = 0; i < nums.length; i++) {
			if (map.containsKey(nums[i])) {
				int pre = map.get(nums[i]);
				if (i - pre <= k)
					return true;
			}
			map.put(nums[i], i);
		}
		return false;
	}

	public boolean check(String s, String t) {

		HashMap<Character, Character> map1 = new HashMap<>();
		HashMap<Character, Character> map2 = new HashMap<>();

		for (int i = 0; i < s.length(); i++) {
			char c1 = s.charAt(i);
			char c2 = t.charAt(i);
			if (map1.containsKey(c1)) {
				if (map1.get(c1) != c2)
					return false;
			}
			if (map2.containsKey(c2)) {
				if (map2.get(c2) != c1)
					return false;
			}
			map1.put(c1, c2);
			map2.put(c2, c1);
		}
		return true;
	}

	public int countPrimes(int n) {
		boolean[] isPrime = new boolean[n];
		for (int i = 2; i < n; i++) {
			isPrime[i] = true;
		}
		// Loop's ending condition is i * i < n instead of i < sqrt(n)
		// to avoid repeatedly calling an expensive function sqrt().
		for (int i = 2; i * i < n; i++) {
			if (!isPrime[i])
				continue;
			for (int j = i * i; j < n; j += i) {
				isPrime[j] = false;
			}
		}
		int count = 0;
		for (int i = 2; i < n; i++) {
			if (isPrime[i])
				count++;
		}
		return count;
	}

	boolean isHappy(int n) {
		Set<Integer> set = new HashSet<>();
		while (n != 1) {
			int sum = 0;
			while (n > 0) {
				sum += ((n % 10) * (n % 10));
				n /= 10;
			}
			n = sum;
			if (n != 1 && set.contains(n)) {
				return false;
			} else {
				set.add(n);
			}
		}
		return true;
	}

	public int reverseBits(int n) {
		int res = 0;
		for (int i = 0; i < 32; i++) {
			if ((n & 1) == 1) {
				res = (res << 1) + 1;
			} else {
				res = res << 1;
			}
			n = n >> 1;
		}
		return res;
	}

	public String convertToTitle(int n) {
		String res = "";
		int rest = n;
		int mod = 0;
		while (rest > 26) {
			mod = rest % 26;
			rest = rest / 26;
			if (mod != 0) {
				res = (char) (mod - 1 + 'A') + res;
			} else {
				res = 'Z' + res;
				rest--;
			}
		}
		res = (char) (rest - 1 + 'A') + res;
		return res;
	}

	public int compareVersion(String version1, String version2) {
		String[] arr1 = version1.split("(.)+");
		String[] arr2 = version2.split("\\.");
		int i = 0;
		while (i < arr1.length || i < arr2.length) {
			if (i < arr1.length && i < arr2.length) {
				if (Integer.parseInt(arr1[i]) < Integer.parseInt(arr2[i])) {
					return -1;
				} else if (Integer.parseInt(arr1[i]) > Integer.parseInt(arr2[i])) {
					return 1;
				}
			} else if (i < arr1.length) {
				if (Integer.parseInt(arr1[i]) != 0) {
					return 1;
				}
			} else if (i < arr2.length) {
				if (Integer.parseInt(arr2[i]) != 0) {
					return -1;
				}
			}
			i++;
		}
		return 0;
	}

	public List<List<Integer>> palindromePairs(String[] words) {
		Map<String, Integer> map = new HashMap<>();
		Map<String, Integer> revMap = new HashMap<>();
		String[] revWords = new String[words.length];
		for (int i = 0; i < words.length; ++i) {
			String s = words[i];
			String r = new StringBuilder(s).reverse().toString();
			map.put(s, i);
			revMap.put(r, i);
			revWords[i] = r;
		}
		List<List<Integer>> result = new ArrayList<>();
		result.addAll(findPairs(words, revWords, revMap, false));
		result.addAll(findPairs(revWords, words, map, true));
		return result;
	}

	private List<List<Integer>> findPairs(String[] words, String[] revWords, Map<String, Integer> revMap,
			boolean reverse) {
		List<List<Integer>> result = new ArrayList<>();
		for (int i = 0; i < words.length; ++i) {
			String s = words[i];
			for (int k = reverse ? 1 : 0; k <= s.length(); k++) { // check
				// suffixes,
				// <=
				// because
				// we allow
				// empty
				// words
				Integer j = revMap.get(s.substring(k));
				if (j != null && j != i) { // reversed suffix is present in the
					// words list
					String prefix = s.substring(0, k); // check whether the
					// prefix is a
					// palindrome
					String revPrefix = revWords[i].substring(s.length() - k);
					if (prefix.equals(revPrefix)) {
						result.add(reverse ? Arrays.asList(i, j) : Arrays.asList(j, i));
					}
				}
			}
		}
		return result;
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
				item0.add(cur.size(), num[i]);
				newRes.add(item0);
				for (int k = cur.size() - 1; k >= 0 && num[i] != cur.get(k); k--) {
					ArrayList<Integer> item = new ArrayList<Integer>(cur);
					item.add(k, num[i]);
					newRes.add(item);
				}
			}
			res = newRes;
		}
		return res;
	}

	public void swap(int A[], int i, int j) {
		int tmp = A[i];
		A[i] = A[j];
		A[j] = tmp;
	}

	public void sortColors(int[] nums) {
		if (nums == null || nums.length == 0)
			return;

		int notred = 0;
		int notblue = nums.length - 1;

		while (notred < nums.length && nums[notred] == 0)
			notred++;

		while (notblue >= 0 && nums[notblue] == 2)
			notblue--;

		int i = notred;
		while (i <= notblue) {
			if (nums[i] == 0) {
				swap(nums, i, notred);
				notred++;
				i++;
			} else if (nums[i] == 2) {
				swap(nums, i, notblue);
				notblue--;
			} else {
				i++;
			}
		}
	}

	public List<List<Integer>> permute(int[] S) {
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		permute(new ArrayList<Integer>(), S, result);
		return result;
	}

	void permute(List<Integer> li, int[] S, List<List<Integer>> result) {
		if (S.length == 0) {
			result.add(new ArrayList<Integer>(li));
			return;
		}

		for (int i = 0; i < S.length; i++) {
			// List<Integer> tmp = new ArrayList<Integer>(li);
			int tmp = S[i];
			li.add(tmp);
			permute(li, remove(S, i), result);
			li.remove(new Integer(tmp));
		}
	}

	int[] remove(int[] S, int index) {
		int[] r = new int[S.length - 1];
		for (int i = 0; i < S.length; i++) {
			if (i < index) {
				r[i] = S[i];
			} else if (i > index) {
				r[i - 1] = S[i];
			}
		}
		return r;
	}

	public List<List<Integer>> permute2(int[] num) {
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
				for (int k = 0; k < cur.size() + 1; k++) {
					ArrayList<Integer> item = new ArrayList<Integer>(cur);
					item.add(k, num[i]);
					newRes.add(item);
				}
			}
			res = newRes;
		}
		return res;
	}

	public int countDigitOne2(int n) {
		int res = 0;
		for (long m = 1; m <= n; m *= 10) {
			long a = n / m, b = n % m;
			res += (a + 8) / 10 * m;
			if (a % 10 == 1)
				res += b + 1;
			System.out.println("res: " + res);
			System.out.println("b: " + b);
			System.out.println("a: " + a);
		}
		return res;
	}

	public int countDigitOne(int n) {
		int res = 0, a = 1, b = 1;
		while (n > 0) {
			res += (n + 8) / 10 * a;
			if (n % 10 == 1) {
				res += b;
			}
			b += n % 10 * a;
			a *= 10;
			System.out.println("res: " + res);
			System.out.println("b: " + b);
			System.out.println("a: " + a);
			n /= 10;
		}
		return res;
	}

	public int[] singleNumber(int[] nums) {
		int xor = 0;
		for (int i = 0; i < nums.length; i++) {
			xor ^= nums[i];
		}
		System.out.println(xor);
		int lastBit = xor - (xor & (xor - 1));
		System.out.println(lastBit);
		int group0 = 0, group1 = 0;
		for (int i = 0; i < nums.length; i++) {
			if ((lastBit & nums[i]) == 0) {
				group0 ^= nums[i];
			} else {
				group1 ^= nums[i];
			}
		}

		int[] res = new int[2];
		res[0] = group0;
		res[1] = group1;
		return res;
	}

	public boolean searchMatrix(int[][] matrix, int target) {
		if (matrix.length == 0 || matrix[0].length == 0) {
			return false;
		}

		int i = 0, j = matrix[0].length - 1;

		while (i < matrix.length && j >= 0) {
			int x = matrix[i][j];
			if (target == x)
				return true;
			else if (target < x)
				j--;
			else
				i++;
		}
		return false;
	}

	public int lengthOfLIS2(int[] nums) {
		if (nums.length == 0)
			return 0;
		List<Integer> ends = new ArrayList<>();
		ends.add(nums[0]);
		for (int a : nums) {
			if (a < ends.get(0)) {
				ends.set(0, a);
			} else if (a > ends.get(ends.size() - 1)) {
				ends.add(a);
			}
		}
		return ends.size();
	}

	public int lengthOfLIS(int[] nums) {
		int[] f = new int[nums.length];
		int max = 0;
		for (int i = 0; i < nums.length; i++) {
			f[i] = 1;
			for (int j = 0; j < i; j++) {
				if (nums[j] < nums[i]) {
					f[i] = f[i] > f[j] + 1 ? f[i] : f[j] + 1;
				}
			}
			if (f[i] > max) {
				max = f[i];
			}
		}
		return max;
	}

	public void gameOfLife(int[][] board) {
		int m = board.length, n = board[0].length;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				int lives = 0;

				if (i > 0) {
					lives += board[i - 1][j] == 1 || board[i - 1][j] == 2 ? 1 : 0;
				}

				if (j > 0) {
					lives += board[i][j - 1] == 1 || board[i][j - 1] == 2 ? 1 : 0;
				}

				if (i < m - 1) {
					lives += board[i + 1][j] == 1 || board[i + 1][j] == 2 ? 1 : 0;
				}

				if (j < n - 1) {
					lives += board[i][j + 1] == 1 || board[i][j + 1] == 2 ? 1 : 0;
				}

				if (i > 0 && j > 0) {
					lives += board[i - 1][j - 1] == 1 || board[i - 1][j - 1] == 2 ? 1 : 0;
				}

				if (i < m - 1 && j < n - 1) {
					lives += board[i + 1][j + 1] == 1 || board[i + 1][j + 1] == 2 ? 1 : 0;
				}

				if (i > 0 && j < n - 1) {
					lives += board[i - 1][j + 1] == 1 || board[i - 1][j + 1] == 2 ? 1 : 0;
				}

				if (i < m - 1 && j > 0) {
					lives += board[i + 1][j - 1] == 1 || board[i + 1][j - 1] == 2 ? 1 : 0;
				}

				if (board[i][j] == 0 && lives == 3) {
					board[i][j] = 3;
				} else if (board[i][j] == 1) {
					if (lives < 2 || lives > 3)
						board[i][j] = 2;
				}
			}
		}
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				board[i][j] = board[i][j] % 2;
			}
		}
	}

	public List<Integer> diffWaysToCompute(String input) {
		List<Integer> result = new ArrayList<Integer>();
		if (input == null || input.length() == 0) {
			return result;
		}
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != '+' && c != '-' && c != '*') {
				continue;
			}
			List<Integer> part1Result = diffWaysToCompute(input.substring(0, i));
			List<Integer> part2Result = diffWaysToCompute(input.substring(i + 1, input.length()));
			for (Integer m : part1Result) {
				for (Integer n : part2Result) {
					if (c == '+') {
						result.add(m + n);
					} else if (c == '-') {
						result.add(m - n);
					} else if (c == '*') {
						result.add(m * n);
					}
				}
			}
		}

		if (result.size() == 0) {
			result.add(Integer.parseInt(input));
		}
		return result;
	}

	class PeekingIterator implements Iterator<Integer> {

		private Iterator<Integer> iterator;
		private boolean flag = false;
		private Integer cache = null;

		public PeekingIterator(Iterator<Integer> iterator) {
			// initialize any member here.
			this.iterator = iterator;
		}

		// Returns the next element in the iteration without advancing the
		// iterator.
		public Integer peek() {
			if (!flag) {
				cache = iterator.next();
				// Throws NoSuchElementException - if the iteration has no more
				// elements
				// flag is still false
				flag = true;
			}
			return cache;
		}

		// hasNext() and next() should behave the same as in the Iterator
		// interface.
		// Override them if needed.
		@Override
		public Integer next() {
			if (!flag) {
				return iterator.next();
			} else {
				flag = false;
				Integer res = cache;
				cache = null;
				return res;
			}
		}

		@Override
		public boolean hasNext() {
			return flag || iterator.hasNext();
		}
	}

	public int rob(TreeNode root) {
		int[] result = findMax(root);
		return Math.max(result[0], result[1]);
	}

	// returns int[2] result.
	// result[0] -- max value robbing current root; result[1] -- max value
	// without robbing current root.
	private int[] findMax(TreeNode root) {
		if (root == null) {
			return new int[] { 0, 0 };
		}
		int[] left = findMax(root.left);
		int[] right = findMax(root.right);
		int result0 = root.val + left[1] + right[1]; // rob current root
		int result1 = Math.max(left[0], left[1]) + Math.max(right[0], right[1]); // not
		// rob
		// current
		// root
		return new int[] { result0, result1 };
	}

	public int hIndex(int[] citations) {
		Arrays.sort(citations);
		int res = 0;
		for (int i = citations.length - 1; i >= 0; i--) {
			int k = citations.length - i;
			if (k > citations[i]) {
				return k - 1;
			}
		}
		res = citations.length;
		return res;
	}


	public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || root == p || root == q) {
			return root;
		}
		TreeNode left = lowestCommonAncestor(root.left, p, q);
		TreeNode right = lowestCommonAncestor(root.right, p, q);
		if (left != null && right != null) {
			return root;
		}
		return left == null ? right : left;
	}

	public int nthUglyNumber(int n) {

		int[] uglyNumbers = new int[n];
		uglyNumbers[0] = 1;

		int idx2 = 0;
		int idx3 = 0;
		int idx5 = 0;

		int counter = 1;
		while (counter < n) {
			int min = minOf(uglyNumbers[idx2] * 2, uglyNumbers[idx3] * 3, uglyNumbers[idx5] * 5);
			if (min == uglyNumbers[idx2] * 2) {
				idx2++;
			}
			if (min == uglyNumbers[idx3] * 3) {
				idx3++;
			}
			if (min == uglyNumbers[idx5] * 5) {
				idx5++;
			}
			uglyNumbers[counter] = min;
			counter++;
		}
		return uglyNumbers[n - 1];
	}

	private int minOf(int a, int b, int c) {
		int temp = a < b ? a : b;
		return temp < c ? temp : c;
	}

	public int minSubArrayLen1(int s, int[] nums) {
		if (nums == null || nums.length == 0) {
			return 0;
		}

		int result = nums.length;
		int i = 0;
		int sum = nums[0];

		for (int j = 0; j < nums.length;) {
			if (i == j) {
				if (nums[i] >= s) {
					return 1;
				} else {
					j++;
					if (j < nums.length) {
						sum += nums[j];
					} else {
						return result;
					}
				}
			} else {
				// if sum is large enough, move left cursor
				if (sum >= s) {
					result = Math.min(j - i + 1, result);
					sum -= nums[i];
					i++;
					// if sum is not large enough, move right cursor
				} else {
					j++;
					if (j < nums.length) {
						sum = sum + nums[j];
					} else {
						if (i == 0) {
							return 0;
						} else {
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	public List<Integer> majorityElement(int[] nums) {
		List<Integer> res = new ArrayList<Integer>();
		int m = 0, n = 0, cm = 0, cn = 0;
		for (int a : nums) {
			if (a == m) {
				cm++;
			} else if (a == n) {
				cn++;
			} else if (cm == 0) {
				m = a;
				cm = 1;
			} else if (cn == 0) {
				n = a;
				cn = 1;
			} else {
				cm--;
				cn--;
			}
		}

		cm = cn = 0;
		for (int a : nums) {
			if (a == m) {
				cm++;
			} else if (a == n) {
				cn++;
			}
		}
		if (cm > nums.length / 3) {
			res.add(m);
		}
		if (cn > nums.length / 3) {
			res.add(n);
		}
		return res;
	}

	public int calculate2(String s) {
		ArrayDeque<Integer> iq = new ArrayDeque<Integer>();
		ArrayDeque<Character> cq = new ArrayDeque<Character>();

		char last = ' ';
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			if (ch == ' ') {
				continue;
			}
			if (ch == '-' || ch == '+') {
				cq.push(ch);
			} else if (ch == '*' || ch == '/') {
				last = ch;
			} else {
				int num = 0;
				// add bound check
				while (i < s.length() && Character.isDigit(s.charAt(i))) {
					num = num * 10 + (s.charAt(i) - '0');
					i++;
				}
				i--;
				if (last == '*') {
					num = iq.pop() * num;
					last = ' ';
				} else if (last == '/') {
					num = iq.pop() / num;
					last = ' ';
				}
				iq.push(num);
			}
		}

		int res = 0;
		while (!cq.isEmpty()) {
			char c = cq.pop();
			if (c == '+') {
				res += iq.pop();
			} else {
				res -= iq.pop();
			}
		}
		if (!iq.isEmpty()) {
			res += iq.pop();
		}
		return res;
	}

	public int maximalSquare(char[][] matrix) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return 0;
		}

		int m = matrix.length;
		int n = matrix[0].length;
		int[][] t = new int[m][n];

		for (int i = 0; i < m; i++) {
			t[i][0] = matrix[i][0] - '0';
		}
		for (int j = 0; j < n; j++) {
			t[0][j] = matrix[0][j] - '0';
		}
		for (int i = 1; i < m; i++) {
			for (int j = 1; j < n; j++) {
				if (matrix[i][j] == '1') {
					int min = Math.min(t[i - 1][j], t[i - 1][j - 1]);
					min = Math.min(min, t[i][j - 1]);
					t[i][j] = min + 1;
				} else {
					t[i][j] = 0;
				}
			}
		}

		int max = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (t[i][j] > max) {
					max = t[i][j];
				}
			}
		}
		return max * max;
	}

	public int calculate(String s) {
		s = "(" + s + ")";
		Stack<Character> cstack = new Stack<Character>();
		Stack<Integer> istack = new Stack<Integer>();

		boolean isNum = false;
		int sum = 0;

		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ' ')
				continue;
			if (Character.isDigit(s.charAt(i))) {
				sum = sum * 10 + s.charAt(i) - '0';
				isNum = true;
				continue;
			} else if (isNum) {
				istack.push(sum);
				sum = 0;
				isNum = false;
			}

			if (s.charAt(i) == '+' || s.charAt(i) == '-' || s.charAt(i) == '(') {
				cstack.push(s.charAt(i));
			} else if (s.charAt(i) == ')') {
				int temp = 0;
				while (cstack.peek() != '(') {
					int a = istack.pop();
					int c = cstack.pop();
					if (c == '+') {
						temp += a;
					} else if (c == '-') {
						temp -= a;
					}
				}
				temp += istack.pop();
				istack.push(temp);
				cstack.pop();
			}
		}
		return istack.pop();
	}

	public boolean wordPattern(String pattern, String str) {
		String[] strs = str.split(" ");
		if (pattern.length() != strs.length) {
			return false;
		}
		Map<Character, String> map = new HashMap<Character, String>();
		List<String> list = new ArrayList<String>(map.values());
		Collections.sort(list, Collections.reverseOrder());
		System.out.println(list);
		for (int i = 0; i < pattern.length(); i++) {
			if (!map.containsKey(pattern.charAt(i))) {
				if (map.containsValue(strs[i])) {
					return false;
				}
				map.put(pattern.charAt(i), strs[i]);
			} else {
				if (!strs[i].equals(map.get(pattern.charAt(i)))) {
					return false;
				}
			}
		}
		return true;
	}

	public String getHint(String secret, String guess) {
		int bulls = 0;
		int cows = 0;
		int[] numbers = new int[10];
		for (int i = 0; i < secret.length(); i++) {
			int s = secret.charAt(i) - '0';
			int g = guess.charAt(i) - '0';
			if (s == g)
				bulls++;
			else {
				if (numbers[s] < 0)
					cows++;
				if (numbers[g] > 0)
					cows++;
				numbers[s]++;
				numbers[g]--;
			}
		}
		return bulls + "A" + cows + "B";
	}

	public String shortestPalindrome(String s) {
		int j = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == s.charAt(j)) {
				j++;
			}
		}

		if (j == s.length()) {
			return s;
		}

		String suffix = s.substring(j);
		String prefix = new StringBuilder(suffix).reverse().toString();
		String mid = shortestPalindrome(s.substring(0, j));
		String ans = prefix + mid + suffix;
		return ans;
	}

	public int[] maxNumber(int[] nums1, int[] nums2, int k) {
		int[] ans = new int[k];
		for (int i = Math.max(k - nums2.length, 0); i <= Math.min(nums1.length, k); i++) {
			int[] res1 = fn(nums1, i);
			int[] res2 = fn(nums2, k - i);
			int[] res = new int[k];
			int pos1 = 0, pos2 = 0, tpos = 0;

			while (pos1 < res1.length || pos2 < res2.length) {
				res[tpos++] = greater(res1, pos1, res2, pos2) ? res1[pos1++] : res2[pos2++];
			}

			if (!greater(ans, 0, res, 0)) {
				ans = res;
			}
		}

		return ans;
	}

	// nums1 = [9, 8], num2 = [9, 8, 3] -> false, ans = [9, 9, 8, 8, 3]
	private boolean greater(int[] nums1, int start1, int[] nums2, int start2) {
		for (; start1 < nums1.length && start2 < nums2.length; start1++, start2++) {
			if (nums1[start1] > nums2[start2])
				return true;
			if (nums1[start1] < nums2[start2])
				return false;
		}
		return start1 != nums1.length;
	}

	// find largest number from selected digit.
	// nums2 = [9, 1, 2, 5, 8, 3], k = 3 -> [983]
	private int[] fn(int[] nums, int k) {
		int[] res = new int[k];
		int top = 0;
		int remove = nums.length - k;
		for (int i = 0; i < nums.length; i++) {
			while (top > 0 && remove > 0 && res[top - 1] < nums[i]) {
				top--;
				remove--;
			}
			if (top == k) {
				remove--;
			}
			if (top < k)
				res[top++] = nums[i];
		}
		return res;
	}

	public int minCut(String s) {
		int n = s.length();
		boolean B[][] = new boolean[n][n];
		int[] A = new int[n + 1];

		// init min cut: A[0] = n - 1 A[n-1] = 0;
		for (int i = 0; i < n; i++) {
			A[i] = n - i - 1;
		}

		for (int i = n - 1; i >= 0; i--) {
			for (int j = i; j < n; j++) {
				if (s.charAt(i) == s.charAt(j) && (j - i < 2 || B[i + 1][j - 1])) {
					B[i][j] = true;
					A[i] = Math.min(A[i], A[j + 1]);
				}
			}
		}
		return A[0];
	}

	public int countRangeSum(int[] nums, int lower, int upper) {
		int n = nums.length;
		long[] sums = new long[n + 1];
		for (int i = 0; i < n; i++) {
			sums[i + 1] = sums[i] + nums[i];
		}
		return countWhileMergeSort(sums, 0, n + 1, lower, upper);
	}

	private int countWhileMergeSort(long[] sums, int start, int end, int lower, int upper) {
		if (end - start <= 1) {
			return 0;
		}
		int mid = start + (end - start) / 2;
		int count = countWhileMergeSort(sums, start, mid, lower, upper)
				+ countWhileMergeSort(sums, mid, end, lower, upper);
		int j = mid, k = mid, t = mid, r = 0;
		long[] cache = new long[end - start];
		for (int i = start; i < mid; ++i, ++r) {
			while (k < end && sums[k] - sums[i] < lower)
				k++;
			while (j < end && sums[j] - sums[i] <= upper)
				j++;
			while (t < end && sums[t] < sums[i])
				cache[r++] = sums[t++]; // start merging

			cache[r] = sums[i];
			count += j - k;
		}
		System.out.println(Arrays.toString(cache));
		System.arraycopy(cache, 0, sums, start, r);
		// System.out.println(Arrays.toString(sums));
		return count;
	}

	int[] dx = { 1, -1, 0, 0 };
	int[] dy = { 0, 0, 1, -1 };

	public int longestIncreasingPath(int[][] matrix) {
		if (matrix.length == 0)
			return 0;
		int m = matrix.length, n = matrix[0].length;
		int[][] dis = new int[m][n];
		int ans = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				ans = Math.max(ans, dfs(i, j, m, n, matrix, dis));
			}
		}
		return ans;
	}

	int dfs(int x, int y, int m, int n, int[][] matrix, int[][] dis) {
		if (dis[x][y] != 0) {
			return dis[x][y];
		}
		for (int i = 0; i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			if (nx >= 0 && ny >= 0 && nx < m && ny < n && matrix[nx][ny] > matrix[x][y]) {
				dis[x][y] = Math.max(dis[x][y], dfs(nx, ny, m, n, matrix, dis));
			}
		}
		return ++dis[x][y];
	}

	public List<String> removeInvalidParentheses(String s) {
		Queue<String> candidates = new LinkedList<String>();
		List<String> results = new ArrayList<String>();
		if (s.equals("")) {
			results.add("");
			return results;
		}

		// The overall idea is just to remove one character each time, if after
		// removing, the number of mismatch is
		// going down, then put this new expression into a new queue.
		// If after removing, the new expression is a valid expression,
		// then we find the result with the minimum removing steps. After
		// processing the remainder elements in the
		// current queue, we can return the results.
		candidates.add(s);
		while (!candidates.isEmpty()) {
			Queue<String> candidates2 = new LinkedList<String>();
			// Using a set to remove the duplicates
			Set<String> candidateSet = new HashSet<>();
			boolean valid = false;
			while (!candidates.isEmpty()) {
				String cur = candidates.remove();
				int count = findNumMisMatch(cur);
				if (count == 0) {
					results.add(cur);
					valid = true;
				} else {
					for (int i = 0; i < cur.length(); i++) {
						if (i != 0 && cur.charAt(i) == cur.charAt(i - 1))
							continue;
						StringBuilder newSB = new StringBuilder(cur.substring(0, i));
						if (i != cur.length() - 1)
							newSB.append(cur.substring(i + 1));
						String newS = newSB.toString();
						if (!candidateSet.contains(newS) && findNumMisMatch(newS) < count) {
							candidateSet.add(newS);
							candidates2.add(newS);
						}
					}
				}
			}
			if (valid == true)
				return results;
			candidates.addAll(candidates2);
		}
		return results;
	}

	class Pair implements Comparable<Pair> {
		int i, j, sum;

		public int compareTo(Pair p) {
			return sum - p.sum;
		}
	}

	public List<int[]> kSmallestPairs(int[] nums1, int[] nums2, int k) {
		return kSmallestPairs0(nums1, nums2, k);
	}

	public List<int[]> kSmallestPairs0(final int[] nums1, final int[] nums2, int k) {
		List<int[]> res = new ArrayList<>();
		if (nums1 == null || nums2 == null || nums1.length == 0 || nums2.length == 0 || k == 0) {
			return res;
		}
		boolean visit[][] = new boolean[nums1.length][nums2.length];
		Queue<int[]> heap = new PriorityQueue<int[]>(new Comparator<int[]>() {
			public int compare(int[] i, int[] j) {
				return (nums1[i[0]] + nums2[i[1]] - (nums1[j[0]] + nums2[j[1]]));
			}
		});

		heap.add(new int[] { 0, 0 });
		visit[0][0] = true;

		while (!heap.isEmpty() && res.size() < k) {
			int d[] = heap.poll();
			res.add(new int[] { nums1[d[0]], nums2[d[1]] });

			if (d[1] + 1 < nums2.length && visit[d[0]][d[1] + 1] == false) {
				heap.add(new int[] { d[0], d[1] + 1 });
				visit[d[0]][d[1] + 1] = true;
			}
			if (d[0] + 1 < nums1.length && visit[d[0] + 1][d[1]] == false) {
				heap.add(new int[] { d[0] + 1, d[1] });
				visit[d[0] + 1][d[1]] = true;
			}
		}
		return res;
	}

	// This function is used to evaluate whether an expression is valid. If it
	// returns 0, the expression is valid.
	// It can also give the number of elements need to be removed to make a
	// valid expression.
	public int findNumMisMatch(String s) {
		int numLeft = 0, numRight = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '(') {
				numLeft++;
			} else if (s.charAt(i) == ')') {
				if (numLeft == 0) {
					numRight++;
				} else {
					numLeft--;
				}
			}
		}
		return numLeft + numRight;
	}

	public int findDuplicate(int[] nums) {

		int left = 0, right = nums.length - 1;
		while (left < right) {
			int mid = left + (right - left) / 2;
			int cnt = 0;
			for (int a : nums) {
				if (a <= mid + 1) {
					cnt++;
				}
			}
			if (cnt <= mid + 1) {
				left = mid + 1;
			} else {
				right = mid;
			}
		}
		return right + 1;
	}

	public int wiggleMaxLength(int[] nums) {
		if (nums.length <= 1) {
			return nums.length;
		}
		int count = 0;
		// (nums[i] > nums[i-1] && nums[i] < nums[i+1]) || (nums[i] < nums[i-1]
		// && nums[i] > nums[i+1]) ||
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] == nums[i - 1]) {
				count++;
			}
		}
		for (int i = 1; i < nums.length - 1; i++) {
			if (nums[i] != nums[i - 1]) {
				int j = i + 1;
				while (j < nums.length && nums[j] == nums[i]) {
					j++;
				}
				if (j == nums.length) {
					break;
				}
				if (nums[i] < nums[i - 1] && nums[i] > nums[j] || nums[i] > nums[i - 1] && nums[i] < nums[j]) {
					count++;
				}
			}
		}
		return nums.length - count;
	}

	public int solution2(int[] A) {
		// write your code in Java SE 8
		int res = 0;
		if (A.length < 2) {
			return 0;
		}
		long[] S = new long[A.length + 1];
		long sum = 0;
		for (int i = 0; i < A.length; i++) {
			sum += A[i];
			S[i + 1] = sum;
		}
		for (int i = 1; i < A.length; i++) {
			if (S[i - 1] == S[A.length] - S[i]) {
				return i - 1;
			}
		}
		return res;
	}

	int solution3Unifocus(int n) {
		int[] d = new int[31];
		int l = 0;
		int p;
		while (n > 0) {
			d[l] = n % 2;
			n /= 2;
			l++;
		}
		for (p = 1; p < 1 + l && p <= l / 2; ++p) {
			int i;
			boolean ok = true;
			for (i = 0; i < l - p; ++i) {
				if (d[i] != d[i + p]) {
					ok = false;
					break;
				}
			}
			if (ok) {
				return p;
			}
		}
		return -1;
	}

	public List<String> readBinaryWatch3(int num) {
		List<String> res = new ArrayList<>();
		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		int value = 1;
		for (int i = 0; i < 4; i++) {
			list1.add(value);
			value *= 2;
		}
		value = 1;
		for (int i = 0; i < 6; i++) {
			list2.add(value);
			value *= 2;
		}
		return res;
	}

	// a bad solution
	List<Integer> getCombination(List<Integer> list, int k) {
		List<Integer> res = new ArrayList<>();
		if (k == 0) {
			res.add(0);
			return res;
		}
		if (list.size() == k) {
			int sum = 0;
			for (int e : list) {
				sum += e;
			}
			res.add(sum);
			return res;
		}
		int value = list.remove(0);
		List<Integer> copy = new ArrayList<>(list);
		List<Integer> select = getCombination(list, k - 1);
		List<Integer> unselect = getCombination(copy, k);
		for (int e : select) {
			res.add(value + e);
		}
		for (int e : unselect) {
			res.add(e);
		}
		return res;
	}

	public List<String> readBinaryWatch(int num) {
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 60; j++) {
				int total = countDigits(i) + countDigits(j);
				if (total == num) {
					String s = "";
					s += i + ":";

					if (j < 10) {
						s += "0" + j;
					} else {
						s += j;
					}
					result.add(s);
				}
			}
		}
		return result;
	}

	public int countDigits(int num) {
		int result = 0;
		while (num > 0) {
			if ((num & 1) == 1) {
				result++;
			}
			num >>= 1;
		}
		return result;
	}

	public int[] searchRange(int[] nums, int target) {
		int[] res = { -1, -1 };
		if (nums == null || nums.length == 0 || target < nums[0] || target > nums[nums.length - 1]) {
			return res;
		}
		// {2, 4, 7}
		// {4, 4, 4}
		int s = 0, e = nums.length - 1;
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
		if (left <= right) {
			res[0] = left;
			res[1] = right;
		}
		return res;
	}

	// Search in Rotated Sorted Array
	public int search(int[] nums, int target) {
		int start = 0, end = nums.length - 1;
		while (start <= end) {
			int mid = start + (end - start) / 2;
			if (target == nums[mid]) {
				return mid;
			}
			// when start] == mid, start = mid + 1
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
		return -1;
	}

	public boolean exist(char[][] board, String word) {
		if (board == null || board.length == 0) {
			return false;
		}
		if (word.length() == 0) {
			return true;
		}
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (find(board, word, i, j, 0)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean find(char[][] board, String word, int i, int j, int k) {
		if (k == word.length()) {
			return true;
		}
		if (i < 0 || i >= board.length || j < 0 || j >= board[0].length || board[i][j] != word.charAt(k)) {
			return false;
		}
		board[i][j] = '#';
		boolean res = find(board, word, i, j - 1, k + 1) || find(board, word, i, j + 1, k + 1)
				|| find(board, word, i - 1, j, k + 1) || find(board, word, i + 1, j, k + 1);
		board[i][j] = word.charAt(k);
		return res;
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

	// 403
	public boolean canCross(int[] stones) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < stones.length; i++) {
			map.put(stones[i], i);
		}
		return fn(stones, 0, 0, map);
	}

	private boolean fn(int[] stones, int i, int k, Map<Integer, Integer> map) {
		if (i == stones.length - 1) {
			return true;
		}
		for (int jump = k - 1; jump <= k + 1; jump++) {
			if (jump > 0 && map.containsKey(stones[i] + jump)) {
				if (fn(stones, map.get(stones[i] + jump), k, map)) {
					return true;
				}
			}
		}
		return false;
	}

	public int[][] reconstructQueue(int[][] people) {
		List<int[]> list = new LinkedList<int[]>();
		for (int[] e : people) {
			list.add(e);
		}
		Collections.sort(list, new Comparator<int[]>() {
			public int compare(int[] a, int[] b) {
				if (a[0] == b[0]) {
					return a[1] - b[1];
				}
				return b[0] - a[0];
			}
		});
		System.out.println(list.get(0)[0] + "-" + list.get(0)[1]);
		System.out.println(list.get(1)[0] + "-" + list.get(1)[1]);
		System.out.println(list.get(2)[0] + "-" + list.get(2)[1]);
		System.out.println(list.get(3)[0] + "-" + list.get(3)[1]);
		return people;
	}

	@SuppressWarnings("unused")
	@Test
	public void test() {
		String.valueOf(7);
		int[][] pepple = { { 7, 0 }, { 4, 4 }, { 7, 1 }, { 5, 0 }, { 6, 1 }, { 5, 2 } };
		reconstructQueue(pepple);

		int[] A = { -1, 3, -4, 5, 1, -6, 2, 1 };
		int[] A2 = { 1, 1, 1, 5 };
		System.out.println(removeKdigits("1432219", 3));

		List<Integer> list1 = new ArrayList<Integer>();
		List<Integer> list2 = new ArrayList<Integer>();
		int value = 1;
		for (int i = 0; i < 4; i++) {
			list1.add(value);
			value *= 2;
		}
		value = 1;
		for (int i = 0; i < 6; i++) {
			list2.add(value);
			value *= 2;
		}
		System.out.println(getCombination(list1, 2));

		int num = 5;
		num = num >> 1;
	}

	/*
	 * //@Test public void test3() { int[][] array = { { 30, 50 }, { 12, 2 }, { 3, 4
	 * }, { 12, 15 } }; // maxEnvelopes(array); int a; try { a = array[4][5]; }
	 * catch (ArrayIndexOutOfBoundsException e) { try { a = array[1][2]; } catch
	 * (ArrayIndexOutOfBoundsException e2) { a = array[1][1]; } } }
	 * 
	 * void fn(int[][] array, int a) { try { a = array[4][5]; } catch
	 * (ArrayIndexOutOfBoundsException e) { fn(array, a); } }
	 * 
	 * class MyMap<K, V> extends HashMap<K, V> {
	 * 
	 * @Override public V get(Object key) { return super.get(key); } }
	 * 
	 * // @Test public void testFile() throws IOException {
	 * 
	 * // File File folder = new File("C:/Users/Weiwei/Desktop/house2"); // File
	 * folder = new File("/home/you/Desktop"); File[] listOfFiles =
	 * folder.listFiles();
	 * System.out.println("house.of.cards.2013.s02e03".length()); for (File file :
	 * listOfFiles) { if (file.isFile()) { System.out.println(file.getName()); //
	 * file.renameTo(new File("C:/Users/Weiwei Liu/Downloads/chap2/" // +
	 * file.getName().substring(0, 12) // + ".java")); file.renameTo(new
	 * File("C:/Users/Weiwei/Desktop/h2/" + file.getName().substring(0, 26) +
	 * ".720p.webrip.x264-2hd.srt")); } } }
	 */
}
