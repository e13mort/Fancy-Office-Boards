/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboards.db

import app.cash.turbine.test
import createTestFile
import dev.pavel.dashboard.db.DBWebDashboardRepository
import dev.pavel.dashboard.db.DriverFactory
import dev.pavel.dashboard.db.createDatabase
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CommonTestDBWebDashboard {

    private val testDB = createTestFile()
    private val database = createDatabase(DriverFactory.persistent(testDB.name()))
    private val repository = DBWebDashboardRepository(database.dashboardQueries)

    @AfterTest
    fun removeDB() {
        testDB.remove()
    }

    @Test
    fun `there is a single created dashboard`() = runTest {
        repository.createDashboard(emptyList(), "testName", 0)
        assertEquals(1, repository.allDashboards().size)
    }

    @Test
    fun `created dashboard can be requested by its id`() = runTest {
        val dashboard = repository.createDashboard(emptyList(), "testName", 0)
        assertNotNull(repository.findDashboardById(dashboard))
    }

    @Test
    fun `there are two created dashboard`() = runTest {
        val firstId = repository.createDashboard(emptyList(), "testName", 0)
        val secondId = repository.createDashboard(emptyList(), "testName2", 0)
        repository.findDashboardById(firstId)!!.assertTargetProperties(
            "testName", 0, emptyList()
        )
        repository.findDashboardById(secondId)!!.assertTargetProperties(
            "testName2", 0, emptyList()
        )
    }

    @Test
    fun `delete dashboard`() = runTest {
        val dashboard = repository.createDashboard(emptyList(), "testName", 0)
        repository.delete(dashboard)
        assertNull(repository.findDashboardById(dashboard))
    }

    @Test
    fun `created dashboard has valid properties`() = runTest {
        val dashboardId =
            repository.createDashboard(listOf("first", "second", "third"), "testName", 5)
        val dashboard = repository.findDashboardById(dashboardId)!!
        val targetLinks = listOf("first", "second", "third")
        val targetName = "testName"
        val targetTimeout = 5
        dashboard.assertTargetProperties(targetName, targetTimeout, targetLinks)
    }

    @Test
    fun `update dashboard`() = runTest {
        val id =
            repository.createDashboard(listOf("first", "second", "third"), "testName", 5)
        repository.updateDashboard(id, listOf("updated1", "updated2"), "updatedName", 10)
        repository.findDashboardById(id)!!.assertTargetProperties(
            "updatedName",
            10,
            listOf("updated1", "updated2")
        )
    }

    @Test
    fun `observe dashboard notifications`() = runTest {
        val id =
            repository.createDashboard(listOf("first", "second", "third"), "testName", 5)
        repository.observeDashboardById(id).test {
            awaitItem().assertTargetProperties(
                "testName",
                5,
                listOf("first", "second", "third")
            )
            repository.updateDashboard(id, listOf("updated1", "updated2"), "updatedName", 10)
            awaitItem().assertTargetProperties(
                "updatedName",
                10,
                listOf("updated1", "updated2")
            )
        }
    }

    private fun Entities.WebPagesDashboard.assertTargetProperties(
        targetName: String,
        targetTimeout: Int,
        targetLinks: List<String>
    ) {
        assertEquals(targetName, name())
        assertEquals(targetTimeout, switchTimeoutSeconds())
        targetLinks.forEach {
            assertContains(targets(), it)
        }
    }
}