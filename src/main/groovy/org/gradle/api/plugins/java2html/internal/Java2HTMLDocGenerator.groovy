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

import groovy.text.SimpleTemplateEngine
import groovy.text.TemplateEngine
import groovy.util.logging.Slf4j
import org.gradle.api.GradleException
import org.gradle.api.plugins.java2html.internal.model.DocClass
import org.gradle.api.plugins.java2html.internal.model.DocGenerationInput
import org.gradle.api.plugins.java2html.internal.model.DocPackage

/**
 * Java2HTML document generator.
 *
 * @author Benjamin Muschko
 */
@Slf4j
class Java2HTMLDocGenerator {
    final static String TEMPLATES_DIR = '/templates'
    final static String FILE_SEPARATOR = System.getProperty('file.separator')
    final DocGenerationInput docGenerationInput
    final DocMetaDataExtractor docMetaDataExtractor
    final FileCopier fileCopier

    Java2HTMLDocGenerator(DocGenerationInput docGenerationInput) {
        this.docGenerationInput = docGenerationInput
        docMetaDataExtractor = new DocMetaDataExtractor()
        fileCopier = new FileCopier()
    }

    /**
     * Generates overview documentation.
     */
    void generate() {
        List<String> htmlFilenames = getHtmlFilenames()
        List<DocPackage> docPackages = docMetaDataExtractor.getPackages(htmlFilenames, docGenerationInput.srcDirs)
        List<DocClass> docClasses = docMetaDataExtractor.getClasses(htmlFilenames, docGenerationInput.srcDirs)
        generateIndexFile()
        generateOverviewFrameFile(docPackages)
        generateAllClassesFrameFile(docClasses)
        generateOverviewSummaryFrameFile()
        generatePackageFrameFiles(docPackages, docClasses)

        if(isNotNullAndExists(docGenerationInput.stylesheet)) {
            fileCopier.copyExternalFile(docGenerationInput.stylesheet, new File(docGenerationInput.destDir, DocFile.STYLESHEET.filename))
        }
        else {
            fileCopier.copyInternalFile(new File(getFullTemplateFilename(DocFile.STYLESHEET.filename)), new File(docGenerationInput.destDir, DocFile.STYLESHEET.filename))
        }

        if(isNotNullAndExists(docGenerationInput.icon)) {
            fileCopier.copyExternalFile(docGenerationInput.icon, new File(docGenerationInput.destDir, DocFile.ICON.filename))
        }
    }

    /**
     * Generates index file.
     */
    private void generateIndexFile() {
        def binding = ['windowTitle': docGenerationInput.windowTitle, 'icon': DocFile.ICON.filename]
        generateTemplateFile(docGenerationInput.destDir.absolutePath, DocFile.INDEX.filename, binding)
    }

    /**
     * Generates overview frame file.
     *
     * @param docPackages Document packages
     */
    private void generateOverviewFrameFile(List<DocPackage> docPackages) {
        def binding = ['docTitle': docGenerationInput.docTitle, 'packages': docPackages]
        generateTemplateFile(docGenerationInput.destDir.absolutePath, DocFile.OVERVIEW_FRAME.filename, binding)
    }

    /**
     * Generates all classes frame file.
     *
     * @param docClasses Document classes
     */
    private void generateAllClassesFrameFile(List<DocClass> docClasses) {
        def binding = ['docTitle': docGenerationInput.docTitle, 'classes': docClasses]
        generateTemplateFile(docGenerationInput.destDir.absolutePath, DocFile.ALLCLASSES_FRAME.filename, binding)
    }

    /**
     * Generates overview summary frame file.
     */
    private void generateOverviewSummaryFrameFile() {
        def binding = ['docTitle': docGenerationInput.docTitle, 'docDescription': docGenerationInput.docDescription]
        generateTemplateFile(docGenerationInput.destDir.absolutePath, DocFile.OVERVIEW_SUMMARY.filename, binding)
    }

    /**
     * Generates package frame files. Each packages has a package frame file.
     *
     * @param docPackages Document packages
     * @param docClasses Document classes
     */
    private void generatePackageFrameFiles(List<DocPackage> docPackages, List<DocClass> docClasses) {
        docPackages.each { docPackage ->
            List<DocClass> docClassesInPackage = docClasses.findAll { it.link.substring(0, it.link.lastIndexOf(FILE_SEPARATOR)) == docPackage.link }
            def binding = ['docTitle': docGenerationInput.docTitle, 'packageName': docPackage.name, 'classes': docClassesInPackage]
            generateTemplateFile("${docGenerationInput.destDir.absolutePath}/${docPackage.link}", DocFile.PACKAGE_FRAME.filename, binding)
        }
    }

    /**
     * Generates template file.
     *
     * @param destDirName Destination directory name
     * @param templateFilename Template file name
     * @param binding Binding
     */
    private void generateTemplateFile(String destDirName, String templateFilename, Map binding) {
        TemplateEngine engine = new SimpleTemplateEngine()
        Writable writable = engine.createTemplate(getTemplateUrl(templateFilename)).make(binding)
        File destDir = new File(destDirName)
        createDestDir(destDir)
        writable.writeTo(new FileWriter(new File(destDir, templateFilename)))
    }

    /**
     * Creates destination directory if it doesn't exist yet.
     * 
     * @param destDir Destination directory
     */
    private void createDestDir(File destDir) {
        if(!destDir.exists()) {
            boolean success = destDir.mkdirs()

            if(!success) {
                throw new GradleException("Unable to create destination directory $destDir.absolutePath")
            }
        }
    }

    /**
     * Gets template URL.
     *
     * @param templateFileName Template file name
     * @return Template URL
     */
    private URL getTemplateUrl(String templateFileName) {
        getClass().getResource(getFullTemplateFilename(templateFileName))
    }

    /**
     * Gets full template file name.
     *
     * @param templateFileName Template file name
     * @return Full template file name
     */
    private String getFullTemplateFilename(String templateFileName) {
        "$TEMPLATES_DIR/$templateFileName"
    }

    /**
     * Gets HTML filenames.
     *
     * @return HTML file names.
     */
    private List<String> getHtmlFilenames() {
        def htmlFilenames = []

        docGenerationInput.srcDirs.each { File srcDir ->
            if(srcDir.exists()) {
                htmlFilenames.addAll(new FileNameFinder().getFileNames(srcDir.absolutePath, docGenerationInput.pattern))
            }
        }

        htmlFilenames
    }

    /**
     * Check to see if file is not null and exists.
     *
     * @param file File
     * @return Flag
     */
    private boolean isNotNullAndExists(File file) {
        file && file.exists()
    }
}
