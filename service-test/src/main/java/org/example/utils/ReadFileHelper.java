package org.example.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.exceptions.TestExecutionException;

import java.io.File;
import java.io.IOException;

public class ReadFileHelper {
    private static final String RESOURCE_PATH = "src/test/resources/templates/";

    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public static <T> T getRequestAs(String fileTemplate, String fileName, Class<T> aClass){
        File file = new File(RESOURCE_PATH + fileTemplate + "/" + fileName + ".json");

        try {
            return mapper.readValue(file, aClass);
        } catch (IOException ioException) {
            throw new TestExecutionException(ioException.getMessage());
        }
    }
}
