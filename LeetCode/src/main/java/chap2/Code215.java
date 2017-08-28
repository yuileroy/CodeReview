package chap2;

import java.util.Arrays;

public class Code215 {

	public int findMedianSortedArrays(int A[], int m, int B[], int n) {
		return findK(A, m, B, n, (m + n) / 2);
	}

	public static int findK(int A[], int m, int B[], int n, int k) {

		if (m < n)
			return findK(B, n, A, m, k);

		if (n == 0)
			return A[k - 1];
		else if (k == 1)
			return A[0] < B[0] ? A[0] : B[0];

		int BI = k / 2 < n ? k / 2 : n;
		int AI = k - BI;
		System.out.println("k:" + k + ", BI: " + BI + ", AI: " + AI);
		if (B[BI - 1] == A[AI - 1])
			return A[AI - 1];
		else if (B[BI - 1] < A[AI - 1]) {
			B = Arrays.copyOfRange(B, BI, n);
			return findK(A, m, B, n - BI, k - BI);
		} else {
			A = Arrays.copyOfRange(A, AI, m);
			return findK(A, m - AI, B, n, k - AI);
		}
	}

	public static void main(String[] args) {
		int A[] = { 4, 6, 8, 10, 12, 14 };
		int B[] = { 9, 33 };
		int result = findK(A, A.length, B, B.length, 4);
		System.out.println(result);
	}

}
