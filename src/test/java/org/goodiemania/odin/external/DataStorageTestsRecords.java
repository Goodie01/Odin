package org.goodiemania.odin.external;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleId;
import org.goodiemania.odin.entities.ExampleRecordEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataStorageTestsRecords {
    private static final String EXAMPLE_ENTITY_NAME = "Example Entity";
    private static final String EXAMPLE_ENTITY_DESCRIPTION = "This is a description so YAY";
    private static final String EXAMPLE_ENTITY_MAP_KEY = "Example";
    private static final String EXAMPLE_ENTITY_MAP_VALUE = "Howdy";
    private static final String DATABASE_NAME_FOR_RUN = UUID.randomUUID().toString();
    private EntityManager<ExampleRecordEntity> em;

    @BeforeEach
    void setUp() {
        Odin odin = Odin.create()
                .addPackageName("org.goodiemania.odin.entities")
                .setJdbcConnectUrl("jdbc:sqlite:" + DATABASE_NAME_FOR_RUN)
                .build();
        em = odin.createFor(ExampleRecordEntity.class);
    }

    @AfterEach
    void tearDown() {
        final File databaseFile = new File(DATABASE_NAME_FOR_RUN);
        assertTrue(databaseFile.delete());
    }

    @Test
    void saveAndRestoreRecord() {
        ExampleId id = new ExampleId(UUID.randomUUID().toString());

        ExampleRecordEntity exampleEntity = createExampleRecordEntity(id);

        em.save(exampleEntity);

        Optional<ExampleRecordEntity> possiblyFoundEntity = em.getById(id);
        assertTrue(possiblyFoundEntity.isPresent());

        ExampleRecordEntity foundEntity = possiblyFoundEntity.get();
        assertEquals(id, foundEntity.id());
        assertEquals(EXAMPLE_ENTITY_NAME, foundEntity.name());
        assertEquals(EXAMPLE_ENTITY_DESCRIPTION, foundEntity.description());
        assertEquals(EXAMPLE_ENTITY_MAP_VALUE, foundEntity.map().get(EXAMPLE_ENTITY_MAP_KEY));
    }

    private ExampleRecordEntity createExampleRecordEntity(final ExampleId id) {
        return new ExampleRecordEntity(id,
                EXAMPLE_ENTITY_NAME,
                EXAMPLE_ENTITY_DESCRIPTION,
                Map.of(EXAMPLE_ENTITY_MAP_KEY, EXAMPLE_ENTITY_MAP_VALUE));
    }
}
