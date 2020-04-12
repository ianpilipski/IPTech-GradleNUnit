package com.iptech.gradle.nunit.tasks

import com.iptech.gradle.nunit.internal.ReportBaseClass

class ReportNUnitReport extends ReportBaseClass {
    @Override
    protected void taskExec(File inFile, File outFile) {
        outFile.text = project.nunit.parseReport(inFile)
    }
}
