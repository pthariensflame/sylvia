plugins {
    java
    kotlin("jvm") version "1.4-M1"
    `maven-publish`
    idea
}

group = "com.pthariensflame"
version = "0.0.1-SNAPSHOT"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
//    test("org.junit.jupiter", "jupiter-api")
//    testRuntimeOnly("org.junit.jupiter", "jupiter-engine")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_14
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }
}
