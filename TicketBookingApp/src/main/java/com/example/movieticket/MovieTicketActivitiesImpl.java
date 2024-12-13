package com.example.movieticket;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class MovieTicketActivitiesImpl implements MovieTicketActivities {

    @Override
    public void updateInventory(String movieName, int seatCount) {
        MongoDatabase database = MongoDBConfig.getDatabase();
        MongoCollection<Document> collection = database.getCollection("movie_inventory");

        Document movie = collection.find(eq("movie_name", movieName)).first();

        if (movie == null) {
            throw new IllegalArgumentException("Movie not found: " + movieName);
        }

        int availableSeats = movie.getInteger("available_seats");
        if (availableSeats < seatCount) {
            throw new IllegalArgumentException("Not enough seats available for: " + movieName);
        }

        collection.updateOne(eq("movie_name", movieName),
                Updates.inc("available_seats", -seatCount));
        System.out.println("Inventory updated for movie: " + movieName);
    }

    @Override
    public void processPayment(String movieName, int seatCount) {
        System.out.println("Payment processed for movie: " + movieName + " with seats: " + seatCount);
    }

    @Override
    public void sendNotification(String message) {
        MongoDatabase database = MongoDBConfig.getDatabase();
        MongoCollection<Document> bookings = database.getCollection("bookings");

        bookings.insertOne(new Document()
                .append("message", message)
                .append("status", "CONFIRMED"));
        System.out.println("Notification sent: " + message);
    }
}
