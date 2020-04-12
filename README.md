# IPTech-GradleNunit
A gradle plugin for parsing NUnit xml files

This plugin exposes an api that can be used to parse NUnit xml results that 
can be used in your own gradle scripts.

The api is accessd through an extension object named "nunit" in your project.

## API

*Integer nunit.parseFailureCounts(File testResultFile)*  
*Integer nunit.parseFailureCounts(String testResultFle)*  
> returns the number of failures parsed in the xml file   
> configuring xsltParser.failureCountsXSLT will replace the built in transform for custom output

*String nunit.parseFailures(File testResultFile)*  
*String nunit.parseFailures(String testResultFile)*
> returns the summary with errors expanded and listed   
> configuring xsltParser.failuresXSLT will replace the built in transform for custom output

*String nunit.parseSummary(File testResultFile)*  
*String nunit.parseSummary(String testResultFile)*
> returns the summary of the test results   
> configuring xsltParser.summaryXSLT will replace the built in transform for custom output

*String nunit.parseReport(File testResultFile)*  
*String nunit.parseReport(String testResultFile)*  
> returns the full report of the test results   
> configuring xsltParser.reportXSLT will replace the built in transform for custom output

## Configuration
If you would like to customize the xslt transforms used to create the output you can provide your own xslt files for each of the above functions.  
    
*Example Configuration*  
```groovy
// gradle.build file
 
nunit {  
    xsltParser {  
        failureCountsXSLT = file('myFailureCounts.xslt')  
        failuresXSLT = file('myFailures.xslt)
        summaryXSLT = file('mySummary.xslt')
        reportXSLT = file('myReport.xslt')
    }
}
```       

## Usage
This plugin simply exposes a few functions that allow you to get the result of an NUnit run from it's XML file.  You can use that output however you like.  
   
```groovy
task showUnitTestResults {
    doLast {
        print project.nunit.parseSummary('build/output/myUnitTestRunResultFile.xml')
        // if you want to fail the build on any test failures
        if(project.nunit.parseFailureCounts('build/output/myUnitTestRunResultFile.xml')>0) {
            throw new GradleException('There were unit test failures')
        }
    }
}
```