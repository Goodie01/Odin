package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InvalidArgumentsExceptionTest {

    @Test
    void creationTest() {
        InvalidArgumentsException entityWritingException = new InvalidArgumentsException("Hello there");

        assertEquals("Hello there", entityWritingException.getMessage());
    }

}