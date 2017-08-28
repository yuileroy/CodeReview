package chap2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//if sorted:
//{4, 6, 9, 11, 12, 13, 15, 19}, target=20
//19 > 16, next 19 -> 15
//15 < 16, next 4 -> 6
//6 > 5, next 15 -> 13

public class Code217 {

	public static List<Integer> twoSum(int A[], int sum) {
		List<Integer> result = new ArrayList<Integer>();
		
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		for(int i = 0; i<A.length; i++) {
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
	
	public static void main(String[] args) {
		int A[] = {18, 6, 8, 11, 12, 13, 15, 19};
		List<Integer> result = twoSum(A, 20);
		System.out.println(result);
	}

}
