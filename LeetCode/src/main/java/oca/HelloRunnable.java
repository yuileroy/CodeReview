package oca;

public class HelloRunnable implements Runnable {
	@Override
	public void run() {
		System.out.println("Hello from a runnable class!");
	}

	public static void main(String[] args) {
		(new Thread(new HelloRunnable())).start();
		(new HelloThread()).start();
	}

}

class HelloThread extends Thread {
	@Override
	public void run() {
		System.out.println("Hello from a thread!");
	}
}
