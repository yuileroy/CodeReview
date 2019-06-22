package leetcodelock;

import java.util.Iterator;
import java.util.List;

// 276. Paint Fence
// There is a fence with n posts, each post can be painted with one of the k colors.
// You have to paint all the posts such that no more than two adjacent fence posts have the same color.
// Return the total number of ways you can paint the fence.
public class Solution276 {
	public int numWays(int n, int k) {
		if (n == 0) {
			return 0;
		}
		if (n == 1) {
			return k;
		}
		int diff = k * (k - 1), same = k;
		// (diff + same) is total for each fence
		for (int i = 3; i <= n; i++) {
			int tmp = diff;
			diff = (diff + same) * (k - 1);
			same = tmp;
		}

		return diff + same;
	}

	// 277. Find the Celebrity
	// The definition of a celebrity is that all the other n - 1 people know him/her but he/she does not know any of them.
	public int findCelebrity(int n) {
		int res = 0;
		for (int i = 1; i < n; i++) {
			if (knows(res, i)) {
				res = i;
			}
		}
		for (int i = 0; i < n; i++) {
			if (i != res && (knows(res, i) || !knows(i, res))) {
				return -1;
			}
		}
		return res;
	}

	// mock
	boolean knows(int a, int b) {
		return true;
	}

	// 280. Wiggle Sort
	// Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <= nums[3]....
	// Input: nums = [3,5,2,1,6,4]
	// Output: One possible answer is [3,5,1,6,2,4]
	public void wiggleSort(int[] nums) {
		for (int i = 1; i < nums.length; i++) {
			if (i % 2 != 0 && nums[i] < nums[i - 1])
				swap(nums, i);
			if (i % 2 == 0 && nums[i] > nums[i - 1])
				swap(nums, i);
		}
	}

	private void swap(int[] nums, int i) {
		int tmp = nums[i];
		nums[i] = nums[i - 1];
		nums[i - 1] = tmp;
	}

	// 281. Zigzag Iterator
	class ZigzagIterator {
		boolean first;
		Iterator<Integer> it1, it2;

		public ZigzagIterator(List<Integer> v1, List<Integer> v2) {
			first = true;
			it1 = v1.iterator();
			it2 = v2.iterator();
		}

		public int next() {
			if (first && it1.hasNext()) {
				first = false;
				return it1.next();
			} else if (!first && it2.hasNext()) {
				first = true;
				return it2.next();
			} else {
				return it1.hasNext() ? it1.next() : it2.next();
			}

		}

		public boolean hasNext() {
			return it1.hasNext() || it2.hasNext();
		}
	}
}
