package com.event.first;

import java.util.Random;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

/**
 * @author Miro Wengner (@miragemiko)
 */
public class Consumer implements Flow.Subscriber<FirstEvent> {

    private static final int MAX_PROCESS_TIME = 2;
    private static final int REQUESTED_MESSAGES = 1;
	private Random random = new Random();
	private String name;
	private Flow.Subscription subscription;

	Consumer(String name) {
		this.name = name;
	}

	@Override
	public void onComplete() {
		this.showMessage("No more events");
	}

	@Override
	public void onError(Throwable error) {
		this.showMessage("An error has ocurred");
		error.printStackTrace();
	}

	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		this.subscription.request(REQUESTED_MESSAGES);
		this.showMessage("Subscription OK");
	}

	@Override
	public void onNext(FirstEvent eventOne) {
		this.showMessage("An eventOne has arrived: " + eventOne.getSource() + ": " + eventOne.getDate() + ": "
				+ eventOne.getMessage());
		this.subscription.request(REQUESTED_MESSAGES);
		processEvent(eventOne);
	}

	// Private Methods
	private void processEvent(FirstEvent eventOne) {

		int number = random.nextInt(MAX_PROCESS_TIME);
		System.out.println(name + " processEvent " + eventOne + " time in seconds: " + number);

		try {
			TimeUnit.SECONDS.sleep(number);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void showMessage(String message) {
		System.out.println(Thread.currentThread().getName() + " : " + this.name + " : " + message);
	}
}
