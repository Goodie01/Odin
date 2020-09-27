package org.goodiemania.odin.internal.database.sqlite;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.database.Database;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.ClassInfo;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;

public class SqliteWrapper implements Database {
    private final Jdbi jdbi;

    public SqliteWrapper(String jdbcConnectUrl) {
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
    public void createEntitySearchFieldTable(final ClassInfo<?> classInfo) {
        jdbi.useHandle(handle -> {
            int execute = handle.execute(
                    String.format(
                            "CREATE TABLE IF NOT EXISTS %s ("
                                    + "objectId varchar(255), "
                                    + "fieldName varchar(255), "
                                    + "fieldValue varchar(255))",
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
            handle.execute(
                    String.format(
                            "insert or replace into %s (id, jsonBlob) values (?, ?)",
                            classInfo.getTableName()),
                    id, blob);

            if (!classInfo.getIndexedFields().isEmpty()) {
                deleteSearchTableEntries(classInfo, id, handle);

                searchFields.forEach(field -> handle.execute(
                        String.format(
                                "insert into %s (objectId, fieldName, fieldValue) values (?, ?, ?)",
                                classInfo.getSearchTableName()),
                        id, field.getFieldName(), field.getFieldValue()));
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

    /*
      Lets talk about this method how it works...
      * First it queries the database and simply has a list of IDs
      * It then collates these IDs by occurrences
      * It sorts these, and converts them to get the backing object,
            to return said backing object in a list
      I've separated these steps to make the code more readable.
      While the steps could all be chained together... there doesn't seem to be much reason to.

      We call list() instead of stream() at the end of the JDBI call
      to ensure that JDBI can quickly and easily close the connection.
     */
    @Override
    public List<String> search(final ClassInfo<?> classInfo, final List<SearchTerm> searchTerms) {
        List<String> searchResults = jdbi.withHandle(handle -> {
            StringBuilder queryString = new StringBuilder("select objectId from ").append(classInfo.getSearchTableName()).append(" where ");
            for (int i = 0; i < searchTerms.size(); i++) {
                if (i != 0) {
                    queryString.append(" or ");
                }

                queryString.append("(fieldName like :fieldName").append(i).append(" and fieldValue like :value").append(i).append(")");
            }

            Query query = handle.createQuery(queryString.toString());

            for (int i = 0; i < searchTerms.size(); i++) {
                query.bind("fieldName" + i, searchTerms.get(i).getFieldName());
                query.bind("value" + i, searchTerms.get(i).getFieldValue());
            }

            return query.mapTo(String.class)
                    .list();
        });

        Set<Map.Entry<String, Long>> searchResultsCollated = searchResults.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet();

        return searchResultsCollated.stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .flatMap(entry -> getById(classInfo, entry.getKey()).stream())
                .collect(Collectors.toList());
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
                    deleteSearchTableEntries(classInfo, id, handle);
                }
        );
    }
}
