package dev.pavel.dashboard

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    routing {
        staticResources("scripts", "scripts")
        staticResources("static", "static")
        registerPage("/", "Dashboard", "web.js")
        registerPage("/admin", "Dashboard Admin", "admin.js")
    }
}

fun Routing.registerPage(route: String, pageTitle: String, scriptName: String) {
    get(route) {
        call.respondHtml {
            mainHTMLContent(pageTitle, scriptName)
        }
    }
}