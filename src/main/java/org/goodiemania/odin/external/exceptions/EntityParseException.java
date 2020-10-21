package org.goodiemania.odin.external.exceptions;

import java.io.IOException;

public class EntityParseException extends OdinException {
    public EntityParseException(final IOException e) {
        super(e);
    }
}
