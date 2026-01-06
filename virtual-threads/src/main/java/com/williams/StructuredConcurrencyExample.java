package com.williams;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Demonstrates Java's Structured Concurrency API (JEP 453).
 * Structured Concurrency treats multiple tasks running in different threads as a single unit of work,
 * streamlining error handling and cancellation while improving reliability and observability.
 */
public class StructuredConcurrencyExample {

    /**
     * Example 1: Basic StructuredTaskScope with ShutdownOnFailure
     * All subtasks must complete successfully, or the scope fails fast.
     */
    public void basicExample() {
        System.out.println("\n=== Basic Structured Concurrency Example ===");
        System.out.println("Main thread: " + Thread.currentThread().getName());

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Fork multiple subtasks
            Subtask<String> task1 = scope.fork(() -> fetchDataFromService1());
            Subtask<String> task2 = scope.fork(() -> fetchDataFromService2());
            Subtask<String> task3 = scope.fork(() -> fetchDataFromService3());

            // Wait for all tasks to complete or any to fail
            scope.join();           // Wait for all subtasks
            scope.throwIfFailed();  // Throw if any subtask failed

            // All tasks succeeded - get results
            String result1 = task1.get();
            String result2 = task2.get();
            String result3 = task3.get();

            System.out.println("All results: " + result1 + ", " + result2 + ", " + result3);

        } catch (InterruptedException e) {
            System.out.println("Task was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Task failed: " + e.getMessage());
        }
    }

    /**
     * Example 2: ShutdownOnSuccess - first successful result wins
     * Useful for racing multiple operations where any successful result is acceptable.
     */
    public void shutdownOnSuccessExample() {
        System.out.println("\n=== ShutdownOnSuccess Example ===");
        System.out.println("Racing multiple services for fastest response...");

        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            // Fork multiple tasks - first to complete successfully wins
            scope.fork(() -> fetchFromSlowService());
            scope.fork(() -> fetchFromFastService());
            scope.fork(() -> fetchFromMediumService());

            // Wait for first successful result
            scope.join();

            // Get the first successful result
            String result = scope.result();
            System.out.println("First successful result: " + result);

        } catch (InterruptedException e) {
            System.out.println("Task was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("All tasks failed: " + e.getMessage());
        }
    }

    /**
     * Example 3: Custom timeout with structured concurrency
     * Demonstrates how to apply timeouts to concurrent operations.
     */
    public void timeoutExample() {
        System.out.println("\n=== Timeout Example ===");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> task1 = scope.fork(() -> longRunningTask());
            Subtask<String> task2 = scope.fork(() -> quickTask());

            // Wait with timeout
            scope.joinUntil(java.time.Instant.now().plus(Duration.ofSeconds(3)));
            scope.throwIfFailed();

            System.out.println("Task 1: " + task1.get());
            System.out.println("Task 2: " + task2.get());

        } catch (InterruptedException e) {
            System.out.println("Tasks timed out or were interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Task execution failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Example 4: Error handling with partial results
     * Shows how to handle scenarios where some tasks might fail.
     */
    public void errorHandlingExample() {
        System.out.println("\n=== Error Handling Example ===");

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> task1 = scope.fork(() -> successfulTask("Task-1"));
            Subtask<String> task2 = scope.fork(() -> failingTask());
            Subtask<String> task3 = scope.fork(() -> successfulTask("Task-3"));

            scope.join();

            // Check individual task states
            System.out.println("Task 1 state: " + task1.state());
            System.out.println("Task 2 state: " + task2.state());
            System.out.println("Task 3 state: " + task3.state());

            // This will throw because task2 failed
            scope.throwIfFailed();

        } catch (InterruptedException e) {
            System.out.println("Tasks were interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.out.println("Expected failure caught: " + e.getMessage());
        }
    }

    // Simulated service methods
    private String fetchDataFromService1() throws InterruptedException {
        Thread.sleep(100);
        System.out.println("  Service 1 completed on: " + Thread.currentThread().getName());
        return "Data-1";
    }

    private String fetchDataFromService2() throws InterruptedException {
        Thread.sleep(150);
        System.out.println("  Service 2 completed on: " + Thread.currentThread().getName());
        return "Data-2";
    }

    private String fetchDataFromService3() throws InterruptedException {
        Thread.sleep(120);
        System.out.println("  Service 3 completed on: " + Thread.currentThread().getName());
        return "Data-3";
    }

    private String fetchFromSlowService() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("  Slow service completed");
        return "Slow-Result";
    }

    private String fetchFromFastService() throws InterruptedException {
        Thread.sleep(100);
        System.out.println("  Fast service completed");
        return "Fast-Result";
    }

    private String fetchFromMediumService() throws InterruptedException {
        Thread.sleep(500);
        System.out.println("  Medium service completed");
        return "Medium-Result";
    }

    private String longRunningTask() throws InterruptedException {
        Thread.sleep(5000);
        return "Long-running-result";
    }

    private String quickTask() throws InterruptedException {
        Thread.sleep(500);
        return "Quick-result";
    }

    private String successfulTask(String name) throws InterruptedException {
        Thread.sleep(100);
        System.out.println("  " + name + " completed successfully");
        return name + "-result";
    }

    private String failingTask() throws Exception {
        Thread.sleep(50);
        System.out.println("  Failing task about to throw exception");
        throw new RuntimeException("Simulated failure in task");
    }

    /**
     * Main method to run all examples
     */
    public static void main(String[] args) {
        StructuredConcurrencyExample example = new StructuredConcurrencyExample();

        example.basicExample();
        example.shutdownOnSuccessExample();
        example.timeoutExample();
        example.errorHandlingExample();

        System.out.println("\n=== All examples completed ===");
    }
}
