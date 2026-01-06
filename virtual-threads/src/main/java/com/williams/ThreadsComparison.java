package com.williams;

public class ThreadsComparison {

    public void runThreads() {

        Thread platformThread = new Thread(() ->
                //Thread.sleep(1000)
                System.out.println("Platform thread running: " + Thread.currentThread().getName())
        );
        platformThread.start();

        Thread virtualThread = Thread.ofVirtual().start(() ->
                System.out.println("Virtual thread running: " + Thread.currentThread().getName())
        );

    }

}
