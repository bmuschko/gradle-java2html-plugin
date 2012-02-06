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
import org.gradle.api.plugins.java2html.internal.Java2HTMLDocGenerator
import org.gradle.api.plugins.java2html.internal.model.DocGenerationInput
import org.gradle.api.tasks.*
import org.gradle.api.file.FileCollection

/**
 * Java2HTML overview generation task.
 *
 * @author Benjamin Muschko
 */
@Slf4j
class GenerateOverviewTask extends DefaultTask {
    @Input FileCollection srcDirs
    @OutputDirectory File destDir
    String pattern
    String windowTitle
    String docTitle
    String docDescription
    @InputFile @Optional File icon
    @InputFile @Optional File stylesheet

    @TaskAction
    void start() {
        log.info 'Starting to generate Java2HTML overview.'

        // Copy submodule directories
        ant.copy(toDir: getDestDir()) {
            getSrcDirs().each { srcDir ->
                ant.fileset(dir: srcDir)
            }
        }

        DocGenerationInput input = new DocGenerationInput(srcDirs: getSrcDirs(), destDir: getDestDir(), pattern: getPattern(),
                                                          windowTitle: getWindowTitle(), docTitle: getDocTitle(), docDescription: getDocDescription(),
                                                          icon: getIcon(), stylesheet: getStylesheet())
        Java2HTMLDocGenerator docGenerator = new Java2HTMLDocGenerator(input)
        docGenerator.generate()

        log.info 'Finished generating Java2HTML overview.'
    }
}
