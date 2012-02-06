/*
 * Copyright 2012 the original author or authors.
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
package org.gradle.api.plugins.java2html.internal

import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.java2html.internal.model.DocClass
import org.gradle.api.plugins.java2html.internal.model.DocPackage

/**
 * Document meta-data extractor.
 *
 * @author Benjamin Muschko
 */
class DocMetaDataExtractor {
    final static String FILE_SEPARATOR = System.getProperty('file.separator')

    /**
     * Gets classes of generated Java2HTML files.
     *
     * @param htmlFilenames HTML absolute file names
     * @param srcDirs Source directories
     * @return Document classes
     */
    List<DocClass> getClasses(List<String> htmlFilenames, FileCollection srcDirs) {
        def docClasses = []

        htmlFilenames.each {
            String relativePathClassFilename = getRelativeClassFilename(it, srcDirs)
            String className = relativePathClassFilename.substring(relativePathClassFilename.lastIndexOf(FILE_SEPARATOR) + 1, relativePathClassFilename.indexOf('.'))
            docClasses << new DocClass(link: relativePathClassFilename, name: className)
        }

        docClasses.sort()
    }

    /**
     * Gets packages of generated Java2HTML files.
     *
     * @param htmlFilenames HTML absolute file names
     * @param srcDirs Source directories
     * @return Document packages
     */
    List<DocPackage> getPackages(List<String> htmlFilenames, FileCollection srcDirs) {
        def docPackages = []

        htmlFilenames.each {
            String relativePathClassFilename = getRelativeClassFilename(it, srcDirs)
            String packageName = relativePathClassFilename.substring(0, relativePathClassFilename.lastIndexOf(FILE_SEPARATOR))
            DocPackage docPackage = new DocPackage(link: packageName, name: packageName.replaceAll(FILE_SEPARATOR, '.'))

            if(!docPackages.contains(docPackage)) {
                docPackages << docPackage
            }
        }

        docPackages.sort()
    }

    /**
     * Gets relative class filename.
     *
     * @param htmlFilename HTML absolute filenames
     * @param srcDirs Source directories
     * @return Relative class file name
     */
    private String getRelativeClassFilename(String htmlFilename, FileCollection srcDirs) {
        String sourceDirPath = srcDirs.find { htmlFilename.startsWith(it.absolutePath) }
        String relativePathClassFilename = htmlFilename.replaceFirst(sourceDirPath, '')

        if(relativePathClassFilename.startsWith(FILE_SEPARATOR)) {
            relativePathClassFilename = relativePathClassFilename.replaceFirst(FILE_SEPARATOR, '')
        }

        relativePathClassFilename
    }
}
