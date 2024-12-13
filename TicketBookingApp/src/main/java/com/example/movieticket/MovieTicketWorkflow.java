package com.example.movieticket;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface MovieTicketWorkflow {

    @WorkflowMethod
    void bookTicket(String movieName, int seatCount);
}
