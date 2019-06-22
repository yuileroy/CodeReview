package keycode;

import org.junit.Test;

public class CompileCode {

	public int canCompleteCircuit(int[] gas, int[] cost) {
		int gasTotal = 0;
		int costTotal = 0;
		for (int i = 0; i < gas.length; i++) {
			gasTotal += gas[i];
			costTotal += cost[i];
		}
		if (gasTotal < costTotal) {
			return -1;
		}
		int res = -1;
		gasTotal = 0;
		costTotal = 0;
		for (int i = 0; i < gas.length; i++) {
			gasTotal += gas[i];
			costTotal += cost[i];
			if (gasTotal >= costTotal) {
				if (res == -1) {
					res = i;
				}
			} else {
				res = -1;
				gasTotal = 0;
				costTotal = 0;
			}
		}
		return res;
	}

	@Test
	public void test() {
		String[] ss = "done d  v".split("[ ]+");
		for (String s : ss) {
			System.out.println(s);
		}
		System.out.println("done");
	}
}