package chap2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Code218 {
	
	public static List<Integer> twoSum(int A[], int sum, int start) {
		
		List<Integer> result = new ArrayList<Integer>();	
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int i = start; i<A.length; i++) {
			if(map.containsKey(A[i])) {
				result.add(map.get(A[i]));
				result.add(i+1);
				return result;
			}
			map.put(A[i], i+1);
			if(A[i]<sum)
				map.put(sum-A[i], i+1);
		}
		return result;
	}
	
	public static List<ArrayList<Integer>> threeSum(int A[], int sum) {
		
		List<ArrayList<Integer>> listSet = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i<A.length; i++) {
			List<Integer> result = twoSum(A, sum-A[i], i+1);
			if(result.size() != 0) {
				result.add(0, i+1);
				listSet.add((ArrayList<Integer>) result);
			}
		}
		return listSet;
	}

	public static void main(String[] args) {
		int A[] = {18, 6, 8, 11, 12, 13, 15, 19};
		List<ArrayList<Integer>> result = threeSum(A, 32);
		System.out.println(result);
	}
}
