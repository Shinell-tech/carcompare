package com.shinhyeong.carcompare.feature.compare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinhyeong.carcompare.databinding.FragmentCompareBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CompareFragment : Fragment() {

    private var _binding: FragmentCompareBinding? = null
    private val binding get() = _binding!!

    private val vm: CompareViewModel by viewModels()
    private lateinit var adapter: CompareAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = CompareAdapter()
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@CompareFragment.adapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        // 로딩 시작
        vm.loadDefault()

        viewLifecycleOwner.lifecycleScope.launch {
            vm.state.collectLatest { st ->
                binding.progress.isVisible = st.isLoading
                binding.errorGroup.isVisible = st.error != null
                binding.errorText.text = st.error ?: ""

                // 상단 트림 타이틀 바인딩
                binding.trimTitleContainer.removeAllViews()
                st.titleTrims.forEachIndexed { index, (_, name) ->
                    val tv = layoutInflater.inflate(
                        com.shinhyeong.carcompare.R.layout.item_trim_title, binding.trimTitleContainer, false
                    ) as android.widget.TextView
                    tv.text = "${index + 1}. $name"
                    binding.trimTitleContainer.addView(tv)
                }

                // 리스트
                adapter.submitList(st.rows)
            }
        }

        binding.btnRetry.setOnClickListener { vm.loadDefault() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
