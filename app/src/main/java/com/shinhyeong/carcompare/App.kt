package com.shinhyeong.carcompare

import android.app.Application
import android.util.Log
import com.shinhyeong.carcompare.data.local.seed.SeedImporter
import com.shinhyeong.carcompare.di.IODispatcher
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var importer: SeedImporter
    @Inject @IODispatcher lateinit var io: CoroutineDispatcher

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(SupervisorJob() + io).launch {
            runCatching { importer.importIfEmpty() }
                .onFailure { Log.e("App", "Seeding failed at launch.", it) }
        }
    }
}
