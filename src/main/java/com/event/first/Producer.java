package com.event.first;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

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

        for (int i=0 ; i < events; i++) {
            FirstEvent eventOne = new FirstEvent();
            eventOne.setMessage("FirstEvent number "+i);
            eventOne.setSource(this.name);
            eventOne.setDate(new Date());

            publisher.submit(eventOne);

            int number = random.nextInt(events);

            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
