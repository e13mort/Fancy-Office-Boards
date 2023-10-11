/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard

import dev.pavel.dashboard.pm.DisplayListPM
import dev.pavel.dashboard.pm.MainWebClientPM
import dev.pavel.dashboard.pm.WebPagesDashboardPM
import me.dmdev.premo.PmFactory
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel

class ClientPMFactory(
    private val dependencies: WebClientApp.Dependencies
) : PmFactory {
    override fun createPm(params: PmParams): PresentationModel {
        return when (val description = params.description) {
            MainWebClientPM.Description -> MainWebClientPM(params)
            DisplayListPM.Description -> DisplayListPM(params, dependencies.displayRepository)
            is WebPagesDashboardPM.Description -> WebPagesDashboardPM(
                params,
                dependencies.dashboardRepository
            )

            else -> throw IllegalArgumentException("Not handled instance creation for pm description $description")
        }
    }
}