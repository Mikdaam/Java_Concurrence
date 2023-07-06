package fr.concurrence.signals.exo3;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Vote {
	private final Object lock = new Object();
	private final HashMap<String, Integer> votes = new HashMap<>();
	private final int voters;
	private int count;
	private String winner;

	public Vote(int voters) {
		this.voters = voters;
	}

	public String vote(String vote) throws InterruptedException {
		Objects.requireNonNull(vote);
		synchronized (lock) {
			if (count == voters) return winner;

			votes.merge(vote, 1, Integer::sum);
			count++;

			if (count == voters) {
				lock.notifyAll();
				winner = computeWinner();
			}

			while (count < voters) {
				lock.wait();
			}

			return winner;
		}
	}

	private String computeWinner() {
		var score = -1;
		String winner = null;
		for (var e : votes.entrySet()) {
			var key = e.getKey();
			var value = e.getValue();
			if (value > score || (value == score && key.compareTo(winner) < 0)) {
				winner = key;
				score = value;
			}
		}
		return winner;

		// * Stream version
		/*return votes.entrySet().stream()
				.max(Comparator.comparingInt(Map.Entry::getValue)
						.thenComparing(Map.Entry::getKey))
				.map(Map.Entry::getKey)
				.orElse(null);*/
	}

	public static void main(String[] args) throws InterruptedException {
		var vote = new Vote(4);
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(2_000);
				System.out.println("The winner is " + vote.vote("un"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(1_500);
				System.out.println("The winner is " + vote.vote("zero"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		Thread.ofPlatform().start(() -> {
			try {
				Thread.sleep(1_000);
				System.out.println("The winner is " + vote.vote("un"));
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		});
		System.out.println("The winner is " + vote.vote("zero"));
	}
}
