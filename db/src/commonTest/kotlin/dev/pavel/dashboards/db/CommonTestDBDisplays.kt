/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboards.db

import createTestFile
import dev.pavel.dashboard.db.DBDisplayRepository
import dev.pavel.dashboard.db.DBWebDashboardRepository
import dev.pavel.dashboard.db.DriverFactory
import dev.pavel.dashboard.db.createDatabase
import dev.pavel.dashboard.entity.DashboardId
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.AfterTest
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CommonTestDBDisplays {

    private val testDB = createTestFile()
    private val database = createDatabase(DriverFactory.persistent(testDB.name()))
    private val dashboardRepository = DBWebDashboardRepository(database.dashboardQueries)
    private val displayRepository = DBDisplayRepository(database.displayQueries)

    @AfterTest
    fun removeDB() {
        testDB.remove()
    }

    @Test
    fun `create display without dashboard`() = runTest {
        displayRepository.createDisplay("display1", "testDescription", null)
        assertEquals(1, displayRepository.allDisplays().size)
    }

    @Test
    fun `delete display`() = runTest {
        val id = displayRepository.createDisplay("display1", "testDescription", null)
        displayRepository.delete(id)
        assertEquals(0, displayRepository.allDisplays().size)
    }

    @Test
    fun `create display with correct properties`() = runTest {
        displayRepository.createDisplay("display1", "testDescription", null)
        displayRepository.allDisplays()[0].assertProperties("display1", "testDescription", null)
    }

    @Test
    fun `update display`() = runTest {
        val id = displayRepository.createDisplay("display1", "testDescription", null)
        displayRepository.updateDisplay(id, "updatedName", "updatedDescription", null)
        displayRepository.allDisplays()[0].assertProperties(
            "updatedName",
            "updatedDescription",
            null
        )
    }

    @Test
    fun `create display with invalid dashboard id`() = runTest {
        assertFails {
            displayRepository.createDisplay("display1", "testDescription", 123)
        }
    }

    @Test
    fun `update display with invalid dashboard id`() = runTest {
        assertFails {
            val id = displayRepository.createDisplay("display1", "testDescription", null)
            displayRepository.updateDisplay(id, "updatedName", "updatedDescription", 123)
        }
    }

    @Test
    fun `create display with valid dashboard id`() = runTest {
        val id = dashboardRepository.createDashboard(listOf(), "test", 10)
        displayRepository.createDisplay("display1", "testDescription", id)
        displayRepository.allDisplays()[0].assertProperties("display1", "testDescription", id)
    }

    @Test
    fun `deletion of dashboard erases bound display property`() = runTest {
        val id = dashboardRepository.createDashboard(listOf(), "test", 10)
        displayRepository.createDisplay("display1", "testDescription", id)
        dashboardRepository.delete(id)
        displayRepository.allDisplays()[0].assertProperties("display1", "testDescription", null)
    }
}

private fun Entities.Display.assertProperties(
    name: String,
    description: String,
    dashboardId: DashboardId?
) {
    assertEquals(name, name())
    assertEquals(description, description())
    assertEquals(dashboardId, dashboardId())
}
