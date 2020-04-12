package com.iptech.gradle.nunit.internal

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ReportBaseClass extends DefaultTask {
    @InputFile final RegularFileProperty resultFile = project.objects.fileProperty()
    @OutputFile final RegularFileProperty outputFile = project.objects.fileProperty()

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
