package org.goodiemania.odin.external.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;

public class EntityWritingException extends OdinException{
    public EntityWritingException(JsonProcessingException e) {
        super(e);
    }
}
