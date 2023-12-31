/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.interactors

import dev.pavel.dashboard.entity.DisplayRepository
import dev.pavel.dashboard.entity.Entities
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DisplaysInteractor(
    private val displayRepository: DisplayRepository,
    private val backgroundDispatcher: CoroutineDispatcher
) : DataItemsInteractor<Entities.Display> {
    override suspend fun allDataItems(): List<Entities.Display> {
        return withContext(backgroundDispatcher) {
            displayRepository.allDisplays()
        }
    }

}