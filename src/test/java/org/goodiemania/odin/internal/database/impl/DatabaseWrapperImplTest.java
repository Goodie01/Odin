package org.goodiemania.odin.internal.database.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.junit.jupiter.api.Test;

class DatabaseWrapperImplTest {
    @Test
    void creatingDatabaseMultipleTimesThrowsNoError() {
        final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
        classInfoHolder.add(ExampleEntity.class);

        final ClassInfo<ExampleEntity> exampleEntityClassInfo = classInfoHolder.retrieve(ExampleEntity.class)
                .orElseThrow();

        final DatabaseWrapperImpl databaseWrapper = new DatabaseWrapperImpl("jdbc:sqlite:mainDatabase");
        databaseWrapper.createEntitySearchFieldTable(exampleEntityClassInfo);
        databaseWrapper.createEntitySearchFieldTable(exampleEntityClassInfo);

        final File databaseFile = new File("mainDatabase");
        assertTrue(databaseFile.delete());
    }
}