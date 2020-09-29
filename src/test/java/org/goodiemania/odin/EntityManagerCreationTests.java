package org.goodiemania.odin;

import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.example.NotPartOfHolderEntity;
import org.goodiemania.odin.example.TestExampleEntity;
import org.goodiemania.odin.external.Odin;
import org.goodiemania.odin.external.exceptions.UnknownClassException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerCreationTests {
    private Odin odin;

    @BeforeEach
    void setUp() {
        odin = Odin.create()
                .addPackageName("org.goodiemania.odin.example")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
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
