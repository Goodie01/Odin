package org.goodiemania.odin.internal.manager.classinfo;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.internal.database.Database;
import org.goodiemania.odin.internal.database.sqlite.SqliteWrapper;
import org.goodiemania.odin.internal.manager.ClassInfo;
import org.goodiemania.odin.internal.manager.ClassInfoBuilder;
import org.goodiemania.odin.internal.manager.ClassManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassInfoHolderTest {
    @Test
    public void happyFlow() {
        final Database database = new SqliteWrapper("jdbc:sqlite:testDatabase");
        final Set<String> packageNames = Set.of("org.goodiemania.odin.example");

        final ClassManager classManager = new ClassManager(database, new ClassInfoBuilder());
        Set<ClassInfo<?>> classInfoSet = packageNames.stream()
                .flatMap(classManager::find)
                .collect(Collectors.toSet());
        ClassInfoHolder classInfoHolder = new ClassInfoHolder(classInfoSet);

        final Optional<ClassInfo<ExampleEntity>> exampleEntityClassInfo = classInfoHolder.find(ExampleEntity.class);

        Assertions.assertTrue(exampleEntityClassInfo.isPresent());
    }
}