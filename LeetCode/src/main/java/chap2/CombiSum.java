package chap2;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CombiSum {
    List<List<Integer>> res = new ArrayList<List<Integer>>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
	combineHelper(candidates, target, 0, new ArrayList<Integer>());
	return res;
    }

    private void combineHelper(int[] candidates, int target, int start, List<Integer> curr) {
	if (res.size() == 1) {
	    return;
	}
	if (target == 0) {
	    res.add(new ArrayList<Integer>(curr));
	    return;
	}
	for (int i = start; i < candidates.length; i++) {
	    if (candidates[i] > target) {
		continue;
	    }
	    curr.add(candidates[i]);
	    combineHelper(candidates, target - candidates[i], i, curr);
	    curr.remove(curr.size() - 1);
	}
    }

    //
    @Test
    public void test() {

	List<Integer> li = new ArrayList<>();
	int[] A = { 3, 5, 7, 9 };

	System.out.println(combinationSum(A, 12));
    }
}