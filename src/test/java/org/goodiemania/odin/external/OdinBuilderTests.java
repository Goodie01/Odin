package org.goodiemania.odin.external;

import org.goodiemania.odin.external.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OdinBuilderTests {
    @Test
    void noPackageNamesProvided() {
        final Odin.OdinBuilder odinBuilder = Odin.create().setJdbcConnectUrl("jdbc:sqlite:mainDatabase");
        final InvalidArgumentsException exception = Assertions.assertThrows(InvalidArgumentsException.class, odinBuilder::build);
        Assertions.assertEquals("You must provide at least one package name",
                exception.getMessage());
    }

    @Test
    void noJdbcUrlProvided() {
        final Odin.OdinBuilder odinBuilder = Odin.create().addPackageName("org.goodiemania.odin.entities");
        final InvalidArgumentsException exception = Assertions.assertThrows(InvalidArgumentsException.class, odinBuilder::build);
        Assertions.assertEquals("JDBC connection URL must be set",
                exception.getMessage());
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
