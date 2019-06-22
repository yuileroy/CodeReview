package leetcodelock;

import java.util.Arrays;

public class Solution256 {

	// 256. Paint House
	// Input: [[17,2,17],[16,16,5],[14,3,19]]
	// Output: 10
	public int minCost(int[][] costs) {
		int a = 0, b = 0, c = 0, a2, b2, c2;
		for (int[] e : costs) {
			a2 = e[0] + Math.min(b, c);
			b2 = e[1] + Math.min(a, c);
			c2 = e[2] + Math.min(a, b);
			a = a2;
			b = b2;
			c = c2;
		}

		return Math.min(Math.min(a, b), c);
	}

	// 265. Paint House II
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

	// 259. 3Sum Smaller
	// Given an array of n integers nums and a target, find the number of index triplets i, j, k with 0 <= i < j < k < n that satisfy the condition
	// nums[i] + nums[j] + nums[k] < target.
	public int threeSumSmaller(int[] nums, int target) {
		Arrays.sort(nums);
		int res = 0;
		for (int i = 0; i < nums.length - 2; i++) {
			int s = i + 1, e = nums.length - 1;
			int t2 = target - nums[i];
			while (s < e) {
				while (s < e && nums[s] + nums[e] >= t2) {
					e--;
				}
				res += e - s;
				s++;
			}
		}
		return res;
	}

	// 261. Graph Valid Tree
	// Given n nodes labeled from 0 to n-1 and a list of undirected edges (each edge is a pair of nodes), write a function to check whether these
	// edges make up a valid tree.
	// Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
	// Output: true

	public boolean validTree(int n, int[][] edges) {
		if (n != edges.length + 1) {
			return false;
		}
		int[] A = new int[n];
		for (int i = 0; i < n; i++) {
			A[i] = i;
		}
		for (int[] e : edges) {
			if (find(A, e[0]) == find(A, e[1])) {
				return false;
			}
			union(A, e[0], e[1]);
		}
		return true;
	}

	private int find(int[] A, int i) {
		if (A[i] == i) {
			return i;
		}
		// return find(A, A[i]);
		// better update A[i] for shortcut
		A[i] = find(A, A[i]);
		return A[i];
	}

	private void union(int[] A, int i, int j) {
		int a = find(A, i);
		int b = find(A, j);
		A[b] = a;
	}

}
