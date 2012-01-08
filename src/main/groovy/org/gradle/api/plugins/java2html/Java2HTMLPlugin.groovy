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
    static final String TASK_NAME = 'java2html'
    static final String DOCUMENTATION_GROUP = 'documentation'
    static final String DEFAULT_JAVA_FILE_PATTERN = '**/*.java'
    static final String DEFAULT_GROOVY_FILE_PATTERN = '**/*.groovy'

    @Override
    void apply(Project project) {
        project.configurations.add(CONFIGURATION_NAME).setVisible(false).setTransitive(true)
               .setDescription('The Java2HTML library to be used for this project.')

        Java2HTMLPluginConvention java2HTMLPluginConvention = new Java2HTMLPluginConvention()
        project.convention.plugins.java2html = java2HTMLPluginConvention

        configureJava2HTMLTask(project, java2HTMLPluginConvention)
    }

    private void configureJava2HTMLTask(Project project, Java2HTMLPluginConvention java2HTMLPluginConvention) {
        project.tasks.withType(Java2HTMLTask).whenTaskAdded { Java2HTMLTask java2HTMLTask ->
            java2HTMLTask.conventionMapping.map('classpath') { project.configurations.getByName(CONFIGURATION_NAME).asFileTree }
            java2HTMLTask.conventionMapping.map('srcDirs') { getSrcDirs(project) }
            java2HTMLTask.conventionMapping.map('destDir') { java2HTMLPluginConvention.destDir ?: new File(project.docsDir, 'java2html') }
            java2HTMLTask.conventionMapping.map('includes') { java2HTMLPluginConvention.includes ?: "$DEFAULT_JAVA_FILE_PATTERN,$DEFAULT_GROOVY_FILE_PATTERN".toString() }
            java2HTMLTask.conventionMapping.map('outputFormat') { java2HTMLPluginConvention.outputFormat ?: 'html' }
            java2HTMLTask.conventionMapping.map('tabs') { java2HTMLPluginConvention.tabs ?: 2 }
            java2HTMLTask.conventionMapping.map('style') { java2HTMLPluginConvention.style ?: 'eclipse' }
            java2HTMLTask.conventionMapping.map('showLineNumbers') { java2HTMLPluginConvention.showLineNumbers ?: true }
            java2HTMLTask.conventionMapping.map('showFileName') { java2HTMLPluginConvention.showFileName ?: false }
            java2HTMLTask.conventionMapping.map('showDefaultTitle') { java2HTMLPluginConvention.showDefaultTitle ?: false }
            java2HTMLTask.conventionMapping.map('showTableBorder') { java2HTMLPluginConvention.showTableBorder ?: false }
            java2HTMLTask.conventionMapping.map('includeDocumentHeader') { java2HTMLPluginConvention.includeDocumentHeader ?: true }
            java2HTMLTask.conventionMapping.map('includeDocumentFooter') { java2HTMLPluginConvention.includeDocumentFooter ?: true }
            java2HTMLTask.conventionMapping.map('addLineAnchors') { java2HTMLPluginConvention.addLineAnchors ?: false }
            java2HTMLTask.conventionMapping.map('lineAnchorPrefix') { java2HTMLPluginConvention.lineAnchorPrefix ?: '' }
            java2HTMLTask.conventionMapping.map('horizontalAlignment') { java2HTMLPluginConvention.horizontalAlignment ?: 'left' }
            java2HTMLTask.conventionMapping.map('useShortFileName') { java2HTMLPluginConvention.useShortFileName ?: false }
            java2HTMLTask.conventionMapping.map('overwrite') { java2HTMLPluginConvention.overwrite ?: false }
        }

        project.afterEvaluate {
            if(hasJavaPlugin(project) || hasGroovyPlugin(project)) {
                Java2HTMLTask java2HTMLTask = project.tasks.add(TASK_NAME, Java2HTMLTask)
                java2HTMLTask.description = 'Generates Java2HTML documentation for the main source code.'
                java2HTMLTask.group = DOCUMENTATION_GROUP
            }
        }
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
    private Set<File> getSrcDirs(Project project) {
        if(hasGroovyPlugin(project)) {
            return project.sourceSets.main.groovy.srcDirs
        }
        else if(hasJavaPlugin(project)) {
            return project.sourceSets.main.java.srcDirs
        }
    }
}
