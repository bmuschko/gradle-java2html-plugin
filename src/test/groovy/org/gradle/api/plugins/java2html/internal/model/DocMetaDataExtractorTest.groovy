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
package org.gradle.api.plugins.java2html.internal.model

import groovy.mock.interceptor.MockFor
import org.gradle.api.file.FileCollection
import org.gradle.api.plugins.java2html.internal.DocMetaDataExtractor
import org.junit.Test

import java.lang.reflect.Field
import java.lang.reflect.Modifier

import static org.junit.Assert.assertEquals

class DocMetaDataExtractorTest {

    @Test
    public void testGetClassesWindowsPath() {
        def htmlFileNames = [
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\app\\AbstractSyntheticMetaMethods.html",
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\application\\StandaloneGriffonApplication.html",
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\core\\AddonManager.html"
        ]

        def fileMock = new MockFor(File)
        fileMock.ignore('getAbsolutePath') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.ignore('toString') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.ignore('path') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.use {

            File srcFile = new File("")
            def fileCollectionMock = new MockFor(FileCollection)
            fileCollectionMock.demand.find(3) {
                srcFile.toString()
            }
            FileCollection fileCollection = fileCollectionMock.proxyInstance()

            DocMetaDataExtractor docMetaDataExtractor = new DocMetaDataExtractor()
            changeFileSeparator('\\')
            def results = docMetaDataExtractor.getClasses(htmlFileNames, fileCollection)

            assertEquals "AbstractSyntheticMetaMethods", results[0].name
            assertEquals "griffon\\app\\AbstractSyntheticMetaMethods.html", results[0].link
            assertEquals "AddonManager", results[1].name
            assertEquals "griffon\\core\\AddonManager.html", results[1].link
            assertEquals "StandaloneGriffonApplication", results[2].name
            assertEquals "griffon\\application\\StandaloneGriffonApplication.html", results[2].link
        }
    }

    @Test
    public void testGetClassesUnixPath() {
        def htmlFileNames = [
            "/home/jocki/project/build/java2html/griffon/app/AbstractSyntheticMetaMethods.html",
            "/home/jocki/project/build/java2html/griffon/application/StandaloneGriffonApplication.html",
            "/home/jocki/project/build/java2html/griffon/core/AddonManager.html"
        ]

        def fileMock = new MockFor(File)
        fileMock.ignore('getAbsolutePath') { "/home/jocki/project/build/java2html" }
        fileMock.ignore('toString') { "/home/jocki/project/build/java2html" }
        fileMock.ignore('path') { "/home/jocki/project/build/java2html" }
        fileMock.use {

            File srcFile = new File("")
            def fileCollectionMock = new MockFor(FileCollection)
            fileCollectionMock.demand.find(3) {
                srcFile.toString()
            }
            FileCollection fileCollection = fileCollectionMock.proxyInstance()

            DocMetaDataExtractor docMetaDataExtractor = new DocMetaDataExtractor()
            changeFileSeparator('/')
            def results = docMetaDataExtractor.getClasses(htmlFileNames, fileCollection)

            assertEquals "AbstractSyntheticMetaMethods", results[0].name
            assertEquals "griffon/app/AbstractSyntheticMetaMethods.html", results[0].link
            assertEquals "AddonManager", results[1].name
            assertEquals "griffon/core/AddonManager.html", results[1].link
            assertEquals "StandaloneGriffonApplication", results[2].name
            assertEquals "griffon/application/StandaloneGriffonApplication.html", results[2].link
        }

    }

    @Test
    public void testGetPackagesWindowsPath() {
        def htmlFileNames = [
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\app\\AbstractSyntheticMetaMethods.html",
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\application\\StandaloneGriffonApplication.html",
            "C:\\Users\\Jocki Hendry\\project name\\build\\java2html\\griffon\\core\\AddonManager.html"
        ]

        def fileMock = new MockFor(File)
        fileMock.ignore('getAbsolutePath') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.ignore('toString') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.ignore('path') { "C:\\Users\\Jocki Hendry\\project name\\build\\java2html" }
        fileMock.use {

            File srcFile = new File("")
            def fileCollectionMock = new MockFor(FileCollection)
            fileCollectionMock.demand.find(3) {
                srcFile.toString()
            }
            FileCollection fileCollection = fileCollectionMock.proxyInstance()

            DocMetaDataExtractor docMetaDataExtractor = new DocMetaDataExtractor()
            changeFileSeparator('\\')
            def results = docMetaDataExtractor.getPackages(htmlFileNames, fileCollection)

            assertEquals "griffon.app", results[0].name
            assertEquals "griffon\\app", results[0].link
            assertEquals "griffon.application", results[1].name
            assertEquals "griffon\\application", results[1].link
            assertEquals "griffon.core", results[2].name
            assertEquals "griffon\\core", results[2].link
        }
    }

    @Test
    public void testGetPackagesUnixPath() {
        def htmlFileNames = [
            "/home/jocki/project/build/java2html/griffon/app/AbstractSyntheticMetaMethods.html",
            "/home/jocki/project/build/java2html/griffon/application/StandaloneGriffonApplication.html",
            "/home/jocki/project/build/java2html/griffon/core/AddonManager.html"
        ]

        def fileMock = new MockFor(File)
        fileMock.ignore('getAbsolutePath') { "/home/jocki/project/build/java2html" }
        fileMock.ignore('toString') { "/home/jocki/project/build/java2html" }
        fileMock.ignore('path') { "/home/jocki/project/build/java2html" }
        fileMock.use {

            File srcFile = new File("")
            def fileCollectionMock = new MockFor(FileCollection)
            fileCollectionMock.demand.find(3) {
                srcFile.toString()
            }
            FileCollection fileCollection = fileCollectionMock.proxyInstance()

            DocMetaDataExtractor docMetaDataExtractor = new DocMetaDataExtractor()
            changeFileSeparator('/')
            def results = docMetaDataExtractor.getPackages(htmlFileNames, fileCollection)

            assertEquals "griffon.app", results[0].name
            assertEquals "griffon/app", results[0].link
            assertEquals "griffon.application", results[1].name
            assertEquals "griffon/application", results[1].link
            assertEquals "griffon.core", results[2].name
            assertEquals "griffon/core", results[2].link
        }

    }

    public void changeFileSeparator(String newSeparator) {
        Field field = DocMetaDataExtractor.getDeclaredField('FILE_SEPARATOR')
        field.accessible = true
        Field modifiers = Field.getDeclaredField('modifiers')
        modifiers.accessible = true
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL)
        field.set(null, newSeparator)
    }

}
