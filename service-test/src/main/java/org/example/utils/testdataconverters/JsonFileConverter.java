package org.example.utils.testdataconverters;

import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import static org.example.utils.JsonUtils.readJsonFileAsObject;

@Slf4j
public class JsonFileConverter extends SimpleArgumentConverter {

    public static final String TEMPLATE_BASE_PATH = "src/test/resources/templates/";
    public static final String RESPONSE_BASE_PATH = TEMPLATE_BASE_PATH + "responses/";
    public static final String REQUEST_BASE_PATH = TEMPLATE_BASE_PATH + "requests/";

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        log.info("Converted object: {}", source);
        return readJsonFileAsObject(getTemplatePath(source), targetType);
    }

    private String getTemplatePath(Object fileName) {
        Path responseTemplate = Path.of(RESPONSE_BASE_PATH + fileName);
        Path requestTemplate = Path.of(REQUEST_BASE_PATH + fileName);

        return Files.exists(responseTemplate) ? responseTemplate.toString() : requestTemplate.toString();
    }
}

