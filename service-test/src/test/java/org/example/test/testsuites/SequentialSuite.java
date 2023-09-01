package org.example.test.testsuites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@IncludeTags("sequence")
@SelectPackages({"org.example.test.testcases"})
@SuiteDisplayName("Sequential Test Suite - One Thread")
public class SequentialSuite {
}

