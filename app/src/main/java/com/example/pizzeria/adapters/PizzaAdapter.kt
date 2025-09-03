package com.example.pizzeria.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pizzeria.databinding.ItemPizzaBinding
import com.example.pizzeria.models.Pizza

class PizzaAdapter(
    private val items: MutableList<Pizza>,
    private val clickListener: (Pizza, Int) -> Unit
) : RecyclerView.Adapter<PizzaAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPizzaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pizza: Pizza, position: Int) {
            binding.tvItemName.text = pizza.name
            binding.tvItemDesc.text = pizza.description

            if (pizza.imageUri != null) {
                try {
                    val uri = Uri.parse(pizza.imageUri)
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
                clickListener(pizza, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPizzaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }

    override fun getItemCount() = items.size

    fun addPizza(pizza: Pizza) {
        items.add(pizza)
        notifyItemInserted(items.size - 1)
    }

    fun updatePizza(pizza: Pizza, position: Int) {
        items[position] = pizza
        notifyItemChanged(position)
    }

    fun removePizza(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}