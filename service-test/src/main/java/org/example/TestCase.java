package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCase {

    @Test
    public void test1() {
        ServiceClass serviceClass = new ServiceClass("TEST");
        Assertions.assertEquals(serviceClass.serviceClassFieldName, "TEST");
    }
}
