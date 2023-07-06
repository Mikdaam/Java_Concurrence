package fr.concurrence.signals.exo2;

import java.util.ArrayList;

public class Aggregator {
	private final Object lock = new Object();
	private final ArrayList<Integer> temperatures = new ArrayList<>();
	private final int count;

	public Aggregator(int count) {
		this.count = count;
	}

	public void add(int value) {
		synchronized (lock) {
			temperatures.add(value);
			lock.notifyAll();
		}
	}

	public double average () throws InterruptedException {
		synchronized (lock) {
			while (temperatures.size() < count) {
				lock.wait();
			}
			return temperatures.stream().mapToInt(Integer::intValue).average().getAsDouble();
		}
	}
}
