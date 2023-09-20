package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.Entities
import kotlinx.serialization.Serializable

@Serializable
data class DataDisplay(
    private val id: DisplayId,
    private val name: String,
    private val description: String
) : Entities.Display {
    override fun name(): String = name
    override fun description(): String = description
    override fun id(): DisplayId = id
}