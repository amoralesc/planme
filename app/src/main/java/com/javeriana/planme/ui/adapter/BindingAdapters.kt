package com.javeriana.planme.ui.adapter

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javeriana.planme.data.model.Plan
import com.javeriana.planme.data.model.Product
import com.javeriana.planme.data.model.Reservation
import com.javeriana.planme.data.model.Review

@JvmName("bindRecyclerViewPlans")
@BindingAdapter("listPlans")
fun bindRecyclerView(
	recyclerView: RecyclerView,
	data: List<Plan>?
) {
	val adapter = recyclerView.adapter as PlanItemAdapter
	adapter.submitList(data)
}

@JvmName("bindRecyclerViewReviews")
@BindingAdapter("listReviews")
fun bindRecyclerView(
	recyclerView: RecyclerView,
	data: List<Review>?
) {
	val adapter = recyclerView.adapter as ReviewItemAdapter
	adapter.submitList(data)
}

@JvmName("bindRecyclerViewProducts")
@BindingAdapter("listProducts")
fun bindRecyclerView(
	recyclerView: RecyclerView,
	data: List<Product>?
) {
	val adapter = recyclerView.adapter as ProductItemAdapter
	adapter.submitList(data)
}

@JvmName("bindRecyclerViewPictures")
@BindingAdapter("listPictures")
fun bindRecyclerView(
	recyclerView: RecyclerView,
	data: List<String>?
) {
	val adapter = recyclerView.adapter as PictureItemAdapter
	adapter.submitList(data)
}

@JvmName("bindRecyclerViewReservations")
@BindingAdapter("listReservations")
fun bindRecyclerView(
	recyclerView: RecyclerView,
	data: List<Reservation>?
) {
	val adapter = recyclerView.adapter as ReservationItemAdapter
	adapter.submitList(data)
}
