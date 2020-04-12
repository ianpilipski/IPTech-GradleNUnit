package com.iptech.gradle.nunit

import com.iptech.gradle.nunit.NUnitExtension
import com.iptech.gradle.nunit.internal.XSLTParser
import org.gradle.api.Plugin
import org.gradle.api.Project

class NUnitPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        NUnitExtension nunit = project.extensions.create('nunit', NUnitExtension, project, new XSLTParser(project.objects))
    }
}
