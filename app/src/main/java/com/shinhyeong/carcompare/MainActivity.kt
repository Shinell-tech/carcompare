package com.shinhyeong.carcompare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shinhyeong.carcompare.databinding.ActivityMainBinding
import com.shinhyeong.carcompare.feature.compare.CompareFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, CompareFragment())
                .commit()
        }
    }
}
