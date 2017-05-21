package com.event.first;

import java.util.List;
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

	private static final int INDEX_START = 0;
	private static final int REG_CONSUMERS = 5;
	private static final int REG_PRODUCERS = 2;
	private static final int PRODUCER_EVENTS = 1;

	public static void main(String[] args) {
		SubmissionPublisher<FirstEvent> publisher = new SubmissionPublisher<>();

		IntStream.range(INDEX_START, REG_CONSUMERS).forEach(i -> {
			Consumer consumer = new Consumer("Consumer: " + i);
			publisher.subscribe(consumer);
		});

		List<Producer> producers = IntStream.range(INDEX_START, REG_PRODUCERS)
				.mapToObj(i -> new Producer(PRODUCER_EVENTS, publisher, "ProducerSystem:" + i))
				.collect(Collectors.toList());

		ForkJoinTask<?> task1 = ForkJoinPool.commonPool().submit(producers.get(0));
		ForkJoinTask<?> task2 = ForkJoinPool.commonPool().submit(producers.get(1));

		do {
			System.out.println("Main: Task 1: " + task1.isDone());
			System.out.println("Main: Task 2: " + task2.isDone());

			System.out.println("Publisher: MaximumLag: " + publisher.estimateMaximumLag());
			System.out.println("Publisher: Max Buffer Capacity: " + publisher.getMaxBufferCapacity());

			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		} while ((!task1.isDone()) || (!task2.isDone()) || (publisher.estimateMaximumLag() > 0));

		System.out.println("Main Task 1 status: " + task1.isDone());
		System.out.println("Main Task 2 status: " + task2.isDone());
	}
}
