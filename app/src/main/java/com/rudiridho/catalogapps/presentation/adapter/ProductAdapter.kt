package com.rudiridho.catalogapps.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rudiridho.catalogapps.databinding.ItemProductBinding
import com.rudiridho.catalogapps.presentation.model.ProductUI

class ProductAdapter(private val onFavoriteClick: (ProductUI) -> Unit) :
    ListAdapter<ProductUI, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    inner class ProductViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: ProductUI) {
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "$${product.price}"
            Glide.with(binding.root.context)
                .load(product.imageUrl)
                .into(binding.ivProductImage)

            binding.ivFavorite.isSelected = product.isFavorite
            binding.ivFavorite.setOnClickListener {
                onFavoriteClick(product)
                binding.ivFavorite.isSelected = !binding.ivFavorite.isSelected
            }
        }
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductUI>() {
    override fun areItemsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUI, newItem: ProductUI): Boolean {
        return oldItem == newItem
    }
}