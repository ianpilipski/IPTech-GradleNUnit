package com.iptech.gradle.nunit

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class NUnitPluginTest {
    @Rule public TemporaryFolder testProjectDir = new TemporaryFolder()
    File buildFile

    @Before
    void setUp() {
        buildFile = testProjectDir.newFile('build.gradle')
        buildFile << """
            plugins {
                id 'com.iptech.gradle.nunit-plugin'
            }
        """
    }

    @Test
    void applyPlugin_addsTheNUNITExtensionObject() {
        buildFile << """
            task checkNUnitApplied {
                doLast {
                    if(!project.hasProperty('nunit')) {
                        throw new GradleException('could not find the nunit extension')
                    }
                }
            }
        """
        BuildResult result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments('checkNUnitApplied')
            .withPluginClasspath()
            .build()

        Assert.assertEquals(TaskOutcome.SUCCESS, result.task(":checkNUnitApplied").outcome)
    }

    @Test
    void providingConfigurationForXSLT_overridesTheValuesProperly() {
        buildFile << """
        File f1 = file('testFailures.xslt')
        File f2 = file('testFailureCounts.xslt')
        File f3 = file('summary.xslt')
        File f4 = file('report.xslt')
        nunit {
            xsltParser {
                failuresXSLT = f1
                failureCountsXSLT = f2
                summaryXSLT = f3
                reportXSLT = f4
            }
        }
        
        task checkXSLTParserConfig {
            doLast {
                if(!(
                    project.nunit.xsltParser.failuresXSLT.isPresent() &&
                    project.nunit.xsltParser.failureCountsXSLT.isPresent() &&
                    project.nunit.xsltParser.summaryXSLT.isPresent() &&
                    project.nunit.xsltParser.reportXSLT.isPresent()
                )) {
                    throw new GradleException('one of the xsltParser properties was not configured properly')
                }
                
                if(!(
                    f1.name == project.nunit.xsltParser.failuresXSLT.get().asFile.name &&
                    f2.name == project.nunit.xsltParser.failureCountsXSLT.get().asFile.name &&
                    f3.name == project.nunit.xsltParser.summaryXSLT.get().asFile.name &&
                    f4.name == project.nunit.xsltParser.reportXSLT.get().asFile.name 
                )) {
                    throw new GradleException('no good')
                }
            }
        }
        """

        BuildResult result = GradleRunner.create()
                .withProjectDir(testProjectDir.root)
                .withArguments('checkXSLTParserConfig')
                .withPluginClasspath()
                .build()
    }
}
