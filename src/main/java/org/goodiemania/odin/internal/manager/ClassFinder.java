package org.goodiemania.odin.internal.manager;

import java.util.Set;
import org.goodiemania.odin.external.annotations.Entity;
import org.reflections.Reflections;

public class ClassFinder {
    public ClassFinder() {
    }

    public Set<Class<?>> find(final String packageName) {
        final Reflections reflections = new Reflections(packageName);

        return reflections.getTypesAnnotatedWith(Entity.class);
    }
}
