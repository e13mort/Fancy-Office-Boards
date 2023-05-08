package dev.pavel.dashboard.client

import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Text("Hello from WEB")
        println("RENDER!!!")
    }
}