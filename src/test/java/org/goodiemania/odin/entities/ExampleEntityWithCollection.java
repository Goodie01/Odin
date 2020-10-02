package org.goodiemania.odin.entities;

import java.util.List;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
public class ExampleEntityWithCollection {
    @Id
    private String id;
    @Index
    private List<String> stringList;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(final List<String> stringList) {
        this.stringList = stringList;
    }
}
