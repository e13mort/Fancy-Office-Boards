/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

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
    }

    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.premo.main)
                implementation(libs.premo.navigation)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
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
