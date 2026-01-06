package com.williams;

import java.util.concurrent.Executors;

public class VirtualThreadService {


    public void run() {

        var executor = Executors.newVirtualThreadPerTaskExecutor();

        System.out.println("Starting by " + Thread.currentThread().getName());

        for (int i = 0; i < 1_000; i++) {
            int requestId = i;
            executor.submit(
                    () -> handleRequestId(requestId)
            );
        }

        executor.close();

        System.out.println("Completed by " + Thread.currentThread().getName());


    }

    private void handleRequestId(int requestId) {
        try {
            Thread.sleep(100);
            System.out.println("Processed requestId " + requestId + "by Thread:" + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            System.out.println("Someone woked me up");
        }
    }

}
