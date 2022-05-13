package com.javeriana.planme.ui.plan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.javeriana.planme.databinding.FragmentPlanDetailBinding

class PlanDetailFragment : Fragment() {

	companion object {
		const val TAG = "PlanDetailFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPlanDetailBinding? = null
	private val binding get() = _binding!!

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
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}