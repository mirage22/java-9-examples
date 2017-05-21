package com.event.first;

import static com.event.first.Main.INDEX_START;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * @author Miro Wengner (@miragemiko)
 */
public class Producer implements Runnable {

	private Random random = new Random();
	private SubmissionPublisher<FirstEvent> publisher;
	private String name;
	private int events;

	Producer(int events, SubmissionPublisher<FirstEvent> publisher, String name) {
		this.events = events;
		this.publisher = publisher;
		this.name = name;
	}

	public void run() {

		IntStream.range(INDEX_START, events).forEach(i -> {
			FirstEvent eventOne = new FirstEvent();
			eventOne.setMessage("FirstEvent number " + i);
			eventOne.setSource(this.name);
			eventOne.setDate(new Date());

			publisher.submit(eventOne);

			int number = random.nextInt(events);

			try {
				TimeUnit.SECONDS.sleep(number);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

	}

}
