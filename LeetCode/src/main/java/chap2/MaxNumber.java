package chap2;

import org.junit.Test;

public class MaxNumber {

	public int calculateMinimumHP(int[][] dungeon) {
		int m = dungeon.length;
		int n = dungeon[0].length;
		
		int[][] health = new int[m][n];
		health[m - 1][n - 1] = Math.max(1 - dungeon[m - 1][n - 1], 1);
		
		for (int i = m - 2; i >= 0; i--) {
			health[i][n - 1] = Math.max(health[i + 1][n - 1] - dungeon[i][n - 1], 1);
		}

		for (int j = n - 2; j >= 0; j--) {
			health[m - 1][j] = Math.max(health[m - 1][j + 1] - dungeon[m - 1][j], 1);
		}

		for (int i = m - 2; i >= 0; i--) {
			for (int j = n - 2; j >= 0; j--) {
				int down = Math.max(health[i + 1][j] - dungeon[i][j], 1);
				int right = Math.max(health[i][j + 1] - dungeon[i][j], 1);
				health[i][j] = Math.min(right, down);
			}
		}
		return health[0][0];
	}

	@Test
	public void test() {
		// int[] nums1 = { 7, 6, 1, 9, 3, 2, 3, 1, 1 };
		// int[] nums2 = { 4, 0, 9, 9, 0, 5, 5, 4, 7};
		System.out.println(0x7ffffffe);
	}
}
