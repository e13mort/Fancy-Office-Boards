/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    application
    `maven-publish`
}

/* required for maven publication */
group = "dev.pavel.dashboard"
version = "0.1"

val mainUIDist by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}
val adminDist by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

kotlin {
    jvm {
        withJava()
        dependencies {
            dependencies {
                mainUIDist(
                    project(
                        mapOf(
                            "path" to ":client:web",
                            "configuration" to "browserDist"
                        )
                    )
                )
                adminDist(
                    project(
                        mapOf(
                            "path" to ":client:admin",
                            "configuration" to "browserDist"
                        )
                    )
                )
            }
        }
    }

    sourceSets {
        /* Main source sets */
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.touchlab.kermit)
                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.sqldelight.coroutines)
                implementation(project(":common"))
                implementation(project(":db"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
                implementation(libs.sqlite.jdbc) //explicit declaration in order to run backend from IDE
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.builder.html)
                implementation(libs.ktor.server.auth)
                implementation(libs.ktor.server.content.negotiation)
                implementation(libs.ktor.server.resources)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.logback)
            }
        }

        /* Main hierarchy */
        jvmMain.dependsOn(commonMain)

        /* Test source sets */
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmTest by getting

        /* Test hierarchy */
        jvmTest.dependsOn(commonTest)
    }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

createCopyWebArtifactsTask(mainUIDist)
createCopyWebArtifactsTask(adminDist)

fun createCopyWebArtifactsTask(configuration: Configuration) {
    tasks.withType<Copy>().named("jvmProcessResources") {
        from(configuration) {
            into("scripts")
            exclude {
                it.name.endsWith("html") // ignore html from modules
            }
        }
    }
}