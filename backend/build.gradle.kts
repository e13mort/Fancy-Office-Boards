@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    application
    `maven-publish`
    alias(libs.plugins.sqldelight)
}

/* required for maven publication */
group = "dev.pavel.dashboard"
version = "0.1"

val browserDist by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

kotlin {
    jvm {
        withJava()
        dependencies {
            dependencies {
                browserDist(
                    project(
                        mapOf(
                            "path" to ":client:web",
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
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.driver.jvm)
                implementation(libs.ktor.server.netty)
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
    mainClass.set("dev.pavel.dashboard.ApplicationKt")
}

tasks.withType<Copy>().named("jvmProcessResources") {
    from(browserDist)
}
