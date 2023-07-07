package fr.concurrency.exam.exo2;

import java.util.stream.IntStream;

public class Application {
	public static void main(String[] args) {
		var bid = new Bid(5);
		IntStream.range(1, 6).forEach(i -> Thread.ofPlatform().start(() -> {
			try {
				System.out.println("Thread " + i + " proposes " );
				var reject = bid.propose(i);
				while (true) {
					Thread.sleep(i * 1_000L);
					bid.propose(i);
					System.out.println("Thread " + i + " was unblocked because its proposed value " + i + " is now the smallest");
				}
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
		}));
	}
}
