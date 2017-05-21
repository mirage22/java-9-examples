package com.event.first;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Miro Wengner (@miragemiko)
 */
public class Main {

	static final int INDEX_START = 0;
	private static final int REG_CONSUMERS = 5;
	private static final int REG_PRODUCERS = 2;
	private static final int PRODUCER_EVENTS = 1;

	public static void main(String[] args) {
		SubmissionPublisher<FirstEvent> publisher = new SubmissionPublisher<>();

		IntStream.range(INDEX_START, REG_CONSUMERS).forEach(i -> {
			Consumer consumer = new Consumer("Consumer: " + i);
			publisher.subscribe(consumer);
		});

		Set<Producer> producers = IntStream.range(INDEX_START, REG_PRODUCERS)
				.mapToObj(i -> new Producer(PRODUCER_EVENTS, publisher, "ProducerSystem:" + i))
				.collect(Collectors.toSet());

		List<ForkJoinTask<?>> tasks = producers.stream().map(t -> ForkJoinPool.commonPool().submit(t))
				.collect(Collectors.toList());
		do {
			System.out.println("... tasks ...");
			printStatus(tasks);
			System.out.println("Publisher: MaximumLag: " + publisher.estimateMaximumLag());
			System.out.println("Publisher: Max Buffer Capacity: " + publisher.getMaxBufferCapacity());

			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while (tasks.stream().filter(t -> !t.isDone()).count() != 0 || (publisher.estimateMaximumLag() > 0));

		System.out.println("... After execution ...");
		printStatus(tasks);

	}

	// Private Methods
	private static void printStatus(List<ForkJoinTask<?>> tasks) {
		tasks.forEach(t -> System.out.println("Main Task: " + t.isDone()));
	}
}
