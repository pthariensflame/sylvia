import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.net.URL

plugins {
    `java-library`
    antlr
    kotlin("jvm") version "1.4-M1"
    kotlin("kapt") version "1.4-M1"
//    id("kotlinx-atomicfu") version "0.14.2-1.4-M1"
    id("org.jetbrains.dokka") version "0.10.1"
    `maven-publish`
    application
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
        if (id.group.startsWith("org.jline")) {
            belongsTo("org.jline:jline-virtual-platform:${id.version}", true)
        }
        if (id.group.startsWith("org.junit")) {
            belongsTo("org.junit:junit-bom:${id.version}", false)
        }
        if (id.group.startsWith("io.kotest")) {
            belongsTo("io.kotest:kotest-virtual-platform:${id.version}", true)
        }
    }
    api(enforcedPlatform("org.graalvm:graalvm-virtual-platform:$graalVMVersion"))
    implementation(enforcedPlatform("org.jline:jline-virtual-platform:3.14.1"))
    testApi(platform("org.junit:junit-bom:5.7.+"))
    testApi(enforcedPlatform("io.kotest:kotest-virtual-platform:4.0.5"))
    constraints {
        testImplementation("junit", "junit", "[4.13,)")
        implementation("org.fusesource.jansi", "jansi", "[1.18,)")
    }


    api("org.jetbrains", "annotations", "19.+")
    api(kotlin("stdlib-jdk8"))
    api(kotlin("reflect"))
    testApi(kotlin("test"))
    testApi(kotlin("test-junit5"))

    testImplementation("org.junit.jupiter", "junit-jupiter-api")
    testImplementation("org.junit.jupiter", "junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter", "junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage", "junit-vintage-engine")

    testImplementation("io.kotest", "kotest-runner-junit5-jvm", "4.0.5")
    testImplementation("io.kotest", "kotest-assertions-core-jvm", "4.0.5")
    testApi("io.kotest", "kotest-property-jvm", "4.0.5")
    testImplementation("io.kotest", "kotest-assertions-compiler-jvm", "4.0.5")

    antlr("org.antlr", "antlr4", "4.8-1")
    api("org.antlr", "antlr4-runtime", "4.8-1")

    implementation("com.ibm.icu", "icu4j", "[67.1,)")

    api("org.jline", "jline-terminal", "3.14.1")
    api("org.jline", "jline-reader", "3.14.1")
    api("org.jline", "jline-style", "3.14.1")
    implementation("org.jline", "jline-terminal-jansi", "3.14.1")

    api("org.graalvm.truffle", "truffle-api", graalVMVersion)
    runtimeOnly("org.graalvm.truffle", "truffle-nfi", graalVMVersion)
    api("org.graalvm.sdk", "graal-sdk", graalVMVersion)
    api("org.graalvm.sdk", "launcher-common", graalVMVersion)
    kapt("org.graalvm.truffle", "truffle-dsl-processor", graalVMVersion)
    runtimeOnly("org.graalvm.compiler", "compiler", graalVMVersion)
    api("org.graalvm.tools", "lsp_api", graalVMVersion)
    testApi("org.graalvm.truffle", "truffle-tck", graalVMVersion)
    testApi("org.graalvm.sdk", "polyglot-tck", graalVMVersion)
}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_11
    withJavadocJar()
    withSourcesJar()
    modularity.inferModulePath.set(true)
}
val compileJava: JavaCompile by tasks
val javaCompileClasspath = compileJava.classpath.asPath

kapt {
    correctErrorTypes = true
    includeCompileClasspath = false

    javacOptions {
        option("--module-path", javaCompileClasspath)
    }
}

//atomicfu {
//    dependenciesVersion = "0.14.2-1.4-M1"
//    transformJvm = true
//    variant = "BOTH"
//    transformJs = false
//}

application {
    applicationName = "sylvia"
//    mainModule.set("com.pthariensflame.sylvia")
    mainClassName = "com.pthariensflame.sylvia.shell.SylviaLauncher"
}

tasks {
    compileKotlin.configure { dependsOn.add(generateGrammarSource) }
    compileTestKotlin.configure { dependsOn.add(generateTestGrammarSource) }

//    withType<JavaCompile>().configureEach {
//    }

    withType<AntlrTask>().configureEach {
        arguments = arguments + sequenceOf(
            "-listener",
            "-visitor",
            "-long-messages"
        )
    }

    withType<KotlinCompile>().configureEach {
        usePreciseJavaTracking = true

        kotlinOptions {
            jvmTarget = "11"
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
                "-Xjsr305=under-migration:strict",
                "-Xjvm-default=enable",
                "-Xuse-mixed-named-arguments",
                "-Xnew-inference",
                "-Xeffect-system",
                "-Xread-deserialized-contracts",
                "-Xassertions=jvm",
                "-Xstrict-java-nullability-assertions",
                "-Xgenerate-strict-metadata-version",
                "-Xemit-jvm-type-annotations",
                "-Xinline-classes"
//                "-Xmodule-path=$javaCompileClasspath"
            )
        }
    }

    withType<DokkaTask>().configureEach {
        dependsOn(compileJava, javadoc)

        outputFormat = "gfm"
        outputDirectory = "$buildDir/docs/dokka"

        configuration {
            //reportUndocumented = true
            jdkVersion = 11
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
            externalDocumentationLink {
                url = URL("https://javadoc.io/static/org.graalvm.tools/lsp_api/$graalVMVersion/")
            }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.junit.jupiter/junit-jupiter-api/5.6.2/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.junit.jupiter/junit-jupiter-params/5.6.2/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/com.ibm.icu/icu4j/67.1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.jline/jline-terminal/3.14.1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.jline/jline-reader/3.14.1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.jline/jline-style/3.14.1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.jline/jline-terminal-jansi/3.14.1/") }
            externalDocumentationLink { url = URL("https://javadoc.io/static/org.fusesource.jansi/jansi/1.18/") }
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
                "Automatic-Module-Name" to "com.pthariensflame.sylvia"
            )
        }
    }

    withType<PublishToMavenLocal>().configureEach { dependsOn(assemble) }
    withType<PublishToMavenRepository>().configureEach { dependsOn(assemble) }
}

idea {
    project {
        jdkName = "SapMachine 14"
    }

    module {
        isDownloadJavadoc = true
        isDownloadSources = true
        sourceDirs = sourceDirs + sequenceOf(
            File("$rootDir/src/main/antlr")
        )
        testSourceDirs = testSourceDirs + sequenceOf(
            File("$rootDir/src/test/antlr")
        )
        excludeDirs = excludeDirs + sequenceOf(
            File("$rootDir/src/main/unwanted"),
            File("$rootDir/src/test/unwanted")
        )
    }
}
