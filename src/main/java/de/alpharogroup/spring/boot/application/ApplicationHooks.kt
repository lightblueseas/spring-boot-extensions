package de.alpharogroup.spring.boot.application

import de.alpharogroup.jdbc.PostgreSQLConnectionsExtensions
import de.alpharogroup.yaml.YamlToPropertiesExtensions
import org.springframework.boot.SpringApplication
import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import java.io.File

/**
 * Holds some application hooks for the SpringApplication
 */
object ApplicationHooks {
	
	/**
	 * Creates a new database if not exists
	 */
    fun addDatabaseIfNotExists(application: SpringApplication, parent: File, yamlFilename: String) {
        application.addDatabaseIfNotExists(parent, yamlFilename)
    }
}

/**
 * Extension method for the SpringApplication to add a new database if not exists
 */
fun SpringApplication.addDatabaseIfNotExists(parent: File, yamlFilename: String) {
    this.addListeners(ApplicationListener<ApplicationStartingEvent> { _: ApplicationStartingEvent ->
        PostgreSQLConnectionsExtensions.newDatabase(YamlToPropertiesExtensions
                .toProperties(File(parent, yamlFilename).absolutePath) )
    })
}