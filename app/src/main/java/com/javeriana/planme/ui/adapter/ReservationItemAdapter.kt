package com.javeriana.planme.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javeriana.planme.data.model.Reservation
import com.javeriana.planme.databinding.ReservationListItemBinding
import com.javeriana.planme.util.formatDateFromMillis
import com.javeriana.planme.util.formatTime

class ReservationItemAdapter:
	ListAdapter<Reservation, ReservationItemAdapter.ReservationItemViewHolder>(DiffCallback) {

	class ReservationItemViewHolder(
		private var binding: ReservationListItemBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(reservation: Reservation) {
			binding.apply {
				reservationDate.text =
					reservation.date?.let { formatDateFromMillis(it) } ?: ""
				reservationTime.text =
					formatTime(reservation.hour ?: 0, reservation.minute ?: 0)
				reservationSize.text = reservation.size.toString()

				reservationPlanName.text = reservation.plan_name ?: ""
				reservationPlanAddressLine1.text = reservation.plan_address_line_1 ?: ""
				reservationPlanAddressLine2.text = reservation.plan_address_line_2 ?: ""
			}
		}
	}

	override fun onCreateViewHolder(
		parent: ViewGroup, viewType: Int
	): ReservationItemViewHolder {
		return ReservationItemViewHolder(
			ReservationListItemBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			)
		)
	}

	override fun onBindViewHolder(
		holder: ReservationItemViewHolder, position: Int
	) {
		val current = getItem(position)
		holder.bind(current)
	}

	companion object {
		private val DiffCallback = object : DiffUtil.ItemCallback<Reservation>() {
			override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
				return oldItem == newItem
			}

			override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
				return oldItem == newItem
			}
		}
	}
}
