package com.shinhyeong.carcompare.feature.compare

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shinhyeong.carcompare.databinding.ItemCompareRowBinding

class CompareAdapter(
    private val trimOrder: List<Long>
) : ListAdapter<CompareRowUi, CompareAdapter.VH>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<CompareRowUi>() {
        override fun areItemsTheSame(o: CompareRowUi, n: CompareRowUi) = o.key == n.key
        override fun areContentsTheSame(o: CompareRowUi, n: CompareRowUi) = o == n
    }

    inner class VH(val b: ItemCompareRowBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCompareRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val row = getItem(position)
        val b = holder.b

        b.tvTitle.text = row.title + (row.unit?.let { " ($it)" } ?: "")
        b.tvTitle.setTypeface(null, if (row.isDifferent) Typeface.BOLD else Typeface.NORMAL)

        // 동적 컬럼
        b.cellContainer.removeAllViews()
        val ctx = b.root.context
        val cellsByTrim = row.cells.associateBy { it.trimId }
        trimOrder.forEach { tid ->
            val tv = TextView(ctx).apply {
                text = cellsByTrim[tid]?.display ?: "-"
                setPadding(12)
            }
            b.cellContainer.addView(tv)
        }
    }
}
