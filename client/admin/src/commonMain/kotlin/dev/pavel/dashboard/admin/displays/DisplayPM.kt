/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.displays

import dev.pavel.dashboard.admin.EditableCollectionChildPM
import dev.pavel.dashboard.admin.properties.ListProperty
import dev.pavel.dashboard.admin.properties.StringProperty
import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.Entities
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams

class DisplayPM(
    pmParams: PmParams,
    delegate: DisplayPMDelegate
) : EditableCollectionChildPM<DisplayPM.Description>(
    pmParams = pmParams,
    delegate = delegate
) {
    data class Description(
        val id: DisplayId?,
        val name: StringProperty,
        val description: StringProperty,
        val dashboard: ListProperty<String>,
    ) : PmDescription {
        constructor(
            id: DisplayId? = null,
            displayName: String = "",
            description: String = "",
            dashboardId: DashboardId? = null,
            availableDashboards: List<Entities.Dashboard>
        ) : this(
            id,
            StringProperty(displayName),
            StringProperty(description),
            dashboard = ListProperty<DashboardId>(
                initValue = dashboardId,
                listItems = availableDashboards.map {
                    ListProperty.ListItem(it.name(), it.id())
                }
            ),
        )
    }
}