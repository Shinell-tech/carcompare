package com.shinhyeong.carcompare.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class VehicleFull(
    val vehicleId: String,
    val brand: String,
    val model: String,
    val year: Int,
    val trim: String? = null,
    val marketRegion: String,
    val powertrain: Powertrain? = null,
    val performance: Performance? = null,
    val dimensions: Dimensions? = null,
    val price: Price? = null,
    val safety: Safety? = null
) {
    @Serializable data class Powertrain(
        val type: String? = null, val drivetrain: String? = null,
        val powerKw: Double? = null, val torqueNm: Double? = null,
        val batteryCapacityKwh: Double? = null, val estRangeKm: Double? = null,
        val chargingDcKwPeak: Double? = null
    )
    @Serializable data class Performance(
        val accel0To100S: Double? = null, val topSpeedKph: Double? = null, val curbWeightKg: Double? = null
    )
    @Serializable data class Dimensions(
        val lengthMm: Double? = null, val widthMm: Double? = null, val heightMm: Double? = null,
        val wheelbaseMm: Double? = null, val cargoLiters: Double? = null
    )
    @Serializable data class Price(
        val msrpLocal: Double? = null, val currency: String? = null
    )
    @Serializable data class Safety(
        val nhtsaOverall: Double? = null, val iihsOverall: String? = null, val euroncapOverall: Double? = null
    )
}
