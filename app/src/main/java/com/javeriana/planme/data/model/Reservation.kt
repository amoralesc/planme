package com.javeriana.planme.data.model

import java.util.*

data class Reservation(
	val plan_id: String? = null,
	val user_id: String? = null,
	var date: Long? = null,
	var hour: Int? = null,
	var minute: Int? = null,
	var size: Int? = null,
	val status: String? = null
)