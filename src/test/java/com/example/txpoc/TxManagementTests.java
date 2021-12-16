package com.example.txpoc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
public class TxManagementTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SampleService svc;

    /**
     * See output logs in console for processing details. Note how
     * {@link TransactionalContextCreatedEvent} event is processed only after transaction has
     * been committed, even though in the {@link TransactionalContextManager} code it
     * is published way ahead.
     */
    @Test
    public void shouldHandleSuccessfulTx() {
        Assertions.assertDoesNotThrow(() -> svc.doInTransaction(false));
    }

    /**
     * See output logs in console for processing details. Note that the
     * transaction is rolled back and the transactional context is discarded.
     * Also, there is no message publishing in this case.
     */
    @Test
    public void shouldHandleFailedTx() {
        Assertions.assertThrows(RuntimeException.class, () -> svc.doInTransaction(true));
    }

}
