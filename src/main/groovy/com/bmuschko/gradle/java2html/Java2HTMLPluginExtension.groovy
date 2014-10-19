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

import org.gradle.util.ConfigureUtil

/**
 * Java2HTML plugin extension.
 *
 * @author Benjamin Muschko
 */
class Java2HTMLPluginExtension {
    ConversionConvention conversion = new ConversionConvention()
    OverviewConvention overview = new OverviewConvention()

    def conversion(Closure closure) {
        ConfigureUtil.configure(closure, conversion)
    }

    def overview(Closure closure) {
        ConfigureUtil.configure(closure, overview)
    }
}
