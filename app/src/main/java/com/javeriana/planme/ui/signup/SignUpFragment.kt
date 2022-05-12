package com.javeriana.planme.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.javeriana.planme.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

	companion object {
		const val TAG = "SignUpFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentSignUpBinding? = null
	private val binding get() = _binding!!

	private val viewModel: SignUpViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentSignUpBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.apply {

			confirmPassword.setOnEditorActionListener { _, actionId, _ ->
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					onSignUpClicked()
				}
				false
			}

			signUp.setOnClickListener { onSignUpClicked() }
		}

		viewModel.signUpResult.observe(viewLifecycleOwner) { signUpResult ->
			signUpResult ?: return@observe
			signUpResult.error?.let {
				showSignUpFailed(it)
			}
			if (signUpResult.success) {
				goToWelcomeScreen()
			}
		}
	}

	private fun onSignUpClicked() {
		binding.apply {
			viewModel.signUp(
				name.text.toString(),
				email.text.toString(),
				password.text.toString(),
				confirmPassword.text.toString()
			)
		}
	}

	private fun showSignUpFailed(@StringRes errorString: Int) {
		Toast.makeText(
			requireContext(), errorString, Toast.LENGTH_SHORT
		).show()
	}

	private fun goToWelcomeScreen() {

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
