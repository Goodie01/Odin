package org.goodiemania.odin.entities;

import java.util.Map;
import org.goodiemania.odin.external.annotations.Entity;
import org.goodiemania.odin.external.annotations.Id;
import org.goodiemania.odin.external.annotations.Index;

@Entity
public record ExampleRecordEntity(@Id ExampleId id,
                                  String name,
                                  @Index String description,
                                  Map<String, String> map) {
}
