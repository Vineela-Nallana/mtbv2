package com.example.movieticket;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.temporal.client.WorkflowOptions;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class MainApplication {

    @Autowired
    private WorkflowClient workflowClient;

    @Autowired
    private WorkflowServiceStubs serviceStubs;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    // Starts the Temporal worker and a sample workflow for testing
    @PostConstruct
    public void startWorkerAndWorkflow() {
        // Step 1: Create Worker Factory and Worker
        WorkerFactory factory = WorkerFactory.newInstance(workflowClient);
        Worker worker = factory.newWorker("MOVIE_TICKET_TASK_QUEUE");

        // Step 2: Register Workflow and Activities with the Worker
        worker.registerWorkflowImplementationTypes(MovieTicketWorkflowImpl.class);
        worker.registerActivitiesImplementations(new MovieTicketActivitiesImpl());

        // Step 3: Start the Worker Factory to listen for workflows and activities
        factory.start();
        System.out.println("Worker started and listening on task queue: MOVIE_TICKET_TASK_QUEUE");

        // Step 4: Start a sample workflow for testing the setup
        startTestWorkflow();
    }

    // Starts a sample workflow for testing movie ticket booking
    private void startTestWorkflow() {
        // Step 5: Define Workflow Options
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("MOVIE_TICKET_TASK_QUEUE") // The same task queue name as in Worker
                .build();

        // Step 6: Create Workflow Stub and start workflow
        MovieTicketWorkflow workflow = workflowClient.newWorkflowStub(MovieTicketWorkflow.class, options);
        workflow.bookTicket("Inception", 3);  // Test movie and seat count
        System.out.println("Workflow execution started for movie booking.");
    }
}

//package com.example.movieticket;
//
//import io.temporal.client.WorkflowClient;
//import io.temporal.client.WorkflowOptions;
//import io.temporal.serviceclient.WorkflowServiceStubs;
//import io.temporal.worker.Worker;
//import io.temporal.worker.WorkerFactory;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//
//@SpringBootApplication
//
//public class MainApplication {
//
//    public static void main(String[] args) {
//        // Step 1: Create the Workflow Service Stubs
//        WorkflowServiceStubs serviceStubs = WorkflowServiceStubs.newInstance();
//
//        // Step 2: Create the Workflow Client
//        WorkflowClient client = WorkflowClient.newInstance(serviceStubs);
//
//        // Step 3: Create the Worker Factory
//        WorkerFactory factory = WorkerFactory.newInstance(client);
//
//        // Step 4: Define the Task Queue for Workers
//        String taskQueue = "MOVIE_TICKET_TASK_QUEUE";
//
//        // Step 5: Create a Worker
//        Worker worker = factory.newWorker(taskQueue);
//
//        // Step 6: Register Workflow and Activities
//        worker.registerWorkflowImplementationTypes(MovieTicketWorkflowImpl.class);
//        worker.registerActivitiesImplementations(new MovieTicketActivitiesImpl());
//
//        // Step 7: Start the Worker Factory
//        factory.start();
//
//        System.out.println("Worker started and listening on task queue: " + taskQueue);
//
//        // Step 8: Start a Workflow Execution (for testing)
//        startTestWorkflow(client, taskQueue);
//    }
//
//    /**
//     * Starts a test workflow execution for demonstration.
//     * You can remove or modify this method as per your requirements.
//     */
//    private static void startTestWorkflow(WorkflowClient client, String taskQueue) {
//        // Define the Workflow Options
//        WorkflowOptions options = WorkflowOptions.newBuilder()
//                .setTaskQueue(taskQueue)
//                .build();
//
//        // Create a Workflow Stub using the Workflow Client
//        MovieTicketWorkflow workflow = client.newWorkflowStub(MovieTicketWorkflow.class, options);
//
//        // Start the Workflow
//        System.out.println("Starting workflow for movie ticket booking...");
//        workflow.bookTicket("Inception", 3);
//        System.out.println("Workflow execution started.");
//    }
//}
