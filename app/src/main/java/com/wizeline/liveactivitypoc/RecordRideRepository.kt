package com.wizeline.liveactivitypoc

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object RecordRideRepository {
    private val _destinationNameFlow = MutableStateFlow("")
    private val _milesLeftFlow = MutableStateFlow(.0f)
    private val _progressFlow = MutableStateFlow(0)

    val progressFlow: StateFlow<Int> get() = _progressFlow

    suspend fun updateDestinationName(destination: String) {
        _destinationNameFlow.emit(destination)
    }

    suspend fun setMilesLeft(milesLeft: Float) {
        _milesLeftFlow.emit(milesLeft)
    }

    suspend fun updateProgress(percentageDriven: Int) {
        _progressFlow.emit(percentageDriven)
    }
}