package com.shinhyeong.carcompare.feature.model

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinhyeong.carcompare.R
import com.shinhyeong.carcompare.databinding.FragmentModelListBinding
import com.shinhyeong.carcompare.feature.model.ModelDetailFragment.Companion.newInstance as detailOf
import com.shinhyeong.carcompare.ui.adapter.VehicleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ModelListFragment : Fragment() {

    private var _binding: FragmentModelListBinding? = null
    private val binding get() = _binding!!

    private val vm: ModelListViewModel by viewModels()
    private lateinit var adapter: VehicleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModelListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = VehicleAdapter { vehicle ->
            parentFragmentManager.beginTransaction()
                .replace(requireActivity().findViewById<View>(R.id.container).id, detailOf(vehicle.id))
                .addToBackStack(null)
                .commit()
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = this@ModelListFragment.adapter
        }

        // 카테고리 토글
        binding.segmentPassenger.setOnClickListener { vm.setCategory("passenger") }
        binding.segmentCommercial.setOnClickListener { vm.setCategory("commercial") }

        // 검색 연동
        binding.search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                vm.setQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    vm.category.collect { cat ->
                        binding.segmentPassenger.isChecked = cat == "passenger"
                        binding.segmentCommercial.isChecked = cat == "commercial"
                    }
                }
                launch {
                    vm.vehicles.collect { list ->
                        binding.empty.isVisible = list.isEmpty()
                        adapter.submitList(list)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object { fun newInstance() = ModelListFragment() }
}
