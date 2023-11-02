/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.basic
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.nio.file.Paths
import kotlin.io.path.isDirectory
import kotlin.io.path.isWritable

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    val authProviderName = "auth-admin"
    setupBasicAuthProvider(authProviderName)
    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    installRestApi(authProviderName, prepareDBFile())
    routing {
        staticResources("scripts", "scripts")
        staticResources("static", "static")
        registerPage("/", "Dashboard", "web.js")
        authenticate(authProviderName) {
            registerPage("/admin", "Dashboard Admin", "admin.js")
        }
    }
}

private fun Application.setupBasicAuthProvider(authProviderName: String) {
    val (adminUserName, adminPassword) = readAdminCredentialsFromEnv()
    install(Authentication) {
        basic(authProviderName) {
            realm = "Access to Admin Panel"
            validate { credential ->
                if (credential.name == adminUserName && credential.password == adminPassword)
                    UserIdPrincipal(credential.name)
                else null
            }
        }
    }
}

fun Application.readAdminCredentialsFromEnv(): Pair<String, String> {
    // todo: add to docs steps for app launch
    // (installDist, run with /bin/backend -P:ktor.adminBasicAuth.userName=*** -P:ktor.adminBasicAuth.password=*** )
    return environment.config.run {
        Pair(
            property(PROPERTY_ADMIN_USERNAME).getString(),
            property(PROPERTY_ADMIN_PASSWORD).getString(),
        )
    }
}

private fun Application.prepareDBFile(): String {
    val dataDir = environment.config.property(PROPERTY_DATA_DIR)
    val dirPath = Paths.get(dataDir.getString())
    if (!dirPath.isDirectory() || !dirPath.isWritable()) {
        throw IllegalArgumentException("$dirPath should be writable directory")
    }
    val dbFileName = environment.config.property(PROPERTY_DB_FILE_NAME).getString()
    return dirPath.resolve(dbFileName).toString()
}

fun Route.registerPage(route: String, pageTitle: String, scriptName: String) {
    get(route) {
        call.respondHtml {
            mainHTMLContent(pageTitle, scriptName)
        }
    }
}