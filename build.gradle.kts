plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
//    kotlin("jvm") version "1.7.10"
    id("application")
}

group = "com.bingo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
}

application {
    mainClass.set("com.bingo.BingoGeneratorKt")
}

tasks.test {
    useJUnitPlatform()
}