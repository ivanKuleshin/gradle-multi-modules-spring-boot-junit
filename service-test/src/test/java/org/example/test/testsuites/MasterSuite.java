package org.example.test.testsuites;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SuiteDisplayName("A Master Test Suite")
@SelectClasses({SequentialSuite.class, ParallelSuite.class})
public class MasterSuite {
}

