package com.iptech.gradle.nunit

import com.iptech.gradle.nunit.internal.XSLTParser
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

class NUnitExtension {
    public final XSLTParser xsltParser
    private Project project

    NUnitExtension(Project project, XSLTParser xsltParser) {
        this.project = project
        this.xsltParser = xsltParser
    }

    void xsltParser(Action<? super XSLTParser> configuration) {
        configuration.execute(xsltParser)
    }

    Integer parseFailureCounts(String fileName) {
        return xsltParser.parseFailureCounts(project.layout.projectDirectory.file(fileName).asFile)
    }

    Integer parseFailureCounts(File file) {
        return xsltParser.parseFailureCounts(file)
    }

    String parseFailures(String fileName) {
        return xsltParser.parseFailures(project.layout.projectDirectory.file(fileName).asFile)
    }

    String parseFailures(File file) {
        return xsltParser.parseFailures(file)
    }

    String parseSummary(String fileName) {
        return xsltParser.parseSummary(project.layout.projectDirectory.file(fileName).asFile)
    }

    String parseSummary(File file) {
        return xsltParser.parseSummary(file)
    }

    String parseReport(String fileName) {
        return xsltParser.parseReport(project.layout.projectDirectory.file(fileName).asFile)
    }

    String parseReport(File file) {
        return xsltParser.parseReport(file)
    }
}
