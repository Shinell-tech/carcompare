package com.shinhyeong.carcompare.data.local.seed

import android.content.Context
import android.util.Log
import com.shinhyeong.carcompare.data.local.db.AppDatabase
import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import com.shinhyeong.carcompare.di.AppJson
import com.shinhyeong.carcompare.di.IODispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.FileNotFoundException
import javax.inject.Inject

class SeedImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: AppDatabase,
    @AppJson private val json: Json,
    @IODispatcher private val io: CoroutineDispatcher
) {
    @Serializable
    data class VehicleSeed(
        val make: String,
        val model: String,
        val variant: String? = null,
        val category: String,
        val bodyType: String? = null,
        val propulsion: String? = null,
        val yearFrom: Int? = null,
        val yearTo: Int? = null,
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

    private val assetPath = "hyundai/hyundai_kr_2025-09-01.json"

    suspend fun importIfEmpty() = withContext(io) {
        try {
            val count = db.hyundaiVehicleDao().count()
            if (count > 0) {
                Log.d("SeedImporter", "DB already seeded: $count rows")
                return@withContext
            }
            val rows = try {
                val raw = context.assets.open(assetPath).bufferedReader().use { it.readText() }
                val seeds = json.decodeFromString<List<VehicleSeed>>(raw)
                seeds.map { it.toEntity() }.also {
                    Log.i("SeedImporter", "Loaded ${it.size} seeds from assets/$assetPath")
                }
            } catch (fnf: FileNotFoundException) {
                Log.w("SeedImporter", "Asset not found: assets/$assetPath, using fallback seeds.")
                fallbackSeeds()
            }
            db.hyundaiVehicleDao().insertAll(rows)
            Log.i("SeedImporter", "Seed completed: inserted ${rows.size} rows.")
        } catch (t: Throwable) {
            Log.e("SeedImporter", "Seeding failed (app continues).", t)
        }
    }

    private fun VehicleSeed.toEntity() = HyundaiVehicleEntity(
        make = make, model = model, variant = variant, category = category,
        bodyType = bodyType, propulsion = propulsion, yearFrom = yearFrom, yearTo = yearTo,
        imageUrl = imageUrl, priceMinWon = priceMinWon, priceMaxWon = priceMaxWon,
        lengthMm = lengthMm, widthMm = widthMm, heightMm = heightMm, wheelbaseMm = wheelbaseMm,
        curbWeightKg = curbWeightKg, powerKw = powerKw, torqueNm = torqueNm
    )

    private fun fallbackSeeds(): List<HyundaiVehicleEntity> = listOf(
        HyundaiVehicleEntity(make="Hyundai", model="Avante", variant="1.6", category="passenger",
            bodyType="Sedan", propulsion="ICE", yearFrom=2020, imageUrl="https://picsum.photos/seed/avante/640/360"),
        HyundaiVehicleEntity(make="Hyundai", model="Sonata", variant="2.0", category="passenger",
            bodyType="Sedan", propulsion="ICE", yearFrom=2019, imageUrl="https://picsum.photos/seed/sonata/640/360"),
        HyundaiVehicleEntity(make="Hyundai", model="Kona", variant="EV", category="passenger",
            bodyType="SUV", propulsion="EV", yearFrom=2021, imageUrl="https://picsum.photos/seed/kona/640/360"),
        HyundaiVehicleEntity(make="Hyundai", model="Porter II", variant="3.5", category="commercial",
            bodyType="Truck", propulsion="ICE", yearFrom=2015, imageUrl="https://picsum.photos/seed/porter/640/360"),
        HyundaiVehicleEntity(make="Hyundai", model="County", variant="Bus", category="commercial",
            bodyType="Bus", propulsion="ICE", yearFrom=2012, imageUrl="https://picsum.photos/seed/county/640/360")
    )
}
