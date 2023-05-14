package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val pm = remember {
            Admin.createPMDelegate().let { delegate ->
                delegate.onCreate()
                delegate.onForeground()
                delegate.presentationModel
            }
        }
        RenderUI(pm)
    }
}

@Composable
private fun RenderUI(pm: AdminPM) {
    pm.RenderMaster()
    when (val detailsPM = pm.navigation.detailFlow.collectAsState().value) {
        is DisplaysPM -> detailsPM.Render()
        is DashboardsPM -> detailsPM.Render()
    }
}