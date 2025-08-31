package com.shinhyeong.carcompare

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import com.shinhyeong.carcompare.data.local.seed.SeedImporter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@HiltAndroidApp
class App : Application() {

    @Inject lateinit var importer: SeedImporter

    // 전역 코루틴 스코프(초기화/시드 임포트용)
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // 앱 최초 실행 시 시드 임포트(이미 있으면 내부에서 skip)
        appScope.launch {
            runCatching { importer.importIfNeeded() }
                .onFailure { it.printStackTrace() }
        }
    }
}
