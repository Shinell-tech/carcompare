package com.shinhyeong.carcompare.feature.compare

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinhyeong.carcompare.data.HyundaiRepository
import com.shinhyeong.carcompare.data.local.seed.SeedImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val repo: HyundaiRepository,
    private val importer: SeedImporter
) : ViewModel() {

    private val _category = MutableStateFlow("passenger") // 기본값 고정
    private val _selectedA = MutableStateFlow<UiVehicle?>(null)

    /** 초기 시딩을 안전하게 수행 */
    init {
        viewModelScope.launch {
            runCatching { importer.importIfEmpty() }
                .onFailure { Log.e("CompareViewModel", "Seeding failed", it) }
        }
    }

    /** 선택된 카테고리(승용/상용)에 맞는 리스트 스트림 */
    val vehicles: StateFlow<List<UiVehicle>> =
        _category
            .flatMapLatest { cat ->
                repo.observeByCategory(cat)
            }
            .map { list ->
                list.map {
                    UiVehicle(
                        id = it.id,
                        displayName = it.variant?.let { v -> "${it.model} ($v)" } ?: it.model,
                        model = it.model,
                        variant = it.variant,
                        bodyType = it.bodyType,
                        propulsion = it.propulsion,
                        category = it.category
                    )
                }
            }
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val selectedA: StateFlow<UiVehicle?> = _selectedA.asStateFlow()

    fun setCategory(value: String) {
        if (value != _category.value) {
            _category.value = value
            _selectedA.value = null // 카테고리 변경 시 선택 초기화
        }
    }

    fun selectA(index: Int) {
        val list = vehicles.value
        if (index in list.indices) _selectedA.value = list[index]
    }

    data class UiVehicle(
        val id: Long,
        val displayName: String,
        val model: String,
        val variant: String?,
        val bodyType: String?,
        val propulsion: String?,
        val category: String
    )
}
