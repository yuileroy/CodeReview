package systemdesign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

import org.junit.Test;

// if distributed with multiple hashmap-heap, combine top K.
// put each item randomly can avoid a top K item appears beyond top K in every heap, 
// also increasing heap size to 2K makes that almost impossible to happen.

// create 3 tabls, min, hour, day
// min-table won't store all time data, set an expiration time
// hour-table combine values of 60 min-table
// day-table combine values of 24 hour-table
// expire and combine should be scheduled as per request

// use Redis cache if query top K frequently, and no need to expire past data in cache since they won't change.

// 100K QPS, consider bulk calculation.
// Redis performance
// bloom filter!
// 

public class TopKIP {
	Random r = new Random();
	int K = 10;
	List<Map<Integer, Integer>> maps = new ArrayList<>(3600);
	PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);

	public TopKIP() {
		for (int i = 0; i < 3600; i++) {
			maps.add(new HashMap<Integer, Integer>());
		}
	}

	void addItem() {
		int idx = r.nextInt(3600);
		int ip = r.nextInt(100);
		maps.get(idx).put(ip, maps.get(idx).getOrDefault(ip, 0) + 1);
		int cnt = cntIP(ip);
		addToQueue(new int[] { ip, cnt });
	}

	void addItemBulk(int size) {
		HashMap<Integer, Integer> bulk = new HashMap<>();
		while (size-- > 0) {
			int ip = r.nextInt(100);
			bulk.put(ip, bulk.getOrDefault(ip, 0) + 1);
		}
		int idx = r.nextInt(3600);
		for (int ip : bulk.keySet()) {
			maps.get(idx).put(ip, maps.get(idx).getOrDefault(ip, 0) + bulk.get(ip));
			int cnt = cntIP(ip);
			addToQueue(new int[] { ip, cnt });
		}
	}

	int cntIP(int ip) {
		int sum = 0;
		for (Map<Integer, Integer> map : maps) {
			sum += map.getOrDefault(ip, 0);
		}
		return sum;
	}

	void addToQueue(int[] item) {
		for (int[] e : pq) {
			if (e[0] == item[0]) {
				pq.remove(e);
				e[1] = item[1];
				pq.add(e);
				return;
			}
		}
		if (pq.size() < K) {
			pq.add(item);
		} else if (item[1] > pq.peek()[1]) {
			pq.remove();
			pq.add(item);
		}
	}

	String printRes() {
		StringBuilder sb = new StringBuilder();
		for (int[] item : pq) {
			sb.append(item[0] + "-" + item[1] + ", ");
		}
		pq.iterator();
		return sb.toString();
	}

	// @Test
	public void test1() throws InterruptedException {
		TopKIP test = new TopKIP();
		int num = 500000;
		while (num-- > 0) {
			test.addItem();
			if (num % 50000 == 0) {
				System.out.println(test.printRes());
				Thread.sleep(500);
			}
		}
	}

	@Test
	public void test2() throws InterruptedException {
		TopKIP test = new TopKIP();
		int num = 500000;
		int bulk = 50000;
		for (int i = 0; i * bulk < num; i++) {
			test.addItemBulk(bulk);
			System.out.println(test.printRes());
			Thread.sleep(500);
		}
	}

}
