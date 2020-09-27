package com.radiancegames.odin.internal.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.radiancegames.odin.external.EntityManager;
import com.radiancegames.odin.external.exceptions.EntityParseException;
import com.radiancegames.odin.external.model.SearchTerm;
import com.radiancegames.odin.internal.database.Database;
import com.radiancegames.odin.internal.database.SearchField;
import com.radiancegames.odin.internal.manager.search.SearchFieldGenerator;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EntityManagerImpl<T> implements EntityManager<T> {
    private final ObjectWriter objectWriter;
    private final ObjectReader objectReader;
    private final ClassInfo<T> classInfo;
    private final Database database;
    private final SearchFieldGenerator searchFieldGenerator;

    public EntityManagerImpl(
            final Database database,
            final ObjectMapper objectMapper,
            final SearchFieldGenerator searchFieldGenerator,
            final ClassInfo<T> classInfo) {
        this.classInfo = classInfo;
        this.database = database;
        this.searchFieldGenerator = searchFieldGenerator;
        this.objectReader = objectMapper.readerFor(classInfo.getClassInformation());
        this.objectWriter = objectMapper.writerFor(classInfo.getClassInformation());
    }

    @Override
    public void save(final T object) {
        try {
            String id = String.valueOf(classInfo.getIdField().getReadMethod().invoke(object));
            String blob = objectWriter.writeValueAsString(object);
            List<SearchField> searchFields = searchFieldGenerator.generate(object);

            database.saveEntity(classInfo, id, searchFields, blob);
        } catch (JsonProcessingException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void saveWithAdditionalSearchParams(final T object, final Object... additionalObjects) {
        try {
            String id = String.valueOf(classInfo.getIdField().getReadMethod().invoke(object));
            String blob = objectWriter.writeValueAsString(object);
            List<SearchField> searchFields = Arrays.stream(additionalObjects)
                    .flatMap(o -> searchFieldGenerator.generate(o).stream())
                    .collect(Collectors.toList());
            searchFields.addAll(searchFieldGenerator.generate(object));

            database.saveEntity(classInfo, id, searchFields, blob);
        } catch (JsonProcessingException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Optional<T> getById(final String id) {
        return database.getById(classInfo, id)
                .map(this::convertJsonStringToObject);
    }

    @Override
    public void deleteById(final String id) {
        database.deleteById(classInfo, id);
    }

    @Override
    public List<T> search(final List<SearchTerm> searchTerms) {
        return database.search(classInfo, searchTerms)
                .stream()
                .map(this::convertJsonStringToObject)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getAll() {
        return database.getAll(classInfo)
                .stream()
                .map(this::convertJsonStringToObject)
                .collect(Collectors.toList());
    }

    private T convertJsonStringToObject(final String jsonBlob) {
        try {
            return objectReader.readValue(jsonBlob);
        } catch (IOException e) {
            throw new EntityParseException(e);
        }
    }
}
