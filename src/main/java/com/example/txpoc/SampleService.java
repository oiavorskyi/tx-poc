package com.example.txpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SampleService {

    private static final Logger log = LoggerFactory.getLogger(SampleService.class);

    private final ContextConsumer consumer;
    private final ContextManager ctxManager;

    public SampleService(ContextConsumer consumer, ContextManager ctxManager) {
        this.consumer = consumer;
        this.ctxManager = ctxManager;
    }

    /**
     * Method will be executed within existing transaction or Spring
     * will start a new transaction if none exists.
     * <p>
     * It demonstrates decoupled approach to transactional context management
     * where business logic doesn't need to be mixed with context management and
     * transaction state tracking.
     * <p>
     * <p>
     * See {@link Transactional} documentation for configuration
     * parameter details.
     *
     * @param shouldFail true when method should emulate an error within
     *                   the transaction
     */
    @Transactional
    public void doInTransaction(boolean shouldFail) {
        log.info("STARTING BUSINESS TRANSACTION");
        String importantData = "whaterver";

        ctxManager.pin("my-tx-context", "some-id", importantData);

        consumer.doSomethingWithContext();

        try {
            if (shouldFail) {
                throw new RuntimeException("OOPS");
            }
        } finally {
            log.info("ENDING BUSINESS TRANSACTION");
        }
    }

}
