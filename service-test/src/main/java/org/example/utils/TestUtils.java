package org.example.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class TestUtils {
    private static final Map<Object, Object> invalidParams;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        invalidParams =
                new ImmutableMap.Builder<>()
                        .put("[]", Collections.emptyList())
                        .put("[:]", Collections.emptyMap())
                        .put("[emptyString]", StringUtils.EMPTY)
                        .put("[blankString]", StringUtils.SPACE)
                        .build();

        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private TestUtils() {
    }

    public static Object invalidateParam(String param) {
        return invalidParams.getOrDefault(param, param);
    }

    public static Map<String, Object> invalidateParams(Map<String, ?> params) {
        Map<String, Object> invalidatedMap = new HashMap<>();
        IterableUtils.forEach(params.entrySet(), x -> {
            var invalidValue = x.getValue();

            Object invalidatedValue = invalidValue instanceof Map
                    ? invalidateParams(convertObject(invalidValue, new TypeReference<>() {}))
                    : invalidateParam(invalidValue.toString());

            invalidatedMap.put(x.getKey(), invalidatedValue);
        });

        return invalidatedMap;
    }

    public static <T> T convertObject(Object object, TypeReference<T> typeReference) {
        return OBJECT_MAPPER.convertValue(object, typeReference);
    }

    public static <T> T convertObject(Object object, Class<T> classToCast) {
        return OBJECT_MAPPER.convertValue(object, classToCast);
    }

    public static <T> T castToList(Object valueToConvert) {
        return Optional.ofNullable(valueToConvert)
                .map(value -> {
                    if (value instanceof List) {
                        return convertObject(value, new TypeReference<>() {});
                    } else {
                        return convertObject(List.of(value), new TypeReference<T>() {});
                    }
                }).orElse(null);
    }

    @SafeVarargs
    public static Map<String, Object> mergeMaps(Map<String, ?>... maps) {
        return Stream.of(maps)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <T> List<T> filterList(List<T> listToFilter, Predicate<? super T> predicate) {
        CollectionUtils.filter(listToFilter, predicate);

        return listToFilter;
    }

}
