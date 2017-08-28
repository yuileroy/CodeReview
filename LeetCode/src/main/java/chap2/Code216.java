package chap2;

import java.util.HashMap;

/**
 * Given an unsorted array of integers, find the length of the longest
 * consecutive elements sequence. For example, Given [100, 4, 200, 1, 3, 2], The
 * longest consecutive elements sequence is [1, 2, 3, 4]. Return its length: 4.
 * Your algorithm should run in O(n) complexity.
 * 
 * Solution: store to Map
 */

public class Code216 {
	
	public static int longestConsecutive(int A[]) {
		HashMap<Integer, Boolean> map = new HashMap<Integer, Boolean>();
		for(int x : A) {
			map.put(x, true);
		}

		int max = 1;
		int length = 1;
		
		for(int x : map.keySet()) {

			if(map.get(x)) {
				int xValue = x;
				map.replace(x, false);
				while(map.get(++x) != null && map.get(x)) {
					map.replace(x, false);
					length++;
				}
				x = xValue;
				while(map.get(--x) != null && map.get(x)) {
					map.replace(x, false);
					length++;
				}
			}
			
			if(max<length) 
				max = length;
			length = 1;
		}
		
		return max;
	}

	public static void main(String[] args) {
		int A[] = {100, 8,6, 5, 200, 1, 3, 2};
		int n = longestConsecutive(A);
		System.out.println(n);
	}

}
