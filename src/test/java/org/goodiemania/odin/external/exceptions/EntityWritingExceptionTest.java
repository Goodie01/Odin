package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class EntityWritingExceptionTest {

    @Test
    void creationTest() {
        IOException ioException = new IOException();
        EntityWritingException entityWritingException = new EntityWritingException(ioException);

        assertEquals(entityWritingException.getCause(), ioException);
    }
}