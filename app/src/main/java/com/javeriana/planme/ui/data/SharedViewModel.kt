package com.javeriana.planme.ui.data

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.javeriana.planme.R
import com.javeriana.planme.data.FirebasePlanRepository
import com.javeriana.planme.data.model.Plan
import com.javeriana.planme.data.model.Product
import com.javeriana.planme.data.model.Reservation
import com.javeriana.planme.data.model.Review
import com.javeriana.planme.util.REVIEWS_PER_PLAN_DETAIL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {

	companion object {
		const val TAG = "SharedViewModel"
	}

	private val planRepository = FirebasePlanRepository.getInstance()

	private val _plans: MutableLiveData<List<Plan>> = MutableLiveData()
	val plans: LiveData<List<Plan>>
		get() = _plans

	private val _filteredPlans: MutableLiveData<List<Plan>> = MutableLiveData()
	val filteredPlans: LiveData<List<Plan>>
		get() = _filteredPlans

	private val _selectedPlan: MutableLiveData<Plan> = MutableLiveData()
	val selectedPlan: LiveData<Plan>
		get() = _selectedPlan

	private val _selectedPlanReviews: MutableLiveData<List<Review>> = MutableLiveData()
	val selectedPlanReviews: LiveData<List<Review>>
		get() = _selectedPlanReviews
	val shortSelectedPlanReviews: LiveData<List<Review>>
		get() = _selectedPlanReviews.map { it.take(REVIEWS_PER_PLAN_DETAIL) }

	private val _selectedPlanProducts: MutableLiveData<List<Product>> = MutableLiveData()
	val selectedPlanProducts: LiveData<List<Product>>
		get() = _selectedPlanProducts

	private val _selectedPlanPictures: MutableLiveData<List<String>> = MutableLiveData()
	val selectedPlanPictures: LiveData<List<String>>
		get() = _selectedPlanPictures

	private val _userReservations: MutableLiveData<List<Reservation>> = MutableLiveData()
	val userReservations: LiveData<List<Reservation>>
		get() = _userReservations

	private val _newReservation: MutableLiveData<Reservation> = MutableLiveData()
	val newReservation: LiveData<Reservation>
		get() = _newReservation

	private val _reservationResult = MutableLiveData<Result>()
	val reservationResult: LiveData<Result> = _reservationResult

	init {
		retrievePlans()
		retrieveUserReservations()
	}

	private fun retrievePlans() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val plans = planRepository.refreshPlans()
				_plans.postValue(plans)

				Log.d(TAG, "Plans: $plans")
			} catch (e: Exception) {
				Log.e(TAG, "Error retrieving plans")
				Log.e(TAG, e.stackTraceToString())
			}
		}
	}

	private fun retrieveUserReservations() {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val reservations = planRepository.getUserReservations(
					FirebaseAuth.getInstance().currentUser!!.uid
				)
				_userReservations.postValue(reservations)

				Log.d(TAG, "Reservations: $reservations")
			} catch (e: Exception) {
				Log.e(TAG, "Error retrieving reservations")
				Log.e(TAG, e.stackTraceToString())
			}
		}
	}

	private fun retrievePlan(planId: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val reviews = planRepository.getPlanReviews(planId)
				_selectedPlanReviews.postValue(reviews)

				val products = planRepository.getPlanProducts(planId)
				_selectedPlanProducts.postValue(products)

				_selectedPlanPictures.postValue(
					selectedPlan.value?.img ?: emptyList()
				)

				Log.d(TAG, "Reviews: $reviews")
				Log.d(TAG, "Products: $products")
				Log.d(TAG, "Pictures: ${selectedPlan.value?.img}")
			} catch (e: Exception) {
				Log.e(TAG, "Error retrieving plan")
				Log.e(TAG, e.stackTraceToString())
			}
		}
	}

	fun setSelectedPlan(plan: Plan) {
		restartPlan()
		_selectedPlan.value = plan
		plan.id?.let {
			retrievePlan(it)
		}
	}

	fun setSelectedPlan(planId: String) {
		restartPlan()
		_selectedPlan.value = plans.value?.find { it.id == planId }
		retrievePlan(planId)
	}

	private fun restartPlan() {
		_selectedPlan.value = Plan()
		_selectedPlanReviews.value = emptyList()
		_selectedPlanProducts.value = emptyList()
		_selectedPlanPictures.value = emptyList()
	}

	fun startNewReservation() {
		_newReservation.value = Reservation(
			plan_id = selectedPlan.value?.id ?: "",
			user_id = FirebaseAuth.getInstance().currentUser!!.uid,
			status = "PENDING",
		)
		_reservationResult.value = Result()
	}

	fun updateNewReservation(
		date: Long? = null,
		hour: Int? = null,
		minute: Int? = null,
		size: Int? = null,
	) {
		if (date != null) {
			_newReservation.value?.date = date
		}
		if (hour != null) {
			_newReservation.value?.hour = hour
		}
		if (minute != null) {
			_newReservation.value?.minute = minute
		}
		if (size != null) {
			_newReservation.value?.size = size
		}
	}

	fun reserve() {

		if (checkNewReservation()) {
			viewModelScope.launch(Dispatchers.IO) {
				try {
					planRepository.addReservation(newReservation.value!!)
					retrieveUserReservations()

					_reservationResult.postValue(
						Result(
							success = true,
							message = R.string.reservation_created
						)
					)
				} catch (e: Exception) {
					Log.e(TAG, "Error reserving plan")
					Log.e(TAG, e.stackTraceToString())
				}
			}
		} else {
			_reservationResult.value = Result(
				message = R.string.reservation_failed_empty_fields
			)
		}
	}

	private fun checkNewReservation(): Boolean {

		newReservation.value?.apply {
			if (date == null || hour == null || minute == null || size == null) {
				return false
			}
			return true
		}.let {
			return false
		}
	}
}
