package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class ShouldNeverHappenExceptionTest {
    @Test
    void creationTest() {
        IOException ioException = new IOException();
        ShouldNeverHappenException entityWritingException = new ShouldNeverHappenException(ioException);

        assertEquals(entityWritingException.getCause(), ioException);
    }
}