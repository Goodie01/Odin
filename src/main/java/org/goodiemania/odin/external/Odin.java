package org.goodiemania.odin.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.goodiemania.odin.external.exceptions.InvalidArgumentsException;
import org.goodiemania.odin.internal.OdinImpl;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.database.sqlite.SqliteWrapper;
import org.goodiemania.odin.internal.manager.ClassManager;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoBuilder;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.goodiemania.odin.internal.manager.search.SearchFieldGenerator;

/*
    TODO:
        Expand search, eg I should be able to search for a blank field
        write some unit tests
 */

public interface Odin {
    <T> EntityManager<T> createFor(Class<T> entityClass);

    static Odin.OdinBuilder create() {
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
            if(jdbcConnectUrl == null) {
                throw new InvalidArgumentsException("JDBC connection URL must be set");
            }

            if (packageNames.isEmpty()) {
                throw new InvalidArgumentsException("You must provide at least one package name");
            }

            if (objectMapper == null) {
                objectMapper = new ObjectMapper();
            }

            final DatabaseWrapper databaseWrapper = new SqliteWrapper(jdbcConnectUrl);
            final ClassManager classManager = new ClassManager(databaseWrapper, new ClassInfoBuilder());
            Set<ClassInfo<?>> classInfoSet = packageNames.stream()
                    .flatMap(classManager::find)
                    .collect(Collectors.toSet());
            ClassInfoHolder classInfoHolder = new ClassInfoHolder(classInfoSet);
            SearchFieldGenerator searchFieldGenerator = new SearchFieldGenerator(classInfoHolder);

            return new OdinImpl(objectMapper, classInfoHolder, databaseWrapper, searchFieldGenerator);
        }
    }
}
