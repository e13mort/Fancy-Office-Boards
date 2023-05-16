package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.Entities

class FakeWebDashboard(
    private val targets: List<String>,
    private val timeout: Int,
    private val name: String
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

}