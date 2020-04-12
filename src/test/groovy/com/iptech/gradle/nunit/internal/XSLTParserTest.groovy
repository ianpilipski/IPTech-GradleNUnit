package com.iptech.gradle.nunit.internal

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import static org.mockito.Mockito.*

class XSLTParserTest {
    static final String RESULT_FILE_WITH_ERRORS = '/nunit-result-witherrors.xml'
    static final String RESULT_FILE_WITHOUT_ERRORS = '/nunit-result-noerrors.xml'

    static final String EXPECTED_PARSEFAILURES_RESULT =
            'Command Line \r\n' +
            'test commandline that ran the unit tests\r\n' +
            '\r\n' +
            'Runtime Environment \r\n' +
            '   OS Version: testOS,1.0\r\n' +
            '  CLR Version: 4.0.30319.42000\r\n' +
            '\r\n' +
            'NUnit Version: 3.5.0.0\r\n' +
            '\r\n' +
            'Errors and Failures\r\n' +
            '\r\n' +
            '1) Failed : testName-1422\r\n' +
            'this test failed\r\n' +
            'this is a test stack trace\r\n' +
            '\r\n' +
            '2) Failed : testName-1423\r\n' +
            'this test also failed\r\n' +
            'this is also a test stack trace\r\n' +
            '\r\n' +
            'Test Run Summary \r\n' +
            ' Overall result: Failed\r\n' +
            ' Test Count: 3, Passed: 1, Failed: 2, Inconclusive: 0, Skipped: 0\r\n' +
            '   Failed Tests - Failures: 2, Errors: 0, Invalid: 0\r\n' +
            ' Start time: 2020-04-11 02:00:37Z\r\n' +
            '   End time: 2020-04-11 02:04:02Z\r\n' +
            '   Duration: 205.337 seconds\r\n' +
            '\r\n'

    final static String EXPECTED_PARSESUMMARY_RESULT =
            'Command Line \r\n' +
            'test commandline that ran the unit tests\r\n' +
            '\r\n' +
            'Runtime Environment \r\n' +
            '   OS Version: testOS,1.0\r\n' +
            '  CLR Version: 4.0.30319.42000\r\n' +
            '\r\n' +
            'NUnit Version: 3.5.0.0\r\n' +
            '\r\n' +
            'Test Run Summary \r\n' +
            ' Overall result: Failed\r\n' +
            ' Test Count: 3, Passed: 1, Failed: 2, Inconclusive: 0, Skipped: 0\r\n' +
            '   Failed Tests - Failures: 2, Errors: 0, Invalid: 0\r\n' +
            ' Start time: 2020-04-11 02:00:37Z\r\n' +
            '   End time: 2020-04-11 02:04:02Z\r\n' +
            '   Duration: 205.337 seconds\r\n' +
            '\r\n'

    final static String EXPECTED_PARSEREPORT_RESULT =
            'Command Line \r\n' +
                    'test commandline that ran the unit tests\r\n' +
                    '\r\n' +
                    'Runtime Environment \r\n' +
                    '   OS Version: testOS,1.0\r\n' +
                    '  CLR Version: 4.0.30319.42000\r\n' +
                    '\r\n' +
                    'NUnit Version: 3.5.0.0\r\n' +
                    '\r\n' +
                    'Test Files \r\n' +
                    '\r\n' +
                    '\r\n' +
                    'Errors and Failures\r\n' +
                    '\r\n' +
                    '1) Failed : testName-1422\r\n' +
                    'this test failed\r\n' +
                    'this is a test stack trace\r\n' +
                    '2) Failed : testName-1423\r\n' +
                    'this test also failed\r\n' +
                    'this is also a test stack trace\r\n' +
                    'Run Settings\r\n' +
                    '    DefaultTimeout: \r\n' +
                    '    WorkDirectory: \r\n' +
                    '    ImageRuntimeVersion: \r\n' +
                    '    ImageTargetFrameworkName: \r\n' +
                    '    ImageRequiresX86: \r\n' +
                    '    ImageRequiresDefaultAppDomainAssemblyResolver: \r\n' +
                    '    NumberOfTestWorkers: \r\n' +
                    '\r\n' +
                    'Test Run Summary \r\n' +
                    ' Overall result: Failed\r\n' +
                    ' Test Count: 3, Passed: 1, Failed: 2, Inconclusive: 0, Skipped: 0\r\n' +
                    '   Failed Tests - Failures: 2, Errors: 0, Invalid: 0\r\n' +
                    ' Start time: 2020-04-11 02:00:37Z\r\n' +
                    '   End time: 2020-04-11 02:04:02Z\r\n' +
                    '   Duration: 205.337 seconds\r\n' +
                    '\r\n'

    @Mock RegularFileProperty mockRegularFileProperty
    @Mock ObjectFactory mockObjectFactory

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule()

    File fileWithErrors
    File fileWithoutErrors
    XSLTParser systemUnderTest

    @Before
    void setUp() {
        fileWithErrors = loadResourceFile(RESULT_FILE_WITH_ERRORS)
        fileWithoutErrors = loadResourceFile(RESULT_FILE_WITHOUT_ERRORS)
        when(mockObjectFactory.fileProperty()).thenReturn(mockRegularFileProperty)
        when(mockRegularFileProperty.isPresent()).thenReturn(false)

        systemUnderTest = new XSLTParser(mockObjectFactory)
    }

    @Test
    void parseFailureCounts_whenResultHasNoError_returnsZero() {
        Integer actual = systemUnderTest.parseFailureCounts(fileWithoutErrors)

        Assert.assertEquals(0, actual)
    }

    @Test
    void parseFailureCounts_whenResultHasErrors_returnsErrorCount() {
        Integer actual = systemUnderTest.parseFailureCounts(fileWithErrors)

        Assert.assertEquals(2, actual)
    }

    @Test
    void parseFailures_returnsExpectedResult() {
        String actual = systemUnderTest.parseFailures(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSEFAILURES_RESULT, actual)
    }

    @Test
    void parseSummary_returnsExpectedResult() {
        String actual = systemUnderTest.parseSummary(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSESUMMARY_RESULT, actual)
    }

    @Test
    void parseReport_returnsExpectedResult() {
        String actual = systemUnderTest.parseReport(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSEREPORT_RESULT, actual)
    }

    @Test
    void parse_returnsExpectedResult() {
        String actual = systemUnderTest.parse(
                '<?xml version="1.0" encoding="UTF-8" ?><group><one>hello</one><two>world</two></group>',
                '<?xml version="1.0" encoding="UTF-8" ?>' +
                '<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">' +
                '<xsl:output method="text"/>' +
                '<xsl:template match="/">' +
                '<xsl:value-of select="\'idowhatiwant\'" />' +
                '</xsl:template>' +
                '</xsl:stylesheet>'
        )

        Assert.assertEquals('idowhatiwant', actual)
    }

    private File loadResourceFile(String name) {
        return new File(getClass().getResource(name).getFile())
    }
}
