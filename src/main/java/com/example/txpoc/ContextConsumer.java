package com.example.txpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * Sample service that represents business logic executed within
 * parent transaction scope and leveraging data from the context
 */
@Service
public class ContextConsumer {
    private static final Logger log = LoggerFactory.getLogger(ContextConsumer.class);

    private final TransactionalContextManager ctxManager;

    public ContextConsumer(TransactionalContextManager ctxManager) {
        this.ctxManager = ctxManager;
    }

    /**
     * This method can rely on the fact that all objects pinned in the current
     * transaction are visible
     */
    public void doSomethingWithContext() {
        log.info("Retrieving data with some-id from my-tx-context: {}",
                ctxManager.getPinned("my-tx-context", "some-id"));
    }

}
