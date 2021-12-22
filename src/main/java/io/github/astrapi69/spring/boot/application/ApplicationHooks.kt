/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.spring.boot.application

import io.github.astrapi69.jdbc.CreationState
import io.github.astrapi69.jdbc.PostgreSQLConnectionsExtensions
import io.github.astrapi69.yaml.YamlToPropertiesExtensions
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import java.io.File

/**
 * Holds some application hooks for the SpringApplication
 */
object ApplicationHooks {

    lateinit var creationState: CreationState
	/**
	 * Creates a new database if not exists
	 */
    fun addDatabaseIfNotExists(application: SpringApplication, parent: File, yamlFilename: String) {
        application.addDbIfNotExists(parent, yamlFilename)
    }

    /**
     * Extension method for the SpringApplication to add a new database if not exists
     */
    fun SpringApplication.addDbIfNotExists(parent: File, yamlFilename: String) {

        this.addListeners(ApplicationListener<ApplicationStartingEvent> { _: ApplicationStartingEvent ->
           creationState = PostgreSQLConnectionsExtensions.newDatabase(YamlToPropertiesExtensions
                    .toProperties(File(parent, yamlFilename).absolutePath) )
        })
    }
}

