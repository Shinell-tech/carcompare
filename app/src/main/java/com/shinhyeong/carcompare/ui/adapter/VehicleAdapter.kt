package com.shinhyeong.carcompare.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.shinhyeong.carcompare.data.local.db.HyundaiVehicleEntity
import com.shinhyeong.carcompare.databinding.ItemVehicleBinding

class VehicleAdapter(
    private val onClick: (HyundaiVehicleEntity) -> Unit
) : ListAdapter<HyundaiVehicleEntity, VehicleAdapter.VH>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemVehicleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemVehicleBinding,
        private val onClick: (HyundaiVehicleEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HyundaiVehicleEntity) {
            val title = buildString {
                append(item.model)
                item.variant?.takeIf { it.isNotBlank() }?.let { append(" (").append(it).append(")") }
            }
            binding.title.text = title
            binding.subtitle.text = listOfNotNull(
                item.bodyType,
                item.propulsion,
                yearRange(item.yearFrom, item.yearTo)
            ).joinToString(" · ")

            // 썸네일
            binding.thumbnail.load(item.imageUrl)

            binding.root.setOnClickListener { onClick(item) }
        }

        private fun yearRange(from: Int?, to: Int?): String? {
            if (from == null && to == null) return null
            return when {
                from != null && to != null -> "$from–$to"
                from != null -> "$from–"
                else -> "–$to"
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<HyundaiVehicleEntity>() {
            override fun areItemsTheSame(
                oldItem: HyundaiVehicleEntity, newItem: HyundaiVehicleEntity
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: HyundaiVehicleEntity, newItem: HyundaiVehicleEntity
            ) = oldItem == newItem
        }
    }
}
