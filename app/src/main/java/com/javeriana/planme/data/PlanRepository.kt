package com.javeriana.planme.data

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.javeriana.planme.data.model.ListReview
import com.javeriana.planme.data.model.Plan
import com.javeriana.planme.data.model.Review
import kotlinx.coroutines.tasks.await

interface PlanRepository {
	suspend fun refreshPlans(): List<Plan>
	suspend fun getPlanReviews(planId: String): List<Review>
}

class FirebasePlanRepository : PlanRepository {

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

		data.forEach {
			val listReviews = it.toObject(ListReview::class.java)
			reviews.addAll(listReviews.list ?: listOf())
		}

		return reviews
	}
}
