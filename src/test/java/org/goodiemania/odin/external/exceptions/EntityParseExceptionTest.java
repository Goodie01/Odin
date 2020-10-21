package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class EntityParseExceptionTest {
    @Test
    void creationTest() {
        IOException ioException = new IOException();
        EntityParseException entityParseException = new EntityParseException(ioException);

        assertEquals(entityParseException.getCause(), ioException);
    }
}