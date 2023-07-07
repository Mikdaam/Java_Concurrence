package fr.concurrency.exam.exo2;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Bid {
	private final Object lock = new Object();
	private final HashMap<Thread, Integer> propositions = new HashMap<>();
	private final int nbParticipants;
	private int threadCount;

	public Bid(int nbParticipants) {
		if (nbParticipants < 0) {
			throw new IllegalArgumentException();
		}
		this.nbParticipants = nbParticipants;
	}

	public int propose(int proposition) throws InterruptedException {
		synchronized (lock) {
			if (propositions.containsValue(proposition)) {
				return -1;
			}

			propositions.put(Thread.currentThread(), proposition);
			threadCount++;

			if (threadCount == nbParticipants) {
				var currentAvg = average();
				var min = getMin();
				min.interrupt();
				//return currentAvg;
			}

			while (threadCount < nbParticipants) {
				lock.wait();
			}

			return 0;
		}
	}

	private Thread getMin() {
		return propositions.entrySet().stream()
				.min(Comparator.comparingInt(Map.Entry::getValue))
				.map(Map.Entry::getKey)
				.orElse(null);
	}

	private int average() {
		return (int) propositions.values().stream().mapToInt(Integer::intValue).average().getAsDouble();
	}
}
