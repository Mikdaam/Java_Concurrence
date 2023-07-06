package fr.concurrence.signals;

import java.util.Objects;

public class RendezVous<T> {
	private final Object lock = new Object();
	private T value;
	private boolean isProposed = false;

	public void set(T value) {
		synchronized (lock) {
			Objects.requireNonNull(value);
			this.value = value;
			isProposed = true;
			lock.notify();
		}
	}

	public T get() throws InterruptedException {
		synchronized (lock) {
			while (!isProposed) {
				lock.wait();
			}
			return value;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var rdv = new RendezVous<String>();
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(20_000);
				rdv.set("Message");
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		System.out.println(rdv.get());
	}
}
