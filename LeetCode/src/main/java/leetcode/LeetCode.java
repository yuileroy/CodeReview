package leetcode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class LeetCode {

	public int[] exclusiveTime0(int n, List<String> logs) {
		int[] res = new int[n];
		int sum = 0;
		Deque<Integer> iq = new ArrayDeque<>();
		Deque<Integer> tq = new ArrayDeque<>();
		String[] t = null;
		for (String lg : logs) {
			t = lg.split(":");
			int id = Integer.parseInt(t[0]);
			boolean isStart = t[1].equals("start");
			int ts = Integer.parseInt(t[2]);
			if (isStart) {
				iq.addLast(id);
				tq.addLast(ts);
			} else {
				int startT = tq.removeLast();
				res[id] = 1 + ts - startT - sum;
				sum += 1 + ts - startT;
				if (tq.isEmpty()) {
					sum = 0;
				}
			}
		}
		return res;
	}

	public int[] exclusiveTime2(int n, List<String> logs) {
		int[] res = new int[n];

		List<int[]> iq = new ArrayList<>();
		List<Integer> tq = new ArrayList<>();
		String[] t = null;
		for (String lg : logs) {
			t = lg.split(":");
			int id = Integer.parseInt(t[0]);
			boolean isStart = t[1].equals("start");
			int ts = Integer.parseInt(t[2]);
			if (isStart) {
				int[] x = { id, 0 };
				iq.add(x);
				tq.add(ts);
			} else {
				if (res[id] == 0) {
					if (iq.get(iq.size() - 1)[0] == id && iq.get(iq.size() - 1)[1] == 0) {
						int startT = tq.get(tq.size() - 1);
						res[id] = 1 + ts - startT;
					} else {
						int sum = 0;
						for (int k = iq.size() - 1; k >= 0; k--) {
							if (iq.get(k)[0] == id && iq.get(k)[1] == 0) {
								res[id] = 1 + ts - tq.get(k) - sum / 2;
								break;
							} else {
								sum += res[iq.get(k)[0]];
							}
						}
					}
				}
				int[] x = { id, 1 };
				iq.add(x);
				tq.add(ts);
			}
		}
		return res;
	}

	public int shoppingOffers(List<Integer> price, List<List<Integer>> special, List<Integer> needs) {
		int basePrice = 0;
		for (int i = 0; i < price.size(); i++) {
			basePrice += price.get(i) * needs.get(i);
		}
		for (int i = 0; i < special.size(); i++) {
			List<Integer> cp = special.get(i);
			boolean feasible = true;
			int currPrice = 0;
			for (int j = 0; j < needs.size() && feasible; j++) {
				if (cp.get(j) > needs.get(j)) {
					feasible = false;
				} else {
					currPrice += cp.get(j) * price.get(j);
				}
			}
			// sum cost of each > use special price
			if (feasible && currPrice > cp.get(needs.size())) {
				List<Integer> newNeeds = new ArrayList<>();
				for (int j = 0; j < needs.size(); j++) {
					newNeeds.add(needs.get(j) - cp.get(j));
				}
				int newPrice = cp.get(needs.size()) + shoppingOffers(price, special, newNeeds);
				if (newPrice < basePrice) {
					basePrice = newPrice;
				}
			}
		}
		return basePrice;
	}
}
