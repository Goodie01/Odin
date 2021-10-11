package org.goodiemania.odin.external;

import java.util.List;
import java.util.Optional;
import org.goodiemania.odin.external.model.SearchTerm;

public interface EntityManager<T> {
    Optional<T> getById(String id);

    Optional<T> getById(Object id);

    List<T> search(SearchTerm searchTerms);

    List<T> getAll();

    void save(T object);

    void deleteById(String id);
}
