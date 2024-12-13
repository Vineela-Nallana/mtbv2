package com.example.movieticket;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class MovieTicketController {

    private final WorkflowClient workflowClient;

    @Autowired
    public MovieTicketController(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    @PostMapping("/book")
    public String bookTicket(@RequestParam String movieName, @RequestParam int seatCount) {
        try {
            // Define Workflow Options
            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setTaskQueue("MOVIE_TICKET_TASK_QUEUE")
                    .build();

            // Create Workflow Stub
            MovieTicketWorkflow workflow = workflowClient.newWorkflowStub(MovieTicketWorkflow.class, options);

            // Start the Workflow
            workflow.bookTicket(movieName, seatCount);
            return "Workflow started for booking ticket: " + movieName + " with seats: " + seatCount;
        } catch (Exception e) {
            return "Error starting workflow: " + e.getMessage();
        }
    }
}
