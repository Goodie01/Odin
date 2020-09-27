package org.goodiemania.odin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.internal.database.Database;
import org.goodiemania.odin.internal.manager.ClassInfo;
import org.goodiemania.odin.internal.manager.EntityManagerImpl;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.goodiemania.odin.internal.manager.search.SearchFieldGenerator;

public class OdinImpl implements Odin {
    private final ClassInfoHolder entityClasses;
    private final ObjectMapper objectMapper;
    private final Database database;
    private final SearchFieldGenerator searchFieldGenerator;

    public OdinImpl(
            final ObjectMapper objectMapper,
            final ClassInfoHolder entityClasses,
            final Database database,
            final SearchFieldGenerator searchFieldGenerator) {
        this.objectMapper = objectMapper;
        this.entityClasses = entityClasses;
        this.database = database;
        this.searchFieldGenerator = searchFieldGenerator;
    }

    @Override
    public <T> EntityManager<T> createFor(final Class<T> entityClass) {
        ClassInfo<T> classInfo = entityClasses.find(entityClass)
                .orElseThrow(() -> new IllegalStateException("Unable to find given class in any packages"));

        return new EntityManagerImpl<T>(database, objectMapper, searchFieldGenerator, classInfo);
    }
}
