package org.goodiemania.odin.entities;

import java.time.ZonedDateTime;
import java.util.Optional;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class ExampleEntityWithOptional {
    @Id
    private String id;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Optional<String> getTime() {
        return Optional.ofNullable(time);
    }

    public void setTime(final String time) {
        this.time = time;
    }
}
