package dev.pavel.dashboard.admin.properties

class ListProperty<T>(
    val initValue: T? = null,
    val listItems: List<ListItem<T>>
) :
    AbstractViewProperty<ListProperty.ListItem<T>?>(
        listItems.firstOrNull {
            initValue != null && it.data == initValue
        }
    ) {
    data class ListItem<T>(
        val name: String,
        val data: T
    )
}