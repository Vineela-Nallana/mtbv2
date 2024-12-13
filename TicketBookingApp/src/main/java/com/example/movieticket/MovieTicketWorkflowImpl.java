package com.example.movieticket;

import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;

import java.time.Duration;

public class MovieTicketWorkflowImpl implements MovieTicketWorkflow {

    private final MovieTicketActivities activities;

    public MovieTicketWorkflowImpl() {
        this.activities = Workflow.newActivityStub(MovieTicketActivities.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofMinutes(5))
                        .build());
    }

    @Override
    public void bookTicket(String movieName, int seatCount) {
        activities.updateInventory(movieName, seatCount);
        activities.processPayment(movieName, seatCount);
        activities.sendNotification("Booking confirmed for " + movieName + " with seats: " + seatCount);
    }
}
