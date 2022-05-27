package com.javeriana.planme.data.model

data class Product(
	val name: String? = null,
	val price: Double? = null,
	val description: String? = null
)

data class ListProduct(
	val list: List<Product>? = null
)