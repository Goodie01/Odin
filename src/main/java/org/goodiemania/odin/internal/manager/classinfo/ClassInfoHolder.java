package org.goodiemania.odin.internal.manager.classinfo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;
import org.goodiemania.odin.external.exceptions.EntityException;
import org.goodiemania.odin.external.exceptions.ShouldNeverHappenException;

public class ClassInfoHolder {
    private final Set<ClassInfo<?>> classInformation = new HashSet<>();

    public <T> Optional<ClassInfo<T>> retrieve(final Class<T> entityClass) {
        for (ClassInfo<?> potentialClassInfo : classInformation) {
            if (potentialClassInfo.getRawClass().isAssignableFrom(entityClass)) {
                return Optional.of((ClassInfo<T>) potentialClassInfo);
            }
        }

        return Optional.empty();
    }

    public Set<ClassInfo<Object>> retrieveAll() {
        return classInformation.stream()
                .map(classInfo -> (ClassInfo<Object>) classInfo)
                .collect(Collectors.toSet());
    }

    public <T> void add(final Class<T> rawClass) {
        try {
            final ClassInfo<?> classInfo = buildClassInfo(rawClass);
            classInformation.add(classInfo);
        } catch (IntrospectionException e) {
            throw new ShouldNeverHappenException(e);
        }

    }

    private <T> ClassInfo<T> buildClassInfo(final Class<T> rawClass) throws IntrospectionException {
        String tableName = rawClass.getSimpleName();
        String searchTableName = tableName + "___SearchTable";

        BeanInfo beanInfo = Introspector.getBeanInfo(rawClass);
        PropertyDescriptor idField = findIdField(rawClass, beanInfo);
        List<PropertyDescriptor> indexedFields = findIndexFields(rawClass, beanInfo);

        return new ClassInfo<>(rawClass, tableName, searchTableName, idField, indexedFields);
    }

    private <T> List<PropertyDescriptor> findIndexFields(final Class<T> rawClass, final BeanInfo beanInfo) {
        List<PropertyDescriptor> indexedFields;
        if (rawClass.isAnnotationPresent(Index.class)) {
            indexedFields = findAllFields(beanInfo);
        } else {
            indexedFields = findFieldsWithAnnotation(beanInfo, rawClass, Index.class);
        }
        return indexedFields;
    }


    private static PropertyDescriptor findIdField(
            final Class<?> classInformation,
            final BeanInfo beanInfo) {
        List<PropertyDescriptor> foundIdFields = findFieldsWithAnnotation(beanInfo, classInformation, Id.class);

        if (foundIdFields.size() != 1) {
            throw new EntityException(
                    classInformation,
                    "Require exactly one, and only one, field annotated with @Id, on attached class");
        }

        return foundIdFields.get(0);
    }

    private static List<PropertyDescriptor> findAllFields(final BeanInfo beanInfo) {
        return Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor ->
                        !StringUtils.equals("class", propertyDescriptor.getName()))
                .collect(Collectors.toList());
    }

    private static List<PropertyDescriptor> findFieldsWithAnnotation(
            final BeanInfo beanInfo,
            final Class<?> classInformation,
            final Class<? extends Annotation> annotationClass) {
        return findAllFields(beanInfo)
                .stream()
                .filter(propertyDescriptor -> {
                    try {
                        return classInformation.getDeclaredField(propertyDescriptor.getName())
                                .isAnnotationPresent(annotationClass);
                    } catch (NoSuchFieldException e) {
                        throw new ShouldNeverHappenException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
