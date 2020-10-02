package org.goodiemania.odin.entities;

import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
public class ExampleEntityWithException {
    @Id
    private String id;
    @Index
    private String description;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getDescription() {
        throw new IllegalStateException("Test");
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
