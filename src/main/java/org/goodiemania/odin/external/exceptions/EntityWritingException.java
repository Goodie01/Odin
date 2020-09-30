package org.goodiemania.odin.external.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public class EntityWritingException extends OdinException {
    public EntityWritingException(IOException e) {
        super(e);
    }
}
