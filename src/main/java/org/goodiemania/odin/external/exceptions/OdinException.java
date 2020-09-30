package org.goodiemania.odin.external.exceptions;

public class OdinException extends RuntimeException {
    public OdinException() {
        super();
    }

    public OdinException(final String message) {
        super(message);
    }

    public OdinException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public OdinException(final Throwable cause) {
        super(cause);
    }
}
