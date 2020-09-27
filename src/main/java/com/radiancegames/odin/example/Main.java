package com.radiancegames.odin.example;

import com.radiancegames.odin.external.EntityManager;
import com.radiancegames.odin.external.Odin;
import com.radiancegames.odin.external.model.SearchTerm;
import java.util.List;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        String id = UUID.randomUUID().toString();

        ExampleEntity exampleEntity = new ExampleEntity();
        exampleEntity.setId(id);
        exampleEntity.setName("Example Entity");
        exampleEntity.setDescription("This is a description so YAY");
        exampleEntity.getMap().put("Example", "Howdy");

        Odin odin = Odin.create()
                .addPackageName("com.radiancegames.odin.example")
                .setJdbcConnectUrl("jdbc:sqlite:mainDatabase")
                .build();
        EntityManager<ExampleEntity> em = odin.createFor(ExampleEntity.class);

        em.save(exampleEntity);

        em.getById(id)
                .ifPresent(byId -> {
                    System.out.println(byId.getId());
                    System.out.println(byId.getName());
                    System.out.println(byId.getDescription());
                });

        exampleEntity.setDescription("This is a new Example description!");
        em.save(exampleEntity);

        System.out.println("-------------------------");
        em.getById(id)
                .ifPresent(byId -> {
                    System.out.println(byId.getId());
                    System.out.println(byId.getName());
                    System.out.println(byId.getDescription());
                });

        List<ExampleEntity> searchResults = em.search(List.of(SearchTerm.of("%", "%YAY")));


        System.out.println("-------------------------");
        System.out.println("Search results for YAY:");
        searchResults.forEach(searchResult ->
                System.out.println("Search result: " + searchResult.getName()
                        + "; " + searchResult.getDescription())
        );


        System.out.println("-------------------------");
        System.out.println("Search results for Example:");
        searchResults = em.search(List.of(SearchTerm.of("%", "%Example%")));

        searchResults.forEach(searchResult ->
                System.out.println("Search result: " + searchResult.getName()
                        + "; " + searchResult.getDescription())
        );

        em.deleteById(id);
        if (em.getById(id).isEmpty()) {
            System.out.println("Successfully did NOT find the entity");
        }
    }
}
