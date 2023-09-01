package org.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import org.example.exceptions.TestExecutionException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Map;

public final class JsonUtils {

    private static final String JSON_ERROR_MESSAGE = "Problem with json file: %s";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ROOT = "$";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    static {
        //OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String readJsonFile(Path filePath) {
        try {
            return Files.readString(filePath);
        } catch (IOException ioException) {
            throw new TestExecutionException(JSON_ERROR_MESSAGE, ioException.getMessage());
        }
    }

    public static <T> T readJsonFileAsObject(String path, Class<T> classToCast) {
        try {
            return OBJECT_MAPPER.readValue(new File(path), classToCast);
        } catch (IOException ioException) {
            throw new TestExecutionException(JSON_ERROR_MESSAGE, ioException.getMessage());
        }
    }

    public static <T> T readJsonFileAsObject(String path, TypeReference<T> valueTypeRef) {
        try {
            return OBJECT_MAPPER.readValue(new File(path), valueTypeRef);
        } catch (IOException ioException) {
            throw new TestExecutionException(JSON_ERROR_MESSAGE, ioException.getMessage());
        }
    }

    public static <T> T readJsonStringAsObject(String value, Class<T> classToCast) {
        try {
            return OBJECT_MAPPER.readValue(value, classToCast);
        } catch (IOException ioException) {
            throw new TestExecutionException(JSON_ERROR_MESSAGE, ioException.getMessage());
        }
    }

    public static String castObjectToJsonString(Object objectToCast) {
        try {
            return OBJECT_MAPPER.writeValueAsString(objectToCast);
        } catch (JsonProcessingException e) {
            throw new TestExecutionException(e.getMessage());
        }
    }

    public static <T> T updateFieldsByPathInObject(Object object, Map<String, ?> values, Class<T> classToCast) {
        return getDocumentContext(object, values).read(ROOT, classToCast);
    }

    public static <T> T updateFieldsByPathInObject(Object object, Map<String, ?> values, TypeRef<T> typeReference) {
        return getDocumentContext(object, values).read(ROOT, typeReference);
    }

    private static DocumentContext getDocumentContext(Object object, Map<String, ?> values) {
        Configuration configuration = getJsonPathConfiguration();
        DocumentContext documentContext = JsonPath.using(configuration).parse(castObjectToJsonString(object));

        try {
            TestUtils.invalidateParams(values).forEach(documentContext::set);
        } catch (PathNotFoundException e) {
            throw new TestExecutionException("Path not found during update! Reason: %s", e.getMessage());
        }

        return documentContext;
    }

    private static Configuration getJsonPathConfiguration() {
        return Configuration.builder()
                .jsonProvider(new JacksonJsonProvider(OBJECT_MAPPER))
                .mappingProvider(new JacksonMappingProvider(OBJECT_MAPPER))
                .build();
    }
}
