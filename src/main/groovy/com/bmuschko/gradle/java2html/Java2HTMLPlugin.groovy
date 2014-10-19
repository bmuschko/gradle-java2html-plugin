/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bmuschko.gradle.java2html

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin

/**
 * <p>A {@link org.gradle.api.Plugin} that provides a task for coverting Java and Groovy source code into browsable
 * and colourized HTML using Java2HTML.</p>
 *
 * @author Benjamin Muschko
 */
class Java2HTMLPlugin implements Plugin<Project> {
    static final String CONFIGURATION_NAME = 'java2html'
    static final String CONVERT_CODE_TASK_NAME = 'java2htmlConvertCode'
    static final String GENERATE_OVERVIEW_TASK_NAME = 'java2htmlGenerateOverview'
    static final String DOCUMENTATION_GROUP = 'documentation'
    static final String DEFAULT_JAVA_FILE_PATTERN = '**/*.java'
    static final String DEFAULT_GROOVY_FILE_PATTERN = '**/*.groovy'
    static final String DEFAULT_DOCS_DIR = 'docs/java2html'

    @Override
    void apply(Project project) {
        project.configurations.create(CONFIGURATION_NAME).setVisible(false).setTransitive(true)
               .setDescription('The Java2HTML library to be used for this project.')

        Java2HTMLPluginExtension extension = project.extensions.create('java2html', Java2HTMLPluginExtension)

        configureConvertCodeTask(project, extension)
        configureGenerateOverviewTask(project, extension)
    }

    private void configureConvertCodeTask(Project project, Java2HTMLPluginExtension extension) {
        project.tasks.withType(ConvertCodeTask).whenTaskAdded { ConvertCodeTask convertCodeTask ->
            convertCodeTask.conventionMapping.map('classpath') { project.configurations.getByName(CONFIGURATION_NAME).asFileTree }
            convertCodeTask.conventionMapping.map('srcDirs') { extension.conversion.srcDirs ?: getSrcDirs(project) }
            convertCodeTask.conventionMapping.map('destDir') { getReportDirectory(project, extension.conversion.destDir) }
            convertCodeTask.conventionMapping.map('includes') { extension.conversion.includes ?: getDefaultIncludes() }
            convertCodeTask.conventionMapping.map('outputFormat') { extension.conversion.outputFormat ?: 'html' }
            convertCodeTask.conventionMapping.map('tabs') { extension.conversion.tabs ?: 2 }
            convertCodeTask.conventionMapping.map('style') { extension.conversion.style ?: 'eclipse' }
            convertCodeTask.conventionMapping.map('showLineNumbers') { extension.conversion.showLineNumbers ?: true }
            convertCodeTask.conventionMapping.map('showFileName') { extension.conversion.showFileName ?: false }
            convertCodeTask.conventionMapping.map('showDefaultTitle') { extension.conversion.showDefaultTitle ?: false }
            convertCodeTask.conventionMapping.map('showTableBorder') { extension.conversion.showTableBorder ?: false }
            convertCodeTask.conventionMapping.map('includeDocumentHeader') { extension.conversion.includeDocumentHeader ?: true }
            convertCodeTask.conventionMapping.map('includeDocumentFooter') { extension.conversion.includeDocumentFooter ?: true }
            convertCodeTask.conventionMapping.map('addLineAnchors') { extension.conversion.addLineAnchors ?: false }
            convertCodeTask.conventionMapping.map('lineAnchorPrefix') { extension.conversion.lineAnchorPrefix ?: '' }
            convertCodeTask.conventionMapping.map('horizontalAlignment') { extension.conversion.horizontalAlignment ?: 'left' }
            convertCodeTask.conventionMapping.map('useShortFileName') { extension.conversion.useShortFileName ?: false }
            convertCodeTask.conventionMapping.map('overwrite') { extension.conversion.overwrite ?: false }
        }

        ConvertCodeTask convertCodeTask = project.tasks.create(CONVERT_CODE_TASK_NAME, ConvertCodeTask)
        convertCodeTask.description = 'Converts source code to Java2HTML documentation for the main source code.'
        convertCodeTask.group = DOCUMENTATION_GROUP
    }

    private void configureGenerateOverviewTask(Project project, Java2HTMLPluginExtension extension) {
        project.tasks.withType(GenerateOverviewTask).whenTaskAdded { GenerateOverviewTask generateOverviewTask ->
            generateOverviewTask.conventionMapping.map('srcDirs') { extension.overview.srcDirs ?: getOverviewSourceDirectories(project, extension.conversion.destDir) }
            generateOverviewTask.conventionMapping.map('destDir') { getReportDirectory(project, extension.overview.destDir) }
            generateOverviewTask.conventionMapping.map('pattern') { extension.overview.pattern ?: '**/*.html' }
            generateOverviewTask.conventionMapping.map('windowTitle') { extension.overview.windowTitle ?: project.name }
            generateOverviewTask.conventionMapping.map('docTitle') { extension.overview.docTitle ?: project.name }
            generateOverviewTask.conventionMapping.map('docDescription') { extension.overview.docDescription }
            generateOverviewTask.conventionMapping.map('icon') { extension.overview.icon }
            generateOverviewTask.conventionMapping.map('stylesheet') { extension.overview.stylesheet }
        }

        project.afterEvaluate {
            addGenerateOverviewTask(determineGenerateOverviewTaskProject(project))
        }
    }

    /**
     * Determines project to assign the generate overview task. If it's a multi-module build we only want to assign
     * it to the root project.
     *
     * @param project Project
     * @return Project
     */
    private Project determineGenerateOverviewTaskProject(Project project) {
        (project == project.rootProject && project.subprojects.size() > 0) ? project.rootProject : project
    }

    /**
     * Adds overview generation task to project.
     *
     * @param project Project
     */
    private void addGenerateOverviewTask(Project project) {
        GenerateOverviewTask generateOverviewTask = project.tasks.create(GENERATE_OVERVIEW_TASK_NAME, GenerateOverviewTask)
        generateOverviewTask.description = 'Generates Java2HTML index file.'
        generateOverviewTask.group = DOCUMENTATION_GROUP
    }

    /**
     * Checks to see if Java plugin got applied to project.
     *
     * @param project Project
     * @return Flag
     */
    private boolean hasJavaPlugin(Project project) {
        project.plugins.hasPlugin(JavaPlugin)
    }

    /**
     * Checks to see if Groovy plugin got applied to project.
     *
     * @param project Project
     * @return Flag
     */
    private boolean hasGroovyPlugin(Project project) {
        project.plugins.hasPlugin(GroovyPlugin)
    }

    /**
     * Gets source directories based on the applied project plugins.
     *
     * @param project Project
     * @return Source directories
     */
    private FileCollection getSrcDirs(Project project) {
        if(hasGroovyPlugin(project)) {
            return project.files(project.sourceSets.main.groovy.srcDirs)
        }
        else if(hasJavaPlugin(project)) {
            return project.files(project.sourceSets.main.java.srcDirs)
        }
        
        project.files()
    }

    /**
     * Gets report directory.
     *
     * @param project Project
     * @param conventionValue Convention value
     * @return Report directory
     */
    private File getReportDirectory(Project project, File conventionValue) {
        conventionValue ?: new File(project.buildDir, DEFAULT_DOCS_DIR)
    }

    /**
     * Gets default includes.
     *
     * @return Includes
     */
    private String getDefaultIncludes() {
        "$DEFAULT_JAVA_FILE_PATTERN,$DEFAULT_GROOVY_FILE_PATTERN".toString()
    }

    /**
     * Gets overview source directories.
     *
     * @param project Project
     * @param conventionValue Convention value
     * @return Overview source directories
     */
    private FileCollection getOverviewSourceDirectories(Project project, File conventionValue) {
        def srcDirs = project.files()
        
        if(project.subprojects.size() > 0) {
            project.subprojects.each { Project subproject ->
                srcDirs += project.files(getReportDirectory(subproject, conventionValue))
            }
        }
        else {
            srcDirs += project.files(getReportDirectory(project, conventionValue))
        }

        srcDirs
    }
}
