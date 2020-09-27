package org.goodiemania.odin.example;

import java.util.HashMap;
import java.util.Map;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
@Index
public class ExampleEntity {
    @Id
    private String id;
    private String name;
    private String description;
    private Map<String, String> map = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(final Map<String, String> map) {
        this.map = map;
    }
}
