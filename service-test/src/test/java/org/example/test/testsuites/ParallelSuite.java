package org.example.test.testsuites;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@IncludeTags({"parallel"})
@SelectPackages({"org.example.test.testcases"})
@SuiteDisplayName("Parallel Test Suite - Several(All available) Threads")
public class ParallelSuite {
}