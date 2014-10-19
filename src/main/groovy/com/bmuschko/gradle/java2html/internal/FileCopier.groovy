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
package com.bmuschko.gradle.java2html.internal

/**
 * File copier.
 *
 * @author Benjamin Muschko
 */
class FileCopier {
    /**
     * Copies internal file from templates directory.
     *
     * @param sourceFile Source file
     * @param targetFile Target file
     */
    void copyInternalFile(File sourceFile, File targetFile) {
        InputStream is = getClass().getResourceAsStream(sourceFile.absolutePath)
        copyInputStreamToFile(is, targetFile)
    }

    /**
     * Copies external file.
     *
     * @param sourceFile Source file
     * @param targetFile Target file
     */
    void copyExternalFile(File sourceFile, File targetFile) {
        InputStream is = new FileInputStream(sourceFile)
        copyInputStreamToFile(is, targetFile)
    }

    /**
     * Copies input stream to file.
     *
     * @param inputStream Input stream
     * @param targetFile Target file
     */
    private void copyInputStreamToFile(InputStream inputStream, File targetFile) {
        DataOutputStream out

        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(targetFile)))
            int c

            while((c = inputStream.read()) != -1) {
                out.writeByte(c)
            }
        }
        finally {
            if(inputStream) inputStream.close()
            if(out) out.close()
        }
    }
}
