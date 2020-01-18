import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.15"
    id("com.github.sherter.google-java-format") version "0.8"
}

group = "com.github.gilday"
version = "1.1.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.5.2"))
    testApi("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

intellij {
    version = "2019.3"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.test {
    useJUnitPlatform()
}

tasks.patchPluginXml {
    setSinceBuild("192")
}

tasks.publishPlugin {
    val jetBrainsToken: String? by project
    token(jetBrainsToken)
}
