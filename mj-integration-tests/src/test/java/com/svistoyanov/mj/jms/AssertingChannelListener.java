package com.svistoyanov.mj.jms;

import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.exception.MessengerException;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AssertingChannelListener<T> {
    protected static final Logger logger = LoggerFactory.getLogger(AssertingChannelListener.class);

    private   T        response;
    protected ErrorDto error;

    public T getResponse() {
        return response;
    }

    public void waitForSuccess(CompletableFuture<T> completableFuture, long timeout) {
        waitForResponse(completableFuture, timeout, TimeUnit.SECONDS);
        printError();
        Assertions.assertNull(error);  // We should not have an error
    }

    public ErrorDto waitForError(CompletableFuture<T> completableFuture, long timeout) {
        waitForResponse(completableFuture, timeout, TimeUnit.SECONDS);
        Assertions.assertNotNull(error);
        return error;
    }

    public void waitForResponse(CompletableFuture<T> completableFuture, long timeout, TimeUnit timeUnit) {
        logger.info("Waiting for response for listener");
        final CountDownLatch latch = new CountDownLatch(1);

        logger.info("{{{{{{ 1 }}}}}}}");
        completableFuture.whenComplete((result, throwable) -> {
                            logger.info("{{{{{{ 2 }}}}}}}");
                            if (throwable == null) {
                                this.response = result;
                            }
                            else if (throwable instanceof MessengerException fpException) {
                                this.error = fpException.getErrorDto();
                            }
                            else {
                                Assertions.fail("No response received for listener");
                            }
                            logger.info("{{{{{{ 3 }}}}}}}");
                            latch.countDown();
                            logger.info("{{{{{{ 4 }}}}}}}");
                        }
                )
                .orTimeout(timeout, timeUnit);
        logger.info("{{{{{{ 5 }}}}}}}");
        try {
            logger.info("{{{{{{ 6 }}}}}}}");
            latch.await(timeout, timeUnit);
            logger.info("{{{{{{ 7 }}}}}}}");
        }
        catch (InterruptedException e) {
            Assertions.fail("Interrupted exception: " + e.getMessage());
        }
    }

    private void printError() {
        if (error == null) {
            return;
        }

        logger.error("ERRORS: {}", error.getMessages());
    }
}

