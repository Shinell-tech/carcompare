package com.shinhyeong.carcompare.feature.compare

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shinhyeong.carcompare.databinding.ItemCompareRowBinding

class CompareAdapter :
    ListAdapter<CompareRowUi, CompareAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<CompareRowUi>() {
        override fun areItemsTheSame(oldItem: CompareRowUi, newItem: CompareRowUi) =
            oldItem.key == newItem.key

        override fun areContentsTheSame(oldItem: CompareRowUi, newItem: CompareRowUi) =
            oldItem == newItem
    }

    inner class VH(val binding: ItemCompareRowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CompareRowUi) = with(binding) {
            title.text = buildString {
                append(item.title)
                item.unit?.let { append(" (").append(it).append(")") }
            }

            // 최대 3개 값 표시(필요 시 늘려도 됨)
            val values = item.cells.map { it.display ?: "-" }
            v1.text = values.getOrNull(0) ?: "-"
            v2.text = values.getOrNull(1) ?: "-"
            v3.text = values.getOrNull(2) ?: "-"

            val style = if (item.isDifferent) Typeface.BOLD else Typeface.NORMAL
            v1.setTypeface(null, style)
            v2.setTypeface(null, style)
            v3.setTypeface(null, style)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inf = LayoutInflater.from(parent.context)
        val b = ItemCompareRowBinding.inflate(inf, parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }
}
