package org.goodiemania.odin.internal.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.goodiemania.odin.external.EntityManager;
import org.goodiemania.odin.external.exceptions.EntityParseException;
import org.goodiemania.odin.external.exceptions.EntityWritingException;
import org.goodiemania.odin.external.exceptions.ShouldNeverHappenException;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.database.DatabaseWrapper;
import org.goodiemania.odin.internal.database.SearchField;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;
import org.goodiemania.odin.internal.manager.search.SearchFieldGenerator;

public class EntityManagerImpl<T> implements EntityManager<T> {
    private final ObjectWriter objectWriter;
    private final ObjectReader objectReader;
    private final ObjectReader objectListReader;
    private final ClassInfo<T> classInfo;
    private final DatabaseWrapper databaseWrapper;
    private final SearchFieldGenerator searchFieldGenerator;

    public EntityManagerImpl(
            final DatabaseWrapper databaseWrapper,
            final ObjectMapper objectMapper,
            final SearchFieldGenerator searchFieldGenerator,
            final ClassInfo<T> classInfo) {
        this.classInfo = classInfo;
        this.databaseWrapper = databaseWrapper;
        this.searchFieldGenerator = searchFieldGenerator;
        this.objectWriter = objectMapper.writerFor(classInfo.getRawClass());
        this.objectReader = objectMapper.readerFor(classInfo.getRawClass());
        this.objectListReader = objectMapper.readerFor(new TypeReference<List<T>>() {
        });
    }

    @Override
    public void save(final T object) {
        try {
            String id = String.valueOf(classInfo.getIdField().readMethod().invoke(object));
            String blob = objectWriter.writeValueAsString(object);
            List<SearchField> searchFields = searchFieldGenerator.generate(object);

            databaseWrapper.saveEntity(classInfo, id, searchFields, blob);
        } catch (JsonProcessingException e) {
            throw new EntityWritingException(e);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new ShouldNeverHappenException(e);
        }
    }

    @Override
    public Optional<T> getById(final String id) {
        return databaseWrapper.getById(classInfo, id)
                .map(this::proProcessJsonBlob)
                .map(jsonBlob -> {
                    try {
                        return objectReader.readValue(jsonBlob);
                    } catch (IOException e) {
                        throw new EntityParseException(e);
                    }
                });
    }

    @Override
    public void deleteById(final String id) {
        databaseWrapper.deleteById(classInfo, id);
    }

    @Override
    public List<T> search(final SearchTerm searchTerms) {
        return processList(databaseWrapper.search(classInfo, searchTerms));
    }

    @Override
    public List<T> getAll() {
        return processList(databaseWrapper.getAll(classInfo));
    }

    private List<T> processList(List<String> listOfObjects) {
        String joinedString = listOfObjects.stream()
                .map(this::proProcessJsonBlob)
                .collect(Collectors.joining(","));

        try {
            return objectListReader.readValue("[%s]".formatted(joinedString));
        } catch (IOException e) {
            throw new EntityParseException(e);
        }
    }

    private String proProcessJsonBlob(final String jsonBlob) {
        //If this is probably a H2 database then...
        if (jsonBlob.charAt(0) == '"' && jsonBlob.charAt(jsonBlob.length() - 1) == '"') {
            String substring = jsonBlob.substring(1, jsonBlob.length() - 1);
            return substring.replace("\\\"", "\"");
        }
        return jsonBlob;
    }
}
