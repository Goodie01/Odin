package org.goodiemania.odin.internal.database.impl;

import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.database.SearchTermProcessor;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchQueryBuilderTest {
    private final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
    private ClassInfo<ExampleEntity> exampleEntityClassInfo;

    @BeforeEach
    void setUp() {
        classInfoHolder.add(ExampleEntity.class);
        exampleEntityClassInfo = classInfoHolder.retrieve(ExampleEntity.class)
                .orElseThrow();
    }

    @Test
    void searchTermBuilder() {
        final SearchTerm and = SearchTerm.equals("testField", "testValue");

        final SearchTermProcessor searchTermProcessor = new SearchTermProcessor(exampleEntityClassInfo, and);
        final String queryString = searchTermProcessor.getQueryString();

        Assertions.assertEquals(
                "select jsonBlob from ExampleEntity where id in (select objectId from ExampleEntity___SearchTable where fieldName like :fieldName1 and fieldValue like :value1)",
                queryString
        );

        Assertions.assertEquals(1, searchTermProcessor.getParams().size());
        Assertions.assertEquals("testField", searchTermProcessor.getParams().get(0).getName());
        Assertions.assertEquals("testValue", searchTermProcessor.getParams().get(0).getValue());
        Assertions.assertEquals(1, searchTermProcessor.getParams().get(0).getCount());
    }

    @Test
    void searchTermBuilderAndFlow() {
        final SearchTerm and = SearchTerm.and(SearchTerm.equals("testField", "testValue"),
                SearchTerm.equals("myField", "myValue"));

        final SearchTermProcessor searchTermProcessor = new SearchTermProcessor(exampleEntityClassInfo, and);
        final String queryString = searchTermProcessor.getQueryString();

        Assertions.assertEquals(
                "select jsonBlob from ExampleEntity where id in (select objectId from ExampleEntity___SearchTable where fieldName like :fieldName1 and fieldValue like :value1) and id in (select objectId from ExampleEntity___SearchTable where fieldName like :fieldName2 and fieldValue like :value2)",
                queryString
        );
        Assertions.assertEquals("testField", searchTermProcessor.getParams().get(0).getName());
        Assertions.assertEquals("testValue", searchTermProcessor.getParams().get(0).getValue());
        Assertions.assertEquals(1, searchTermProcessor.getParams().get(0).getCount());

        Assertions.assertEquals("myField", searchTermProcessor.getParams().get(1).getName());
        Assertions.assertEquals("myValue", searchTermProcessor.getParams().get(1).getValue());
        Assertions.assertEquals(2, searchTermProcessor.getParams().get(1).getCount());
    }
}