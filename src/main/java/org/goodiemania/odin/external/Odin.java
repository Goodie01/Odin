package org.goodiemania.odin.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.ArrayList;
import java.util.List;
import org.goodiemania.odin.external.exceptions.InvalidArgumentsException;
import org.goodiemania.odin.internal.OdinImpl;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.database.impl.DatabaseWrapperImpl;
import org.goodiemania.odin.internal.manager.ClassFinder;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfoHolder;
import org.goodiemania.odin.internal.manager.search.SearchFieldGenerator;

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
            if (jdbcConnectUrl == null) {
                throw new InvalidArgumentsException("JDBC connection URL must be set");
            }

            if (packageNames.isEmpty()) {
                throw new InvalidArgumentsException("You must provide at least one package name");
            }

            if (objectMapper == null) {
                objectMapper = JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .addModule(new Jdk8Module())
                        .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                        .build();
            }

            final DatabaseWrapper databaseWrapper = new DatabaseWrapperImpl(jdbcConnectUrl);
            final ClassInfoHolder classInfoHolder = new ClassInfoHolder();
            final ClassFinder classFinder = new ClassFinder();
            packageNames.stream()
                    .flatMap((String packageName) -> classFinder.find(packageName).stream())
                    .forEach(classInfoHolder::add);

            classInfoHolder.retrieveAll().forEach(classInfo -> {
                databaseWrapper.createEntityTable(classInfo);

                if (!classInfo.getIndexedFields().isEmpty()) {
                    databaseWrapper.createEntitySearchFieldTable(classInfo);
                }
            });
            SearchFieldGenerator searchFieldGenerator = new SearchFieldGenerator(classInfoHolder);

            return new OdinImpl(objectMapper, classInfoHolder, databaseWrapper, searchFieldGenerator);
        }
    }
}
