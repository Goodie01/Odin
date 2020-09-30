package org.goodiemania.odin.internal.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
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
        this.objectReader = objectMapper.readerFor(classInfo.getRawClass());
        this.objectWriter = objectMapper.writerFor(classInfo.getRawClass());
    }

    @Override
    public void save(final T object) {
        try {
            String id = String.valueOf(classInfo.getIdField().getReadMethod().invoke(object));
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
                .map(this::convertJsonStringToObject);
    }

    @Override
    public void deleteById(final String id) {
        databaseWrapper.deleteById(classInfo, id);
    }

    @Override
    public List<T> search(final List<SearchTerm> searchTerms) {
        return databaseWrapper.search(classInfo, searchTerms)
                .stream()
                .map(this::convertJsonStringToObject)
                .collect(Collectors.toList());
    }

    @Override
    public List<T> getAll() {
        return databaseWrapper.getAll(classInfo)
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
