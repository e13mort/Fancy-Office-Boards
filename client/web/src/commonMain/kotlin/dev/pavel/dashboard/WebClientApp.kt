/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.pm.MainWebClientPM
import me.dmdev.premo.JsPmDelegate
import me.dmdev.premo.saver.JsonStateSaver

object WebClientApp {
    data class Dependencies(
        val displayRepository: DisplayRepository,
        val dashboardRepository: DashboardRepository<Entities.WebPagesDashboard>
    )

    fun createPMDelegate(dependencies: Dependencies): JsPmDelegate<MainWebClientPM> {
        return JsPmDelegate(
            pmDescription = MainWebClientPM.Description,
            pmFactory = ClientPMFactory(dependencies),
            pmStateSaver = JsonStateSaver(Serializers.json)
        )
    }
}