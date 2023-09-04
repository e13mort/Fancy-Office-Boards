package dev.pavel.dashboard.interactors

interface DataItemsInteractor<T> {
    suspend fun allDataItems(): List<T>
}