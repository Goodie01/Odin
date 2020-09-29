package org.goodiemania.odin.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.goodiemania.odin.example.ExampleEntity;
import org.goodiemania.odin.external.exceptions.EntityException;
import org.goodiemania.odin.external.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OdinBuilderTests {
    @Test
    void noPackageNamesProvided() {
        final InvalidArgumentsException exception = Assertions.assertThrows(InvalidArgumentsException.class, () ->
                Odin.create()
                        .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                        .build());
        Assertions.assertEquals(exception.getMessage(),
                "You must provide at least one package name");
    }

    @Test
    void noJdbcUrlProvided() {
        final InvalidArgumentsException exception = Assertions.assertThrows(InvalidArgumentsException.class, () ->
                Odin.create()
                        .addPackageName("org.goodiemania.odin.example")
                        .build());
        Assertions.assertEquals(exception.getMessage(),
                "JDBC connection URL must be set");
    }

    @Test
    void happyFlow() {
        new ObjectMapper();
        final Odin odinInstance = Odin.create()
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .addPackageName("org.goodiemania.odin.example")
                .build();

        Assertions.assertNotNull(odinInstance);
    }
}
