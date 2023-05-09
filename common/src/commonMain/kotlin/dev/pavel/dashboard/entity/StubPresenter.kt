package dev.pavel.dashboard.entity

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

class StubPresenter(
    private val links: List<String>,
    private val timeoutMillis: Long
) : Presenter {
    override fun observeUIStates(): Flow<Presenter.UIState> {

        val links = this.links.toMutableList()

        return flow {
            while (currentCoroutineContext().isActive) {
                delay(timeoutMillis)
                val first = links.removeFirst()
                emit(Presenter.UIState.WebPage(first))
                links.add(first)
            }
        }
    }

}