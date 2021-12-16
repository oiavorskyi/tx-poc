package com.example.txpoc;

/**
 * Example of the business-level abstraction that allows to manage
 * execution context that spans the entire transaction. Usage of this
 * interface decouples business logic from the low-level details of
 * context management and tracking transaction state changes.
 */
public interface ContextManager {

    /**
     * Put something to the context identified by ctxId parameter
     */
    void pin(String ctxId, Object key, Object value);

    /**
     * Obtain previously pinned object from the context. Returns null
     * when either context with the specified ctxId does not exist, or the
     * context does not contain object with the specified key
     */
    Object getPinned(String ctxId, Object key);

}
