package com.shinhyeong.carcompare.feature.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinhyeong.carcompare.data.HyundaiRepository
import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import com.shinhyeong.carcompare.data.local.seed.SeedImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

@HiltViewModel
class ModelListViewModel @Inject constructor(
    private val repo: HyundaiRepository,
    private val importer: SeedImporter
) : ViewModel() {

    private val _category = MutableStateFlow("passenger")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        viewModelScope.launch { runCatching { importer.importIfEmpty() } }
    }

    private val source: Flow<List<HyundaiVehicleEntity>> =
        _category.flatMapLatest { repo.observeByCategory(it) }

    val vehicles: StateFlow<List<HyundaiVehicleEntity>> =
        combine(source, _query) { list, q ->
            val nq = q.trim().lowercase(Locale.getDefault())
            if (nq.isEmpty()) list
            else list.filter {
                it.model.lowercase(Locale.getDefault()).contains(nq) ||
                        (it.variant?.lowercase(Locale.getDefault())?.contains(nq) == true)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun setCategory(value: String) {
        if (value != _category.value) _category.value = value
    }

    fun setQuery(value: String) { _query.value = value }
}
