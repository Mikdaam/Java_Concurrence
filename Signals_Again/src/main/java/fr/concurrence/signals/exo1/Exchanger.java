package fr.concurrence.signals.exo1;

public class Exchanger<T> {
	private final Object lock = new Object();
	private T firstCallValue;
	private T secondCallValue;
	private boolean firstCall = true;
	private boolean secondCall;
	public T exchange(T value) throws InterruptedException {
		synchronized (lock) {
			if (firstCall) {
				firstCall = false;
				firstCallValue = value;
				while (!secondCall) {
					lock.wait();
				}
				return secondCallValue;
			} else  {
				secondCall = true;
				secondCallValue = value;
				lock.notify();
				return firstCallValue;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var exchanger = new Exchanger<String>();
		Thread.ofPlatform().start(() -> {
			try {
				System.out.println("thread 1 " + exchanger.exchange("foo1"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		System.out.println("main " + exchanger.exchange(null));
	}
}
