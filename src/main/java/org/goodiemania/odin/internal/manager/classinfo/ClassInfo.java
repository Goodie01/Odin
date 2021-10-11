package org.goodiemania.odin.internal.manager.classinfo;

import java.beans.PropertyDescriptor;
import java.util.List;

public class ClassInfo<T> {
    private final Class<T> rawClass;
    private final String tableName;
    private final String searchTableName;
    private final ClassPropertyInfo idField;
    private final List<ClassPropertyInfo> indexedFields;

    public ClassInfo(
            final Class<T> rawClass,
            final String tableName,
            final String searchTableName,
            final ClassPropertyInfo idField,
            final List<ClassPropertyInfo> indexedFields) {
        this.rawClass = rawClass;
        this.tableName = tableName;
        this.searchTableName = searchTableName;
        this.idField = idField;
        this.indexedFields = indexedFields;
    }

    public Class<T> getRawClass() {
        return rawClass;
    }

    public ClassPropertyInfo getIdField() {
        return idField;
    }

    public List<ClassPropertyInfo> getIndexedFields() {
        return indexedFields;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSearchTableName() {
        return searchTableName;
    }
}
