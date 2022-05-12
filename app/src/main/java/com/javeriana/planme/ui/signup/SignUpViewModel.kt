package com.javeriana.planme.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.javeriana.planme.R
import com.javeriana.planme.ui.data.FormResult
import com.javeriana.planme.util.isEmailValid
import com.javeriana.planme.util.isPasswordValid

class SignUpViewModel : ViewModel() {

	private val _signUpResult = MutableLiveData<FormResult>()
	val signUpResult: LiveData<FormResult> = _signUpResult

	private val mAuth = FirebaseAuth.getInstance()

	fun signUp(
		name: String, email: String, password: String, passwordAgain: String
	) {

		if (checkSignUpForm(name, email, password, passwordAgain)) {

			// Create a new user with email and password in Firebase
			mAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Log.d(SignUpFragment.TAG, "Signed up successfully")

						_signUpResult.value = FormResult(
							success = true, error = null
						)
					} else {
						Log.d(SignUpFragment.TAG, "Sign up failed: ${task.exception?.message}")

						_signUpResult.value = FormResult(
							success = false,
							error = R.string.sign_up_failed
						)
					}
				}
		}
	}

	private fun checkSignUpForm(
		name: String, email: String, password: String, passwordAgain: String
	): Boolean {

		return if (
			name.isEmpty() || email.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()
		) {
			Log.d(SignUpFragment.TAG, "Sign up failed: form is empty")

			_signUpResult.value = FormResult(error = R.string.signup_failed_empty_fields)
			false
		} else if (!isEmailValid(email)) {
			Log.d(SignUpFragment.TAG, "Sign up failed: email is invalid")

			_signUpResult.value = FormResult(error = R.string.invalid_email)
			false
		} else if (!isPasswordValid(password)) {
			Log.d(SignUpFragment.TAG, "Sign up failed: password is invalid")

			_signUpResult.value = FormResult(error = R.string.invalid_password)
			false
		} else if (password != passwordAgain) {
			Log.d(SignUpFragment.TAG, "Sign up failed: passwords are not equal")

			_signUpResult.value = FormResult(error = R.string.password_mismatch)
			false
		} else {
			true
		}
	}
}
