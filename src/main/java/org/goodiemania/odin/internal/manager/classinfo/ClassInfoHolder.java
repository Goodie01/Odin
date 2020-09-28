package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import java.util.Set;

public class ClassInfoHolder {
    private final Set<ClassInfo<?>> classInformation;

    public ClassInfoHolder(final Set<ClassInfo<?>> classInformation) {
        this.classInformation = classInformation;
    }

    public <T> Optional<ClassInfo<T>> find(final Class<T> entityClass) {
        for (ClassInfo<?> potentialClassInfo : classInformation) {
            if (potentialClassInfo.getRawClass().isAssignableFrom(entityClass)) {
                return Optional.of((ClassInfo<T>) potentialClassInfo);
            }
        }

        return Optional.empty();
    }
}
