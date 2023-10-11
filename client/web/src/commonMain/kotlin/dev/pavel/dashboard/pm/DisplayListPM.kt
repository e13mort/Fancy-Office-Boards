/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.pm

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class DisplayListPM(
    pmParams: PmParams,
    private val displayRepository: DisplayRepository,
) : PresentationModel(pmParams) {
    @Serializable
    object Description : PmDescription

    private val _displays = MutableStateFlow<List<Entities.Display>>(emptyList())
    val displays: StateFlow<List<Entities.Display>> = _displays

    class DisplaySelectedMessage(val dashboardId: DashboardId) : PmMessage

    fun load() {
        scope.launch {
            _displays.value = displayRepository.allDisplays().toList()
        }
    }

    fun onDashboardSelected(dashboardId: DashboardId) {
        messageHandler.send(DisplaySelectedMessage(dashboardId))
    }
}

