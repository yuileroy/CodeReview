package chap2;

public class Code213 {

	// {7,9,1,3,5,6} {6,7,8,9,20,1,3,5} 4 or 7

	public static int search(int A[], int target) {

		int result = -1;
		int first = 0, last = A.length - 1;
		
		while (first <= last) {
			int mid = (first + last) / 2;
			System.out.println("mid: " + mid + ", first: " + first + ", last: " + last);
			if (target == A[mid]) {
				result = mid;
				break;
			} else if (A[first] <= A[mid]) { //left sorted
				if(target < A[mid] && target >= A[first])
					last = mid;
				else
					first = mid + 1; //-> first
			} else { //right sorted
				if(target > A[mid] && target <= A[last]) 
					first = mid + 1;
				else
					last = mid;
			}
		}
		
		//if(target == A[last])
		//	result = last;	
		
		return result;
	}
	
	public static void main(String args[]) {
		int[] A = {5, 7, 1, 2, 4};
		int result = search(A, 4);
		System.out.println("index: " + result);
	}
}
