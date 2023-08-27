package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import kotlinx.serialization.Serializable

@Serializable
class DataWebDashboard(
    private val targets: List<String>,
    private val timeout: Int,
    private val name: String,
    private val id: String
) : Entities.WebPagesDashboard {
    override fun targets(): List<String> {
        return targets
    }

    override fun switchTimeoutSeconds(): Int {
        return timeout
    }

    override fun name(): String {
        return name
    }

    override fun id(): DashboardId {
        return id
    }

}