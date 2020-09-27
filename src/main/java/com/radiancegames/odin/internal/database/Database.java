package com.radiancegames.odin.internal.database;

import com.radiancegames.odin.external.model.SearchTerm;
import com.radiancegames.odin.internal.manager.ClassInfo;
import java.util.List;
import java.util.Optional;

//TODO this is a bad name
public interface Database {
    void createEntityTable(final ClassInfo<?> classInfo);

    void createEntitySearchFieldTable(final ClassInfo<?> classInfo);

    void saveEntity(
            final ClassInfo<?> classInfo,
            final String id,
            final List<SearchField> searchFields,
            final String blob);

    void deleteById(final ClassInfo<?> classInfo, final String id);

    Optional<String> getById(final ClassInfo<?> classInfo, final String id);

    List<String> search(final ClassInfo<?> classInfo, final List<SearchTerm> searchTerms);

    List<String> getAll(final ClassInfo<?> classInfo);
}
