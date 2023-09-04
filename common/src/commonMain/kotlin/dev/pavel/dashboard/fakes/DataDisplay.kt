package dev.pavel.dashboard.fakes

import dev.pavel.dashboard.entity.DisplayId
import dev.pavel.dashboard.entity.Entities

data class DataDisplay(
    private val _name: String,
    private val _id: DisplayId
) : Entities.Display {
    override fun name(): String = _name

    override fun id(): DisplayId = _id
}