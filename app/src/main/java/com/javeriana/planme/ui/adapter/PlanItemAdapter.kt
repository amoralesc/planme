package com.javeriana.planme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
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
