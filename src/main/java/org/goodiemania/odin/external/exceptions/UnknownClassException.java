package org.goodiemania.odin.external.exceptions;

public class UnknownClassException extends OdinException {
    private Class<? extends Object> entityClass;

    public UnknownClassException(final String s) {
        super(s);
    }

    public UnknownClassException(final Class<? extends Object> entityClass, final String s) {
        super(s);
        this.entityClass = entityClass;
    }

    public Class<? extends Object> getEntityClass() {
        return entityClass;
    }
}
