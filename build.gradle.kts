plugins {
    id("java")
    id("org.jetbrains.intellij") version "0.4.14"
    id("com.github.sherter.google-java-format") version "0.8"
}

group = "com.github.gilday"
version = "1.0.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.5.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.test {
    useJUnitPlatform()
}

intellij {
    version = "2019.2"
}

tasks.patchPluginXml {
    setSinceBuild("191")
}

tasks.publishPlugin {
    val jetBrainsToken: String? by project
    token(jetBrainsToken)
}
