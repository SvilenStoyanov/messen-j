package com.svistoyanov.mj.jms;

import com.svistoyanov.mj.api.dto.AbstractDto;
import com.svistoyanov.mj.api.dto.ErrorDto;
import com.svistoyanov.mj.api.exception.MessengerException;
import com.svistoyanov.mj.api.utils.DeserializationProcessor;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ResponseListener implements MessageListener {
    private static final Logger                         logger     = LoggerFactory.getLogger(ResponseListener.class);
    private final        Map<String, CompletableFuture> futureById = new HashMap<>();

    public void attachCompletableFuture(String correlationId, CompletableFuture future) {
        futureById.put(correlationId, future);
    }

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Recieved: {}", message.getJMSCorrelationID());

            if (message instanceof TextMessage textMessage) {
                //1.get completableFuture and 2.remove it from the map before 3.complete it
                CompletableFuture completableFuture = futureById.remove(message.getJMSCorrelationID());

                if (completableFuture == null) {
                    throw new IllegalArgumentException(message.getJMSCorrelationID());
                }

                AbstractDto responseDto = DeserializationProcessor.instance.deserialize(textMessage.getText());

                //complete the future
                if (responseDto instanceof ErrorDto errorDto) {
                    logger.info("{{{{{{ 8 }}}}}}}");
                    completableFuture.completeExceptionally(new MessengerException(errorDto));
                    logger.info("{{{{{{ 11 }}}}}}}");
                }
                else {
                    logger.info("{{{{{{ 9 }}}}}}}");
                    completableFuture.complete(responseDto);
                    logger.info("{{{{{{ 10 }}}}}}}");
                }

            }
            else {
                throw new JMSException("Message is not the correct TextMessage format.");
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
