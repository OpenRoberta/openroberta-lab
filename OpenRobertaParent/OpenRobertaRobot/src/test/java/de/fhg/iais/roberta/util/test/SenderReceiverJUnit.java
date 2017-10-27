package de.fhg.iais.roberta.util.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Assert;
import org.slf4j.LoggerFactory;

public class SenderReceiverJUnit {
    final AtomicBoolean wasTheReceiverSuccessful = new AtomicBoolean(false);
    final AtomicBoolean wasTheSenderSuccessful = new AtomicBoolean(false);
    final AtomicBoolean isTheReceiverWaiting = new AtomicBoolean(false);

    public SenderReceiverJUnit() {
    }

    public void run(final ThreadedFunction receiver, final ThreadedFunction sender) throws Exception {
        Runnable theReceiverThread = new Runnable() {
            @Override
            public void run() {
                try {
                    SenderReceiverJUnit.this.isTheReceiverWaiting.set(true);
                    boolean result = receiver.apply();
                    SenderReceiverJUnit.this.wasTheReceiverSuccessful.set(result);
                } catch ( Exception e ) {
                    SenderReceiverJUnit.this.wasTheReceiverSuccessful.set(false);
                    LoggerFactory.getLogger("theReceiverThread").error("Exception", e);
                }
            }
        };
        Runnable theSenderThread = new Runnable() {
            @Override
            public void run() {
                try {
                    int numberOfSleeps = 0;
                    Thread.sleep(100);
                    boolean receiverWaiting = SenderReceiverJUnit.this.isTheReceiverWaiting.get();
                    while ( !receiverWaiting && numberOfSleeps < 1000 ) {
                        numberOfSleeps++;
                        Thread.sleep(1);
                        receiverWaiting = SenderReceiverJUnit.this.isTheReceiverWaiting.get();
                    }
                    if ( numberOfSleeps > 0 ) {
                        LoggerFactory.getLogger("theServerThread").info("ExecutorService forced " + numberOfSleeps + " 1msec sleep");
                    }
                    boolean result = sender.apply();
                    SenderReceiverJUnit.this.wasTheSenderSuccessful.set(result);
                } catch ( Throwable e ) {
                    SenderReceiverJUnit.this.wasTheSenderSuccessful.set(false);
                    LoggerFactory.getLogger("theSenderThread").error("Exception", e);
                }
            }
        };
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<?> receiverFuture = executorService.submit(theReceiverThread);
        Future<?> senderFuture = executorService.submit(theSenderThread);
        try {
            receiverFuture.get(1000000, TimeUnit.MILLISECONDS);
        } catch ( TimeoutException e ) {
            Assert.fail("Timeout of receiver");
        }
        senderFuture.get();
        Assert.assertTrue("Receiver finished with error", this.wasTheReceiverSuccessful.get());
        Assert.assertTrue("Sender finished with error", this.wasTheSenderSuccessful.get());
    }
}
