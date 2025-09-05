package com.shinhyeong.carcompare.data.local.db

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "hyundai_vehicles",
    indices = [
        Index(value = ["category"]),
        Index(value = ["model"])
    ]
)
data class HyundaiVehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0, // ⬅️ autoGenerate 오탈자 수정

    // 기본
    val make: String,
    val model: String,
    val variant: String? = null,
    val category: String,           // "passenger" | "commercial"
    val bodyType: String? = null,
    val propulsion: String? = null, // "ICE","HEV","EV"
    val yearFrom: Int? = null,
    val yearTo: Int? = null,

    // 확장 스펙(옵션)
    val imageUrl: String? = null,
    val priceMinWon: Int? = null,
    val priceMaxWon: Int? = null,
    val lengthMm: Int? = null,
    val widthMm: Int? = null,
    val heightMm: Int? = null,
    val wheelbaseMm: Int? = null,
    val curbWeightKg: Int? = null,
    val powerKw: Int? = null,
    val torqueNm: Int? = null
)
