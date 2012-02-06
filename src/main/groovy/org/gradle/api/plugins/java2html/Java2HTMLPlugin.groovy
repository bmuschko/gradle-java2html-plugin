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
package org.gradle.api.plugins.java2html

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
        project.configurations.add(CONFIGURATION_NAME).setVisible(false).setTransitive(true)
               .setDescription('The Java2HTML library to be used for this project.')

        Java2HTMLPluginConvention java2HTMLPluginConvention = new Java2HTMLPluginConvention()
        project.convention.plugins.java2html = java2HTMLPluginConvention

        configureConvertCodeTask(project, java2HTMLPluginConvention)
        configureGenerateOverviewTask(project, java2HTMLPluginConvention)
    }

    private void configureConvertCodeTask(Project project, Java2HTMLPluginConvention java2HTMLPluginConvention) {
        project.tasks.withType(ConvertCodeTask).whenTaskAdded { ConvertCodeTask convertCodeTask ->
            convertCodeTask.conventionMapping.map('classpath') { project.configurations.getByName(CONFIGURATION_NAME).asFileTree }
            convertCodeTask.conventionMapping.map('srcDirs') { java2HTMLPluginConvention.conversion.srcDirs ?: getSrcDirs(project) }
            convertCodeTask.conventionMapping.map('destDir') { getReportDirectory(project, java2HTMLPluginConvention.conversion.destDir) }
            convertCodeTask.conventionMapping.map('includes') { java2HTMLPluginConvention.conversion.includes ?: getDefaultIncludes() }
            convertCodeTask.conventionMapping.map('outputFormat') { java2HTMLPluginConvention.conversion.outputFormat ?: 'html' }
            convertCodeTask.conventionMapping.map('tabs') { java2HTMLPluginConvention.conversion.tabs ?: 2 }
            convertCodeTask.conventionMapping.map('style') { java2HTMLPluginConvention.conversion.style ?: 'eclipse' }
            convertCodeTask.conventionMapping.map('showLineNumbers') { java2HTMLPluginConvention.conversion.showLineNumbers ?: true }
            convertCodeTask.conventionMapping.map('showFileName') { java2HTMLPluginConvention.conversion.showFileName ?: false }
            convertCodeTask.conventionMapping.map('showDefaultTitle') { java2HTMLPluginConvention.conversion.showDefaultTitle ?: false }
            convertCodeTask.conventionMapping.map('showTableBorder') { java2HTMLPluginConvention.conversion.showTableBorder ?: false }
            convertCodeTask.conventionMapping.map('includeDocumentHeader') { java2HTMLPluginConvention.conversion.includeDocumentHeader ?: true }
            convertCodeTask.conventionMapping.map('includeDocumentFooter') { java2HTMLPluginConvention.conversion.includeDocumentFooter ?: true }
            convertCodeTask.conventionMapping.map('addLineAnchors') { java2HTMLPluginConvention.conversion.addLineAnchors ?: false }
            convertCodeTask.conventionMapping.map('lineAnchorPrefix') { java2HTMLPluginConvention.conversion.lineAnchorPrefix ?: '' }
            convertCodeTask.conventionMapping.map('horizontalAlignment') { java2HTMLPluginConvention.conversion.horizontalAlignment ?: 'left' }
            convertCodeTask.conventionMapping.map('useShortFileName') { java2HTMLPluginConvention.conversion.useShortFileName ?: false }
            convertCodeTask.conventionMapping.map('overwrite') { java2HTMLPluginConvention.conversion.overwrite ?: false }
        }

        ConvertCodeTask convertCodeTask = project.tasks.add(CONVERT_CODE_TASK_NAME, ConvertCodeTask)
        convertCodeTask.description = 'Converts source code to Java2HTML documentation for the main source code.'
        convertCodeTask.group = DOCUMENTATION_GROUP
    }

    private void configureGenerateOverviewTask(Project project, Java2HTMLPluginConvention java2HTMLPluginConvention) {
        project.tasks.withType(GenerateOverviewTask).whenTaskAdded { GenerateOverviewTask generateOverviewTask ->
            generateOverviewTask.conventionMapping.map('srcDirs') { java2HTMLPluginConvention.overview.srcDirs ?: getOverviewSourceDirectories(project, java2HTMLPluginConvention.conversion.destDir) }
            generateOverviewTask.conventionMapping.map('destDir') { getReportDirectory(project, java2HTMLPluginConvention.overview.destDir) }
            generateOverviewTask.conventionMapping.map('pattern') { java2HTMLPluginConvention.overview.pattern ?: '**/*.html' }
            generateOverviewTask.conventionMapping.map('windowTitle') { java2HTMLPluginConvention.overview.windowTitle ?: project.name }
            generateOverviewTask.conventionMapping.map('docTitle') { java2HTMLPluginConvention.overview.docTitle ?: project.name }
            generateOverviewTask.conventionMapping.map('docDescription') { java2HTMLPluginConvention.overview.docDescription }
            generateOverviewTask.conventionMapping.map('icon') { java2HTMLPluginConvention.overview.icon }
            generateOverviewTask.conventionMapping.map('stylesheet') { java2HTMLPluginConvention.overview.stylesheet }
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
        GenerateOverviewTask generateOverviewTask = project.tasks.add(GENERATE_OVERVIEW_TASK_NAME, GenerateOverviewTask)
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
