package fr.concurrence.signals.exo2;

import java.util.List;

public class ApplicationBis {
	public static void main(String[] args) throws InterruptedException {
		var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

		var aggregator = new Aggregator(rooms.size());
		// Aggregator
		for (String room : rooms) {
			Thread.ofPlatform().start(() -> {
				while (true) {
					try {
						int temperature = Heat4J.retrieveTemperature(room);
						aggregator.add(temperature);
						System.out.println("Temperature in room " + room + " : " + temperature);
					} catch (InterruptedException e) {
						throw new AssertionError(e);
					}
				}
			});
		}

		System.out.println(aggregator.average());
	}
}
