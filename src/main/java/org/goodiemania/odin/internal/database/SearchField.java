package org.goodiemania.odin.internal.database;

public class SearchField {
    private final String fieldName;
    private final String fieldValue;

    public SearchField(final String fieldName, final String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
