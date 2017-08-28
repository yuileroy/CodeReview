package oca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Solution {
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
		if (idx2 > 0) {
			cur.remove(idx2);
		} else {
			return false;
		}
		int idx3 = Collections.binarySearch(cur, first + 2);
		if (idx3 > 0) {
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
			if (idxk > 0) {
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
}

class Solution0 {
	List<Integer> res = null;
	int min = Integer.MAX_VALUE;

	public List<Integer> cheapestJump(int[] A, int B) {
		List<Integer> pre = new ArrayList<>();
		pre.add(0);
		fn(A, B, pre, 0, 0);

		for (int i = 0; i < res.size(); i++) {
			res.set(i, res.get(i) + 1);
		}
		return res;
	}

	public List<Integer> findClosestElements(List<Integer> arr, int k, int x) {
		List<Integer> res = new ArrayList<>();
		int idx = Collections.binarySearch(arr, x);
		if (idx < 0) {
			idx = -idx;
		}
		int left = idx - 1, right = idx;
		while (left >= 0 || right < arr.size()) {
			if (res.size() == k) {
				break;
			}
			if (left >= 0 && right < arr.size()) {
				if (x - arr.get(left) <= arr.get(right) - x) {
					res.add(arr.get(left));
					left--;
				} else {
					res.add(arr.get(right));
					right++;
				}
			} else if (left < 0) {
				res.add(arr.get(right));
				right++;
			} else {
				res.add(arr.get(left));
				left--;
			}
		}
		return res;
	}

	private void fn(int[] A, int B, List<Integer> pre, int preSum, int start) {
		if (start == A.length - 1) {
			if (preSum < min) {
				res = pre;
				min = preSum;
			}
			return;
		}
		for (int i = 1; i <= B && start + i < A.length; i++) {
			if (A[start + i] != -1) {
				List<Integer> tmp = new ArrayList<>(pre);
				tmp.add(start + i);
				fn(A, B, tmp, preSum + A[start + i], start + i);
			}
		}
	}
}