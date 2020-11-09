package org.goodiemania.odin.entities;

import java.time.ZonedDateTime;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;

@Entity
public class ExampleEntityWithZonedDateTime {
    @Id
    private String id;
    private ZonedDateTime time;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(final ZonedDateTime time) {
        this.time = time;
    }
}
