package org.example.test.testcases.getemployeebyid;

import org.example.ServiceClass;
import org.example.test.testcases.BaseTest;
import org.example.utils.testdataconverters.JsonStringConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Map;

@Tag("parallel")
@Execution(ExecutionMode.CONCURRENT)
public class GetEmployeeByIdTest extends BaseTest {
    protected static final String TEST_DATA_PATH = "/testdata/getemployeebyid/";
    protected static final char PIPE_DELIMITER = '|';

    @Test
    public void test1() {
        ServiceClass serviceClass = new ServiceClass("TEST");
        Assertions.assertEquals(serviceClass.serviceClassFieldName, "TEST");
        System.out.println(serviceClass.serviceClassFieldName);
    }

    @ParameterizedTest
    @Disabled
    @CsvFileSource(resources = TEST_DATA_PATH + "getEmployeeByIdPositiveCase.csv", numLinesToSkip = 1, delimiter = PIPE_DELIMITER)
    void getEmployeeById(@ConvertWith(JsonStringConverter.class) Map<String, Object> pathParams,
                         @ConvertWith(JsonStringConverter.class) Map<String, String> urlParams) {

    }
}
