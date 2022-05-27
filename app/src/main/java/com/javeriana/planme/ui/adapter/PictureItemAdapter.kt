package com.javeriana.planme.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.javeriana.planme.data.model.Plan
import com.javeriana.planme.databinding.PlanListItemBinding
import com.javeriana.planme.databinding.PlanPictureListItemBinding


class PictureItemAdapter(
	private val onItemClicked: (String) -> Unit
) : ListAdapter<String, PictureItemAdapter.PictureItemViewHolder>(DiffCallback) {

	class PictureItemViewHolder(
		private var binding: PlanPictureListItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(pictureUri: String) {
			binding.apply {

				Glide.with(binding.root.context)
					.load(pictureUri)
					.addListener(object : RequestListener<Drawable> {
						override fun onLoadFailed(
							e: GlideException?,
							model: Any?,
							target: Target<Drawable>?,
							isFirstResource: Boolean
						): Boolean {
							progressBar.visibility = View.GONE
							return false
						}

						override fun onResourceReady(
							resource: Drawable?,
							model: Any?,
							target: Target<Drawable>?,
							dataSource: DataSource?,
							isFirstResource: Boolean
						): Boolean {
							progressBar.visibility = View.GONE
							return false
						}
					})
					.into(image)
			}
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): PictureItemViewHolder {
		return PictureItemViewHolder(
			PlanPictureListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: PictureItemViewHolder, position: Int
	) {
		val current = getItem(position)
		holder.itemView.setOnClickListener {
			onItemClicked(current)
		}
		holder.bind(current)
	}

	companion object {
		private val DiffCallback = object : DiffUtil.ItemCallback<String>() {
			override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
				return oldItem == newItem
			}
		}
	}
}
