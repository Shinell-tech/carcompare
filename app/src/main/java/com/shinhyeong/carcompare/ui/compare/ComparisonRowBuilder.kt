package com.shinhyeong.carcompare.ui.compare

import com.shinhyeong.carcompare.data.remote.dto.ComparisonConfig
import com.shinhyeong.carcompare.domain.model.VehicleFull
import com.shinhyeong.carcompare.domain.usecase.ValueExtractor

object ComparisonRowBuilder {

    fun buildRows(config: ComparisonConfig, vehicles: List<VehicleFull>): List<RowRender> {
        val rootJsonList = vehicles.map { ValueExtractor.encodeAnyToJsonElement(it) }
        val rows = mutableListOf<RowRender>()

        for (section in config.sections) {
            // 섹션 헤더
            rows += RowRender(sectionHeader = section.title, label = null)

            for (row in section.rows) {
                val values = rootJsonList.mapIndexed { idx, root ->
                    val node = ValueExtractor.getByPath(root, row.path)
                    val currency = row.currencyPath?.let { cp ->
                        ValueExtractor.getByPath(root, cp)?.let { it.toString().trim('"') }
                    }
                    ValueExtractor.formatValue(node, row.format, row.decimals, row.unit, currency)
                }

                // 하이라이트(최댓값/최솟값) 계산
                val hi = computeHighlight(values, row.higherIsBetter)

                rows += RowRender(
                    sectionHeader = null,
                    label = row.label,
                    values = values,
                    highlightIndex = hi
                )
            }
        }
        return rows
    }

    private fun computeHighlight(values: List<String>, higherIsBetter: Boolean?): Int? {
        if (higherIsBetter == null) return null
        // 문자열을 Double로 파싱 가능한 것만 비교
        val parsed = values.map { it.replace(",", "").split(" ").firstOrNull()?.toDoubleOrNull() }
        val idx = if (higherIsBetter) {
            parsed.withIndex().maxByOrNull { it.value ?: Double.NEGATIVE_INFINITY }?.index
        } else {
            parsed.withIndex().minByOrNull { it.value ?: Double.POSITIVE_INFINITY }?.index
        }
        return idx
    }
}
