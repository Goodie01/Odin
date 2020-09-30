package org.goodiemania.odin.internal.database.impl;

import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.junit.jupiter.api.Test;

class DatabaseWrapperImplTest {
    private final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
    private ClassInfo<ExampleEntity> exampleEntityClassInfo;

    @Test
    void creatingDatabaseMultipleTimes() {
        classInfoHolder.add(ExampleEntity.class);
        exampleEntityClassInfo = classInfoHolder.retrieve(ExampleEntity.class)
                .orElseThrow();

        final DatabaseWrapperImpl databaseWrapper = new DatabaseWrapperImpl("jdbc:sqlite:mainDatabase");
        databaseWrapper.createEntitySearchFieldTable(exampleEntityClassInfo);
        databaseWrapper.createEntitySearchFieldTable(exampleEntityClassInfo);
    }
}