/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.sqldelight.coroutines)
                implementation(libs.kotlinx.serialization.json)
                implementation(project(":common"))
            }
        }
        val jvmMain by getting {
            dependencies {
                api(libs.sqldelight.driver.jvm)
            }
        }

        /* Main hierarchy */
        jvmMain.dependsOn(commonMain)

        /* Test source sets */
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.turbine)
            }
        }
        val jvmTest by getting

        /* Test hierarchy */
        jvmTest.dependsOn(commonTest)
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("dev.pavel.dashboard.db")
        }
    }
}