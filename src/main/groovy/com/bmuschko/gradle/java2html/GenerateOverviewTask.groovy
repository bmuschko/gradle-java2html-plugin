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

import com.bmuschko.gradle.java2html.internal.Java2HTMLDocGenerator
import com.bmuschko.gradle.java2html.internal.model.DocGenerationInput
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction

/**
 * Java2HTML overview generation task.
 *
 * @author Benjamin Muschko
 */
@CacheableTask
class GenerateOverviewTask extends DefaultTask {
    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFiles FileCollection srcDirs

    @PathSensitive(PathSensitivity.RELATIVE)
    @OutputDirectory File destDir

    @Input String pattern
    @Input String windowTitle
    @Input String docTitle
    @Input String docDescription

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFile @Optional File icon

    @PathSensitive(PathSensitivity.RELATIVE)
    @InputFile @Optional File stylesheet

    @TaskAction
    void start() {
        logger.info 'Starting to generate Java2HTML overview.'

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

        logger.info 'Finished generating Java2HTML overview.'
    }
}
