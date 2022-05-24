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

class PlanItemAdapter(
	private val onItemClicked: (Plan) -> Unit
) : ListAdapter<Plan, PlanItemAdapter.PlanItemViewHolder>(DiffCallback) {

	class PlanItemViewHolder(
		private var binding: PlanListItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(plan: Plan) {
			binding.apply {
				planName.text = plan.name ?: ""
				planDescription.text = plan.description ?: ""

				if (plan.cover_img != null) {

					Glide.with(binding.root.context)
						.load(plan.cover_img)
						.addListener(object : RequestListener<Drawable> {
							override fun onLoadFailed(
								e: GlideException?,
								model: Any?,
								target: Target<Drawable>?,
								isFirstResource: Boolean
							): Boolean {
								progressBar.visibility = View.GONE
								planCoverImage.visibility = View.GONE
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
						.into(planCoverImage)
				}
			}
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): PlanItemViewHolder {
		return PlanItemViewHolder(
			PlanListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: PlanItemViewHolder, position: Int
	) {
		val current = getItem(position)
		holder.itemView.setOnClickListener {
			onItemClicked(current)
		}
		holder.bind(current)
	}

	companion object {
		private val DiffCallback = object : DiffUtil.ItemCallback<Plan>() {
			override fun areItemsTheSame(oldItem: Plan, newItem: Plan): Boolean {
				return oldItem.id == newItem.id
			}

			override fun areContentsTheSame(oldItem: Plan, newItem: Plan): Boolean {
				return oldItem == newItem
			}
		}
	}
}
