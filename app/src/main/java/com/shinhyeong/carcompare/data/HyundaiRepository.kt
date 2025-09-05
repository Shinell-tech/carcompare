package com.shinhyeong.carcompare.data

import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import com.shinhyeong.carcompare.data.local.db.dao.HyundaiVehicleDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HyundaiRepository @Inject constructor(
    private val dao: HyundaiVehicleDao
) {
    fun observeByCategory(category: String): Flow<List<HyundaiVehicleEntity>> =
        dao.observeByCategory(category)

    fun observeById(id: Long): Flow<HyundaiVehicleEntity?> =
        dao.observeById(id)
}
