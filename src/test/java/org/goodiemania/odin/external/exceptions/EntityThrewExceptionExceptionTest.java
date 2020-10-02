package org.goodiemania.odin.external.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.Test;

class EntityThrewExceptionExceptionTest {
    @Test
    void creationTest() {
        IOException ioException = new IOException();
        final InvocationTargetException invocationException = new InvocationTargetException(ioException);
        EntityThrewExceptionException entityWritingException = new EntityThrewExceptionException(invocationException);

        assertEquals(entityWritingException.getCause(), invocationException);
        assertEquals(entityWritingException.getCause().getCause(), ioException);
    }

}