package com.iptech.gradle.nunit

import com.iptech.gradle.nunit.internal.XSLTParser
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.ProjectLayout
import org.gradle.api.file.RegularFile
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

import static org.mockito.ArgumentMatchers.isA
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class NUnitExtensionTest {
    static final String EXPECTED_OUTPUT = 'test output'
    static final String EXPECTED_FILE_PATH = 'testfile.path'

    @Mock Project mockProject
    @Mock ProjectLayout mockProjectLayout
    @Mock Directory mockDirectory
    @Mock RegularFile mockRegularFile
    @Mock File mockFile
    @Mock XSLTParser mockXSLTParser

    @Rule
    public MockitoRule mokitoRule = MockitoJUnit.rule()

    NUnitExtension systemUnderTest

    @Before
    void setUp() {
        when(mockProject.layout).thenReturn(mockProjectLayout)
        when(mockProjectLayout.projectDirectory).thenReturn(mockDirectory)
        when(mockDirectory.file((String)isA(String.class))).thenReturn(mockRegularFile)
        when(mockRegularFile.asFile).thenReturn(mockFile)
        when(mockXSLTParser.parseFailureCounts(mockFile)).thenReturn(10)
        when(mockXSLTParser.parseFailures(mockFile)).thenReturn(EXPECTED_OUTPUT)
        when(mockXSLTParser.parseSummary(mockFile)).thenReturn(EXPECTED_OUTPUT)
        when(mockXSLTParser.parseReport(mockFile)).thenReturn(EXPECTED_OUTPUT)

        systemUnderTest = new NUnitExtension(mockProject, mockXSLTParser)
    }

    @Test
    void parseReport_whenCalledWithFile_returnsExpectedValue() {
        String actual = systemUnderTest.parseReport(mockFile)

        verify(mockXSLTParser).parseReport(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }

    @Test
    void parseReport_whenCalledWithString_returnsExpectedValue() {
        String actual = systemUnderTest.parseReport(EXPECTED_FILE_PATH)

        verify(mockDirectory).file((String)EXPECTED_FILE_PATH)
        verify(mockXSLTParser).parseReport(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }

    @Test
    void parseFailureCounts_whenCalledWithFile_returnsExpectedValue() {
        Integer actual = systemUnderTest.parseFailureCounts(mockFile)

        verify(mockXSLTParser).parseFailureCounts(mockFile)
        Assert.assertEquals(10, actual)
    }

    @Test
    void parseFailureCounts_whenCalledWithString_returnsExpectedValue() {
        Integer actual = systemUnderTest.parseFailureCounts(EXPECTED_FILE_PATH)

        verify(mockDirectory).file((String)EXPECTED_FILE_PATH)
        verify(mockXSLTParser).parseFailureCounts(mockFile)
        Assert.assertEquals(10, actual)
    }

    @Test
    void parseFailures_whenCalledWithFile_returnsExpectedValue() {
        String actual = systemUnderTest.parseFailures(mockFile)

        verify(mockXSLTParser).parseFailures(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }

    @Test
    void parseFailures_whenCalledWithString_returnsExpectedValue() {
        String actual = systemUnderTest.parseFailures(EXPECTED_FILE_PATH)

        verify(mockDirectory).file((String)EXPECTED_FILE_PATH)
        verify(mockXSLTParser).parseFailures(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }

    @Test
    void parseSummary_whenCalledWithFile_returnsExpectedValue() {
        String actual = systemUnderTest.parseSummary(mockFile)

        verify(mockXSLTParser).parseSummary(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }

    @Test
    void parseSummary_whenCalledWithString_returnsExpectedValue() {
        String actual = systemUnderTest.parseSummary(EXPECTED_FILE_PATH)

        verify(mockDirectory).file((String)EXPECTED_FILE_PATH)
        verify(mockXSLTParser).parseSummary(mockFile)
        Assert.assertEquals(EXPECTED_OUTPUT, actual)
    }
}
