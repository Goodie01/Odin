package org.goodiemania.odin.internal.manager.search;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchFieldGeneratorTest {
    private SearchFieldGenerator searchFieldGenerator;

    @BeforeEach
    void setUp() {
        final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
        classInfoHolder.add(ExampleEntity.class);
        searchFieldGenerator = new SearchFieldGenerator(classInfoHolder);
    }

    @Test
    void test() {
        final ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId("Hello there");
        exampleEntity.setDescription("General Kenobi");

        final List<SearchField> generate = searchFieldGenerator.generate(exampleEntity);

        Assertions.assertEquals(3, generate.size());
        generate.forEach(searchField -> {
            if (StringUtils.equals(searchField.getFieldName(), "id")) {
                Assertions.assertEquals(searchField.getFieldValue(), "Hello there");
            } else if (StringUtils.equals(searchField.getFieldName(), "description")) {
                Assertions.assertEquals(searchField.getFieldValue(), "General Kenobi");
            } else {
                Assertions.assertEquals(searchField.getFieldName(), "name");
                Assertions.assertEquals(searchField.getFieldValue(), "");
            }
        });
    }
}