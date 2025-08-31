// app/src/main/java/com/shinhyeong/carcompare/feature/compare/CompareModels.kt
package com.shinhyeong.carcompare.feature.compare

import com.shinhyeong.carcompare.data.local.db.FieldType

// DAO 결과 매핑용
data class SpecRow(
    val key: String,
    val title: String,
    val groupKey: String,
    val type: FieldType,
    val unit: String?,
    val numberValue: Double?,
    val intValue: Long?,
    val textValue: String?,
    val boolValue: Boolean?,
    val enumKey: String?,
    val trimId: Long
)

data class CompareCell(val trimId: Long, val display: String?)
data class CompareRowUi(
    val key: String,
    val title: String,
    val unit: String?,
    val cells: List<CompareCell>,
    val isDifferent: Boolean
)
