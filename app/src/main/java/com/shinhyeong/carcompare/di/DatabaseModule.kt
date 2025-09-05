package com.shinhyeong.carcompare.di

import android.content.Context
import androidx.room.Room
import com.shinhyeong.carcompare.data.local.db.AppDatabase
import com.shinhyeong.carcompare.data.local.db.dao.HyundaiVehicleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DB_NAME = "carcompare.db"

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration() // 개발 단계: 스키마 변경 시 드롭 재생성
            .build()

    @Provides
    fun provideHyundaiVehicleDao(db: AppDatabase): HyundaiVehicleDao = db.hyundaiVehicleDao()
}
