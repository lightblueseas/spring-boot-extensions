import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.gradle.DokkaTask

val springBootDependenciesVersion = "2.2.1.RELEASE"
val sillyJdbcVersion = "5.6"
val yamlToPropToYamlVersion = "1.1"

plugins {
    signing
    id("io.franzbecker.gradle-lombok") version "3.1.0"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("java")
    id("maven-publish")
    id("org.jetbrains.kotlin.jvm") version "1.3.61"
    id("org.jetbrains.dokka") version "0.9.17"
}

group = "de.alpharogroup"
version = "1.1-SNAPSHOT"
description = "spring-boot-extensions"

repositories {
    jcenter()
    mavenLocal()
    mavenCentral()
}

// Configure existing Dokka task to output HTML to typical Javadoc directory
tasks.dokka {
    outputFormat = "html"
    outputDirectory = "$buildDir/javadoc"
}

// Create dokka Jar task from dokka task output
val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles Kotlin docs with Dokka"
    classifier = "javadoc"
    // dependsOn(tasks.dokka) not needed; dependency automatically inferred by from(tasks.dokka)
    from(tasks.dokka)
}


dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootDependenciesVersion")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    implementation("de.alpharogroup:silly-jdbc:$sillyJdbcVersion")
    implementation("de.alpharogroup:yaml-to-prop-to-yaml:${yamlToPropToYamlVersion}")
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

publishing {
    publications {
            create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(sourcesJar.get())
            artifact(dokkaJar)

            pom {
                val name = "${rootProject.name}"
                val description = "The target of this project is to provide extensions for spring-boot"
                val url = "https://github.com/lightblueseas/"+"${rootProject.name}"
                organization {
                    val name = "Alpha Ro Group UG (haftungsbeschrängt)"
                    val url = "http://www.alpharogroup.de/"
                }
                issueManagement {
                    val system = "GitHub"
                    val url = "https://github.com/lightblueseas/"+"${rootProject.name}"+"/issues"
                }
                licenses {
                    license {
                        val name = "MIT License"
                        val url = "http://www.opensource.org/licenses/mit-license.php"
                        val distribution = "repo"
                    }
                }
                developers {
                    developer {
                        val id = "astrapi69"
                        val name = "Asterios Raptis"
                    }
                }
                scm {
                    val connection = "scm:git:git:@github.com:lightblueseas/"+"${rootProject.name}"+".git"
                    val developerConnection = "scm:git:git@github.com:lightblueseas/"+"${rootProject.name}"+".git"
                    val url = "git:@github.com:lightblueseas/"+"${rootProject.name}"+".git"
                }
            }

            repositories {
                maven {
                    credentials {
                        val username = System.getenv("ossrhUsername")
                                ?: project.property("ossrhUsername")
                        val password = System.getenv("ossrhPassword")
                                ?: project.property("ossrhPassword")
                    }
                    val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                    val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
                    val url = if(version.endsWith("SNAPSHOT"))  snapshotsRepoUrl else releasesRepoUrl
                }
            }
        }
    }
}

