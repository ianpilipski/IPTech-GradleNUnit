package com.iptech.gradle.nunit.internal

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFile
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ReportBaseClass extends DefaultTask {
    @InputFile
    final RegularFileProperty resultFile = project.objects.fileProperty()

    @OutputFile
    final RegularFileProperty outputFile = project.objects.fileProperty()

    void setResultFile(Object value) {
        resultFile.set(project.file(value))
    }

    void setResultFile(RegularFileProperty value) {
        resultFile.set(value)
    }

    void setResultFile(Provider<? extends RegularFile> value) {
        resultFile.set(value)
    }

    void setOutputFile(Object value) {
        outputFile.set(project.file(value))
    }

    void setOutputFile(RegularFileProperty value) {
        outputFile.set(value)
    }

    void setOutputFile(Provider<? extends RegularFile> value) {
        outputFile.set(value)
    }

    @TaskAction
    private void exec() {
        File inFile = resultFile.get().asFile
        logger.quiet("nunit file = $inFile")
        File outFile = outputFile.get().asFile
        logger.quiet("ouput file = $outFile")
        taskExec(inFile, outFile)
    }

    abstract protected void taskExec(File inFile, File outFile);
}
