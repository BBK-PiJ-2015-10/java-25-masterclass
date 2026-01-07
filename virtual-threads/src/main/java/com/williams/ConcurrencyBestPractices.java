package com.williams;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyBestPractices {

    private AtomicInteger totalRequests = new AtomicInteger();

    public void run(){

        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

        Runnable task = () ->{
          int requestId = totalRequests.getAndIncrement();
          System.out.println("Processing request: "+requestId + " on thread: "+Thread.currentThread().getId());
          String response = "Response for request "+requestId;
          System.out.println(response);
        };

        for (int i=0; i < 500;i++){
            executorService.submit(task);
        }

        executorService.shutdown();

    }
}
