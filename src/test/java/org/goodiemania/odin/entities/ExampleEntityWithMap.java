package org.goodiemania.odin.entities;

import java.util.List;
import java.util.Map;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
public class ExampleEntityWithMap {
    @Id
    private String id;
    @Index
    private Map<String, String> stringList;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Map<String, String> getStringList() {
        return stringList;
    }

    public void setStringList(final Map<String, String> stringList) {
        this.stringList = stringList;
    }
}
