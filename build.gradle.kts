import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
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
    testImplementation(platform("org.junit:junit-bom:[5.6.2,5.7.0)"))

    api(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    implementation("com.ibm.icu", "icu4j", "[66.1,)")

    api("org.graalvm.truffle", "truffle-api", graalVMVersion)
    implementation("org.graalvm.truffle", "truffle-nfi", graalVMVersion)
    implementation("org.graalvm.sdk", "graal-sdk", graalVMVersion)
    implementation("org.graalvm.sdk", "launcher-common", graalVMVersion)
    compileOnly("org.graalvm.truffle", "truffle-dsl-processor", graalVMVersion)
    kapt("org.graalvm.truffle", "truffle-dsl-processor", graalVMVersion)
    runtimeOnly("org.graalvm.compiler", "compiler", graalVMVersion)

    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
//    testRuntimeOnly("org.junit.vintage", "junit-vintage-engine")
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
    withJavadocJar()
    withSourcesJar()
    modularity.inferModulePath.set(true)
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

    withType<KotlinCompile>().configureEach {
        usePreciseJavaTracking = true

        kotlinOptions {
            jvmTarget = "13"
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
            jdkVersion = 14
        }
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }
}

idea {
    project {
        jdkName = "SapMachine 14"
    }

    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
