package com.shinhyeong.carcompare.feature.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.shinhyeong.carcompare.databinding.FragmentModelDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@AndroidEntryPoint
class ModelDetailFragment : Fragment() {

    private var _binding: FragmentModelDetailBinding? = null
    private val binding get() = _binding!!
    private val vm: ModelDetailViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentModelDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.vehicle.collect { v ->
                    if (v == null) return@collect

                    binding.title.text = buildString {
                        append(v.model)
                        v.variant?.takeIf { it.isNotBlank() }?.let { append(" (").append(it).append(")") }
                    }
                    binding.hero.load(v.imageUrl)

                    binding.kvMake.key.text = "제조사";   binding.kvMake.value.text = v.make
                    binding.kvCategory.key.text = "카테고리"; binding.kvCategory.value.text = v.category
                    binding.kvBody.key.text = "차체형식";  binding.kvBody.value.text = v.bodyType ?: "—"
                    binding.kvPropulsion.key.text = "구동";   binding.kvPropulsion.value.text = v.propulsion ?: "—"
                    binding.kvYears.key.text = "연식";      binding.kvYears.value.text = yearRange(v.yearFrom, v.yearTo)

                    binding.kvPrice.key.text = "가격(원)"
                    binding.kvPrice.value.text = priceRange(v.priceMinWon, v.priceMaxWon)

                    binding.kvSize.key.text = "크기(mm)"
                    binding.kvSize.value.text = sizeText(v.lengthMm, v.widthMm, v.heightMm, v.wheelbaseMm, v.curbWeightKg)

                    binding.kvPower.key.text = "성능"
                    binding.kvPower.value.text = perfText(v.powerKw, v.torqueNm)
                }
            }
        }
    }

    private fun yearRange(from: Int?, to: Int?): String =
        when {
            from != null && to != null -> "$from–$to"
            from != null -> "$from–"
            to != null -> "–$to"
            else -> "—"
        }

    private fun priceRange(min: Int?, max: Int?): String {
        val nf = NumberFormat.getInstance(Locale.KOREA)
        return when {
            min != null && max != null -> "${nf.format(min)} ~ ${nf.format(max)}"
            min != null -> "${nf.format(min)} ~"
            max != null -> "~ ${nf.format(max)}"
            else -> "—"
        }
    }

    private fun sizeText(L: Int?, W: Int?, H: Int?, WB: Int?, kg: Int?): String {
        val parts = mutableListOf<String>()
        if (L != null && W != null && H != null) parts += "${L}×${W}×${H}"
        if (WB != null) parts += "축거 $WB"
        if (kg != null) parts += "공차중량 ${kg}kg"
        return parts.ifEmpty { listOf("—") }.joinToString(" · ")
    }

    private fun perfText(kw: Int?, nm: Int?): String {
        val hp = kw?.let { (it * 1.341).toInt() }
        val p = kw?.let { "$it kW" + (hp?.let { " (~${hp} hp)" } ?: "") }
        val t = nm?.let { "$it Nm" }
        return listOfNotNull(p, t).ifEmpty { listOf("—") }.joinToString(" · ")
    }

    override fun onDestroyView() { super.onDestroyView(); _binding = null }

    companion object {
        const val ARG_VEHICLE_ID = "vehicleId"
        fun newInstance(vehicleId: Long) = ModelDetailFragment().apply {
            arguments = bundleOf(ARG_VEHICLE_ID to vehicleId)
        }
    }
}
