package com.shinhyeong.carcompare

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shinhyeong.carcompare.databinding.FragmentCarListBinding
import com.shinhyeong.carcompare.R

// 만약 CompareFragment가 다른 패키지에 있다면 아래 import를 네 패키지에 맞게 수정하세요.
// import com.shinhyeong.carcompare.ui.compare.CompareFragment

class CarListFragment : Fragment() {

    private var _binding: FragmentCarListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CarAdapter
    private val selectedCars = mutableListOf<Car>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1) 데이터 로드
        val cars = loadCars()

        // 2) 어댑터 세팅 (체크박스 선택 콜백)
        adapter = CarAdapter(cars) { car, isChecked ->
            if (isChecked) {
                if (!selectedCars.contains(car)) selectedCars.add(car)
            } else {
                selectedCars.remove(car)
            }
        }

        // 3) RecyclerView 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 4) 비교 버튼 클릭
        binding.btnCompare.setOnClickListener {
            if (selectedCars.size >= 2) {
                // activity_main.xml 안에 @+id/fragment_container 가 있어야 합니다.
                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        CompareFragment.newInstance(ArrayList(selectedCars))
                    )
                    .addToBackStack(null)
                    .commit()
            } else {
                // 최소 2대 선택 유도 (원하면 Toast 등으로 안내 추가)
                Log.w("CarListFragment", "Select at least 2 cars to compare")
            }
        }
    }

    private fun loadCars(): List<Car> {
        return try {
            // res/raw/cars_seed.json 이 정확히 있어야 R.raw.cars_seed 가 생성됩니다.
            val inputStream = resources.openRawResource(R.raw.cars_seed)
            val json = inputStream.bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<Car>>() {}.type
            Gson().fromJson(json, type)
        } catch (t: Throwable) {
            Log.e("CarListFragment", "Failed to load cars_seed.json", t)
            emptyList()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
