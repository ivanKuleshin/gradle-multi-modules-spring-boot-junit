package org.example.utils.testdataconverters;

import lombok.extern.slf4j.Slf4j;
import org.example.utils.JsonUtils;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

@Slf4j
public class JsonStringConverter extends SimpleArgumentConverter {

    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        log.info("Converted object: {}", source);
        return JsonUtils.readJsonStringAsObject(source.toString(), targetType);
    }
}
