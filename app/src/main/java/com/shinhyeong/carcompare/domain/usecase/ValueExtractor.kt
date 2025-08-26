package com.shinhyeong.carcompare.domain.usecase

import kotlinx.serialization.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * VehicleFull 을 kotlinx.serialization 으로 JsonElement로 만든 뒤
 * "a.b.c" 경로(path)로 값을 꺼냅니다.
 */
object ValueExtractor {

    private val json = Json { ignoreUnknownKeys = true; explicitNulls = false }

    fun encodeAnyToJsonElement(value: Any): JsonElement {
        // value가 이미 직렬화 가능한 @Serializable 타입이라고 가정.
        // (VehicleFull 를 @Serializable 로 선언하는 걸 권장)
        val text = json.encodeToString(value)
        return json.parseToJsonElement(text)
    }

    fun getByPath(root: JsonElement, path: String): JsonElement? {
        var cur: JsonElement? = root
        for (key in path.split(".")) {
            cur = (cur as? JsonObject)?.get(key) ?: return null
        }
        return cur
    }

    fun formatValue(
        node: JsonElement?, format: String, decimals: Int? = null, unit: String? = null,
        currency: String? = null
    ): String {
        if (node == null || node is JsonNull) return "—"
        return when (format) {
            "number" -> {
                val d = node.doubleOrNull() ?: return "—"
                val s = if (decimals != null) "%.${decimals}f".format(d) else d.toString()
                if (unit != null) "$s $unit" else s
            }
            "currency" -> {
                val d = node.doubleOrNull() ?: return "—"
                val s = if (decimals != null) "%.${decimals}f".format(d) else d.toString()
                if (!currency.isNullOrBlank()) "$s $currency" else s
            }
            else -> node.asPrimitiveOrString()
        }
    }

    private fun JsonElement.asPrimitiveOrString(): String =
        when (this) {
            is JsonPrimitive -> if (isString) content else booleanOrNull?.toString() ?: doubleOrNull?.toString() ?: content
            else -> toString()
        }

    private fun JsonElement.doubleOrNull(): Double? =
        (this as? JsonPrimitive)?.let { p ->
            p.doubleOrNull ?: p.longOrNull?.toDouble()
        }
}
