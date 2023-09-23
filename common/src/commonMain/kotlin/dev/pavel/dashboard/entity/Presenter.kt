/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.entity

import kotlinx.coroutines.flow.Flow

interface Presenter {
    fun observeUIStates(): Flow<UIState>

    sealed class UIState {
        data class Error(val type: ErrorType) : UIState() {
            enum class ErrorType {
                MISSED_DASHBOARD, GENERAL
            }
        }

        data class WebPage(val path: String): UIState()

        data class Displays(val displays: List<Entities.Display>): UIState()

        object Loading : UIState()
    }

}