package com.javeriana.planme.ui.planme.match

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.javeriana.planme.databinding.FragmentMatchResultsBinding
import com.javeriana.planme.ui.data.SharedViewModel

class MatchResultsFragment : Fragment() {

	companion object {
		const val TAG = "MatchResultsFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentMatchResultsBinding? = null
	private val binding get() = _binding!!

	private val sharedViewModel: SharedViewModel by activityViewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentMatchResultsBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// TODO
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}