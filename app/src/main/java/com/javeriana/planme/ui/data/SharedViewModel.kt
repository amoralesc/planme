package com.javeriana.planme.ui.data

import android.util.Log
import androidx.lifecycle.*
import com.javeriana.planme.data.FirebasePlanRepository
import com.javeriana.planme.data.model.Plan
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

	private val _selectedPlanPictures: MutableLiveData<List<String>> = MutableLiveData()
	val selectedPlanPictures: LiveData<List<String>>
		get() = _selectedPlanPictures

	init {
		retrievePlans()
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

	private fun retrievePlan(planId: String) {
		viewModelScope.launch(Dispatchers.IO) {
			try {
				val reviews = planRepository.getPlanReviews(planId)
				_selectedPlanReviews.postValue(reviews)
				_selectedPlanPictures.postValue(
					selectedPlan.value?.img ?: emptyList()
				)

				Log.d(TAG, "Reviews: $reviews")
				Log.d(TAG, "Pictures: ${selectedPlan.value?.img}")
			} catch (e: Exception) {
				Log.e(TAG, "Error retrieving plan")
				Log.e(TAG, e.stackTraceToString())
			}
		}
	}

	fun setSelectedPlan(plan: Plan) {
		_selectedPlan.value = plan
		plan.id?.let {
			retrievePlan(it)
		}
	}
}
