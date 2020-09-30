package org.goodiemania.odin.external;

import org.goodiemania.odin.external.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OdinBuilderTests {
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
                        .addPackageName("org.goodiemania.odin.entities")
                        .build());
        Assertions.assertEquals(exception.getMessage(),
                "JDBC connection URL must be set");
    }

    @Test
    void happyFlow() {
        final Odin odinInstance = Odin.create()
                .setJdbcConnectUrl("jdbc:h2:mem:test_mem")
                .addPackageName("org.goodiemania.odin.entities")
                .build();

        Assertions.assertNotNull(odinInstance);
    }
}
