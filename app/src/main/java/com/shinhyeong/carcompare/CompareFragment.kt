package com.shinhyeong.carcompare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shinhyeong.carcompare.databinding.FragmentCompareBinding

class CompareFragment : Fragment() {
    private lateinit var binding: FragmentCompareBinding
    private lateinit var cars: ArrayList<Car>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cars = arguments?.getParcelableArrayList("cars") ?: arrayListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val text = buildString {
            cars.forEach {
                append("${it.brand} ${it.name}\nHP: ${it.horsepower} / Torque: ${it.torque} / Weight: ${it.weight}\n\n")
            }
        }
        binding.txtComparison.text = text
    }

    companion object {
        private const val KEY_CARS = "cars"

        fun newInstance(cars: ArrayList<Car>): CompareFragment =
            CompareFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_CARS, cars)   // ← 여기 따옴표 앞에 백슬래시 넣지 말기
                }
            }
    }
}