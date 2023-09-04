package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DisplaysInteractor(
    private val repository: DisplayRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) : DataItemsInteractor<Entities.Display> {
    override suspend fun allDataItems(): List<Entities.Display> {
        return withContext(backgroundDispatcher) {
            repository.allDisplays()
        }
    }

}