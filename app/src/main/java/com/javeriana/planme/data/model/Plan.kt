package com.javeriana.planme.data.model

data class PlanCity(
	val id: Int? = null,
	val city: String? = null,
	val name: String? = null
)

data class PlanContact(
	val cellphone: MutableList<String>? = null,
	val homephone: MutableList<String>? = null
)

data class PlanReviewSummary(
	val quantity: Int? = null,
	val score: Double? = null
)

data class Plan(
	var id: String? = null,
	val city: PlanCity? = null,
	val contact: PlanContact? = null,
	val cover_img: String? = null,
	val description: String? = null,
	val img: List<String>? = null,
	val location: CustomLocation? = null,
	val logo_img: String? = null,
	val name: String? = null,
	val reviews_summary: PlanReviewSummary? = null,
	val tags: MutableMap<String, Double>? = null
)
