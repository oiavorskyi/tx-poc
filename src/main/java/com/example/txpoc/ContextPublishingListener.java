package com.example.txpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Example of decoupled listener that leverages the same events published
 * by {@link TransactionalContextManager} in order to publish something to
 * a message queue.
 *
 * See {@link TransactionalContextManager} for additional details
 */
@Component
public class ContextPublishingListener {
    private static final Logger log = LoggerFactory.getLogger(ContextPublishingListener.class);

    @TransactionalEventListener
    public void publishWhenContextWasCommitted(TransactionalContextCreatedEvent event) {
        log.info("Publishing something to a message queue when the context {} has been committed", event.getCtxId());
    }
}
