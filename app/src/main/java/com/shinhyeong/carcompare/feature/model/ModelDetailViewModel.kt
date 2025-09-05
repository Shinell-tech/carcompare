package com.shinhyeong.carcompare.feature.model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinhyeong.carcompare.data.HyundaiRepository
import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ModelDetailViewModel @Inject constructor(
    private val repo: HyundaiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val vehicleId: Long = checkNotNull(savedStateHandle[ModelDetailFragment.ARG_VEHICLE_ID])

    val vehicle: StateFlow<HyundaiVehicleEntity?> =
        repo.observeById(vehicleId)
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
