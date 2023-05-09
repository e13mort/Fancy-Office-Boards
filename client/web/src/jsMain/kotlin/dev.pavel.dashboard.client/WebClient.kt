package dev.pavel.dashboard.client

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import dev.pavel.dashboard.entity.Presenter
import dev.pavel.dashboard.entity.StubPresenter
import org.jetbrains.compose.web.css.Position
import org.jetbrains.compose.web.css.height
import org.jetbrains.compose.web.css.percent
import org.jetbrains.compose.web.css.position
import org.jetbrains.compose.web.css.width
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Iframe
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable

fun main() {
    val presenter = StubPresenter(mutableListOf("https://kotlinlang.org", "https://android.com", "https://jetbrains.com"), 5000)
    renderComposable(rootElementId = "root") {
        val state by remember {
            mutableStateOf(presenter.observeUIStates())
        }
        val uiStateState = state.collectAsState(Presenter.UIState.Loading)

        when(val value = uiStateState.value) {
            is Presenter.UIState.Error -> ShowError(value)
            Presenter.UIState.Loading -> ShowLoading()
            is Presenter.UIState.WebPage -> ShowPage(value)
        }
    }
}

@Composable
fun ShowError(uiState: Presenter.UIState.Error) {
    H1 {
        Text("Error: ${uiState.type}")
    }
}

@Composable
fun ShowPage(value: Presenter.UIState.WebPage) {
    Iframe(attrs = {
        attr("src", value.path)
        style {
            width(100.percent)
            height(100.percent)
            position(Position.Fixed)
        }
    })
}

@Composable
private fun ShowLoading() {
    H1 {
        Text("Loading...")
    }
}