package com.radiancegames.odin.internal.manager;

import java.beans.PropertyDescriptor;
import java.util.List;

public class ClassInfo<T> {
    private final Class<T> classInformation;
    private final String tableName;
    private final String searchTableName;
    private final PropertyDescriptor idField;
    private final List<PropertyDescriptor> indexedFields;

    public ClassInfo(
            final Class<T> classInformation,
            final String tableName,
            final String searchTableName,
            final PropertyDescriptor idField,
            final List<PropertyDescriptor> indexedFields) {
        this.classInformation = classInformation;
        this.tableName = tableName;
        this.searchTableName = searchTableName;
        this.idField = idField;
        this.indexedFields = indexedFields;
    }

    public Class<T> getClassInformation() {
        return classInformation;
    }

    public PropertyDescriptor getIdField() {
        return idField;
    }

    public List<PropertyDescriptor> getIndexedFields() {
        return indexedFields;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSearchTableName() {
        return searchTableName;
    }
}
