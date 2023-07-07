package fr.concurrence.signals.exo2;

import java.util.ArrayList;
import java.util.HashMap;

public class Aggregator {
	private final Object lock = new Object();
	private final HashMap<Thread, Integer> temperatures = new HashMap<>();
	private final int aggregators;

	public Aggregator(int count) {
		this.aggregators = count;
	}

	public void add(int value) throws InterruptedException {
		synchronized (lock) {
			while (temperatures.containsKey(Thread.currentThread())) {
				lock.wait();
			}

			temperatures.put(Thread.currentThread(), value);
			lock.notifyAll();
		}
	}

	public double average () throws InterruptedException {
		synchronized (lock) {
			while (temperatures.size() < aggregators) {
				lock.wait();
			}
			var average = temperatures.values().stream().mapToInt(Integer::intValue).average().getAsDouble();
			temperatures.clear();
			lock.notifyAll();
			return average;
		}
	}
}
