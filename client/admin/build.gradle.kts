@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.desktop)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport { enabled.set(true) }
                scssSupport { enabled.set(true) }
            }
        }
        binaries.executable()
        nodejs()
    }

    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                api(libs.kotlinx.serialization.json)
                implementation(libs.premo.main)
                implementation(libs.premo.navigation)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(compose.runtime)
                implementation(compose.web.core)
                implementation(libs.kmdc)
                implementation(libs.kmdcx)
            }
        }
        val jsTest by getting
        jsMain.dependsOn(commonMain)
        /* Test source sets */
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        /* Test hierarchy */
        jsTest.dependsOn(commonTest)
    }
}

val browserDist by configurations.creating {
    isCanBeConsumed = true
    isCanBeResolved = false
}

artifacts {
    add(browserDist.name, tasks
        .named("jsBrowserDistribution")
        .map { it.outputs.files.single() }
    )
}