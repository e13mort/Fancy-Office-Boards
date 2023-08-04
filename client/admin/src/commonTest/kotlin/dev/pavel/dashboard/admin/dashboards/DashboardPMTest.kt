package dev.pavel.dashboard.admin.dashboards

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import dev.pavel.dashboard.admin.EmptyPMStateSaver
import dev.pavel.dashboard.entity.Entities
import dev.pavel.dashboard.interactors.CreateDashboardInteractor
import dev.pavel.dashboard.interactors.UpdateDashboardInteractor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmFactory
import me.dmdev.premo.PmParams
import me.dmdev.premo.PresentationModel
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardPMTest {
    @Test
    fun existingItemShouldEmitViewState() = testPMStates {
        nextStateIs<DashboardPM.State.View>()
    }

    @Test
    fun existingItemMovesToEditState() = testPMStates {
        nextState<DashboardPM.State.View> {
            edit()
        }
        nextStateIs<DashboardPM.State.Edit>()
    }

    @Test
    fun cancelOnEditStateMovesToView() = testPMStates {
        nextState<DashboardPM.State.View> {
            edit()
        }
        nextState<DashboardPM.State.Edit> {
            cancel()
        }
        nextStateIs<DashboardPM.State.View>()
    }

    @Test
    fun validTargetOnViewState() = testPMStates {
        nextState<DashboardPM.State.View> {
            targets.states.test {
                val targetStateList = awaitItem()
                assertTrue(targetStateList.size == 3)
                assertEquals("first", targetStateList[0].target.value)
            }
        }
    }

    @Test
    fun removeTarget() = testPMStates {
        nextState<DashboardPM.State.View> {
            edit()
        }
        nextState<DashboardPM.State.Edit> {
            targets.states.test {
                skipItems(1)
                targets.handleAction(0, DashboardPM.TargetAction.Remove)
                val updatedTargets = awaitItem()
                assertEquals(2, updatedTargets.size)
            }

        }
    }

    @Test
    fun updateTargetName() = testPMStates {
        nextState<DashboardPM.State.View> { edit() }
        nextState<DashboardPM.State.Edit> {
            targets.states.test {
                val firstItems = awaitItem()
                val target = firstItems[0].target
                target.flow.test {
                    skipItems(1)
                    target.value = "updated"
                    assertEquals("updated", awaitItem())
                }
            }
        }
    }

    @Test
    fun linkArrowsAreCorrectAtFirst() = testPMStates {
        nextState<DashboardPM.State.View> { edit() }
        nextState<DashboardPM.State.Edit> {
            targets.states.test {
                val targets = awaitItem()
                targets[0].top()
                targets[1].middle()
                targets[2].bottom()
            }
        }
    }
    @Test
    fun linkArrowsAreCorrectAfterUpdate() = testPMStates {
        nextState<DashboardPM.State.View> { edit() }
        nextState<DashboardPM.State.Edit> {
            targets.states.test {
                skipItems(1)
                targets.handleAction(1, DashboardPM.TargetAction.Up)
                val targets = awaitItem()
                targets[0].top()
                targets[1].middle()
                targets[2].bottom()
            }
        }
    }

    private fun DashboardPM.TargetState.top() {
        assertFalse(upEnabled)
        assertTrue(downEnabled)
    }

    private fun DashboardPM.TargetState.middle() {
        assertTrue(upEnabled)
        assertTrue(downEnabled)
    }

    private fun DashboardPM.TargetState.bottom() {
        assertTrue(upEnabled)
        assertFalse(downEnabled)
    }

    private suspend inline fun <reified T : DashboardPM.State> TurbineTestContext<DashboardPM.State>.nextState(
        block: T.() -> Unit
    ) {
        block(awaitItem() as T)
    }

    private suspend inline fun <reified T : DashboardPM.State> TurbineTestContext<DashboardPM.State>.nextStateIs() {
        assertIs<T>(awaitItem())
    }

    private fun testPMStates(
        description: PmDescription = DashboardPM.Description(
            "existing",
            "Test",
            listOf("first", "second", "third")
        ), validate: suspend TurbineTestContext<DashboardPM.State>.() -> Unit
    ) =
        runPMTest(description) {
            observeStates().test(validate = validate)
        }

    private fun runPMTest(description: PmDescription, testBlock: suspend DashboardPM.() -> Unit) =
        runTest {
            testBlock(createTestPm(description))
        }

    private fun createTestPm(description: PmDescription) = DashboardPM(
        PmParams(
            "test",
            description,
            null,
            TestPMFactory()
        ) { EmptyPMStateSaver },
        TestUpdateDashboardInteractor(),
        TestCreateDashboardInteractor()
    )

    internal class TestPMFactory : PmFactory {
        override fun createPm(params: PmParams): PresentationModel {
            TODO("Not yet implemented")
        }

    }

    internal class TestUpdateDashboardInteractor : UpdateDashboardInteractor {
        override suspend fun updateDashboard(
            id: String, targets: List<String>, name: String
        ): Entities.WebPagesDashboard {
            TODO("Not yet implemented")
        }

    }

    internal class TestCreateDashboardInteractor : CreateDashboardInteractor {
        override suspend fun createDashboard(
            targets: List<String>, name: String
        ): Entities.WebPagesDashboard {
            TODO("Not yet implemented")
        }
    }
}