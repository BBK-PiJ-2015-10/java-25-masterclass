package com.williams;

public class VirtualThreadLocalExample {

    private ThreadLocal<String> userContext = ThreadLocal.withInitial(() -> "DefaultUser");

    public void run() {

        System.out.println("Starting");

        Runnable task = () -> {
            userContext.set("User-" + Thread.currentThread().getId());
            System.out.println("Thread ID: " + Thread.currentThread().getId() + " UserContext: " + userContext.get());
            userContext.remove();
        };

        for (int i = 0; i < 10; i++) {
            Thread.ofVirtual().start(task);
        }

    }


}
