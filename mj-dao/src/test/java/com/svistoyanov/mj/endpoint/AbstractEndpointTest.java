package com.svistoyanov.mj.endpoint;

import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.PersistenceTestHelper;
import com.svistoyanov.mj.api.dto.ErrorCodesDto;
import com.svistoyanov.mj.api.exception.MessengerException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractEndpointTest<FutureType, ExpectedType, ActualType>
{
    protected static final Logger logger = LoggerFactory.getLogger(AbstractEndpointTest.class);

    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    protected final DaoProviderFactory daoProviderFactory = PersistenceTestHelper.getDaoProviderFactory();

    @AfterEach
    public void tearDown() {
        PersistenceTestHelper.resetAllTables();
    }

    protected List<String> waitForBadRequest(CompletableFuture<FutureType> cf, ErrorCodesDto expectedErrorCode)
    {
        MessengerException exception = (MessengerException) waitForError(cf);
        Assertions.assertEquals(MessengerException.class, exception.getClass());
        Assertions.assertEquals(expectedErrorCode, exception.getErrorDto().getErrorCode());
        return exception.getErrorDto().getMessages();
    }

    protected Throwable waitForError(CompletableFuture<FutureType> cf)
    {
        Object response = waitForResponse(cf);
        Assertions.assertNotNull(response);
        Assertions.assertTrue(response instanceof Throwable);
        return (Throwable) response;
    }

    protected FutureType waitForSuccess(CompletableFuture<FutureType> cf)
    {
        Object response = waitForResponse(cf);
        Assertions.assertFalse(response instanceof Throwable);
        return (FutureType) response;
    }

    protected Object waitForResponse(CompletableFuture<FutureType> cf)
    {
        CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<Object> resultHolder = new AtomicReference<>();
        cf.whenComplete((FutureType result, Throwable throwable) ->
        {
            if (throwable != null) {
                resultHolder.set(throwable);
            } else {
                resultHolder.set(result);
            }
            latch.countDown();
        });
        try
        {
            latch.await(2, TimeUnit.SECONDS);

        } catch (InterruptedException ie)
        {
            logger.debug("Interrupted exception");
        }

        return resultHolder.get();
    }

    protected abstract void assertDeepEquals(ExpectedType expected, ActualType actual);

}
