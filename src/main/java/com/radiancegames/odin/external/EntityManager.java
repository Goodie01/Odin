package com.radiancegames.odin.external;

import com.radiancegames.odin.external.model.SearchTerm;
import java.util.List;
import java.util.Optional;

public interface EntityManager<T> {
    Optional<T> getById(String id);

    List<T> search(List<SearchTerm> searchTerms);

    List<T> getAll();

    void save(T object);

    void saveWithAdditionalSearchParams(T object, Object... additionalObjects);

    void deleteById(String id);
}
