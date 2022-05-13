package com.javeriana.planme.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.javeriana.planme.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {

	companion object {
		const val TAG = "WelcomeFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentWelcomeBinding? = null
	private val binding get() = _binding!!

	private val viewModel: WelcomeViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentWelcomeBinding.inflate(inflater, container, false)
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
