package org.goodiemania.odin.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.external.model.SearchTerm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerSearchTests {
    private static final String EXAMPLE_ENTITY_NAME = "Example Entity";
    private static final String EXAMPLE_ENTITY_DESCRIPTION = "This is a description so YAY";
    private static final String EXAMPLE_ENTITY_NEW_DESCRIPTION = "This is a new Example description!";
    private static final String EXAMPLE_ENTITY_MAP_KEY = "Example";
    private static final String EXAMPLE_ENTITY_MAP_VALUE = "Howdy";
    private static final String DATABASE_NAME_FOR_RUN = UUID.randomUUID().toString();
    private EntityManager<ExampleEntity> em;

    @BeforeEach
    void setUp() {
        Odin odin = Odin.create()
                .addPackageName("org.goodiemania.odin.entities")
                .setJdbcConnectUrl("jdbc:sqlite:" + DATABASE_NAME_FOR_RUN)
                .build();
        em = odin.createFor(ExampleEntity.class);
    }

    @AfterEach
    void tearDown() {
        final File databaseFile = new File(DATABASE_NAME_FOR_RUN);
        assertTrue(databaseFile.delete());
    }

    @Test
    void searchForItemInDatabase() {
        final ExampleEntity exampleEntity = createExampleEntity(UUID.randomUUID().toString());
        em.save(exampleEntity);

        List<ExampleEntity> searchResults = em.search(SearchTerm.equals("%","%YAY"));

        assertEquals(1, searchResults.size());
    }

    @Test
    void searchForItemNotInDatabase() {
        final ExampleEntity exampleEntity = createExampleEntity(UUID.randomUUID().toString());
        em.save(exampleEntity);

        List<ExampleEntity> searchResults = em.search(SearchTerm.equals("Description", "%Example%"));

        assertEquals(0, searchResults.size());
    }

    private ExampleEntity createExampleEntity(final String id) {
        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId(id);
        exampleEntity.setName(EXAMPLE_ENTITY_NAME);
        exampleEntity.setDescription(EXAMPLE_ENTITY_DESCRIPTION);
        exampleEntity.getMap().put(EXAMPLE_ENTITY_MAP_KEY, EXAMPLE_ENTITY_MAP_VALUE);
        return exampleEntity;
    }
}
