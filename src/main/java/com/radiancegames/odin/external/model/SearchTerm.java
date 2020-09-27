package com.radiancegames.odin.external.model;

public class SearchTerm {
    private final String fieldName;
    private final String fieldValue;

    private SearchTerm(final String fieldName, final String fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public static SearchTerm of(final String fieldName, final String fieldValue) {
        return new SearchTerm(fieldName, fieldValue);
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}
