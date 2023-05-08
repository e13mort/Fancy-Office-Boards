package dev.pavel.dashboard.client

import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        Iframe(attrs = {
            attr("src", "https://google.com")
            style {
                width(100.percent)
                height(100.percent)
                position(Position.Fixed)
            }
        })
    }
}