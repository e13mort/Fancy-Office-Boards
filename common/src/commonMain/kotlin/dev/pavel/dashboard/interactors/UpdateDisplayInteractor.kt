/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.Entities

interface UpdateDisplayInteractor {
    suspend fun createOrUpdateDisplay(
        id: String?,
        name: String,
        description: String,
        dashboardId: DashboardId?
    ): Entities.Display

    suspend fun deleteDisplay(id: DisplayId)
}