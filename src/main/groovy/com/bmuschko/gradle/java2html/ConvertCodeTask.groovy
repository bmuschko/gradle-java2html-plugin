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

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Java2HTML files documentation generation task.
 *
 * @author Benjamin Muschko
 */
@CacheableTask
class ConvertCodeTask extends DefaultTask {
    @PathSensitive(PathSensitivity.RELATIVE)
    @Classpath FileCollection classpath

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles FileCollection srcDirs

    @PathSensitive(PathSensitivity.RELATIVE)
    @OutputDirectory File destDir

    @Input String includes
    @Input String outputFormat
    @Input Integer tabs
    @Input String style
    @Input Boolean showLineNumbers
    @Input Boolean showFileName
    @Input Boolean showDefaultTitle
    @Input Boolean showTableBorder
    @Input Boolean includeDocumentHeader
    @Input Boolean includeDocumentFooter
    @Input Boolean addLineAnchors
    @Input String lineAnchorPrefix
    @Input String horizontalAlignment
    @Input Boolean useShortFileName
    @Input Boolean overwrite

    @TaskAction
    void start() {
        generateReport()
    }

    private void generateReport() {
        logger.info 'Starting to convert source code to Java2HTML documentation.'

        ant.taskdef(name: 'java2html', classname: 'de.java2html.anttasks.Java2HtmlTask', classpath: getClasspath().asPath)
        getSrcDirs().each { srcDir ->
            ant.java2html(srcdir: srcDir, destDir: getDestDir().canonicalPath, includes: getIncludes(), outputFormat: getOutputFormat(), tabs: getTabs(), style: getStyle(),
                          showLineNumbers: getShowLineNumbers(), showFileName: getShowFileName(), showDefaultTitle: getShowDefaultTitle(),
                          showTableBorder: getShowTableBorder(), includeDocumentHeader: getIncludeDocumentHeader(), includeDocumentFooter: getIncludeDocumentFooter(),
                          addLineAnchors: getAddLineAnchors(), lineAnchorPrefix: getLineAnchorPrefix(), horizontalAlignment: getHorizontalAlignment(),
                          useShortFileName: getUseShortFileName(), overwrite: getOverwrite())
        }

        logger.info 'Finished converting source code to Java2HTML documentation.'
    }
}
