/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.displays

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import dev.pavel.dashboard.admin.EditableCollectionChildPM.State
import dev.pavel.dashboard.admin.EmptyPMStateSaver
import dev.pavel.dashboard.admin.test.TestPMFactory
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.fakes.MemoryDashboardsRepository
import dev.pavel.dashboard.fakes.MemoryDisplayRepository
import dev.pavel.dashboard.interactors.DataItemsInteractor
import dev.pavel.dashboard.interactors.UpdateDisplayInteractorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class DisplayPMTest {
    val dashboardRepository = MemoryDashboardsRepository.create(0L)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testNewDisplayCreation() = runTest {
        createTestPm(createNewDisplayDescription()).apply {
            observeStates().test {
                nextState<State.Edit<DisplayPM.Description>> {
                    item.name.value = "Test Display"
                    item.dashboard.value = item.dashboard.listItems[0]
                    save()
                }
                nextStateIs<State.Saving<DisplayPM.Description>>()
                nextStateIs<State.View<DisplayPM.Description>>()
            }
        }

    }

    private suspend inline fun <reified T> TurbineTestContext<State<DisplayPM.Description>>.nextState(
        block: T.() -> Unit
    ) {
        block(awaitItem() as T)
    }

    private suspend inline fun <reified T> TurbineTestContext<State<DisplayPM.Description>>.nextStateIs() {
        assertIs<T>(awaitItem())
    }

    private suspend fun createNewDisplayDescription() = DisplayPM.Description(
        availableDashboards = dashboardRepository.allDashboards()
    )

    private fun createTestPm(description: PmDescription): DisplayPM {
        return DisplayPM(
            PmParams(
                "test",
                description,
                null,
                TestPMFactory()
            ) { EmptyPMStateSaver },
            DisplayPMDelegate(
                updateDisplayInteractor = UpdateDisplayInteractorImpl(
                    MemoryDisplayRepository(),
                    Dispatchers.Main
                ),
                dashboardDataItemsInteractor = object : DataItemsInteractor<Entities.Dashboard> {
                    override suspend fun allDataItems(): List<Entities.Dashboard> {
                        return dashboardRepository.allDashboards()
                    }

                }
            )
        )
    }

}