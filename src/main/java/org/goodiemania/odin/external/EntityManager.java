package org.goodiemania.odin.external;

import java.util.List;
import java.util.Optional;
import org.goodiemania.odin.external.model.SearchTerm;

public interface EntityManager<T> {
    Optional<T> getById(String id);

    List<T> search(List<SearchTerm> searchTerms);

    List<T> getAll();

    void save(T object);

    void saveWithAdditionalSearchParams(T object, Object... additionalObjects);

    void deleteById(String id);
}
