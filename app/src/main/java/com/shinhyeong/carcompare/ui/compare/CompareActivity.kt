package com.shinhyeong.carcompare.ui.compare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinhyeong.carcompare.data.remote.source.ComparisonConfigProvider
import com.shinhyeong.carcompare.databinding.ActivityCompareBinding
import com.shinhyeong.carcompare.domain.model.VehicleFull
import com.shinhyeong.carcompare.data.remote.dto.ComparisonConfig

class CompareActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCompareBinding
    private lateinit var adapter: ComparisonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) 비교 대상 차량들 가져오기 (샘플/인텐트/DB 등)
        val vehicles: List<VehicleFull> = obtainSelectedVehicles()

        // 2) config 로드 (assets 버전)
        val config: ComparisonConfig = ComparisonConfigProvider(this).loadFromAssets()

        // 3) rows 생성
        val rows = ComparisonRowBuilder.buildRows(config, vehicles)

        // 4) 리사이클러뷰
        adapter = ComparisonAdapter(rows, vehicles.size)
        binding.recyclerViewCompare.apply {
            layoutManager = LinearLayoutManager(this@CompareActivity)
            adapter = this@CompareActivity.adapter
            addItemDecoration(
                DividerItemDecoration(this@CompareActivity, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun obtainSelectedVehicles(): List<VehicleFull> {
        // TODO: 실제 데이터 소스로 교체(Repo/DB에서 읽기)
        return listOf(
            VehicleFull(
                vehicleId = "hyundai_ioniq5_2024_lr_kr",
                brand = "Hyundai", model = "Ioniq 5", year = 2024, marketRegion = "KR",
                powertrain = VehicleFull.Powertrain(type="BEV", drivetrain="AWD", powerKw=239.0, torqueNm=605.0, batteryCapacityKwh=77.4, estRangeKm=458.0, chargingDcKwPeak=230.0),
                performance = VehicleFull.Performance(accel0To100S=5.2, topSpeedKph=185.0, curbWeightKg=2050.0),
                dimensions = VehicleFull.Dimensions(lengthMm=4635.0, widthMm=1890.0, heightMm=1605.0, wheelbaseMm=3000.0, cargoLiters=531.0),
                price = VehicleFull.Price(msrpLocal=59990000.0, currency="KRW"),
                safety = VehicleFull.Safety(nhtsaOverall=5.0, iihsOverall="G", euroncapOverall=5.0)
            ),
            VehicleFull(
                vehicleId = "kia_ev6_2024_gtline_kr",
                brand = "Kia", model = "EV6", year = 2024, marketRegion = "KR",
                powertrain = VehicleFull.Powertrain(type="BEV", drivetrain="RWD", powerKw=168.0, torqueNm=350.0, batteryCapacityKwh=77.4, estRangeKm=475.0, chargingDcKwPeak=240.0),
                performance = VehicleFull.Performance(accel0To100S=7.3, topSpeedKph=188.0, curbWeightKg=1940.0),
                dimensions = VehicleFull.Dimensions(lengthMm=4680.0, widthMm=1880.0, heightMm=1550.0, wheelbaseMm=2900.0, cargoLiters=520.0),
                price = VehicleFull.Price(msrpLocal=53500000.0, currency="KRW"),
                safety = VehicleFull.Safety(nhtsaOverall=5.0, iihsOverall="G", euroncapOverall=5.0)
            )
        )
    }
}
