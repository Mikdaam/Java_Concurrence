package fr.concurrence.signals.exo3;

import java.util.stream.IntStream;

public class ExchangerReuse<T> {
	private final Object lock = new Object();
	private T firstCallValue;
	private T secondCallValue;
	private enum STATE {NOT_STARTED, EXCHANGING, DONE}
	private STATE state = STATE.NOT_STARTED;

	public T exchange(T value) throws InterruptedException {
		synchronized (lock) {
			if (state == STATE.NOT_STARTED) {
				firstCallValue = value;
				state = STATE.EXCHANGING;
				while (state != STATE.DONE) {
					lock.wait();
				}
				return secondCallValue;
			} else {
				secondCallValue = value;
				state = STATE.DONE;
				lock.notify();
				return firstCallValue;
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		var exchanger = new ExchangerReuse<String>();
		IntStream.range(0, 10).forEach(i -> {
			Thread.ofPlatform().start(() -> {
				try {
					System.out.println("thread " + i + " received from " + exchanger.exchange("thread " + i));
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			});
		});
	}
}
