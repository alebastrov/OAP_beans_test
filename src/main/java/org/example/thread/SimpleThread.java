package org.example.thread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleThread implements Runnable {
    @Override
    public void run() {
//        log.info("Simple thread started...");
        try {
            Thread.currentThread().sleep(100 );
            log.info("Simple thread finished");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted.");
        }

    }
}
