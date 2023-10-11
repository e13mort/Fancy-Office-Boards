/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.pm.MainWebClientPM
import me.dmdev.premo.PmDelegate
import me.dmdev.premo.PmParams
import me.dmdev.premo.PmStateSaver
import kotlin.reflect.KType

object WebClientApp {
    data class Dependencies(
        val displayRepository: DisplayRepository,
        val dashboardRepository: DashboardRepository<Entities.WebPagesDashboard>
    )

    fun createPMDelegate(dependencies: Dependencies): PmDelegate<MainWebClientPM> {
        return PmDelegate(
            pmParams = PmParams(
                tag = "WebClientApp",
                description = MainWebClientPM.Description,
                parent = null,
                factory = ClientPMFactory(dependencies),
                stateSaverFactory = { EmptyPMStateSaver }
            )
        )
    }

    object EmptyPMStateSaver : PmStateSaver {
        override fun <T> restoreState(key: String, kType: KType): T? = null

        override fun <T> saveState(key: String, kType: KType, value: T?) = Unit

    }
}