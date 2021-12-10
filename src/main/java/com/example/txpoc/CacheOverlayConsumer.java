package com.example.txpoc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

/**
 * Sample service that represents business logic executed within
 * parent transaction scope and leveraging the local cache overlay.
 */
@Service
public class CacheOverlayConsumer {
    private static final Logger log = LoggerFactory.getLogger(CacheOverlayConsumer.class);

    /**
     * This method can rely on the fact that all previous steps in the
     * parent transaction are visible, including population of the local
     * overlay.
     */
    public void doSomethingWithCacheOverlay() {
        log.info("USING CACHE OVERLAY");
    }

}
