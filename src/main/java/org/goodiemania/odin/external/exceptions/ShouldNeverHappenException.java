package org.goodiemania.odin.external.exceptions;

public class ShouldNeverHappenException extends OdinException{
    public ShouldNeverHappenException() {
        super();
    }

    public ShouldNeverHappenException(final String message) {
        super(message);
    }

    public ShouldNeverHappenException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ShouldNeverHappenException(final Throwable cause) {
        super(cause);
    }
}
