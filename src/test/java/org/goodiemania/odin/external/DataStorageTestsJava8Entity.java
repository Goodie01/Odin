package org.goodiemania.odin.external;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleEntityWithOptional;
import org.goodiemania.odin.entities.ExampleEntityWithZonedDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataStorageTestsJava8Entity {
    private static final String DATABASE_NAME_FOR_RUN = UUID.randomUUID().toString();
    private EntityManager<ExampleEntityWithZonedDateTime> zonedDateTimeEm;
    private EntityManager<ExampleEntityWithOptional> optionalEm;

    @BeforeEach
    void setUp() {
        Odin odin = Odin.create()
                .addPackageName("org.goodiemania.odin.entities")
                .setJdbcConnectUrl("jdbc:sqlite:" + DATABASE_NAME_FOR_RUN)
                .build();
        zonedDateTimeEm = odin.createFor(ExampleEntityWithZonedDateTime.class);
        optionalEm = odin.createFor(ExampleEntityWithOptional.class);
    }

    @AfterEach
    void tearDown() {
        final File databaseFile = new File(DATABASE_NAME_FOR_RUN);
        assertTrue(databaseFile.delete());
    }

    @Test
    void saveAndRestoreZonedDateTime() {
        String id = UUID.randomUUID().toString();
        ExampleEntityWithZonedDateTime entity = new ExampleEntityWithZonedDateTime();
        entity.setId(id);
        entity.setTime(ZonedDateTime.now());

        zonedDateTimeEm.save(entity);

        Optional<ExampleEntityWithZonedDateTime> possiblyFoundEntity = zonedDateTimeEm.getById(id);
        assertTrue(possiblyFoundEntity.isPresent());
    }

    @Test
    void saveAndRestoreOptional() {
        String id = UUID.randomUUID().toString();
        ExampleEntityWithOptional entity = new ExampleEntityWithOptional();
        entity.setId(id);
        entity.setTime(null);

        optionalEm.save(entity);

        Optional<ExampleEntityWithOptional> possiblyFoundEntity = optionalEm.getById(id);
        assertTrue(possiblyFoundEntity.isPresent());
    }
}
