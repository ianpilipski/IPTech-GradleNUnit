package com.iptech.gradle.nunit.tasks

import com.iptech.gradle.nunit.internal.ReportBaseClass

class ReportNUnitFailureCounts extends ReportBaseClass {
    @Override
    protected void taskExec(File inFile, File outFile) {
        outFile.text = project.nunit.parseFailureCounts(inFile)
    }
}
