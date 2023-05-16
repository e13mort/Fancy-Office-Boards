package dev.pavel.dashboard.admin

import dev.pavel.dashboard.entity.DashboardRepository
import dev.pavel.dashboard.fakes.FakeDashboardsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import me.dmdev.premo.PmDelegate
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmFactory
import me.dmdev.premo.PmMessage
import me.dmdev.premo.PmMessageHandler
import me.dmdev.premo.PmParams
import me.dmdev.premo.PmStateSaver
import me.dmdev.premo.PresentationModel
import me.dmdev.premo.navigation.MasterDetailNavigation
import me.dmdev.premo.onMessage
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
        masterPmDescription = AdminMasterPM.Description
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

class DisplaysPM(params: PmParams) : PresentationModel(params) {
    @Serializable
    object Description : PmDescription
}

object ShowDisplays : PmMessage

object ShowDashboards : PmMessage

class AdminPMFactory(
    private val repository: DashboardRepository
) : PmFactory {
    override fun createPm(params: PmParams): PresentationModel {
        println("Create PM for ${params.tag} ${params.description}")
        return when (val description = params.description) {
            is AdminMasterPM.Description -> AdminMasterPM(params)
            is DisplaysPM.Description -> DisplaysPM(params)
            is DashboardsPM.Description -> DashboardsPM(params, repository, Dispatchers.Main)
            is AdminPM.Description -> AdminPM(params)
            else -> throw IllegalArgumentException("Not handled instance creation for pm description $description")
        }
    }
}

object Admin {
    fun createPMDelegate() : PmDelegate<AdminPM> = PmDelegate(
        pmParams = PmParams(
            tag = "AdminPM",
            description = AdminPM.Description,
            parent = null,
            factory = AdminPMFactory(FakeDashboardsRepository.create()),
            stateSaverFactory = { EmptyPMStateSaver }
        )
    )
}

object EmptyPMStateSaver : PmStateSaver {
    override fun <T> restoreState(key: String, kType: KType): T? = null

    override fun <T> saveState(key: String, kType: KType, value: T?) = Unit

}