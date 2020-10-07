package org.goodiemania.odin.external;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;
import java.util.UUID;
import org.goodiemania.odin.entities.ExampleEntityWithCollection;
import org.goodiemania.odin.external.model.SearchTerm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityManagerSearchCollectionsTests {
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
    public void searchForItemInList() {
        final EntityManager<ExampleEntityWithCollection> em = odin.createFor(ExampleEntityWithCollection.class);
        final String id = UUID.randomUUID().toString();

        final ExampleEntityWithCollection entityWithCollection = new ExampleEntityWithCollection();
        entityWithCollection.setId(id);
        entityWithCollection.setStringList(List.of("searchableListItem"));

        em.save(entityWithCollection);

        final List<ExampleEntityWithCollection> search = em.search(SearchTerm.equals("stringList", "searchableListItem"));

        Assertions.assertEquals(1, search.size());
    }

    @Test
    public void searchForItemInRemovedFromList() {
        final EntityManager<ExampleEntityWithCollection> em = odin.createFor(ExampleEntityWithCollection.class);
        final String id = UUID.randomUUID().toString();

        ExampleEntityWithCollection entityWithCollection = new ExampleEntityWithCollection();
        entityWithCollection.setId(id);
        entityWithCollection.setStringList(List.of(
                "searchableListItem",
                "secondSearchableListItem"));
        em.save(entityWithCollection);

        entityWithCollection = new ExampleEntityWithCollection();
        entityWithCollection.setId(id);
        entityWithCollection.setStringList(List.of(
                "searchableListItem"));
        em.save(entityWithCollection);

        final List<ExampleEntityWithCollection> search = em.search(SearchTerm.equals("stringList", "secondSearchableListItem"));
        Assertions.assertEquals(0, search.size());
    }
}
