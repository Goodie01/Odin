package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import java.util.Set;
import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.example.NotPartOfHolderEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassInfoHolderTest {
    private ClassInfoHolder classInfoHolder;

    @BeforeEach
    void setUp() {
        final ClassInfoBuilder classInfoBuilder = new ClassInfoBuilder();

        final Set<ClassInfo<?>> classInfoSet = Set.of(classInfoBuilder.build(ExampleEntity.class));
        classInfoHolder = new ClassInfoHolder(classInfoSet);
    }

    @Test
    void happyFlow() {
        final Optional<ClassInfo<ExampleEntity>> exampleEntityClassInfo = classInfoHolder.find(ExampleEntity.class);
        Assertions.assertTrue(exampleEntityClassInfo.isPresent());
    }

    @Test
    void ClassIsNotPartOfHolder() {
        final Optional<ClassInfo<NotPartOfHolderEntity>> exampleEntityClassInfo = classInfoHolder.find(NotPartOfHolderEntity.class);
        Assertions.assertTrue(exampleEntityClassInfo.isEmpty());
    }
}