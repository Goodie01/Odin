package org.goodiemania.odin.internal.database.impl;

import java.util.List;
import java.util.Optional;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.database.SearchTermProcessor;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

public class DatabaseWrapperImpl implements DatabaseWrapper {
    private final Jdbi jdbi;

    public DatabaseWrapperImpl(String jdbcConnectUrl) {
        this.jdbi = Jdbi.create(jdbcConnectUrl);
    }

    @Override
    public void createEntityTable(final ClassInfo<?> classInfo) {
        jdbi.useHandle(handle ->
                handle.execute(
                        String.format(
                                "CREATE TABLE IF NOT EXISTS %s (id varchar(255) UNIQUE, jsonBlob JSON)",
                                classInfo.getTableName())));
    }

    @Override
    public Boolean checkConnection() {
        return jdbi.withHandle(handle ->
                handle.createQuery("select 1")
                        .mapTo(String.class)
                        .findFirst())
                .map(s -> true)
                .orElse(false);
    }

    @Override
    public void createEntitySearchFieldTable(final ClassInfo<?> classInfo) {
        jdbi.useHandle(handle -> {
            int execute = handle.execute(
                    String.format(
                            "CREATE TABLE IF NOT EXISTS %s ("
                                    + "objectId varchar(255), "
                                    + "fieldName varchar(255), "
                                    + "fieldValue TEXT)",
                            classInfo.getSearchTableName()));
            if (execute != 0) {
                handle.execute(String.format("CREATE INDEX objectId ON %s(objectId);", classInfo.getSearchTableName()));
                handle.execute(
                        String.format(
                                "CREATE INDEX fieldValue ON %s(fieldValue);",
                                classInfo.getSearchTableName()));
                handle.execute(
                        String.format(
                                "CREATE INDEX fieldName_fieldValue ON %s(fieldName, fieldValue);",
                                classInfo.getSearchTableName()));
            }
        });
    }


    @Override
    public void saveEntity(
            final ClassInfo<?> classInfo,
            final String id,
            final List<SearchField> searchFields,
            final String blob) {
        jdbi.useTransaction(handle -> {
            String deleteFromTableQuery = String.format("delete from %s where id = ?", classInfo.getTableName());
            handle.execute(deleteFromTableQuery, id);
            String queryString = String.format("insert into %s (id, jsonBlob) values (?, ?)", classInfo.getTableName());
            handle.execute(queryString, id, blob);

            if (!classInfo.getIndexedFields().isEmpty()) {
                deleteSearchTableEntries(classInfo, id, handle);

                searchFields.forEach(field -> {
                    String searchTableQueryString =
                            String.format("insert into %s (objectId, fieldName, fieldValue) values (?, ?, ?)", classInfo.getSearchTableName());
                    handle.execute(searchTableQueryString, id, field.getFieldName(), field.getFieldValue());
                });
            }
        });
    }

    private void deleteSearchTableEntries(
            final ClassInfo<?> classInfo,
            final String id,
            final Handle handle) {
        handle.execute(
                String.format("delete from %s where objectId = ?", classInfo.getSearchTableName()),
                id);
    }

    @Override
    public Optional<String> getById(
            final ClassInfo<?> classInfo,
            final String id) {
        return jdbi.withHandle(handle ->
                handle.createQuery(String.format("select jsonBlob from %s where id = :id", classInfo.getTableName()))
                        .bind("id", id)
                        .mapTo(String.class)
                        .findFirst());
    }

    @Override
    public List<String> search(final ClassInfo<?> classInfo, final SearchTerm searchTerms) {
        final SearchTermProcessor searchTermProcessor = new SearchTermProcessor(classInfo, searchTerms);

        return jdbi.withHandle(handle -> {
            final Query query = handle.createQuery(searchTermProcessor.getQueryString());

            searchTermProcessor.getParams().forEach(parameterPair -> {
                query.bind("fieldName" + parameterPair.getCount(), parameterPair.getName());
                query.bind("value" + parameterPair.getCount(), parameterPair.getValue());
            });

            return query.mapTo(String.class).list();
        });
    }

    @Override
    public List<String> getAll(final ClassInfo<?> classInfo) {
        return jdbi.withHandle(handle ->
                handle.createQuery(String.format("select jsonBlob from %s", classInfo.getTableName()))
                        .mapTo(String.class)
                        .list());
    }

    @Override
    public void deleteById(final ClassInfo<?> classInfo, final String id) {
        jdbi.useHandle(handle -> {
                    handle.createUpdate(
                            String.format("delete from %s where id = :id", classInfo.getTableName()))
                            .bind("id", id)
                            .execute();

                    if (!classInfo.getIndexedFields().isEmpty()) {
                        deleteSearchTableEntries(classInfo, id, handle);
                    }
                }
        );
    }
}
