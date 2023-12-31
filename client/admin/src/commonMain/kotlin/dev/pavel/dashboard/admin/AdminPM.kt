/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin

import dev.pavel.dashboard.admin.dashboards.DashboardPM
import dev.pavel.dashboard.admin.dashboards.DashboardPMDelegate
import dev.pavel.dashboard.admin.dashboards.DashboardsPM
import dev.pavel.dashboard.admin.displays.DisplayPM
import dev.pavel.dashboard.admin.displays.DisplayPMDelegate
import dev.pavel.dashboard.admin.displays.DisplaysPM
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.CreateDashboardInteractor
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor
import dev.pavel.dashboard.interactors.DataItemsInteractor
import dev.pavel.dashboard.interactors.UpdateDisplayInteractor
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDelegate
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmFactory
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmMessageHandler
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.MasterDetailNavigation
import me.dmdev.premo.onMessage
import me.dmdev.premo.saver.PmStateSaver
import me.dmdev.premo.saver.PmStateSaverFactory
import kotlin.reflect.KType

class AdminPM(params: PmParams) : PresentationModel(params) {

    @Serializable
    object Description : PmDescription

    class Tab(
        val title: String,
        private val messageHandler: PmMessageHandler,
        private val message: PmMessage
        ) {
        fun activate() {
            messageHandler.send(message)
        }
    }

    fun activeTabs() = listOf(
        Tab("Displays", messageHandler, ShowDisplays),
        Tab("Dashboards", messageHandler, ShowDashboards),
    )

    val navigation = MasterDetailNavigation<AdminMasterPM, PresentationModel>(
        masterDescription = AdminMasterPM.Description
    ) { navigator ->
        onMessage<ShowDisplays> {
            navigator.changeDetail(Child(DisplaysPM.Description))
        }
        onMessage<ShowDashboards> {
            navigator.changeDetail(Child(DashboardsPM.Description))
        }
    }
}

class AdminMasterPM(params: PmParams) : PresentationModel(params) {
    @Serializable
    object Description : PmDescription
}

object ShowDisplays : PmMessage

object ShowDashboards : PmMessage

class AdminPMFactory(
    private val dependencies: Admin.Dependencies
) : PmFactory {
    override fun createPm(params: PmParams): PresentationModel {
        return when (val description = params.description) {
            is AdminMasterPM.Description -> AdminMasterPM(params)
            is DisplaysPM.Description -> DisplaysPM(
                params = params,
                dataItemsInteractor = dependencies.displayInteractor,
                dashboardsInteractor = dependencies.pagesDashboardInteractor
            )
            is DashboardsPM.Description -> DashboardsPM(
                params,
                dependencies.pagesDashboardInteractor
            )
            is AdminPM.Description -> AdminPM(params)
            is DashboardPM.Description -> DashboardPM(
                params,
                DashboardPMDelegate(
                    dependencies.updateDashboardInteractor,
                    dependencies.createDashboardInteractor
                )

            )
            is DisplayPM.Description -> DisplayPM(params, DisplayPMDelegate(
                dependencies.updateDisplayInteractor,
                dependencies.pagesDashboardInteractor
            ))
            else -> throw IllegalArgumentException("Not handled instance creation for pm description $description")
        }
    }

}

object Admin {
    class Dependencies(
        val pagesDashboardInteractor: DataItemsInteractor<Entities.WebPagesDashboard>,
        val updateDashboardInteractor: UpdateDashboardInteractor,
        val createDashboardInteractor: CreateDashboardInteractor,
        val displayInteractor: DataItemsInteractor<Entities.Display>,
        val updateDisplayInteractor: UpdateDisplayInteractor
    )
    fun createPMDelegate(dependencies: Dependencies) : PmDelegate<AdminPM> {
        return PmDelegate(
            pmParams = PmParams(
                description = AdminPM.Description,
                parent = null,
                factory = AdminPMFactory(
                    dependencies
                ),
                stateSaverFactory = object : PmStateSaverFactory {
                    override fun createPmStateSaver(key: String): PmStateSaver = EmptyPMStateSaver

                    override fun deletePmStateSaver(key: String) = Unit

                }
            )
        )
    }
}

object EmptyPMStateSaver : PmStateSaver {
    override fun <T> restoreState(key: String, kType: KType): T? = null

    override fun <T> saveState(key: String, kType: KType, value: T?) = Unit

}