package com.example.txpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Example of decoupled listener that leverages the same events as the cache
 * overlay management listener.
 *
 * See {@link CacheOverlayListener} for additional details
 */
@Component
public class MessagePublishingListener {
    private static final Logger log = LoggerFactory.getLogger(MessagePublishingListener.class);

    @TransactionalEventListener
    public void publishWhenSomethingIsStored(SomethingStored event) {
        log.info("PUBLISHING SOMETHING TO MESSAGE QUEUE");
    }
}
