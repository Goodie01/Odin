package org.goodiemania.odin.internal.database.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;

public class MockDatabaseWrapperWrapper implements DatabaseWrapper {
    //entity type, ID, blob
    private final Map<String, Map<String, String>> blobStorage = new HashMap<>();
    private final Map<String, Map<String, SearchField>> blobSearchField = new HashMap<>();
    @Override
    public void createEntityTable(final ClassInfo<?> classInfo) {
        blobStorage.put(classInfo.getTableName(), new HashMap<>());
    }

    @Override
    public void createEntitySearchFieldTable(final ClassInfo<?> classInfo) {
        blobSearchField.put(classInfo.getSearchTableName(), new HashMap<>());
    }

    @Override
    public void saveEntity(final ClassInfo<?> classInfo, final String id, final List<SearchField> searchFields, final String blob) {
        blobStorage.get(classInfo.getTableName()).put(id, blob);
    }

    @Override
    public void deleteById(final ClassInfo<?> classInfo, final String id) {
        blobStorage.get(classInfo.getTableName()).remove(id);
    }

    @Override
    public Optional<String> getById(final ClassInfo<?> classInfo, final String id) {
        return Optional.ofNullable(blobStorage.get(classInfo.getTableName()).get(id));
    }

    @Override
    public List<String> search(final ClassInfo<?> classInfo, final List<SearchTerm> searchTerms) {
        throw new IllegalStateException("One day soon we'll get this to work");
    }

    @Override
    public List<String> getAll(final ClassInfo<?> classInfo) {
        final Collection<String> values = blobStorage.get(classInfo.getTableName()).values();
        return new ArrayList<>(values);
    }
}
