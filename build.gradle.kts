import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.21"
    id("com.github.sherter.google-java-format") version "0.9"
}

group = "com.github.gilday"
version = "1.2.6"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.6.2"))
    testApi("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

// https://github.com/gilday/dark-mode-sync-plugin/issues/19
val jvmTarget = JavaVersion.VERSION_1_8

java {
    sourceCompatibility = jvmTarget
    targetCompatibility = jvmTarget
}

intellij {
    version = "2020.1"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmTarget.toString()
}

tasks.test {
    useJUnitPlatform()
}

tasks.patchPluginXml {
    setSinceBuild("201")
    setUntilBuild("203.*")
}

tasks.publishPlugin {
    val jetBrainsToken: String? by project
    token(jetBrainsToken)
}
