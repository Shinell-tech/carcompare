package com.shinhyeong.carcompare.feature.compare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shinhyeong.carcompare.R
import com.shinhyeong.carcompare.databinding.FragmentCompareBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!
    private val vm: CompareViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        super.onViewCreated(v, s)

        binding.categoryGroup.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rbPassenger -> vm.setCategory("passenger")
                R.id.rbCommercial -> vm.setCategory("commercial")
            }
        }

        lifecycleScope.launch {
            vm.vehicles.collectLatest { items ->
                val names = items.map { it.displayName }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, names)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerA.adapter = adapter
                binding.spinnerA.setSelection(0, false)
                if (names.isNotEmpty()) vm.selectA(0)
            }
        }

        binding.spinnerA.setOnItemSelectedListener { index ->
            vm.selectA(index)
        }

        lifecycleScope.launch {
            vm.selectedA.collectLatest { item ->
                binding.cardA.isVisible = item != null
                item ?: return@collectLatest
                binding.tvTitleA.text = item.displayName
                binding.tvBodyTypeA.text = item.bodyType ?: "-"
                binding.tvPropulsionA.text = item.propulsion ?: "-"
                binding.tvCategoryA.text = item.category
            }
        }
    }

    // small extension to avoid setting a whole listener class
    private fun View.setOnItemSelectedListener(onSelected: (Int) -> Unit) {
        if (this !is android.widget.Spinner) return
        this.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long
            ) { onSelected(position) }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
