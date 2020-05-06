import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    `java-library`
    antlr
    kotlin("jvm") version "1.4-M1"
    kotlin("kapt") version "1.4-M1"
    id("org.jetbrains.dokka") version "0.10.1"
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

dependencies {
    components.all {
        if (id.group.startsWith("org.graalvm")) {
            belongsTo("org.graalvm:graalvm-virtual-platform:${id.version}", true)
        }
        if (id.group.startsWith("org.junit")) {
            belongsTo("org.junit:junit-bom:${id.version}", false)
        }
        if (id.group.startsWith("io.kotest")) {
            belongsTo("io.kotest:kotest-virtual-platform:${id.version}", true)
        }
    }
    api(enforcedPlatform("org.graalvm:graalvm-virtual-platform:$graalVMVersion"))
    testImplementation(platform("org.junit:junit-bom:5.7.+"))
    testApi(enforcedPlatform("io.kotest:kotest-virtual-platform:4.0.5"))
    constraints {
        testImplementation("junit", "junit", "[4.13,)")
        api("org.jetbrains", "annotations", "19.+")
    }

    api(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))

    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage", "junit-vintage-engine")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.0.5")
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.0.5")
    testApi("io.kotest:kotest-property-jvm:4.0.5")
    testImplementation("io.kotest:kotest-assertions-compiler-jvm:4.0.5")

    antlr("org.antlr", "antlr4", "4.8-1")
    implementation("org.antlr", "antlr4-runtime", "4.8-1")

    implementation("com.ibm.icu", "icu4j", "[67.1,)")

    api("org.graalvm.truffle", "truffle-api", graalVMVersion)
    runtimeOnly("org.graalvm.truffle", "truffle-nfi", graalVMVersion)
    implementation("org.graalvm.sdk", "graal-sdk", graalVMVersion)
    implementation("org.graalvm.sdk", "launcher-common", graalVMVersion)
    kapt("org.graalvm.truffle", "truffle-dsl-processor", graalVMVersion)
    runtimeOnly("org.graalvm.compiler", "compiler", graalVMVersion)
    testImplementation("org.graalvm.truffle", "truffle-tck", graalVMVersion)
    testImplementation("org.graalvm.sdk", "polyglot-tck", graalVMVersion)
}

//kotlinutils {
//    kotlinVersion.set("1.4-M1")
//}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withJavadocJar()
    withSourcesJar()
//    modularity.inferModulePath.set(true)
}
//val compileJava: JavaCompile by tasks
//val javaClasspath = compileJava.classpath.asPath

kapt {
    correctErrorTypes = true
    includeCompileClasspath = false

    javacOptions {
//        option("--module-path", javaClasspath)
    }
}

tasks {
    compileKotlin.configure { dependsOn.add(generateGrammarSource) }
    compileTestKotlin.configure { dependsOn.add(generateTestGrammarSource) }

//    withType<JavaCompile>().configureEach {
//    }

    withType<AntlrTask>().configureEach {
        arguments = arguments + sequenceOf(
            "-no-listener",
            "-visitor",
            "-long-messages"
        )
    }

    withType<KotlinCompile>().configureEach {
        usePreciseJavaTracking = true

        kotlinOptions {
            jvmTarget = "1.8"
            javaParameters = true
            apiVersion = "1.4"
            languageVersion = "1.4"
            jdkHome = jdkHomePath
            (project.findProperty("warningsAsErrors") as? String)?.toBoolean()?.let {
                allWarningsAsErrors = it
            }
            freeCompilerArgs += sequenceOf(
                "-progressive",
                "-Xopt-in=kotlin.RequiresOptIn",
                "-Xjsr305=strict",
                "-Xjsr305=under-migration:warn",
                "-Xjvm-default=enable",
                "-Xuse-mixed-named-arguments",
                "-Xnew-inference",
                "-Xread-deserialized-contracts",
                "-Xassertions=jvm",
                "-Xstrict-java-nullability-assertions",
                "-Xgenerate-strict-metadata-version",
                "-Xemit-jvm-type-annotations",
                "-XXLanguage:+InlineClasses"
//                "-Xmodule-path=$javaClasspath"
            )
        }
    }

    withType<DokkaTask>().configureEach {
        dependsOn(compileJava, javadoc)

        outputFormat = "gfm"
        outputDirectory = "$buildDir/docs/dokka"

        configuration {
            //reportUndocumented = true
            jdkVersion = 8
            platform = "JVM"

            // remember to keep up to date
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.antlr/antlr4/4.8-1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.antlr/antlr4-runtime/4.8-1/") }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.truffle/truffle-api/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.truffle/truffle-nfi/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.sdk/graal-sdk/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.sdk/launcher-common/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.compiler/compiler/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.truffle/truffle-tck/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.sdk/polyglot-tck/$graalVMVersion/")
            }
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.truffle/truffle-dsl-processor/$graalVMVersion/")
            }
//            externalDocumentationLink { url = URL("https://javadoc.io/static/org.junit.jupiter/junit-jupiter-api/5.6.2/") }
//            externalDocumentationLink { url = URL("https://javadoc.io/static/org.junit.jupiter/junit-jupiter-params/5.6.2/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/com.ibm.icu/icu4j/67.1/") }
        }
    }
    val javadocJar by getting(Jar::class) {
        dependsOn(dokka)
    }

    withType<Test>().configureEach {
        useJUnitPlatform()
    }

    jar.configure {
        manifest {
            attributes(
                "Main-Class" to "com.pthariensflame.sylvia.shell.SylviaShellMainKt",
                "Automatic-Module-Name" to "com.pthariensflame.sylvia"
            )
        }
    }

    withType<PublishToMavenLocal>().configureEach { dependsOn(assemble) }
    withType<PublishToMavenRepository>().configureEach { dependsOn(assemble) }
}

idea {
    project {
        jdkName = "GraalVM 1.8 (20)"
    }

    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        sourceDirs = sourceDirs + sequenceOf(
            File("src/main/antlr")
        )
        testSourceDirs = testSourceDirs + sequenceOf(
            File("src/test/antlr")
        )
    }
}
