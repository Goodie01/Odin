package org.goodiemania.odin.internal.manager;

import java.util.function.Function;
import java.util.stream.Stream;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoBuilder;
import org.reflections.Reflections;

public class ClassManager {
    private ClassInfoBuilder classInfoBuilder;
    private DatabaseWrapper databaseWrapper;

    public ClassManager(final DatabaseWrapper databaseWrapper, final ClassInfoBuilder classInfoBuilder) {
        this.databaseWrapper = databaseWrapper;
        this.classInfoBuilder = classInfoBuilder;
    }

    public Stream<ClassInfo<?>> find(final String packageName) {
        final Reflections reflections = new Reflections(packageName);

        return reflections.getTypesAnnotatedWith(Entity.class)
                .stream()
                .map((Function<Class<?>, ClassInfo<?>>) classInfoBuilder::build)
                .peek(this::buildDatabase);
    }

    private <T> void buildDatabase(final ClassInfo<T> classInfo) {
        databaseWrapper.createEntityTable(classInfo);

        if (!classInfo.getIndexedFields().isEmpty()) {
            databaseWrapper.createEntitySearchFieldTable(classInfo);
        }
    }
}
