package dev.pavel.dashboard.resources

import io.ktor.resources.Resource

@Suppress("unused")
@Resource("/dashboard")
class Dashboard(val parent: RestApi = RestApi()) {
    @Resource("{id}")
    class Id(val parent: Dashboard = Dashboard(), val id: String)
}