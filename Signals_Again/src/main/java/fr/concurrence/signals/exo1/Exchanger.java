package fr.concurrence.signals.exo1;

public class Exchanger<T> {
	private final Object lock = new Object();
	private T firstCallValue;
	private T secondCallValue;
	private enum STATE {FIRST_CALL, SECOND_CALL, NO_CALL}
	private STATE state = STATE.NO_CALL;

	public T exchange(T value) throws InterruptedException {
		synchronized (lock) {
			if (state == STATE.NO_CALL) {
				firstCallValue = value;
				state = STATE.FIRST_CALL;
				while (state != STATE.SECOND_CALL) {
					lock.wait();
				}
				return secondCallValue;
			} else {
				secondCallValue = value;
				state = STATE.SECOND_CALL;
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
