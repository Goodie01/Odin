package org.goodiemania.odin.internal.manager.classinfo;

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

        List<ClassPropertyInfo> classPropertyInfos;
        if (rawClass.isRecord()) {
            classPropertyInfos = Arrays.stream(rawClass.getRecordComponents())
                    .map(recordComponent -> new ClassPropertyInfo(
                            recordComponent.getName(),
                            recordComponent.getAccessor(),
                            recordComponent.getDeclaredAnnotation(Id.class) != null,
                            recordComponent.getDeclaredAnnotation(Index.class) != null))
                    .toList();
        } else {
            classPropertyInfos = Arrays.stream(Introspector.getBeanInfo(rawClass).getPropertyDescriptors())
                    .map(propertyDescriptor -> new ClassPropertyInfo(
                            propertyDescriptor.getName(),
                            propertyDescriptor.getReadMethod(),
                            isAnnotationPresent(rawClass, propertyDescriptor, Id.class),
                            isAnnotationPresent(rawClass, propertyDescriptor, Index.class)
                    ))
                    .toList();
        }
        ClassPropertyInfo idField = findIdField(rawClass, classPropertyInfos);
        List<ClassPropertyInfo> indexedFields = findIndexFields(classPropertyInfos);

        return new ClassInfo<>(rawClass, tableName, searchTableName, idField, indexedFields);
    }

    private List<ClassPropertyInfo> findIndexFields(final List<ClassPropertyInfo> classPropertyInfos) {
        return classPropertyInfos.stream().filter(ClassPropertyInfo::isIndexField).toList();
    }

    private ClassPropertyInfo findIdField(final Class<?> classInformation, final List<ClassPropertyInfo> classProperties) {
        List<ClassPropertyInfo> foundIdFields = classProperties.stream().filter(ClassPropertyInfo::isIdField).toList();

        if (foundIdFields.size() != 1) {
            throw new EntityException(
                    classInformation,
                    "Require exactly one, and only one, field annotated with @Id, on attached class");
        }

        return foundIdFields.get(0);
    }

    private <T, A extends Annotation> boolean isAnnotationPresent(final Class<T> rawClass,
            PropertyDescriptor propertyDescriptor,
            final Class<A> idClass) {
        if (StringUtils.equals(propertyDescriptor.getName(), "class")) {
            return false;
        }

        try {
            return rawClass.getDeclaredField(propertyDescriptor.getName()).isAnnotationPresent(idClass);
        } catch (NoSuchFieldException e) {
            throw new ShouldNeverHappenException(e);
        }
    }
}
