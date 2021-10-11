package org.goodiemania.odin.entities;

import java.util.HashMap;
import java.util.Map;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
public class ExampleObjectIdEntity {
    @Id
    private ExampleId id;
    private String name;
    @Index
    private String description;
    private Map<String, String> map = new HashMap<>();

    public ExampleId getId() {
        return id;
    }

    public void setId(final ExampleId id) {
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
