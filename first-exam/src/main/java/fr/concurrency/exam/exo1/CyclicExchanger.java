package fr.concurrency.exam.exo1;

import java.util.stream.IntStream;

public class CyclicExchanger<T> {
	private final Object lock = new Object();
	private final T[] values;
	private final int nbParticipants;
	private int threadCount;
	@SuppressWarnings("unchecked")
	public CyclicExchanger(int nbParticipants) {
		this.nbParticipants = nbParticipants;
		this.values =  (T[]) new Object[nbParticipants];
	}

	public T exchange(T value) throws InterruptedException {
		synchronized (lock) {
			if (threadCount == nbParticipants) {
				throw new IllegalStateException();
			}

			int i = threadCount;
			values[threadCount] = value;
			threadCount++;

			if (threadCount == nbParticipants) {
				lock.notifyAll();
			}

			while (threadCount < nbParticipants) {
				lock.wait();
			}

			return values[(i + 1) % nbParticipants];
		}
	}

	public static void main(String[] args) {
		var exchanger = new CyclicExchanger<Integer>(5);

		IntStream.range(0, 5).forEach(i -> Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(i * 1_000L);
				System.out.println("Exchange from " + i + " is " + exchanger.exchange(i));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		}));
	}
}
