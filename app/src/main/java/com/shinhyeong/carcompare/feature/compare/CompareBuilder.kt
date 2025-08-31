// app/src/main/java/com/shinhyeong/carcompare/feature/compare/CompareBuilder.kt
package com.shinhyeong.carcompare.feature.compare

import com.shinhyeong.carcompare.data.local.db.FieldType
import java.text.DecimalFormat

fun buildCompare(rows: List<SpecRow>, trimIds: List<Long>): List<CompareRowUi> {
    val byField = rows.groupBy { it.key }
    return byField.map { (key, list) ->
        val first = list.first()
        val cellMap = list.associateBy { it.trimId }
        val cells = trimIds.map { tid ->
            val r = cellMap[tid]
            val disp = when (r?.type) {
                FieldType.NUMBER -> r.numberValue?.let { formatNumber(it, first.unit) }
                FieldType.INTEGER -> r.intValue?.toString()
                FieldType.TEXT -> r.textValue
                FieldType.BOOLEAN -> r.boolValue?.let { if (it) "Yes" else "No" }
                FieldType.ENUM -> r.enumKey
                null -> null
            }
            CompareCell(tid, disp)
        }
        val valuesSet = cells.map { it.display ?: "-" }.toSet()
        CompareRowUi(key, first.title, first.unit, cells, valuesSet.size > 1)
    }.sortedWith(compareBy({ it.isDifferent.not() }, { it.title }))
}

private fun formatNumber(v: Double, unit: String?): String =
    (DecimalFormat("#,###.##").format(v)) + (unit?.let { " $it" } ?: "")
