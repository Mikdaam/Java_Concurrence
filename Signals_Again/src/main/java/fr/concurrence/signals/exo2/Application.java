package fr.concurrence.signals.exo2;

import java.util.ArrayList;
import java.util.List;

public class Application {
	public static void main(String[] args) throws InterruptedException {
		var rooms = List.of("bedroom1", "bedroom2", "kitchen", "dining-room", "bathroom", "toilets");

		var aggregator = new Aggregator(rooms.size());
		// Aggregator
		for (String room : rooms) {
			Thread.ofPlatform().start(() -> {
				try {
					int temperature = Heat4J.retrieveTemperature(room);
					System.out.println("Temperature in room " + room + " : " + temperature);
					aggregator.add(temperature);
				} catch (InterruptedException e) {
					throw new AssertionError(e);
				}
			});
		}

		System.out.println(aggregator.average());
	}
}
