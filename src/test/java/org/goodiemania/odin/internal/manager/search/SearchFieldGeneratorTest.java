package org.goodiemania.odin.internal.manager.search;

import java.util.List;
import java.util.Map;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.entities.ExampleEntityWithCollection;
import org.goodiemania.odin.entities.ExampleEntityWithMap;
import org.goodiemania.odin.entities.ExampleEntityWithException;
import org.goodiemania.odin.external.exceptions.EntityThrewExceptionException;
import org.goodiemania.odin.external.exceptions.ShouldNeverHappenException;
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
        classInfoHolder.add(ExampleEntityWithCollection.class);
        classInfoHolder.add(ExampleEntityWithMap.class);
        classInfoHolder.add(ExampleEntityWithException.class);
        searchFieldGenerator = new SearchFieldGenerator(classInfoHolder);
    }

    @Test
    void textFieldTest() {
        final ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId("Hello there");
        exampleEntity.setDescription("General Kenobi");

        final List<SearchField> generate = searchFieldGenerator.generate(exampleEntity);

        Assertions.assertEquals(1, generate.size());
        Assertions.assertEquals("description", generate.get(0).getFieldName());
        Assertions.assertEquals("General Kenobi", generate.get(0).getFieldValue());
    }

    @Test
    void collectionFieldTest() {
        final ExampleEntityWithCollection exampleEntity = new ExampleEntityWithCollection();
        exampleEntity.setId("You were my brother,");
        exampleEntity.setStringList(List.of("Anakin"));

        final List<SearchField> generate = searchFieldGenerator.generate(exampleEntity);

        Assertions.assertEquals(1, generate.size());
        Assertions.assertEquals("stringList", generate.get(0).getFieldName());
        Assertions.assertEquals("Anakin", generate.get(0).getFieldValue());
    }

    @Test
    void mapFieldTest() {
        final ExampleEntityWithMap exampleEntity = new ExampleEntityWithMap();
        exampleEntity.setId("You were my brother,");
        exampleEntity.setStringList(Map.of("Anakin", "I loved you"));

        final List<SearchField> generate = searchFieldGenerator.generate(exampleEntity);

        Assertions.assertEquals(1, generate.size());
        Assertions.assertEquals("stringList_Anakin", generate.get(0).getFieldName());
        Assertions.assertEquals("I loved you", generate.get(0).getFieldValue());
    }

    @Test
    void nullFieldTest() {
        final ExampleEntityWithCollection exampleEntity = new ExampleEntityWithCollection();
        exampleEntity.setId("You were my brother,");
        exampleEntity.setStringList(null);

        final List<SearchField> generate = searchFieldGenerator.generate(exampleEntity);

        Assertions.assertEquals(1, generate.size());
        Assertions.assertEquals("stringList", generate.get(0).getFieldName());
        Assertions.assertEquals("", generate.get(0).getFieldValue());
    }

    @Test
    void getterThrowsException() {
        final ExampleEntityWithException entity = new ExampleEntityWithException();
        entity.setId("1234");
        entity.setDescription("Hello there");

        Assertions.assertThrows(EntityThrewExceptionException.class, () -> searchFieldGenerator.generate(entity));
    }
}