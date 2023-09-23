/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.entity

import kotlinx.serialization.Serializable

@Serializable
data class DataDisplay(
    private val id: DisplayId,
    private val name: String,
    private val description: String,
    private val dashboardId: DashboardId?
) : Entities.Display {
    override fun name(): String = name
    override fun description(): String = description
    override fun dashboardId(): DashboardId? = dashboardId
    override fun id(): DisplayId = id
}