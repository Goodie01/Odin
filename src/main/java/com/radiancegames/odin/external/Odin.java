package com.radiancegames.odin.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.radiancegames.odin.internal.OdinImpl;
import com.radiancegames.odin.internal.database.Database;
import com.radiancegames.odin.internal.database.sqlite.SqliteWrapper;
import com.radiancegames.odin.internal.manager.ClassInfo;
import com.radiancegames.odin.internal.manager.ClassInfoBuilder;
import com.radiancegames.odin.internal.manager.ClassManager;
import com.radiancegames.odin.internal.manager.classinfo.Holder;
import com.radiancegames.odin.internal.manager.search.SearchFieldGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/*
    TODO:
        Expand search, eg I should be able to search for a blank field
        write some unit tests
 */

public interface Odin {
    <T> EntityManager<T> createFor(Class<T> entityClass);

    static OdinImpl.OdinBuilder create() {
        return new OdinImpl.OdinBuilder();
    }

    class OdinBuilder {
        private List<String> packageNames = new ArrayList<>();
        private ObjectMapper objectMapper;
        private String jdbcConnectUrl;

        private OdinBuilder() {
        }

        public OdinBuilder setJdbcConnectUrl(final String jdbcConnectUrl) {
            this.jdbcConnectUrl = jdbcConnectUrl;
            return this;
        }

        public OdinBuilder addPackageName(final String packageName) {
            packageNames.add(packageName);
            return this;
        }

        public OdinBuilder setPackageNames(final List<String> packageNames) {
            this.packageNames = packageNames;
            return this;
        }

        public OdinBuilder setObjectMapper(final ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public Odin build() {
            final Database database = new SqliteWrapper(
                    Objects.requireNonNull(jdbcConnectUrl, "JDBC connection URL must be set"));

            if (packageNames.isEmpty()) {
                throw new IllegalStateException("You must provide at least one package name");
            }

            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }

            final ClassManager classManager = new ClassManager(database, new ClassInfoBuilder());
            Set<ClassInfo<?>> classInfoSet = packageNames.stream()
                    .flatMap(classManager::find)
                    .collect(Collectors.toSet());
            Holder classInfoHolder = new Holder(classInfoSet);
            SearchFieldGenerator searchFieldGenerator = new SearchFieldGenerator(classInfoHolder);

            return new OdinImpl(objectMapper, classInfoHolder, database, searchFieldGenerator);
        }
    }
}
