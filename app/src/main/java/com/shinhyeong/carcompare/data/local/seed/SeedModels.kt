package com.shinhyeong.carcompare.data.local.seed

import kotlinx.serialization.Serializable

@Serializable
data class SeedField(
    val key: String,
    val title: String,
    val group: String,
    val type: String,
    val unit: String? = null,
    val description: String? = null
)

@Serializable
data class SeedAllowedValue(
    val key: String,
    val label: String
)

@Serializable
data class SeedMake(
    val name: String,
    val country: String? = null
)

@Serializable
data class SeedModel(
    val make: String,
    val name: String,
    val bodyStyle: String? = null
)

@Serializable
data class SeedTrim(
    val model: String,          // "Make:Model"
    val trimName: String,
    val yearStart: Int,
    val yearEnd: Int? = null,
    val priceMsrp: Int? = null,
    val currency: String = "KRW"
)

@Serializable
data class SeedValue(
    val trim: String,           // "Make:Model:Trim"
    val field: String,
    val number: Double? = null,
    val int: Long? = null,
    val text: String? = null,
    val bool: Boolean? = null,
    val enum: String? = null
)

@Serializable
data class Seed(
    val fields: List<SeedField>,
    val allowedValues: Map<String, List<SeedAllowedValue>> = emptyMap(),
    val makes: List<SeedMake> = emptyList(),
    val models: List<SeedModel> = emptyList(),
    val trims: List<SeedTrim> = emptyList(),
    val values: List<SeedValue> = emptyList()
)
