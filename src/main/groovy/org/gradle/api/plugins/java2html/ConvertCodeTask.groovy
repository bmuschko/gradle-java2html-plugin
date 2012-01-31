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

import groovy.util.logging.Slf4j
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Java2HTML files documentation generation task.
 *
 * @author Benjamin Muschko
 */
@Slf4j
class ConvertCodeTask extends DefaultTask {
    @InputFiles FileCollection classpath
    @InputFiles Set<File> srcDirs
    @OutputDirectory File destDir
    @Input String includes
    String outputFormat
    Integer tabs
    String style
    Boolean showLineNumbers
    Boolean showFileName
    Boolean showDefaultTitle
    Boolean showTableBorder
    Boolean includeDocumentHeader
    Boolean includeDocumentFooter
    Boolean addLineAnchors
    String lineAnchorPrefix
    String horizontalAlignment
    Boolean useShortFileName
    Boolean overwrite

    @TaskAction
    void start() {
        generateReport()
    }

    private void generateReport() {
        log.info 'Starting to convert source code to Java2HTML documentation.'

        ant.taskdef(name: 'java2html', classname: 'de.java2html.anttasks.Java2HtmlTask', classpath: getClasspath().asPath)
        getSrcDirs().each { srcDir ->
            ant.java2html(srcdir: srcDir, destDir: getDestDir().canonicalPath, includes: getIncludes(), outputFormat: getOutputFormat(), tabs: getTabs(), style: getStyle(),
                          showLineNumbers: getShowLineNumbers(), showFileName: getShowFileName(), showDefaultTitle: getShowDefaultTitle(),
                          showTableBorder: getShowTableBorder(), includeDocumentHeader: getIncludeDocumentHeader(), includeDocumentFooter: getIncludeDocumentFooter(),
                          addLineAnchors: getAddLineAnchors(), lineAnchorPrefix: getLineAnchorPrefix(), horizontalAlignment: getHorizontalAlignment(),
                          useShortFileName: getUseShortFileName(), overwrite: getOverwrite())
        }

        log.info 'Finished converting source code to Java2HTML documentation.'
    }
}
