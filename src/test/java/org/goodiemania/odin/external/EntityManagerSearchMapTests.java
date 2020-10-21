package org.goodiemania.odin.external;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleEntityWithMap;
import org.goodiemania.odin.external.model.SearchTerm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerSearchMapTests {
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
    public void searchForItemInMap() {
        final EntityManager<ExampleEntityWithMap> em = odin.createFor(ExampleEntityWithMap.class);
        final String id = UUID.randomUUID().toString();

        final ExampleEntityWithMap exampleEntityWithMap = new ExampleEntityWithMap();
        exampleEntityWithMap.setId(id);
        exampleEntityWithMap.setStringList(Map.of("search","mapValue"));

        em.save(exampleEntityWithMap);

        final List<ExampleEntityWithMap> search = em.search(SearchTerm.equals("stringList_search", "mapValue"));

        Assertions.assertEquals(1, search.size());
    }

    @Test
    public void searchForItemInRemovedFromMap() {
        final EntityManager<ExampleEntityWithMap> em = odin.createFor(ExampleEntityWithMap.class);
        final String id = UUID.randomUUID().toString();

        ExampleEntityWithMap exampleEntityWithMap = new ExampleEntityWithMap();
        exampleEntityWithMap.setId(id);
        exampleEntityWithMap.setStringList(Map.of("search","mapValue",
                "secondSearch","hiddenValue"));
        em.save(exampleEntityWithMap);

        exampleEntityWithMap = new ExampleEntityWithMap();
        exampleEntityWithMap.setId(id);
        exampleEntityWithMap.setStringList(Map.of("search","mapValue"));
        em.save(exampleEntityWithMap);

        final List<ExampleEntityWithMap> search = em.search(SearchTerm.equals("stringList_secondSearch", "hiddenValue"));
        Assertions.assertEquals(0, search.size());
    }
}
