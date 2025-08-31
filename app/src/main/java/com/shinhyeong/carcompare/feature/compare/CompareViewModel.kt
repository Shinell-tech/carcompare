package com.shinhyeong.carcompare.feature.compare

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinhyeong.carcompare.data.local.db.dao.CarCompareDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.shinhyeong.carcompare.feature.compare.CompareRowUi
import com.shinhyeong.carcompare.feature.compare.buildCompare

@HiltViewModel
class CompareViewModel @Inject constructor(
    private val dao: CarCompareDao
) : ViewModel() {

    data class UiState(
        val isLoading: Boolean = true,
        val titleTrims: List<Pair<Long, String>> = emptyList(), // (trimId, name)
        val rows: List<CompareRowUi> = emptyList(),
        val error: String? = null
    )

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    fun loadDefault(n: Int = 3) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            runCatching {
                // 1) 최근 트림 n개 가져오기
                val trims = dao.getRecentTrims(n)
                if (trims.isEmpty()) {
                    throw IllegalStateException("No trims found. Seed import may have failed.")
                }
                val trimIds = trims.map { it.id }
                val names = trims.map { it.id to "${it.trimName} (${it.yearStart}${it.yearEnd?.let { "–$it" } ?: ""})" }

                // 2) 스펙 매트릭스 조회
                val rows = dao.getSpecMatrix(trimIds)

                // 3) 비교표 빌드 (기존 유틸)
                val compare = buildCompare(rows, trimIds)

                _state.value = UiState(
                    isLoading = false,
                    titleTrims = names,
                    rows = compare,
                    error = null
                )
            }.onFailure {
                _state.value = UiState(isLoading = false, error = it.message ?: "Unknown error")
            }
        }
    }
}
