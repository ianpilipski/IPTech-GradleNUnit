package com.iptech.gradle.nunit.internal

import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.model.ObjectFactory
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import static org.mockito.Mockito.when

class XSLTParserWithOverridesTest {
    static final String RESULT_FILE_WITH_ERRORS = '/nunit-result-witherrors.xml'
    static final String RESULT_FILE_WITHOUT_ERRORS = '/nunit-result-noerrors.xml'

    static final String EXPECTED_PARSERESULT = 'overriden result'

    static final String OVERRIDEN_XSLT_FAILURECOUNTS =
        '<?xml version="1.0" encoding="UTF-8" ?>' +
        '<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">' +
        '<xsl:output method="text"/>' +
        '<xsl:template match="/">' +
        '<xsl:value-of select="\'777\'" />' +
        '</xsl:template>' +
        '</xsl:stylesheet>'

    static final String OVERRIDDEN_XSLT =
        '<?xml version="1.0" encoding="UTF-8" ?>' +
        '<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">' +
        '<xsl:output method="text"/>' +
        '<xsl:template match="/">' +
        '<xsl:value-of select="\'overriden result\'" />' +
        '</xsl:template>' +
        '</xsl:stylesheet>'

    @Mock RegularFileProperty mockRegularFileProperty
    @Mock RegularFile mockRegularFile
    @Mock ObjectFactory mockObjectFactory

    @Rule public MockitoRule mockitoRule = MockitoJUnit.rule()
    @Rule public TemporaryFolder temporaryFolder = new TemporaryFolder()

    File fileWithErrors
    File fileWithoutErrors
    File overrideXSLTFile
    File overrideXSLTFailureCountsFile
    XSLTParser systemUnderTest

    @Before
    void setUp() {
        overrideXSLTFile = new File(temporaryFolder.root, 'temp.xslt')
        overrideXSLTFile.text = OVERRIDDEN_XSLT
        overrideXSLTFailureCountsFile = new File(temporaryFolder.root, 'tempfailurecounts.xslt')
        overrideXSLTFailureCountsFile.text = OVERRIDEN_XSLT_FAILURECOUNTS

        fileWithErrors = loadResourceFile(RESULT_FILE_WITH_ERRORS)
        fileWithoutErrors = loadResourceFile(RESULT_FILE_WITHOUT_ERRORS)
        when(mockObjectFactory.fileProperty()).thenReturn(mockRegularFileProperty)
        when(mockRegularFileProperty.isPresent()).thenReturn(true)
        when(mockRegularFileProperty.get()).thenReturn(mockRegularFile)
        when(mockRegularFile.asFile).thenReturn(overrideXSLTFile)

        systemUnderTest = new XSLTParser(mockObjectFactory)
    }

    @Test
    void parseFailureCounts_whenResultHasNoError_returnsZero() {
        when(mockRegularFile.asFile).thenReturn(overrideXSLTFailureCountsFile)
        Integer actual = systemUnderTest.parseFailureCounts(fileWithoutErrors)

        Assert.assertEquals(777, actual)
    }

    @Test
    void parseFailureCounts_whenResultHasErrors_returnsErrorCount() {
        when(mockRegularFile.asFile).thenReturn(overrideXSLTFailureCountsFile)
        Integer actual = systemUnderTest.parseFailureCounts(fileWithErrors)

        Assert.assertEquals(777, actual)
    }

    @Test
    void parseFailures_returnsExpectedResult() {
        String actual = systemUnderTest.parseFailures(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSERESULT, actual)
    }

    @Test
    void parseSummary_returnsExpectedResult() {
        String actual = systemUnderTest.parseSummary(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSERESULT, actual)
    }

    @Test
    void parseReport_returnsExpectedResult() {
        String actual = systemUnderTest.parseReport(fileWithErrors)

        Assert.assertEquals(EXPECTED_PARSERESULT, actual)
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
