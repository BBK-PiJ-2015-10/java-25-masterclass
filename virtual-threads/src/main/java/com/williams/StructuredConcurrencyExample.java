package com.williams;

import java.util.concurrent.StructuredTaskScope;
//import java.util.concurrent.ExecutionException;

public class StructuredConcurrencyExample {


    private String fetchDataFromServiceA() throws InterruptedException {
        Thread.sleep(1000);
        return "dataA";
    }

    private String fetchDataFromServiceB() throws InterruptedException {
        Thread.sleep(1000);
        return "dataB";
    }

    public void run() {

        //var cat = StructuredTaskScope.Joiner.allSuccessfulOrThrow();

        try (var scope = StructuredTaskScope.open()) {
            var future1 = scope.fork(() -> fetchDataFromServiceA());
            var future2 = scope.fork(() -> fetchDataFromServiceB());
            scope.join();
            var result1 = future1.get();
            var result2 = future2.get();
            System.out.println("Service A: " + result1 + " Service B: " + result2);
        } catch (Exception e) {

        }

    }
}
