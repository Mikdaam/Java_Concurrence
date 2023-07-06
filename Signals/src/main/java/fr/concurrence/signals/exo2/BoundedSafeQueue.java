package fr.concurrence.signals.exo2;

import java.util.ArrayDeque;
import java.util.Objects;

public class BoundedSafeQueue<T> {
	private final Object lock = new Object();
	private final ArrayDeque<T> queue;
	private final int capacity;

	public BoundedSafeQueue(int capacity) {
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
		queue = new ArrayDeque<>(capacity);
	}

	public void put(T value) throws InterruptedException {
		Objects.requireNonNull(value);
		synchronized (lock) {
			while (queue.size() >= capacity) {
				lock.wait();
			}
			queue.add(value);
			lock.notifyAll();
		}
	}

	public T take() throws InterruptedException {
		synchronized (lock) {
			while (queue.isEmpty()) {
				lock.wait();
			}

			lock.notifyAll();
			return queue.remove();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var queue = new BoundedSafeQueue<String>(100);
		for (int i = 0; i < 3; i++) {
			Thread.ofPlatform().start(() -> {
				while (true) {
					try {
						queue.put(Thread.currentThread().getName());
						Thread.sleep(2_000);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}

		while (true) {
			System.out.println("Take: " + queue.take());
		}
	}
}
