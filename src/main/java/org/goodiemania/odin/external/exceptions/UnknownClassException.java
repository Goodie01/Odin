package org.goodiemania.odin.external.exceptions;

public class UnknownClassException extends OdinException {
    private final Class<?> entityClass;

    public UnknownClassException(final Class<?> entityClass, final String s) {
        super(s);
        this.entityClass = entityClass;
    }

    public Class<? extends Object> getEntityClass() {
        return entityClass;
    }
}
