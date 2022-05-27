package com.javeriana.planme.data

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.javeriana.planme.data.model.*
import kotlinx.coroutines.tasks.await

interface PlanRepository {
	/**
	 * Retrieves all the plans from the database
	 * @return a list of plans
	 */
	suspend fun refreshPlans(): List<Plan>

	/**
	 * Retrieves all the reviews of a plan from the database
	 * @param planId the id of the plan
	 * @return a list of reviews
	 */
	suspend fun getPlanReviews(planId: String): List<Review>

	/**
	 * Retrieves all the products of a plan from the database
	 * @param planId the id of the plan
	 * @return a list of products
	 */
	suspend fun getPlanProducts(planId: String): List<Product>

	/**
	 * Retrieves all the user's reservations from the database
	 * @param userId the id of the user
	 * @return a list of reservations
	 */
	suspend fun getUserReservations(userId: String): List<Reservation>

	/**
	 * Adds a new reservation to the database
	 * @param reservation the reservation to be added
	 */
	suspend fun addReservation(reservation: Reservation)
}

class FirebasePlanRepository : PlanRepository {

	/**
	 * This companion object enforces the repository pattern,
	 * making the firebase repository a singleton.
	 * It is used to retrieve the instance of the repository
	 * or create it if it doesn't exist, from anywhere in the app.
	 */
	companion object {
		private var instance: FirebasePlanRepository? = null

		fun getInstance(): FirebasePlanRepository {
			return instance ?: synchronized(this) {
				instance ?: FirebasePlanRepository()
					.also { instance = it }
			}
		}
	}

	private val database = Firebase.firestore

	override suspend fun refreshPlans(): List<Plan> {

		val plans = mutableListOf<Plan>()
		val data =
			database.collection("plans")
				.get().await()

		data.forEach {
			val plan = it.toObject(Plan::class.java)
			plan.id = it.id
			plans.add(plan)
		}

		return plans
	}

	override suspend fun getPlanReviews(planId: String): List<Review> {

		val reviews = mutableListOf<Review>()
		val data =
			database.collection("reviews")
				.whereEqualTo(FieldPath.documentId(), planId)
				.get().await()

		// There should only be one document
		// However, data is a list of documents
		// Cuz yeah lol :D
		data.forEach {
			val listReviews = it.toObject(ListReview::class.java)
			reviews.addAll(listReviews.list ?: listOf())
		}

		return reviews
	}

	override suspend fun getPlanProducts(planId: String): List<Product> {

		val products = mutableListOf<Product>()
		val data =
			database.collection("products")
				.whereEqualTo(FieldPath.documentId(), planId)
				.get().await()

		// There should only be one document
		// However, data is a list of documents
		// Cuz yeah lol :D
		data.forEach {
			val listProducts = it.toObject(ListProduct::class.java)
			products.addAll(listProducts.list ?: listOf())
		}

		return products
	}

	override suspend fun getUserReservations(userId: String): List<Reservation> {

		val reservations = mutableListOf<Reservation>()
		val data =
			database.collection("reservations")
				.whereEqualTo("user_id", userId)
				.get().await()

		data.forEach {
			val reservation = it.toObject(Reservation::class.java)
			reservations.add(reservation)
		}

		return reservations
	}

	override suspend fun addReservation(reservation: Reservation) {

		database.collection("reservations")
			.document()
			.set(reservation)
			.await()
	}
}
