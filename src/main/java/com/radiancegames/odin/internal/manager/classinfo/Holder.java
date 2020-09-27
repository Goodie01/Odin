package com.radiancegames.odin.internal.manager.classinfo;

import com.radiancegames.odin.internal.manager.ClassInfo;
import java.util.Optional;
import java.util.Set;

public class Holder {
    private final Set<ClassInfo<?>> entityClasses;

    public Holder(final Set<ClassInfo<?>> entityClasses) {
        this.entityClasses = entityClasses;
    }

    public <T> Optional<ClassInfo<T>> find(final Class<T> entityClass) {
        ClassInfo<T> foundClassInfo = null;
        for (ClassInfo<?> classInfo : entityClasses) {
            if (classInfo.getClassInformation().isAssignableFrom(entityClass)) {
                foundClassInfo = (ClassInfo<T>) classInfo;
                break;
            }
        }

        if (foundClassInfo == null) {
            return Optional.empty();
        }

        return Optional.of(foundClassInfo);
    }
}
