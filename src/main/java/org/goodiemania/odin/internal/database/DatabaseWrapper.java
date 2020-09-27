package org.goodiemania.odin.internal.database;

import java.util.List;
import java.util.Optional;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.manager.ClassInfo;

//TODO this is a bad name
public interface DatabaseWrapper {
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
