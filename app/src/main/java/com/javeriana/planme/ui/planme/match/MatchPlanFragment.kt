package com.javeriana.planme.ui.planme.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.javeriana.planme.R
import com.javeriana.planme.databinding.FragmentMatchPlanBinding

class MatchPlanFragment : Fragment() {

	companion object {
		const val TAG = "MatchPlanFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentMatchPlanBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentMatchPlanBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {
			matchPlanMe.setOnClickListener {
				onMatchPlanMeClicked()
			}
		}
	}

	private fun onMatchPlanMeClicked() {
		findNavController().navigate(
			R.id.action_matchPlanFragment_to_matchResultsFragment
		)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}