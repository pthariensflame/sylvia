pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
    resolutionStrategy {
        eachPlugin {
            if(requested.id.name == "kotlinx-atomicfu") {
                useModule("org.jetbrains.kotlinx:atomicfu-gradle-plugin:${requested.version}")
            }
        }
    }
}

rootProject.name = "sylvia"
