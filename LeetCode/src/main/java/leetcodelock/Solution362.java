package leetcodelock;

// 362. Design Hit Counter
public class Solution362 {

}

// Design a hit counter which counts the number of hits received in the past 5 minutes.
// Each function accepts a timestamp parameter (in seconds granularity) and you may assume that calls are being made to the system in chronological
// order (ie, the timestamp is monotonically increasing). You may assume that the earliest timestamp starts at 1.
// It is possible that several hits arrive roughly at the same time.
class HitCounter {
	int N;
	int[] ts, cnt;

	/** Initialize your data structure here. */
	public HitCounter() {
		N = 300;
		ts = new int[N];
		cnt = new int[N];
	}

	/**
	 * Record a hit.
	 * 
	 * @param timestamp
	 *            - The current timestamp (in seconds granularity).
	 */
	public void hit(int timestamp) {
		int idx = timestamp % N;
		if (timestamp != ts[idx]) {
			cnt[idx] = 0;
			ts[idx] = timestamp;
		}
		cnt[idx]++;
	}

	/**
	 * Return the number of hits in the past 5 minutes.
	 * 
	 * @param timestamp
	 *            - The current timestamp (in seconds granularity).
	 */
	public int getHits(int timestamp) {
		int res = 0;
		for (int i = 0; i < N; i++) {
			if (timestamp - ts[i] < N) {
				res += cnt[i];
			}
		}
		return res;
	}
}

// class Solution364 {
// // bfs, pass sum to next level
// public int depthSumInverse(List<NestedInteger> nestedList) {
// int res = 0, sum = 0;
// while (!nestedList.isEmpty()) {
// List<NestedInteger> nList = new ArrayList<>();
// for (NestedInteger item : nestedList) {
// if (item.isInteger()) {
// sum += item.getInteger();
// } else {
// nList.addAll(item.getList());
// }
// }
// res += sum;
// nestedList = nList;
// }
// return res;
// }
// }
