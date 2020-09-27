package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import java.util.Set;
import org.goodiemania.odin.internal.manager.ClassInfo;

public class ClassInfoHolder {
    private final Set<ClassInfo<?>> classInformation;

    public ClassInfoHolder(final Set<ClassInfo<?>> classInformation) {
        this.classInformation = classInformation;
    }

    public <T> Optional<ClassInfo<T>> find(final Class<T> entityClass) {
        ClassInfo<T> foundClassInfo = null;
        for (ClassInfo<?> potentialClassInfo : classInformation) {
            if (potentialClassInfo.getRawClass().isAssignableFrom(entityClass)) {
                foundClassInfo = (ClassInfo<T>) potentialClassInfo;
                break;
            }
        }

        return Optional.ofNullable(foundClassInfo);
    }
}
