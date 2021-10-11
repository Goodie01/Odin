package org.goodiemania.odin.internal.manager.search;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.goodiemania.odin.external.exceptions.EntityThrewExceptionException;
import org.goodiemania.odin.external.exceptions.ShouldNeverHappenException;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.goodiemania.odin.internal.manager.classinfo.ClassPropertyInfo;

public class SearchFieldGenerator {
    private final ClassInfoHolder classInfoHolder;

    public SearchFieldGenerator(final ClassInfoHolder classInfoHolder) {
        this.classInfoHolder = classInfoHolder;
    }

    public List<SearchField> generate(final Object object) {
        ClassInfo<?> classInfo = classInfoHolder.retrieve(object.getClass()).orElseThrow();

        return classInfo
                .getIndexedFields()
                .stream()
                .flatMap(propertyDescriptor -> {
                    try {
                        Object fieldObject = propertyDescriptor.readMethod().invoke(object);
                        if (fieldObject == null) {
                            return generateNullField(propertyDescriptor);
                        } else if (fieldObject instanceof Map) {
                            return generateMapField((Map<?, ?>) fieldObject, propertyDescriptor);
                        } else if (fieldObject instanceof Collection) {
                            return generateCollectionField((Collection<?>) fieldObject, propertyDescriptor);
                        } else {
                            return generateBasicField(fieldObject, propertyDescriptor);
                        }
                    } catch (IllegalAccessException e) {
                        throw new ShouldNeverHappenException(e);
                    } catch (InvocationTargetException e) {
                        throw new EntityThrewExceptionException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Stream<SearchField> generateNullField(final ClassPropertyInfo propertyDescriptor) {
        return Stream.of(new SearchField(propertyDescriptor.name(), ""));
    }

    private Stream<SearchField> generateBasicField(
            final Object object,
            final ClassPropertyInfo propertyDescriptor) {
        String fieldName = propertyDescriptor.name();
        String fieldValue = object.toString();
        return Stream.of(new SearchField(fieldName, fieldValue));
    }

    private Stream<SearchField> generateCollectionField(
            final Collection<?> list,
            final ClassPropertyInfo propertyDescriptor) {
        final String fieldName = propertyDescriptor.name();

        return list.stream()
                .map(o -> new SearchField(fieldName, o.toString()));
    }

    private Stream<SearchField> generateMapField(
            final Map<?, ?> map,
            final ClassPropertyInfo propertyDescriptor) {
        final String fieldName = propertyDescriptor.name();

        return map.entrySet()
                .stream()
                .flatMap(entry -> {
                    final String mapFieldName = String.format("%s_%s", fieldName, entry.getKey().toString());
                    final Object value = entry.getValue();
                    if (value instanceof Collection) {
                        return ((Collection<?>) value).stream()
                                .map(Object::toString)
                                .map(listMapValue -> new SearchField(mapFieldName, listMapValue));
                    } else {
                        final String mapFieldValue = value.toString();
                        return Stream.of(new SearchField(mapFieldName, mapFieldValue));
                    }
                });
    }
}
