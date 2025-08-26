package com.shinhyeong.carcompare.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComparisonConfig(
    val version: Int = 1,
    val sections: List<ComparisonSection> = emptyList()
)

@Serializable
data class ComparisonSection(
    val title: String,
    val rows: List<ComparisonRow>
)

@Serializable
data class ComparisonRow(
    val label: String,
    val path: String,                 // dot-path, e.g., "powertrain.powerKw"
    val format: String = "text",      // "text" | "number" | "currency"
    val decimals: Int? = null,        // number 포맷 소수점
    val higherIsBetter: Boolean? = null, // 하이라이트 판단
    val unit: String? = null,         // 선택: "kW", "km/h" 등
    val currencyPath: String? = null  // format=="currency"일 때 통화코드 경로
)
