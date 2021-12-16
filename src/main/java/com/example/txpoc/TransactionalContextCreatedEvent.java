package com.example.txpoc;

import java.util.Map;

/**
 * Represents event of new transactional context creation
 */
public final class TransactionalContextCreatedEvent {
    private final String ctxId;
    private final Map<Object, Object> ctx;

    public TransactionalContextCreatedEvent(String ctxId, Map<Object, Object> ctx) {
        this.ctxId = ctxId;
        this.ctx = ctx;
    }

    String getCtxId() {
        return ctxId;
    }

    Map<Object, Object> getCtx() {
        return ctx;
    }
}
