package com.example.txpoc;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Demo of a context management abstraction that takes care of storing
 * objects in caches local to the scope of the existing database transaction
 * and flushing or cleaning up these caches upon transaction completion.
 * <p>
 * It utilizes Spring application event publishing mechanism to notify about
 * important processing steps. This allows decoupled listeners to take actions
 * upon completion of the transaction.
 *
 * @see ApplicationEventPublisher
 * @see TransactionSynchronizationManager
 */
@Service
public class TransactionalContextManager implements ContextManager {

    private static final Logger log = LoggerFactory.getLogger(TransactionalContextManager.class);

    private final ApplicationEventPublisher eventPublisher;

    public TransactionalContextManager(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void pin(String ctxId, Object key, Object value) {
        Map<Object, Object> context = ensureContext(ctxId);

        log.info("Put object with key {} to context {}", key, ctxId);
        context.put(key, value);
    }

    @Override
    public Object getPinned(String ctxId, Object key) {
        return getContext(ctxId)
                .map(ctx -> ctx.get(key))
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private Optional<Map<Object, Object>> getContext(String ctxId) {
        return Optional.ofNullable(TransactionSynchronizationManager.getResource(ctxId))
                .map(Map.class::cast);
    }

    private Map<Object, Object> ensureContext(String ctxId) {
        Map<Object, Object> context;

        if (!TransactionSynchronizationManager.hasResource(ctxId)) {
            log.info("No context with id {} found. Creating new one", ctxId);
            context = new ConcurrentHashMap<>();
            TransactionSynchronizationManager.bindResource(ctxId, context);
            eventPublisher.publishEvent(new TransactionalContextCreatedEvent(ctxId, context));
        } else {
            context = (Map<Object, Object>) TransactionSynchronizationManager.getResource(ctxId);
        }

        return context;
    }

    /**
     * Will be called by Spring only after the transaction in which this
     * event was generated has been successfully committed. It is used
     * here to commit the transactional context.
     * <p>
     * Note that the AFTER_COMMIT phase is a default one for the annotation
     * and is specified here only for additional clarity.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void afterCommitWithContext(TransactionalContextCreatedEvent event) {
        log.info("Committing context {}", event.getCtxId());
    }

    /**
     * Will be called by Spring only after the transaction in which this
     * event was generated has been rolled back. It is used here to
     * discard the transactional context, assuming the failure scenario.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void afterRollbackWithContext(TransactionalContextCreatedEvent event) {
        log.info("Discarding context {}", event.getCtxId());
    }

}
