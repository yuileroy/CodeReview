package leetcodelock;

import java.util.Arrays;
import java.util.PriorityQueue;

import keycode.util.Interval;

public class Solution251 {

	// 252. Meeting Rooms
	// Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend
	// all meetings.
	public boolean canAttendMeetings(Interval[] intervals) {
		if (intervals.length < 2) {
			return true;
		}
		Arrays.sort(intervals, ((a, b) -> a.start - b.start));
		for (int i = 1; i < intervals.length; i++) {
			if (intervals[i].start < intervals[i - 1].end) {
				return false;
			}
		}
		return true;
	}

	// 253. Meeting Rooms II
	// Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), find the minimum number of
	// conference rooms required.
    public int minMeetingRooms(int[][] intervals) {
        if (intervals.length < 2) {
            return intervals.length;
        }
        Arrays.sort(intervals, ((a, b) -> a[0] - b[0]));
        // end earliest at top
        PriorityQueue<int[]> heap = new PriorityQueue<>(((a, b) -> a[1] - b[1]));

        heap.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= heap.peek()[1]) {
                heap.remove();
            }
            heap.add(intervals[i]);
        }
        return heap.size();
    }
}

// 251. Flatten 2D Vector
// Design and implement an iterator to flatten a 2d vector. It should support the following operations: next and hasNext.
class Vector2D {
	int i, j;
	int[][] v;

	public Vector2D(int[][] v) {
		i = j = 0;
		this.v = v;
	}

	public int next() {
		hasNext(); // need to call hasNext() first
		return v[i][j++];
	}

	public boolean hasNext() {
		// j < v[i].length means valid
		while (i < v.length && j == v[i].length) {
			i++;
			j = 0;
		}
		return i < v.length && j < v[i].length;
	}
}
