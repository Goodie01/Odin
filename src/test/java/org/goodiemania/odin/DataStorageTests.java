package org.goodiemania.odin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.external.model.SearchTerm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataStorageTests {
    private static final String EXAMPLE_ENTITY_NAME = "Example Entity";
    private static final String EXAMPLE_ENTITY_DESCRIPTION = "This is a description so YAY";
    private static final String EXAMPLE_ENTITY_NEW_DESCRIPTION = "This is a new Example description!";
    private static final String EXAMPLE_ENTITY_MAP_KEY = "Example";
    private static final String EXAMPLE_ENTITY_MAP_VALUE = "Howdy";
    private EntityManager<ExampleEntity> em;

    @BeforeEach
    void setUp() {
        Odin odin = Odin.create()
                .addPackageName("org.goodiemania.odin.example")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
        em = odin.createFor(ExampleEntity.class);
    }

    @AfterEach
    void tearDown() {
        final File databaseFile = new File("mainDatabase");
        assertTrue(databaseFile.delete());
    }

    @Test
    void saveAndRestoreObject() {
        String id = UUID.randomUUID().toString();

        ExampleEntity exampleEntity = createExampleEntity(id);

        em.save(exampleEntity);

        Optional<ExampleEntity> possiblyFoundEntity = em.getById(id);
        assertTrue(possiblyFoundEntity.isPresent());

        ExampleEntity foundEntity = possiblyFoundEntity.get();
        assertEquals(foundEntity.getId(), id);
        assertEquals(foundEntity.getName(), EXAMPLE_ENTITY_NAME);
        assertEquals(foundEntity.getDescription(), EXAMPLE_ENTITY_DESCRIPTION);
        assertEquals(foundEntity.getMap().get(EXAMPLE_ENTITY_MAP_KEY), EXAMPLE_ENTITY_MAP_VALUE);
    }

    @Test
    void updateExistingObject() {
        String id = UUID.randomUUID().toString();

        em.save(createExampleEntity(id));

        em.getById(id).ifPresentOrElse(exampleEntity -> {
            exampleEntity.setDescription(EXAMPLE_ENTITY_NEW_DESCRIPTION);
            em.save(exampleEntity);
        }, Assertions::fail);

        final Optional<ExampleEntity> foundEntity = em.getById(id);
        assertTrue(foundEntity.isPresent());
        assertEquals(foundEntity.get().getId(), id);
        assertEquals(foundEntity.get().getName(), EXAMPLE_ENTITY_NAME);
        assertEquals(foundEntity.get().getDescription(), EXAMPLE_ENTITY_NEW_DESCRIPTION);
        assertEquals(foundEntity.get().getMap().get(EXAMPLE_ENTITY_MAP_KEY), EXAMPLE_ENTITY_MAP_VALUE);
    }

    @Test
    void searchForItemInDatabase() {
        final ExampleEntity exampleEntity = createExampleEntity(UUID.randomUUID().toString());
        em.save(exampleEntity);

        List<ExampleEntity> searchResults = em.search(List.of(SearchTerm.of("%", "%YAY")));

        assertEquals(1, searchResults.size());
    }

    @Test
    void searchForItemNotInDatabase() {
        final ExampleEntity exampleEntity = createExampleEntity(UUID.randomUUID().toString());
        em.save(exampleEntity);

        List<ExampleEntity> searchResults = em.search(List.of(SearchTerm.of("Description", "%Example%")));

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
