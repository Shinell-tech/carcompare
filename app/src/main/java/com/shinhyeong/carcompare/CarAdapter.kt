package com.shinhyeong.carcompare

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shinhyeong.carcompare.databinding.ItemCarBinding

class CarAdapter(
    private val cars: List<Car>,
    private val onCarSelected: (Car, Boolean) -> Unit
) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    private val selected = mutableSetOf<Car>()

    inner class CarViewHolder(val binding: ItemCarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val binding = ItemCarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = cars[position]
        holder.binding.checkBox.text = "${car.brand} ${car.name}"
        holder.binding.checkBox.setOnCheckedChangeListener(null)
        holder.binding.checkBox.isChecked = selected.contains(car)

        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selected.add(car) else selected.remove(car)
            onCarSelected(car, isChecked)
        }
    }

    override fun getItemCount() = cars.size
}
