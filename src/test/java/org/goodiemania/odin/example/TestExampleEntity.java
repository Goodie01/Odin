package org.goodiemania.odin.example;

import org.checkerframework.checker.interning.qual.InternedDistinct;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class TestExampleEntity {
    @Id
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }
}
