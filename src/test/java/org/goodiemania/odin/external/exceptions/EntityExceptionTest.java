package org.goodiemania.odin.external.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EntityExceptionTest {
    @Test
    void creationTest() {
        final EntityException entityException = new EntityException(EntityExceptionTest.class, "Test");

        Assertions.assertEquals(EntityExceptionTest.class, entityException.getEntityClass());
        Assertions.assertEquals("Test", entityException.getMessage());
    }
}