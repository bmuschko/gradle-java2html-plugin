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

import org.gradle.api.file.FileCollection

/**
 * Conversion convention.
 *
 * @author Benjamin Muschko
 */
class ConversionConvention {
    FileCollection srcDirs
    File destDir
    String includes
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

    @Override
    public String toString() {
        return "ConversionConvention{" +
                "srcDirs=" + srcDirs +
                ", destDir=" + destDir +
                ", includes='" + includes + '\'' +
                ", outputFormat='" + outputFormat + '\'' +
                ", tabs=" + tabs +
                ", style='" + style + '\'' +
                ", showLineNumbers=" + showLineNumbers +
                ", showFileName=" + showFileName +
                ", showDefaultTitle=" + showDefaultTitle +
                ", showTableBorder=" + showTableBorder +
                ", includeDocumentHeader=" + includeDocumentHeader +
                ", includeDocumentFooter=" + includeDocumentFooter +
                ", addLineAnchors=" + addLineAnchors +
                ", lineAnchorPrefix='" + lineAnchorPrefix + '\'' +
                ", horizontalAlignment='" + horizontalAlignment + '\'' +
                ", useShortFileName=" + useShortFileName +
                ", overwrite=" + overwrite +
                '}';
    }


}
