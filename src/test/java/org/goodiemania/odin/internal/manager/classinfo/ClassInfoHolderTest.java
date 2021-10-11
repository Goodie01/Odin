package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.entities.NotPartOfHolderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassInfoHolderTest {
    private ClassInfoHolder classInfoHolder;

    @BeforeEach
    void setUp() {
        classInfoHolder = new ClassInfoHolder();
        classInfoHolder.add(ExampleEntity.class);
    }

    @Test
    void happyFlow() {
        final Optional<ClassInfo<ExampleEntity>> exampleEntityClassInfo = classInfoHolder.retrieve(ExampleEntity.class);
        Assertions.assertTrue(exampleEntityClassInfo.isPresent());
    }

    @Test
    void ClassIsNotPartOfHolder() {
        final Optional<ClassInfo<NotPartOfHolderEntity>> exampleEntityClassInfo = classInfoHolder.retrieve(NotPartOfHolderEntity.class);
        Assertions.assertTrue(exampleEntityClassInfo.isEmpty());
    }


    @Test
    void testTableName() {
        Assertions.assertEquals("ExampleEntity", get().getTableName());
    }

    @Test
    void testSearchTableName() {
        Assertions.assertEquals("ExampleEntity___SearchTable", get().getSearchTableName());
    }

    @Test
    void testIndexSearchField() {
        Assertions.assertEquals("id", get().getIdField().name());
    }

    private ClassInfo<ExampleEntity> get() {
        classInfoHolder.add(ExampleEntity.class);
        return classInfoHolder.retrieve(ExampleEntity.class).orElseThrow();
    }
}