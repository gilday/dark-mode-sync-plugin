import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.21"
    id("com.github.sherter.google-java-format") version "0.9"
}

group = "com.github.gilday"
version = "1.2.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.6.2"))
    testApi("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

intellij {
    version = "2020.1"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.majorVersion
}

tasks.test {
    useJUnitPlatform()
}

tasks.patchPluginXml {
    setSinceBuild("201")
    setUntilBuild("202.*")
}

tasks.publishPlugin {
    val jetBrainsToken: String? by project
    token(jetBrainsToken)
}
