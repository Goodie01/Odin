package org.goodiemania.odin.external.exceptions;

import java.io.IOException;

public class EntityWritingException extends OdinException {
    public EntityWritingException(IOException e) {
        super(e);
    }
}
