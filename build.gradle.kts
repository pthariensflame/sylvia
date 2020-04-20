import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    `antlr`
    kotlin("jvm") version "1.4-M1"
    kotlin("kapt") version "1.4-M1"
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.hpe.kraal") version "0.0.15"
    id("ch.tutteli.project.utils") version "0.33.0"
    id("ch.tutteli.kotlin.module.info") version "0.33.0"
    id("ch.tutteli.kotlin.utils") version "0.33.0"
    `maven-publish`
    idea
}

group = "com.pthariensflame"
version = "0.0.1-SNAPSHOT"

val jdkHomePath: String by project
val graalVMVersion: String by project

repositories {
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    maven { url = uri("https://kotlin.bintray.com/kotlinx") }
    mavenCentral()
    jcenter()
}

kotlinutils {
    kotlinVersion.set("1.4-M1")
}

dependencies {
    components.all {
        if (id.group.startsWith("org.graalvm")) {
            belongsTo("org.graalvm:graalvm-virtual-platform:${id.version}", true)
        }
        if (id.group.startsWith("org.junit")) {
            belongsTo("org.junit:junit-bom:${id.version}", false)
        }
    }
    api(enforcedPlatform("org.graalvm:graalvm-virtual-platform:$graalVMVersion"))
    testImplementation(platform("org.junit:junit-bom:5.6.+"))
    constraints {
        testImplementation("junit", "junit", "4.+")
    }

    api(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage", "junit-vintage-engine")

    implementation("com.ibm.icu", "icu4j", "[66.1,)")

    antlr("org.antlr", "antlr4", "4.8-1")
    runtimeOnly("org.antlr", "antlr4-runtime", "4.8-1")

    api("org.graalvm.truffle", "truffle-api", graalVMVersion)
    implementation("org.graalvm.truffle", "truffle-nfi", graalVMVersion)
    implementation("org.graalvm.sdk", "graal-sdk", graalVMVersion)
    implementation("org.graalvm.sdk", "launcher-common", graalVMVersion)
    kapt("org.graalvm.truffle", "truffle-dsl-processor", graalVMVersion)
    runtimeOnly("org.graalvm.compiler", "compiler", graalVMVersion)
    testImplementation("org.graalvm.truffle", "truffle-tck", graalVMVersion)
    testImplementation("org.graalvm.sdk", "polyglot-tck", graalVMVersion)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
//    modularity.inferModulePath.set(true)
}
//val compileJava: JavaCompile by tasks
//val javaClasspath = compileJava.classpath.asPath

//kotlin {
//    target {
//    }
//}

kapt {
    correctErrorTypes = true
    includeCompileClasspath = false

    javacOptions {
//        option("--module-path", javaClasspath)
    }
}

tasks {
//    withType<JavaCompile>().configureEach {
//    }

//    withType<AntlrTask>().configureEach {
//    }

    withType<KotlinCompile>().configureEach {
        usePreciseJavaTracking = true

        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
            apiVersion = "1.4"
            languageVersion = "1.4"
            jdkHome = jdkHomePath
//            freeCompilerArgs += sequenceOf(
//                "-Xmodule-path=$javaClasspath"
//            )
        }
    }

    withType<DokkaTask>().configureEach {
        outputFormat = "html"
        outputDirectory = "$buildDir/dokka"

        configuration {
            jdkVersion = 8
        }
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

idea {
    project {
        jdkName = "GraalVM 1.8 (20)"
    }

    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
