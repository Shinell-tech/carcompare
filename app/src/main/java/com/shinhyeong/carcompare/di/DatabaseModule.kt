// app/src/main/java/com/shinhyeong/carcompare/di/DatabaseModule.kt
package com.shinhyeong.carcompare.di

import android.content.Context
import androidx.room.Room
import com.shinhyeong.carcompare.data.local.db.AppDatabase
import com.shinhyeong.carcompare.data.local.db.dao.CarCompareDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "carcompare.db")
            // Room 2.7.x에선 파라미터 있는 오버로드 권장
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideCarCompareDao(db: AppDatabase): CarCompareDao = db.carCompareDao()
}
