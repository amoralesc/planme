package com.javeriana.planme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javeriana.planme.data.model.Review
import com.javeriana.planme.databinding.PlanReviewListItemBinding
import com.javeriana.planme.util.dateToString

class ReviewItemAdapter() :
	ListAdapter<Review, ReviewItemAdapter.ReviewItemViewHolder>(DiffCallback) {

	class ReviewItemViewHolder(
		private var binding: PlanReviewListItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(review: Review) {
			binding.apply {
				reviewDate.text = review.date?.let { dateToString(it) } ?: ""
				reviewNumber.text = review.score.toString()
				reviewText.text = review.comment ?: ""
			}
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ReviewItemViewHolder {
		return ReviewItemViewHolder(
			PlanReviewListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ReviewItemViewHolder, position: Int
	) {
		val current = getItem(position)
		holder.bind(current)
	}

	companion object {
		private val DiffCallback = object : DiffUtil.ItemCallback<Review>() {
			override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
				return oldItem == newItem
			}
		}
	}
}
