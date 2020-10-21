package org.goodiemania.odin.external.exceptions;

import java.lang.reflect.InvocationTargetException;

public class EntityThrewExceptionException extends OdinException {
    public EntityThrewExceptionException(final InvocationTargetException e) {
        super(e);
    }
}
