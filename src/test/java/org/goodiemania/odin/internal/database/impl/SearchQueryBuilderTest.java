package org.goodiemania.odin.internal.database.impl;

import java.util.List;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchQueryBuilderTest {
    private final SearchQueryBuilder searchQueryBuilder = new SearchQueryBuilder();
    private final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
    private ClassInfo<ExampleEntity> exampleEntityClassInfo;

    @BeforeEach
    void setUp() {
        classInfoHolder.add(ExampleEntity.class);
        exampleEntityClassInfo = classInfoHolder.retrieve(ExampleEntity.class)
                .orElseThrow();
    }

    @Test
    void happyFlow() {
        String searchQuery = searchQueryBuilder.build(exampleEntityClassInfo, List.of(SearchTerm.of("field", "value")));
        Assertions.assertEquals(
                "select objectId from ExampleEntity___SearchTable where (fieldName like :fieldName0 and fieldValue like :value0)",
                searchQuery
        );
    }
}