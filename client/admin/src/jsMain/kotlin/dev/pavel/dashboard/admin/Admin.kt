package dev.pavel.dashboard.admin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import dev.pavel.dashboard.admin.dashboards.DashboardsPM
import dev.pavel.dashboard.fakes.MemoryDashboardsRepository
import dev.pavel.dashboard.interactors.CreateDashboardInteractorImpl
import dev.pavel.dashboard.interactors.UpdateDashboardInteractorImpl
import dev.pavel.dashboard.interactors.WebPagesDashboardInteractorImpl
import kotlinx.coroutines.Dispatchers
import org.jetbrains.compose.web.renderComposable

fun main() {
    renderComposable(rootElementId = "root") {
        val pm = remember {
            Admin.createPMDelegate(createDependencies()).let { delegate ->
                delegate.onCreate()
                delegate.onForeground()
                delegate.presentationModel
            }
        }
        RenderUI(pm)
    }
}

// TODO: delegate to DI framework
fun createDependencies(): Admin.Dependencies {
    val dashboardRepository = MemoryDashboardsRepository.create()
    val backgroundDispatcher = Dispatchers.Main
    return Admin.Dependencies(
        WebPagesDashboardInteractorImpl(dashboardRepository, backgroundDispatcher),
        UpdateDashboardInteractorImpl(dashboardRepository, backgroundDispatcher),
        CreateDashboardInteractorImpl(dashboardRepository, backgroundDispatcher)
    )
}

@Composable
private fun RenderUI(pm: AdminPM) {
    pm.RenderMaster()
    when (val detailsPM = pm.navigation.detailFlow.collectAsState().value) {
        is DisplaysPM -> detailsPM.Render()
        is DashboardsPM -> detailsPM.Render()
    }
}