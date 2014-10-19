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
package com.bmuschko.gradle.java2html.internal.model

/**
 * Document package model.
 *
 * @author Benjamin Muschko
 */
class DocPackage implements Comparable {
    String link
    String name

    @Override
    int compareTo(t) {
        return this.name.compareTo(t.name)
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        DocPackage that = (DocPackage) o

        if (link != that.link) return false
        if (name != that.name) return false

        true
    }

    @Override
    int hashCode() {
        int result
        result = (link != null ? link.hashCode() : 0)
        result = 31 * result + (name != null ? name.hashCode() : 0)
        result
    }

    @Override
    String toString() {
        "DocPackage{" +
        "link='" + link + '\'' +
        ", name='" + name + '\'' +
        '}'
    }
}
