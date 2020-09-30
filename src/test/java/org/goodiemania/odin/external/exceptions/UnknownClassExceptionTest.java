package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnknownClassExceptionTest {
    @Test
    void creationTest() {
        UnknownClassException unknownClassException = new UnknownClassException(UnknownClassExceptionTest.class, "Hello there");

        assertEquals(unknownClassException.getEntityClass(), UnknownClassExceptionTest.class);
        assertEquals(unknownClassException.getMessage(), "Hello there");
    }
}