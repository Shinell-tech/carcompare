package com.shinhyeong.carcompare.ui.compare

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.updateMargins
import androidx.recyclerview.widget.RecyclerView
import com.shinhyeong.carcompare.R

class ComparisonAdapter(
    private var items: List<RowRender>,
    private val vehicleCount: Int
) : RecyclerView.Adapter<ComparisonAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvSection: TextView = v.findViewById(R.id.tvSection)
        val tvLabel: TextView = v.findViewById(R.id.tvLabel)
        val valuesContainer: LinearLayout = v.findViewById(R.id.valuesContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_compare_row, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val row = items[position]

        // 섹션 헤더 줄
        if (row.sectionHeader != null) {
            holder.tvSection.visibility = View.VISIBLE
            holder.tvSection.text = row.sectionHeader
            holder.rowLineVisibility(false)
            return
        } else {
            holder.tvSection.visibility = View.GONE
            holder.rowLineVisibility(true)
        }

        // 라벨
        holder.tvLabel.text = row.label ?: ""

        // 값들 채우기
        holder.valuesContainer.removeAllViews()
        val ctx = holder.itemView.context
        val eachWidth = 0 // weight로 나눔
        for (i in 0 until vehicleCount) {
            val tv = TextView(ctx).apply {
                text = row.values.getOrNull(i) ?: "—"
                textSize = 14f
                setPadding(12, 4, 12, 4)
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply {
                    (this as? ViewGroup.MarginLayoutParams)?.updateMargins(left = 4, right = 4)
                }
                if (row.highlightIndex == i) {
                    setTypeface(typeface, Typeface.BOLD)
                }
            }
            holder.valuesContainer.addView(tv)
        }
    }

    private fun VH.rowLineVisibility(visible: Boolean) {
        itemView.findViewById<View>(R.id.rowLine).visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun submit(newItems: List<RowRender>) {
        items = newItems
        notifyDataSetChanged()
    }
}
