package fr.concurrence.signals.exo2;

import java.util.ArrayDeque;
import java.util.Objects;

public class UnboundedSafeQueue<T> {
	private final Object lock = new Object();
	private final ArrayDeque<T> queue = new ArrayDeque<>();

	public void add(T value) {
		synchronized (lock) {
			Objects.requireNonNull(value);
			queue.add(value);
			lock.notify();
		}
	}

	public T take() throws InterruptedException {
		synchronized (lock) {
			while (queue.isEmpty()) {
				lock.wait();
			}
			return queue.remove();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var queue = new UnboundedSafeQueue<String>();
		for (int i = 0; i < 3; i++) {
			Thread.ofPlatform().start(() -> {
				while (true) {
					try {
						queue.add(Thread.currentThread().getName());
						Thread.sleep(2_000);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}

		while (true) {
			System.out.println("Current First Element : " + queue.take());
		}
	}
}
