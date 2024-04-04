package com.wizeline.liveactivitypoc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecordRideNotificationViewModel : ViewModel() {
    private val repository = RecordRideRepository
    private val totalDistance = MutableLiveData(0.0f)

    val progressState: StateFlow<Int> get() = repository.progressFlow

    fun startRide(destination: String, totalDistance: Float) {
        this.totalDistance.value = totalDistance
        viewModelScope.launch {
            repository.updateDestinationName(destination)
        }
        viewModelScope.launch {
            repository.setMilesLeft(totalDistance)
        }
        viewModelScope.launch {
            (0..5).forEach {
                updateRideDataProgress(it, "12:01")
                delay(3500)
            }
        }
    }

    fun updateRideDataProgress(progression: Int, eta: String) {
        viewModelScope.launch {
            repository.updateProgress(progression)
        }
    }
}