package org.example.test.testcases.getemployeebyid;

import org.example.test.testcases.BaseTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Tag("sequence")
@Execution(ExecutionMode.SAME_THREAD)
public class PutUpdateEmployeeByIdTest extends BaseTest {

    @Test
    void example() {
        System.out.println("Hello world!");
    }
}
