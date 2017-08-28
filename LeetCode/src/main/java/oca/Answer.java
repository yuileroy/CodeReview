package oca;

import java.math.BigInteger;
import java.util.Random;

public class Answer {
	static BigInteger zero = new BigInteger("0");
	static BigInteger one = new BigInteger("1");
	static BigInteger two = new BigInteger("2");
	static String output = "";

	public static String answer(String M, String F) {

		// Your code goes here.
		BigInteger count = new BigInteger("0");
		BigInteger numM = new BigInteger(M);
		BigInteger numF = new BigInteger(F);

		findGeneration(numM, numF, count);
		if(output.equals("impossible")) {
			return output;
		} else {
			return count.toString();
		}
	}

	public static void findGeneration(BigInteger numM, BigInteger numF, BigInteger count) {
		//System.out.println(numM.toString() + " input " + numF.toString());
		if(numM.equals(one)) {
			count.add(numF.subtract(one));
			return;
		} else if(numF.equals(one)) {
			count.add(numM.subtract(one));
			return;
		}
		if(numF.mod(two).equals(BigInteger.ZERO) && numM.mod(two).equals(BigInteger.ZERO)) {
			output = "impossible";
			return;
		}
		if(numM.compareTo(numF) < 0) {
			BigInteger temp = numF;
			numF = numM;
			numM = temp;
		}
		count.add(numM.divide(numF));
		//System.out.println(numM +"," + numF);
		numM = numM.mod(numF);
		if(numM.equals(zero)) {
			output = "impossible";
			return;
		}
		findGeneration(numF, numM, count);
	}

	public static String answer2(String M, String F) {

		try {
			long numM = Long.parseLong(M);
			long numF = Long.parseLong(F);

			String output = "impossible";
			long count = 0;
			while(numM > 1 || numF > 1) {

				if(!checkIsValid(numM, numF)) {
					numM = numF = 0;
				} else if(numM > numF) {
					numM -= numF;
					count++;
				} else if(numF > numM) {
					numF -= numM;
					count++;
				}

				// System.out.println("cycle: " + count + " " + numM + " " + numF);
			}

			if(numM == 1 && numF == 1) {
				output = "" + count;
			}

			return output;
		} catch (NumberFormatException nfe) {
			return "impossible";
		}
	}

	protected static boolean checkIsValid(long numM, long numF) {
		boolean valid = true;
		if(numM % 2 == 0 && numF % 2 == 0) {
			valid = false;
		} else if(numM == numF) {
			valid = false;
		} else if(numM <= 0 || numF <= 0) {
			valid = false;
		} else if(numM > 1 && numF % numM == 0) {
			valid = false;
		} else if(numF > 1 && numM % numF == 0) {
			valid = false;
		}
		return valid;
	}

	public static void main(String[] s) {
		System.out.println(zero);
		System.out.println(one);
		// "53313142", "1757945314"
		//System.out.println(answer("7438211522322636982", "2143006194065126637"));
		boolean next = false;
		while(!next) {
			Random r = new Random();
			long i = r.nextLong();
			while(i < 0) {
				i = r.nextLong();
			}
			long j = r.nextLong();
			while(j < 0) {
				j = r.nextLong();
			}
			System.out.println("test");
			String s1 = answer(String.valueOf(i), String.valueOf(j));
			String s2 = answer2(String.valueOf(i), String.valueOf(j));
			if(!s1.equals("impossible") && !s1.equals(s2)) {
				System.out.println(s1 + " ::: " + s2);
				System.out.println(i + " res " + j);
				break;
			}
		}

	}
}
