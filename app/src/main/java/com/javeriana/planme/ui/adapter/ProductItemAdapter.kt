package com.javeriana.planme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javeriana.planme.data.model.Product
import com.javeriana.planme.databinding.PlanProductListItemBinding
import com.javeriana.planme.util.formatPrice

class ProductItemAdapter :
	ListAdapter<Product, ProductItemAdapter.ProductItemViewHolder>(DiffCallback) {

	class ProductItemViewHolder(
		private var binding: PlanProductListItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(product: Product) {
			binding.apply {
				productName.text = product.name
				productDescription.text = product.description
				productPrice.text = product.price?.let { formatPrice(it) } ?: "0"
			}
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ProductItemViewHolder {
		return ProductItemViewHolder(
			PlanProductListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ProductItemViewHolder, position: Int
	) {
		val current = getItem(position)
		holder.bind(current)
	}

	companion object {
		private val DiffCallback = object : DiffUtil.ItemCallback<Product>() {
			override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
				return oldItem == newItem
			}
		}
	}
}
