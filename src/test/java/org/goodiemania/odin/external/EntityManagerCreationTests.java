package org.goodiemania.odin.external;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.goodiemania.odin.entities.ExampleEntity;
import org.goodiemania.odin.entities.NotPartOfHolderEntity;
import org.goodiemania.odin.external.EntityManager;
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
        final EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);
        Assertions.assertNotNull(em);
    }

    @Test
    void entityNotRegistered() {
        UnknownClassException unknownClassException =
                Assertions.assertThrows(UnknownClassException.class, () -> odin.createFor(NotPartOfHolderEntity.class));

        Assertions.assertEquals(NotPartOfHolderEntity.class, unknownClassException.getEntityClass());
        Assertions.assertEquals("Unable to find given class in any packages", unknownClassException.getMessage());
    }
}
