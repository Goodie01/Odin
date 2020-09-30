package org.goodiemania.odin;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.entities.NotPartOfHolderEntity;
import org.goodiemania.odin.entities.TestExampleEntity;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.external.exceptions.UnknownClassException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntityManagerCreationTests {
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
    void createEntityManagerForExistingEntity() {
        odin.createFor(ExampleEntity.class);
    }

    @Test
    void createEntityManagerForExistingTestEntity() {
        odin.createFor(TestExampleEntity.class);
    }

    @Test
    void createEntityManagerForNotExistingEntity() {
        final UnknownClassException unknownClassException = Assertions.assertThrows(UnknownClassException.class, () -> {
            odin.createFor(NotPartOfHolderEntity.class);
        });

        Assertions.assertEquals(unknownClassException.getEntityClass(), NotPartOfHolderEntity.class);
    }
}
