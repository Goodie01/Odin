package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class InvalidArgumentsExceptionTest {

    @Test
    void creationTest() {
        InvalidArgumentsException entityWritingException = new InvalidArgumentsException("Hello there");

        assertEquals(entityWritingException.getMessage(), "Hello there");
    }

}