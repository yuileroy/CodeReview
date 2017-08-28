package chap2;

public class Code211 {

	// 2.1.1 & 2.1.2
	int removeDuplicates(int A[], int n) {
		if(n == 0)
			return 0;
		int index = 0;
		for(int i = 1; i < n; i++) {
			if(A[index] != A[i]) {
				index++;
				A[index] = A[i];
				// System.out.println(index);
			}
		}
		return index + 1;
	}

	int removeDuplicates2(int A[], int n) {
		if(n == 0)
			return 0;
		int index = 0;
		for(int i = 2; i < n; i++) {
			if(A[index] != A[i] || A[i] != A[i - 2]) {
				index++;
				A[index] = A[i];
			}
		}
		return index + 1;
	}

	public static void main(String args[]) {
		Code211 test = new Code211();
		int[] A = { 1, 1, 1, 2, 4, 5, 5, 5 };
		// int result = test.removeDuplicates(A, A.length);
		int result2 = test.removeDuplicates2(A, A.length);
		System.out.println("length: " + result2);
		for(int i = 0; i < result2; i++) {
			System.out.println(A[i]);
		}
	}
}
