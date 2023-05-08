package dev.pavel.dashboard

import io.ktor.server.application.Application
import io.ktor.server.http.content.defaultResource
import io.ktor.server.http.content.static
import io.ktor.server.routing.routing

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    routing {
        static {
            defaultResource("index.html")
        }
    }
}