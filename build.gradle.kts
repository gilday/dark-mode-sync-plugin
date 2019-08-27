plugins {
    id("java")
    id("org.jetbrains.intellij") version "0.4.10"
}

group = "com.github.gilday"
version = "1.0.3"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
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

tasks.publishPlugin {
    val jetBrainsToken: String? by project
    token(jetBrainsToken)
}
