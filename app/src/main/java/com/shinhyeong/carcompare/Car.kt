package com.shinhyeong.carcompare

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Car(
    val name: String,
    val brand: String,   // ✅ brand 추가
    val horsepower: Int,
    val torque: Int,     // ✅ cars_seed.json에는 torque도 있었음
    val weight: Int,
    val price: Int = 0   // ✅ JSON에 price 없으면 기본값 0
) : Parcelable