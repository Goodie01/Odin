package org.goodiemania.odin.external.exceptions;

public class EntityException extends OdinException {
    private final Class<?> classInformation;

    public EntityException(final Class<?> classInformation, final String s) {
        super(s);
        this.classInformation = classInformation;
    }

    public Class<Object> getEntityClass() {
        return (Class<Object>) classInformation;
    }
}
