package leetcodelock;

import java.util.HashSet;
import java.util.Set;

// 379. Design Phone Directory
public class Solution379 {

}

class PhoneDirectory {

	Set<Integer> set = new HashSet<>();
	int cur = 0;
	int maxNumbers;

	/**
	 * Initialize your data structure here
	 * 
	 * @param maxNumbers
	 *            - The maximum numbers that can be stored in the phone directory.
	 */
	public PhoneDirectory(int maxNumbers) {
		this.maxNumbers = maxNumbers;
	}

	/**
	 * Provide a number which is not assigned to anyone.
	 * 
	 * @return - Return an available number. Return -1 if none is available.
	 */
	public int get() {
		if (!set.isEmpty()) {
			int item = set.iterator().next();
			set.remove(item);
			return item;
		} else if (cur < maxNumbers) {
			return cur++;
		}
		return -1;
	}

	/** Check if a number is available or not. */
	public boolean check(int number) {
		return number >= cur || set.contains(number);
	}

	/** Recycle or release a number. */
	public void release(int number) {
		if (number < cur) {
			set.add(number);
		}
	}
}