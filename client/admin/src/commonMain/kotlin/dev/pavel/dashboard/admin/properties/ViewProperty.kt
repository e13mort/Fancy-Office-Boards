/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.properties

import kotlinx.coroutines.flow.StateFlow

interface ViewProperty<T> {
    val flow: StateFlow<T>
    var value: T
    val errors: StateFlow<String?>

    fun isValid() = errors.value == null
}