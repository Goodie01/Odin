package org.goodiemania.odin.internal.manager.search;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;

public class SearchFieldGenerator {
    private final ClassInfoHolder entityClasses;

    public SearchFieldGenerator(final ClassInfoHolder entityClasses) {
        this.entityClasses = entityClasses;
    }

    public List<SearchField> generate(final Object object) {
        ClassInfo<?> classInfo = entityClasses.find(object.getClass()).orElseThrow();

        return classInfo
                .getIndexedFields()
                .stream()
                .flatMap(propertyDescriptor -> {
                    try {
                        Object fieldObject = propertyDescriptor.getReadMethod().invoke(object);
                        if (fieldObject instanceof Map) {
                            return generateMapField((Map<?, ?>) fieldObject, propertyDescriptor, classInfo);
                        } else if (fieldObject instanceof Collection) {
                            return generateListField((Collection<?>) fieldObject, propertyDescriptor, classInfo);
                        } else {
                            return generateBasicField(fieldObject, propertyDescriptor, classInfo);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new IllegalStateException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Stream<SearchField> generateBasicField(
            final Object object,
            final PropertyDescriptor propertyDescriptor,
            final ClassInfo<?> classInfo) {
        String fieldName = classInfo.getTableName() + "_" + propertyDescriptor.getName();
        String fieldValue = object.toString();
        return Stream.of(new SearchField(fieldName, fieldValue));
    }

    private Stream<SearchField> generateListField(
            final Collection<?> list,
            final PropertyDescriptor propertyDescriptor,
            final ClassInfo<?> classInfo) {
        final String fieldName = String.format("%s_%s", classInfo.getTableName(), propertyDescriptor.getName());

        return list.stream()
                .map(o -> {
                    final String fieldValue = o.toString();

                    return new SearchField(fieldName, fieldValue);
                });
    }

    private Stream<SearchField> generateMapField(
            final Map<?, ?> map,
            final PropertyDescriptor propertyDescriptor,
            final ClassInfo<?> classInfo) {
        final String fieldName = propertyDescriptor.getName();

        return map.entrySet()
                .stream()
                .map(entry -> {
                    final String mapFieldName = String.format("%s_%s_%s", classInfo.getTableName(), fieldName, entry.getKey().toString());
                    final String mapFieldValue = entry.getValue().toString();

                    return new SearchField(mapFieldName, mapFieldValue);
                });
    }
}
