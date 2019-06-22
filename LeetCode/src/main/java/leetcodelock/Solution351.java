package leetcodelock;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// 351. Android Unlock Patterns
// Given an Android 3x3 key lock screen and two integers m and n, where 1 ≤ m ≤ n ≤ 9, 
// count the total number of unlock patterns of the Android
// lock screen, which consist of minimum of m keys and maximum n keys.
public class Solution351 {
	int res = 0;

	public int numberOfPatterns(int m, int n) {
		// Skip array represents number to skip between two pairs
		int skip[][] = new int[10][10];
		skip[1][3] = skip[3][1] = 2;
		skip[1][7] = skip[7][1] = 4;
		skip[3][9] = skip[9][3] = 6;
		skip[7][9] = skip[9][7] = 8;
		skip[1][9] = skip[9][1] = skip[2][8] = skip[8][2] = skip[3][7] = skip[7][3] = skip[4][6] = skip[6][4] = 5;
		boolean visited[] = new boolean[10];
		for (int i = m; i <= n; i++) {
			dfs(visited, skip, 1, i - 1, 4); // 1, 3, 7, 9 are symmetric
			dfs(visited, skip, 2, i - 1, 4); // 2, 4, 6, 8 are symmetric
			dfs(visited, skip, 5, i - 1, 1); // 5
		}
		return res;
	}

	void dfs(boolean[] visited, int[][] skip, int cur, int remain, int mult) {
		if (remain == 0) {
			res += mult;
			return;
		}
		visited[cur] = true;
		for (int i = 1; i <= 9; i++) {
			if (!visited[i] && (skip[cur][i] == 0 || visited[skip[cur][i]])) {
				dfs(visited, skip, i, remain - 1, mult);
			}
		}
		visited[cur] = false;
	}
}

// 353. Design Snake Game
// Design a Snake game that is played on a device with screen size = width x height. Play the game online if you are not familiar with the game.
// The snake is initially positioned at the top left corner (0,0) with length = 1 unit.
// You are given a list of food's positions in row-column order. When a snake eats the food, its length and the game's score both increase by 1.
// Each food appears one by one on the screen. For example, the second food will not appear until the first food was eaten by the snake.
// When a food does appear on the screen, it is guaranteed that it will not appear on a block occupied by the snake.
class SnakeGame {

	ArrayDeque<int[]> deque;
	Map<String, int[]> map;
	int width, height, score;
	int[][] food;

	/**
	 * Initialize your data structure here.
	 * 
	 * @param width
	 *            - screen width
	 * @param height
	 *            - screen height
	 * @param food
	 *            - A list of food positions E.g food = [[1,1], [1,0]] means the first food is positioned at [1,1], the second is at [1,0].
	 */
	public SnakeGame(int width, int height, int[][] food) {
		this.width = width;
		this.height = height;
		this.food = food;
		score = 0;
		deque = new ArrayDeque<>();
		deque.add(new int[] { 0, 0 });
		map = new HashMap<>();
		map.put("U", new int[] { -1, 0 });
		map.put("D", new int[] { 1, 0 });
		map.put("L", new int[] { 0, -1 });
		map.put("R", new int[] { 0, 1 });
	}

	/**
	 * Moves the snake.
	 * 
	 * @param direction
	 *            - 'U' = Up, 'L' = Left, 'R' = Right, 'D' = Down
	 * @return The game's score after the move. Return -1 if game over. Game over when snake crosses the screen boundary or bites its body.
	 */
	public int move(String direction) {
		if (score == -1) {
			return -1;
		}
		int[] dir = map.get(direction);
		int[] h = deque.peekLast();
		int[] head = new int[2];
		head[0] = h[0] + dir[0];
		head[1] = h[1] + dir[1];
		// out bound
		if (head[0] < 0 || head[0] >= height || head[1] < 0 || head[1] >= width) {
			score = -1;
			return -1;
		}
		// eat
		if (score < food.length && head[0] == food[score][0] && head[1] == food[score][1]) {
			score++;
			deque.add(head);
			return score;
		}
		// cut tail, add head
		deque.removeFirst();
		if (contains(deque, head)) {
			score = -1;
			return -1;
		}
		deque.add(head);
		return score;
	}

	boolean contains(ArrayDeque<int[]> deque, int[] head) {
		for (int[] e : deque) {
			if (e[0] == head[0] && e[1] == head[1]) {
				return true;
			}
		}
		return false;
	}

	public boolean isReflected(int[][] points) {
		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;
		Set<String> set = new HashSet<>();
		for (int[] p : points) {
			max = Math.max(max, p[0]);
			min = Math.min(min, p[0]);
			String str = p[0] + "-" + p[1];
			set.add(str);
		}
		int sum = max + min;
		for (int[] p : points) {
			String str = (sum - p[0]) + "-" + p[1];
			if (!set.contains(str))
				return false;
		}
		return true;
	}

	class Point {
		int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int hashCode() {
			return x * 31 + y;
		}
	}
}
