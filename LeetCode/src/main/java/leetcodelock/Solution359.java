package leetcodelock;

import java.util.HashMap;
import java.util.Map;

public class Solution359 {

}

// 359. Logger Rate Limiter
// Design a logger system that receive stream of messages along with its timestamps, each message should be printed if and only if it is not printed
// in the last 10 seconds.
// Given a message and a timestamp (in seconds granularity), return true if the message should be printed in the given timestamp, otherwise returns
// false.
class Logger {

	Map<String, Integer> map;

	/** Initialize your data structure here. */
	public Logger() {
		map = new HashMap<>();
	}

	/**
	 * Returns true if the message should be printed in the given timestamp, otherwise returns false. If this method returns false, the message will
	 * not be printed. The timestamp is in seconds granularity.
	 */
	public boolean shouldPrintMessage(int timestamp, String message) {
		// map only store printed timestamp
		if (!map.containsKey(message) || timestamp - map.get(message) >= 10) {
			map.put(message, timestamp);
			return true;
		}
		return false;
	}
}

// 360. Sort Transformed Array
// Given a sorted array of integers nums and integer values a, b and c. Apply a quadratic function of the form f(x) = ax2 + bx + c to each element x
// in the array.
// The returned array must be in sorted order.
// Expected time complexity: O(n)
// Example 1:
// Input: nums = [-4,-2,2,4], a = 1, b = 3, c = 5
// Output: [3,9,15,33]
class Solution360 {
	public int[] sortTransformedArray(int[] nums, int a, int b, int c) {
		int[] res = new int[nums.length];
		int start = 0;
		int end = nums.length - 1;
		int i = a >= 0 ? nums.length - 1 : 0;
		while (start <= end) {
			int startNum = getNum(nums[start], a, b, c);
			int endNum = getNum(nums[end], a, b, c);
			if (a >= 0) {
				if (startNum >= endNum) {
					res[i--] = startNum;
					start++;
				} else {
					res[i--] = endNum;
					end--;
				}
			} else {
				if (startNum <= endNum) {
					res[i++] = startNum;
					start++;
				} else {
					res[i++] = endNum;
					end--;
				}
			}
		}
		return res;
	}

	private int getNum(int x, int a, int b, int c) {
		return a * x * x + b * x + c;
	}
}

// 361. Bomb Enemy
class Solution361 {
	public int maxKilledEnemies(char[][] grid) {
		if (grid.length == 0 || grid[0].length == 0)
			return 0;
		int m = grid.length, n = grid[0].length;
		int[][] count = new int[m][n];
		int res = 0;
		for (int i = 0; i < m; i++) {
			int tmp = 0;
			for (int j = 0; j < n; j++) {
				if (grid[i][j] == 'E')
					tmp++;
				if (grid[i][j] == 'W')
					tmp = 0;
				if (grid[i][j] == '0') {
					count[i][j] += tmp;
					res = Math.max(count[i][j], res);
				}
			}
			tmp = 0;
			for (int j = n - 1; j >= 0; j--) {
				if (grid[i][j] == 'E')
					tmp++;
				if (grid[i][j] == 'W')
					tmp = 0;
				if (grid[i][j] == '0') {
					count[i][j] += tmp;
					res = Math.max(count[i][j], res);
				}
			}
		}
		for (int j = 0; j < n; j++) {
			int tmp = 0;
			for (int i = 0; i < m; i++) {
				if (grid[i][j] == 'E')
					tmp++;
				if (grid[i][j] == 'W')
					tmp = 0;
				if (grid[i][j] == '0') {
					count[i][j] += tmp;
					res = Math.max(count[i][j], res);
				}
			}
			tmp = 0;
			for (int i = m - 1; i >= 0; i--) {
				if (grid[i][j] == 'E')
					tmp++;
				if (grid[i][j] == 'W')
					tmp = 0;
				if (grid[i][j] == '0') {
					count[i][j] += tmp;
					res = Math.max(count[i][j], res);
				}
			}
		}
		return res;
	}
}