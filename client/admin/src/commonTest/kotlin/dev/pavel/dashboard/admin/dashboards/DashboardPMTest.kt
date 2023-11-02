/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.dashboards

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import dev.pavel.dashboard.admin.EmptyPMStateSaver
import dev.pavel.dashboard.admin.EditableCollectionChildPM.State
import dev.pavel.dashboard.admin.test.TestPMFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import me.dmdev.premo.PmDescription
import me.dmdev.premo.PmParams
import me.dmdev.premo.saver.PmStateSaver
import me.dmdev.premo.saver.PmStateSaverFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardPMTest {
    @Test
    fun existingItemShouldEmitViewState() = testPMStates {
        nextStateIs<State.View<DashboardPM.Description>>()
    }

    @Test
    fun existingItemMovesToEditState() = testPMStates {
        nextState<State.View<DashboardPM.Description>> {
            edit()
        }
        nextStateIs<State.Edit<DashboardPM.Description>>()
    }

    @Test
    fun cancelOnEditStateMovesToView() = testPMStates {
        nextState<State.View<DashboardPM.Description>> {
            edit()
        }
        nextState<State.Edit<DashboardPM.Description>> {
            cancel()
        }
        nextStateIs<State.View<DashboardPM.Description>>()
    }
    @Test
    fun cancelOnEditStateRestoreSourceData() = testPMStates {
        nextState<State.View<DashboardPM.Description>> {
            edit()
        }
        nextState<State.Edit<DashboardPM.Description>> {
            item.name.value = "updated"
            cancel()
        }
        nextState<State.View<DashboardPM.Description>> {
            assertEquals("Test", item.name.value)
        }
    }

    @Test
    fun validTargetOnViewState() = testPMStates {
        nextState<State.View<DashboardPM.Description>> {
            item.targets.states.test {
                val targetStateList = awaitItem()
                assertTrue(targetStateList.size == 3)
                assertEquals("first", targetStateList[0].target.value)
            }
        }
    }

    @Test
    fun removeTarget() = testPMStates {
        nextState<State.View<DashboardPM.Description>> {
            edit()
        }
        nextState<State.Edit<DashboardPM.Description>> {
            item.targets.states.test {
                skipItems(1)
                item.targets.handleAction(0, DashboardPM.TargetAction.Remove)
                val updatedTargets = awaitItem()
                assertEquals(2, updatedTargets.size)
            }

        }
    }

    @Test
    fun updateTargetName() = testPMStates {
        nextState<State.View<DashboardPM.Description>> { edit() }
        nextState<State.Edit<DashboardPM.Description>> {
            item.targets.states.test {
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
        nextState<State.View<DashboardPM.Description>> { edit() }
        nextState<State.Edit<DashboardPM.Description>> {
            item.targets.states.test {
                val targets = awaitItem()
                targets[0].top()
                targets[1].middle()
                targets[2].bottom()
            }
        }
    }
    @Test
    fun linkArrowsAreCorrectAfterUpdate() = testPMStates {
        nextState<State.View<DashboardPM.Description>> { edit() }
        nextState<State.Edit<DashboardPM.Description>> {
            item.targets.states.test {
                skipItems(1)
                item.targets.handleAction(1, DashboardPM.TargetAction.Up)
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

    private suspend inline fun <reified T> TurbineTestContext<State<DashboardPM.Description>>.nextState(
        block: T.() -> Unit
    ) {
        block(awaitItem() as T)
    }

    private suspend inline fun <reified T>TurbineTestContext<State<DashboardPM.Description>>.nextStateIs() {
        assertIs<T>(awaitItem())
    }

    private fun testPMStates(
        description: PmDescription = DashboardPM.Description(
            0,
            "Test",
            0,
            listOf("first", "second", "third"),
        ), validate: suspend TurbineTestContext<State<DashboardPM.Description>>.() -> Unit
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
            description,
            null,
            TestPMFactory(),
            object : PmStateSaverFactory {
                override fun createPmStateSaver(key: String): PmStateSaver = EmptyPMStateSaver

                override fun deletePmStateSaver(key: String) = Unit
            }
        ),
        DashboardPMDelegate(
            TestUpdateDashboardInteractor(),
            TestCreateDashboardInteractor()
        )
    )

}