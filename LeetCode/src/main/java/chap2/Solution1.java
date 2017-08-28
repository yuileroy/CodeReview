package chap2;
// IMPORT LIBRARY PACKAGES NEEDED BY YOUR PROGRAM
// SOME CLASSES WITHIN A PACKAGE MAY BE RESTRICTED
// DEFINE ANY CLASS AND METHOD NEEDED
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

// CLASS BEGINS, THIS CLASS IS REQUIRED
public class Solution1 {
	// METHOD SIGNATURE BEGINS, THIS METHOD IS REQUIRED
	int levelFieldTime(int numRows, int numColumns, List<List<Integer>> field) {
		// WRITE YOUR CODE HERE
		List<Point> li = new ArrayList<>();
		for (int i = 0; i < field.size(); i++) {
			for (int j = 0; j < field.get(0).size(); j++) {
				if (field.get(i).get(j) > 1) {
					li.add(new Point(i, j, field.get(i).get(j)));
				}
			}
		}
		Collections.sort(li, new Comparator<Point>() {
			public int compare(Point o1, Point o2) {
				return o1.h - o2.h;
			}
		});

		int res = 0;
		Point pre = new Point(0, 0, 1);
		for (int i = 0; i < li.size(); i++) {
			Point next = li.get(i);
			res += getSteps(field, pre, next);
			res += next.h;
			field.get(next.x).set(next.y, 1);
			pre = next;
		}
		return res;
	}
	// METHOD SIGNATURE ENDS

	int getSteps(List<List<Integer>> field, Point cur, Point next) {
		List<List<Integer>> tmp = new ArrayList<>();
		for (List<Integer> li : field) {
			tmp.add(new ArrayList<>(li));
		}

		int steps = 0;
		Deque<Point> q = new ArrayDeque<>();
		q.addLast(cur);

		while (!q.isEmpty()) {
			steps++;
			boolean found = false;
			int size = q.size();
			for (int k = 0; k < size; k++) {
				Point cp = q.removeFirst();
				Point p1 = mark(tmp, cp.x, cp.y - 1);
				Point p2 = mark(tmp, cp.x, cp.y + 1);
				Point p3 = mark(tmp, cp.x - 1, cp.y);
				Point p4 = mark(tmp, cp.x + 1, cp.y);
				if (p1 != null) {
					if (p1.x == next.x && p1.y == next.y) {
						found = true;
					}
					q.add(p1);
				}
				if (p2 != null) {
					if (p2.x == next.x && p2.y == next.y) {
						found = true;
					}
					q.add(p2);
				}
				if (p3 != null) {
					if (p3.x == next.x && p3.y == next.y) {
						found = true;
					}
					q.add(p3);
				}
				if (p4 != null) {
					if (p4.x == next.x && p4.y == next.y) {
						found = true;
					}
					q.add(p4);
				}
			}
			if (found) {
				break;
			}
		}
		return steps;
	}

	Point mark(List<List<Integer>> tmp, int i, int j) {
		if (i >= 0 && j >= 0 && i < tmp.size() && j < tmp.get(0).size()) {
			if (tmp.get(i).get(j) == 1) {
				tmp.get(i).set(j, 0);
				return new Point(i, j, 1);
			}
		}
		return null;
	}
}

class Point {
	int x;
	int y;
	int h;

	Point(int x, int y, int h) {
		this.x = x;
		this.y = y;
		this.h = h;
	}
}