package com.shinhyeong.carcompare.ui.compare

data class RowRender(
    val sectionHeader: String?,           // 섹션 헤더 줄이면 값 있음, 일반 데이터 줄이면 null
    val label: String?,                   // 데이터 줄의 라벨
    val values: List<String> = emptyList(), // 차량들 각각의 값
    val highlightIndex: Int? = null       // 최고/최저 강조할 인덱스 (optional)
)
