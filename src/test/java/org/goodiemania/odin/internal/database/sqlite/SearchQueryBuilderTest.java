package org.goodiemania.odin.internal.database.sqlite;

import java.util.List;
import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchQueryBuilderTest {
    private final SearchQueryBuilder builder = new SearchQueryBuilder();
    private final ClassInfo<ExampleEntity> classInfo = new ClassInfoBuilder().build(ExampleEntity.class);

    @Test
    void happyFlow() {
        String searchQuery = builder.build(classInfo, List.of(SearchTerm.of("field", "value")));
        Assertions.assertEquals(
                searchQuery,
                "select objectId from ExampleEntity___SearchTable where (fieldName like :fieldName0 and fieldValue like :value0)"
        );
    }
}