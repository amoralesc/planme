package com.javeriana.planme.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomLocation(
	val address_line_1: String? = null,
	val address_line_2: String? = null,
	val lat: Double? = null,
	val lng: Double? = null
) : Parcelable
