package com.javeriana.planme.data.model

import java.util.*

data class Review(
	val date: Date? = null,
	val score: Double? = null,
	val comment: String? = null
)

data class ListReview(
	val list: List<Review>? = null
)