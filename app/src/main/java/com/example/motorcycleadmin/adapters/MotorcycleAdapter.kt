package com.example.motorcycleadmin.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.motorcycleadmin.databinding.ItemMotorcycleBinding
import com.example.motorcycleadmin.models.Motorcycle

class MotorcycleAdapter(
    private val items: MutableList<Motorcycle>,
    private val clickListener: (Motorcycle, Int) -> Unit
) : RecyclerView.Adapter<MotorcycleAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMotorcycleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(motorcycle: Motorcycle, position: Int) {
            binding.tvItemName.text = motorcycle.name
            binding.tvItemDesc.text = motorcycle.description

            if (motorcycle.imageUri != null) {
                try {
                    val uri = Uri.parse(motorcycle.imageUri)
                    binding.ivItemImage.setImageDrawable(null)
                    binding.ivItemImage.post {
                        binding.ivItemImage.setImageURI(uri)
                    }
                } catch (e: Exception) {
                    binding.ivItemImage.setImageResource(android.R.drawable.ic_menu_report_image)
                }
            } else {
                binding.ivItemImage.setImageResource(android.R.drawable.ic_menu_report_image)
            }

            binding.root.setOnClickListener {
                clickListener(motorcycle, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMotorcycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    fun addMotorcycle(motorcycle: Motorcycle) {
        items.add(motorcycle)
        notifyItemInserted(items.size - 1)
    }

    fun updateMotorcycle(motorcycle: Motorcycle, position: Int) {
        items[position] = motorcycle
        notifyItemChanged(position)
    }

    fun removeMotorcycle(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}
