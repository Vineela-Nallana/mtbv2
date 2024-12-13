package com.example.movieticket;

import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface MovieTicketActivities {

    void updateInventory(String movieName, int seatCount);

    void processPayment(String movieName, int seatCount);

    void sendNotification(String message);
}
