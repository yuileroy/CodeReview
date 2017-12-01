package javacode.code;

import org.junit.Test;

public class CriclularQueue<T> {

	private final int capacity = 5;
	private T[] queue;
	private int front = 0;
	private int rear = 0;
	private int count = 0;

	@SuppressWarnings("unchecked")
	public CriclularQueue() {
		queue = (T[]) (new Object[capacity]);
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public boolean isFull() {
		return count == capacity;
	}

	public void enqueue(T obj) {
		if (isFull()) {
			System.out.println("Queue is Full.");
		} else {
			queue[rear] = obj;
			rear = (rear + 1) % capacity;
			count++;
		}
	}

	public T dequeue() {
		if (isEmpty()) {
			System.out.println("Queue is Empty.");
			return null;
		} else {
			T item = queue[front];
			queue[front] = null;
			front = (front + 1) % capacity;
			count--;
			return item;
		}
	}

	@SuppressWarnings("unchecked")
	public void expandCapacity() {
		T[] larger = (T[]) (new Object[queue.length * 2]);
		for (int scan = 0; scan < count; scan++) {
			larger[scan] = queue[front];
			front = (front + 1) % queue.length;
		}
		front = 0;
		rear = count;
		queue = larger;
	}

	@Test
	public void test() {
		CriclularQueue<String> queue = new CriclularQueue<>();
		queue.enqueue("A");
		queue.enqueue("B");
		queue.enqueue("C");
		queue.dequeue();
		queue.dequeue();
		queue.dequeue();
		queue.enqueue("D");
		// queue.enqueue("E");
		System.out.println(queue.isEmpty());
		queue.enqueue("E");
		System.out.println(queue.size());
		System.out.println(queue.dequeue());
	}

}
