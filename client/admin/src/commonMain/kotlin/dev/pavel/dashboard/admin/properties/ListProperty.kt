/*
 * # Copyright: (c) 2023, Pavel Novikov <mail@pavel.dev>
 * # GNU General Public License v3.0+ (see COPYING or https://www.gnu.org/licenses/gpl-3.0.txt)
 */

package dev.pavel.dashboard.admin.properties

class ListProperty<T>(
    val initValue: T? = null,
    val listItems: List<ListItem<T>>
) :
    AbstractViewProperty<ListProperty.ListItem<T>?>(
        listItems.firstOrNull {
            initValue != null && it.data == initValue
        }, emptyList()
    ) {
    data class ListItem<T>(
        val name: String,
        val data: T
    )
}