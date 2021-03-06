package com.javeriana.planme.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.javeriana.planme.databinding.FragmentPlanReviewsBinding
import com.javeriana.planme.ui.adapter.ReviewItemAdapter
import com.javeriana.planme.ui.data.SharedViewModel

class PlanReviewsFragment : Fragment() {

	companion object {
		const val TAG = "PlanReviewsFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPlanReviewsBinding? = null
	private val binding get() = _binding!!

	private val sharedViewModel: SharedViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentPlanReviewsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = sharedViewModel
			reviews.adapter = ReviewItemAdapter()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
