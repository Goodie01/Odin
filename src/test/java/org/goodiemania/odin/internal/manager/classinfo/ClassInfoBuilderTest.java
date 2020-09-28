package org.goodiemania.odin.internal.manager.classinfo;

import org.goodiemania.odin.example.ExampleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassInfoBuilderTest {
    private final ClassInfoBuilder classInfoBuilder = new ClassInfoBuilder();

    @Test
    public void testTableName() {
        ClassInfo<ExampleEntity> classInfo = classInfoBuilder.build(ExampleEntity.class);
        Assertions.assertEquals("ExampleEntity", classInfo.getTableName());
    }

    @Test
    public void testSearchTableName() {
        ClassInfo<ExampleEntity> classInfo = classInfoBuilder.build(ExampleEntity.class);
        Assertions.assertEquals("ExampleEntity___SearchTable", classInfo.getSearchTableName());
    }

    @Test
    public void testIndexSearchField() {
        ClassInfo<ExampleEntity> classInfo = classInfoBuilder.build(ExampleEntity.class);
        Assertions.assertEquals("id", classInfo.getIdField().getName());
    }
}