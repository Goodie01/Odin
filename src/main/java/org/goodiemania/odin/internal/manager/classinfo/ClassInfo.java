package org.goodiemania.odin.internal.manager.classinfo;

import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Objects;

public class ClassInfo<T> {
    private final Class<T> rawClass;
    private final String tableName;
    private final String searchTableName;
    private final PropertyDescriptor idField;
    private final List<PropertyDescriptor> indexedFields;

    public ClassInfo(
            final Class<T> rawClass,
            final String tableName,
            final String searchTableName,
            final PropertyDescriptor idField,
            final List<PropertyDescriptor> indexedFields) {
        this.rawClass = rawClass;
        this.tableName = tableName;
        this.searchTableName = searchTableName;
        this.idField = idField;
        this.indexedFields = indexedFields;
    }

    public Class<T> getRawClass() {
        return rawClass;
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
