package com.javeriana.planme.ui.plan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.javeriana.planme.databinding.FragmentPlanDetailBinding
import com.javeriana.planme.ui.adapter.PlanItemAdapter
import com.javeriana.planme.ui.adapter.ReviewItemAdapter
import com.javeriana.planme.ui.data.SharedViewModel

class PlanDetailFragment : Fragment() {

	companion object {
		const val TAG = "PlanDetailFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPlanDetailBinding? = null
	private val binding get() = _binding!!

	private val sharedViewModel: SharedViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentPlanDetailBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = sharedViewModel

			// Set the data of the selected plan
			sharedViewModel.selectedPlan.observe(viewLifecycleOwner) { plan ->
				Log.d(TAG, "Selected plan: $plan")

				name.text = plan.name ?: ""
				description.text = plan.description ?: ""

				reviewNumber.text = plan.reviews_summary?.score.toString()

				addressLine1.text = plan.location?.address_line_1 ?: ""
				addressLine2.text = plan.location?.address_line_2 ?: ""
			}

			reviews.adapter = ReviewItemAdapter()
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
