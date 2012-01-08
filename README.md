# Gradle Java2HTML plugin

![Java2HTML Logo](http://www.java2html.de/java2html_logo_big.gif)

The plugin uses [Java2Html Ant task](http://www.java2html.de/docs/anttask/index.html) to convert Java (and other) source
code (complete files or snippets) to HTML, RTF, TeX and XHTML with syntax highlighting. The task is only available to projects
that apply the [Java](http://www.gradle.org/docs/current/userguide/java_plugin.html) or [Groovy](http://www.gradle.org/docs/current/userguide/groovy_plugin.html)
plugin.

## Usage

To use the Java2HTML plugin, include in your build script:

    apply plugin: 'java2html'

The plugin JAR needs to be defined in the classpath of your build script. You can either get the plugin from the GitHub download
section or upload it to your local repository. To define the Java2HTML dependency please use the `java2html`
configuration name in your `dependencies` closure.

    buildscript {
        repositories {
            add(new org.apache.ivy.plugins.resolver.URLResolver()) {
                name = 'GitHub'
                addArtifactPattern 'http://cloud.github.com/downloads/[organisation]/[module]/[module]-[revision].[ext]'
            }
        }

        dependencies {
            classpath 'bmuschko:gradle-java2html-plugin:0.1'
        }
    }

    dependencies {
        java2html 'de.java2html:java2html:5.0'
    }

## Tasks

The Java2HTML plugin defines the following tasks:

* `java2html`: Generates the documentation for source code files.

## Convention properties

The Java2HTML plugin defines the following convention properties in the `java2html` closure:

* `destDir`: Destination folder for output of the converted files (defaults to `file('build/docs/java2html')`).
* `includes`: File mask for input files (defaults to `**/*.java,**/*.groovy`).
* `outputFormat`: File format for conversion output (defaults to `html`). Valid values are `html`, `xhtml11`, `xhtml`, `tex`, `rtf` and `xml`.
* `tabs`: Width in spaces for a tab character (defaults to 2).
* `style`: Sets the table name for the output style (defaults to `eclipse`). Valid values are `eclipse`, `kawa` and `monochrome`.
* `showLineNumbers`: Show line numbers in conversion output (defaults to `true`).
* `showFileName`: Show the file name in conversion output (defaults to `false`).
* `showDefaultTitle`: Sets the title of generated html pages (if any) to the relative name of the source file, e.g. `de/java2html/Java2Html.java` (defaults to empty `false`).
* `showTableBorder`: Show a border around the conversion output (defaults to `false`).
* `includeDocumentHeader`: Add a document header at the beginning of the output file (defaults to `true`).
* `includeDocumentFooter`: Add a document footer at the end of the output file (defaults to `true`).
* `addLineAnchors`: Add html-anchors to each line for html output (defaults to `false`).
* `lineAnchorPrefix`: String that will be added as prefix for the line anchors for html output (defaults to `''`).
* `horizontalAlignment`: Horizontal alignment of the output (defaults to `left`). Valid values are `left`, `center` and `right`.
* `lineAnchorPrefix`: Use short (`ClassName.html`) or long (`ClassName.java.html`) filenames for output (defaults to `false`).
* `overwrite`: Overwrite existing files even if the destination files are newer (defaults to `false`).

### Example

    java2html {
        outputFormat = 'xml'
        tabs = 4
        override = true
    }