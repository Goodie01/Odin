package com.radiancegames.odin.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radiancegames.odin.external.EntityManager;
import com.radiancegames.odin.external.Odin;
import com.radiancegames.odin.internal.database.Database;
import com.radiancegames.odin.internal.manager.ClassInfo;
import com.radiancegames.odin.internal.manager.EntityManagerImpl;
import com.radiancegames.odin.internal.manager.classinfo.Holder;
import com.radiancegames.odin.internal.manager.search.SearchFieldGenerator;

public class OdinImpl implements Odin {
    private final Holder entityClasses;
    private final ObjectMapper objectMapper;
    private final Database database;
    private final SearchFieldGenerator searchFieldGenerator;

    public OdinImpl(
            final ObjectMapper objectMapper,
            final Holder entityClasses,
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
