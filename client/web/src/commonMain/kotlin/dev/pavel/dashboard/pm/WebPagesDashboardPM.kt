/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.pm

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class WebPagesDashboardPM(
    pmParams: PmParams,
    private val dashboardRepository: DashboardRepository<Entities.WebPagesDashboard>
) : PresentationModel(pmParams) {
    @Serializable
    data class Description(val dashboardId: DashboardId) : PmDescription

    sealed class WebPagesUIState {
        object Loading : WebPagesUIState()
        object Error : WebPagesUIState()

        class WebPage(val link: String) : WebPagesUIState()

    }

    private val _currentPage = MutableStateFlow<WebPagesUIState>(WebPagesUIState.Loading)
    val currentPage: StateFlow<WebPagesUIState> = _currentPage

    fun start() {
        scope.launch {
            val dashboard =
                dashboardRepository.findDashboardById((description as Description).dashboardId)
            if (dashboard == null) _currentPage.value = WebPagesUIState.Error
            else {
                launch {
                    val links = dashboard.targets().toMutableList()
                    while (currentCoroutineContext().isActive) {
                        val first = links.removeFirst()
                        _currentPage.value = WebPagesUIState.WebPage(first)
                        links.add(first)
                        delay(dashboard.switchTimeoutSeconds().toLong() * 1000)
                    }
                }
            }
        }
    }
}