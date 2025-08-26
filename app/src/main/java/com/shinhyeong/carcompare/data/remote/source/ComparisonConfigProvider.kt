package com.shinhyeong.carcompare.data.remote.source

import android.content.Context
import com.shinhyeong.carcompare.data.remote.dto.ComparisonConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

class ComparisonConfigProvider(
    private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    // v1: assets에서 로드. 이후 네트워크/캐시로 대체 가능
    fun loadFromAssets(): ComparisonConfig {
        val bytes = context.assets.open("comparison_config.json").use { it.readBytes() }
        return json.decodeFromString(bytes.decodeToString())
    }
}
