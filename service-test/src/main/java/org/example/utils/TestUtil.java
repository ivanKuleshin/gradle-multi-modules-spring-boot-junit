package org.example.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertValueToList(Object valueToConvert, TypeReference<T> typeReference) {
        if (valueToConvert instanceof List) {
            return objectMapper.convertValue(valueToConvert, typeReference);
        } else {
            return objectMapper.convertValue(List.of(valueToConvert), typeReference);
        }
    }

    public static <T> T castMapToObject(Object mapToCast, Class<T> classToCast) {
        return objectMapper.convertValue(mapToCast, classToCast);
    }

    public static String invalidateParam(String param) {
        return switch (param) {
            case "empty" -> "";
            case "blank" -> "    ";
            case "null" -> null;
            default -> param;
        };
    }
}
