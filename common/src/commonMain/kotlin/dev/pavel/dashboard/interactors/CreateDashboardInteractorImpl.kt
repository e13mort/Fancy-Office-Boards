/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.entity.WebPagesDashboardRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext

class CreateDashboardInteractorImpl(
    private val dashboardRepository: WebPagesDashboardRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) : CreateDashboardInteractor {
    override suspend fun createDashboard(
        targets: List<String>,
        name: String,
        switchDelaySeconds: Int
    ): Entities.WebPagesDashboard {
        return withContext(CoroutineScope(backgroundDispatcher).coroutineContext) {
            val id = dashboardRepository.createDashboard(targets, name, switchDelaySeconds)
            val createdDashboard = dashboardRepository.findDashboardById(id)
            createdDashboard ?: throw IllegalStateException("Failed to load created dashboard info")
        }
    }
}