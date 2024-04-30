plugins {
    kotlin("jvm") version "1.9.22"
    apply { `maven-publish` }
}

group = "com.github.kiolk.detektrules"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
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