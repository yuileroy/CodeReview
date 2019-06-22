package leetcodelock;

import java.util.ArrayList;
import java.util.List;

public class Solution254 {

	public List<List<Integer>> getFactors(int n) {
		List<List<Integer>> res = new ArrayList<>();
		// dfs(2, n, new ArrayList<>(), res);
		build(2, n, new ArrayList<>(), res);
		return res;
	}

	void build(int start, int n, List<Integer> item, List<List<Integer>> res) {
		for (int i = start; i <= n / i; i++) {
			if (n % i == 0) {
				item.add(i);

				// -> add the pair and save an item
				item.add(n / i);
				res.add(new ArrayList<>(item));
				item.remove(item.size() - 1);

				build(i, n / i, item, res);
				item.remove(item.size() - 1);
			}
		}
	}

	void dfs(int start, int n, List<Integer> item, List<List<Integer>> res) {
		// ! correct but inefficient
		if (n == 1 && item.size() > 1) {
			res.add(new ArrayList<>(item));
			return;
		}
		for (int i = start; i <= n; i++) {
			if (n % i == 0) {
				item.add(i);
				dfs(i, n / i, item, res);
				item.remove(item.size() - 1);
			}
		}
	}

	void dfs2(int start, int n, List<Integer> item, List<List<Integer>> res) {
		// ! wrong, can't create item here
		if (start >= n) {
			item.add(n);
			res.add(new ArrayList<>(item));
			item.remove(item.size() - 1);
			return;
		}
		for (int i = start; i <= n / i; i++) {
			if (n % i == 0) {
				item.add(i);
				dfs2(i, n / i, item, res);
				item.remove(item.size() - 1);
			}
		}
	}
}
