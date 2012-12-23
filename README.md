# Gradle Java2HTML plugin

![Java2HTML Logo](http://www.java2html.de/java2html_logo_big.gif)

The plugin uses [Java2Html Ant task](http://www.java2html.de/docs/anttask/index.html) to convert Java (and other) source
code (complete files or snippets) to HTML, RTF, TeX and XHTML with syntax highlighting. The task is only available to projects
that apply the [Java](http://www.gradle.org/docs/current/userguide/java_plugin.html) or [Groovy](http://www.gradle.org/docs/current/userguide/groovy_plugin.html)
plugin.

## Usage

To use the Java2HTML plugin, include in your build script:

    apply plugin: 'java2html'

The plugin JAR needs to be defined in the classpath of your build script. It is directly available on
[Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.gradle.api.plugins%22%20AND%20a%3A%22gradle-java2html-plugin%22).
Alternatively, you can download it from GitHub and deploy it to your local repository. The following code snippet shows an
example on how to retrieve it from Maven Central:

    buildscript {
        repositories {
            mavenCentral()
        }

        dependencies {
            classpath 'org.gradle.api.plugins:gradle-java2html-plugin:0.3'
        }
    }

To define the Java2HTML dependency please use the `java2html` configuration name in your `dependencies` closure.

    dependencies {
        java2html 'de.java2html:java2html:5.0'
    }

## Tasks

The Java2HTML plugin defines the following tasks:

* `java2htmlConvertCode`: Converts source code files to Java2HTML documentation.
* `java2htmlGenerateOverview`: Generates HTML overview files for Java2HTML documentation.

## Convention properties

The Java2HTML plugin defines the following convention properties in the `java2html` closure. This closure itself contains
two closures for each of the tasks: `conversion` and `overview`.

In the closure `conversion` you can define property values for converting the source code:

* `srcDirs`: Source directories to look for source code files for conversion (defaults to `file('src/main/java')` for a Java
project and to `files('src/main/java', 'src/main/groovy')` for a Groovy project).
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
* `useShortFileName`: Use short (`ClassName.html`) or long (`ClassName.java.html`) filenames for output (defaults to `false`).
* `overwrite`: Overwrite existing files even if the destination files are newer (defaults to `false`).

In the closure `overview` you can define property values for generating the HTML overview files:

* `srcDirs`: Source directories to look for generated files by Java2HTML (defaults to `file('build/docs/java2html')` of all modules
in project).
* `destDir`: Destination folder for HTML overview files (defaults to `file('build/docs/java2html')`).
* `pattern`: Java2HTML file pattern to be included in HTML overview (defaults to `**/*.html`).
* `windowTitle`: Window title in overview (defaults to project name).
* `docTitle`: Document title in overview (defaults to project name).
* `docDescription`: Document description in overview.
* `icon`: Icon file to be use in overview.
* `stylesheet`: CSS stylesheet file to be use in overview (defaults to JavaDoc stylesheet).

### Example

    java2html {
        conversion {
            tabs = 4
            style = 'eclipse'
            showFileName = true
            useShortFileName = true
            override = true
            showDefaultTitle = true
        }

        overview {
            docDescription = 'Gradle plugin for turning source code into HTML, RTF, TeX and XHTML using Java2HTML.'
        }
    }