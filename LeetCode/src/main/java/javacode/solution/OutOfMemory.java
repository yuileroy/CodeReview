package javacode.solution;

import java.util.ArrayDeque;
import java.util.Deque;

public class OutOfMemory {
	public static void main(String[] args) {
		Deque<StringBuilder> tasks = new ArrayDeque<StringBuilder>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 100000; i++) {
					StringBuilder sb = new StringBuilder();
					
					tasks.add(sb);
					StringBuilder sb2 = tasks.peek();
					for (int k = 0; k < 1000; k++) {
						sb2.append(i + "FrozenOrb,");
					}
					
				}
			}
		}).start();
	}
}