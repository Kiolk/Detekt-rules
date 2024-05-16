import org.jetbrains.dokka.gradle.DokkaTask
import java.io.FileInputStream
import java.util.*

plugins {
    kotlin("jvm") version "1.9.22"
    apply { `maven-publish` }
    apply { `signing` }
    id("org.jetbrains.dokka") version "1.9.20"
    id("com.gradleup.nmcp") version "0.0.7"
}

repositories {
    mavenCentral()
    mavenLocal()
}

allprojects {
    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "signing")

    val dokkaHtml by tasks.existing(DokkaTask::class)
    val dokkaJar by tasks.creating(org.gradle.jvm.tasks.Jar::class) {
        group = JavaBasePlugin.DOCUMENTATION_GROUP
        archiveClassifier.set("javadoc")
        from(dokkaHtml)
    }

    val sourcesJar by tasks.creating(org.gradle.jvm.tasks.Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    val pomArtifactId: String? by project
    if (pomArtifactId != null) {
        publishing {
            publications {
                create<MavenPublication>("maven") {
                    val versionName: String by project
                    val pomGroupId: String by project
                    groupId = pomGroupId
                    artifactId = pomArtifactId
                    version = versionName
                    from(components["java"])

                    artifact(dokkaJar)
                    artifact(sourcesJar)

                    pom {
                        val pomDescription: String by project
                        val pomUrl: String by project
                        val pomName: String by project
                        description.set(pomDescription)
                        url.set(pomUrl)
                        name.set(pomName)
                        scm {
                            val pomScmUrl: String by project
                            val pomScmConnection: String by project
                            val pomScmDevConnection: String by project
                            url.set(pomScmUrl)
                            connection.set(pomScmConnection)
                            developerConnection.set(pomScmDevConnection)
                        }
                        licenses {
                            license {
                                val pomLicenseName: String by project
                                val pomLicenseUrl: String by project
                                val pomLicenseDist: String by project
                                name.set(pomLicenseName)
                                url.set(pomLicenseUrl)
                                distribution.set(pomLicenseDist)
                            }
                        }
                        developers {
                            developer {
                                val pomDeveloperId: String by project
                                val pomDeveloperName: String by project
                                id.set(pomDeveloperId)
                                name.set(pomDeveloperName)
                            }
                        }
                    }
                }
            }

            signing {
                val file = File("maven-secret-key.asc")
                val key = file.readText()
                val properties = Properties()
                properties.load(FileInputStream(file("local.properties")))
                useGpgCmd()
                useInMemoryPgpKeys(key, properties.getProperty("signing_password"))

                sign(publishing.publications["maven"])
            }
            repositories {
                maven {
                    val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    val versionName: String by project
                    url = if (versionName.endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
                }
            }
        }
    }
}

dependencies {
    implementation("io.gitlab.arturbosch.detekt:detekt-api:1.23.6")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:1.23.6")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}

nmcp {
    publish("maven") {
        val properties = Properties()
        properties.load(FileInputStream(file("local.properties")))
        username = properties.getProperty("username")
        password = properties.getProperty("password")
        publicationType = "USER_MANAGED"
    }
}