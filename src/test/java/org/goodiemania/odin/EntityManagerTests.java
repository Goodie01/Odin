package org.goodiemania.odin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.Odin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerTests {
    private Odin odin;

    @BeforeEach
    void setUp() {
        odin = Odin.create()
                .addPackageName("org.goodiemania.odin.entities")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
    }

    @AfterEach
    void tearDown() {
        final File databaseFile = new File("mainDatabase");
        assertTrue(databaseFile.delete());
    }

    @Test
    void testGetById() {
        final EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);

        final ExampleEntity exampleEntity = get();
        final String id = exampleEntity.getId();

        em.save(exampleEntity);
        final ExampleEntity foundId = em.getById(id)
                .orElseThrow();

        Assertions.assertEquals(exampleEntity.getId(), foundId.getId());
        Assertions.assertEquals(exampleEntity.getName(), foundId.getName());
        Assertions.assertEquals(exampleEntity.getDescription(), foundId.getDescription());
    }

    @Test
    void testGetAll() {
        final EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);

        final ExampleEntity firstEntity = get();
        em.save(firstEntity);
        final ExampleEntity secondEntity = get();
        em.save(secondEntity);

        final List<ExampleEntity> all = em.getAll();

        Assertions.assertEquals(2, all.size());
    }

    public ExampleEntity get() {
        final ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId(UUID.randomUUID().toString());
        exampleEntity.setName("Generate Kenobi");
        exampleEntity.setDescription("I have the high ground");

        return exampleEntity;
    }
}
