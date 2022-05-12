package com.javeriana.planme.ui.data

/**
 * Form entry result: success (true) or error message.
 */
data class FormResult(
	val success: Boolean = false,
	val error: Int? = null
)
