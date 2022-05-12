package com.javeriana.planme.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.javeriana.planme.R
import com.javeriana.planme.ui.data.FormResult
import com.javeriana.planme.util.isEmailValid
import com.javeriana.planme.util.isPasswordValid

class LoginViewModel : ViewModel() {

	private val _loginResult = MutableLiveData<FormResult>()
	val loginResult: LiveData<FormResult> = _loginResult

	private val mAuth = FirebaseAuth.getInstance()

	init {
		// Check if user is already logged in
		if (mAuth.currentUser != null) {
			Log.d(LoginFragment.TAG, "User is already logged in")
			_loginResult.value = FormResult(
				true, null
			)
		} else {
			_loginResult.value = FormResult()
		}
	}

	/**
	 * Attempts to sign in the user through Firebase Authentication.
	 * If the user is successfully signed in, the [FormResult.success] will be true.
	 * If there is an error, the [FormResult.error] will be set.
	 *
	 * @param email the user's email
	 * @param password the user's password
	 */
	fun login(email: String, password: String) {

		if (checkLoginForm(email, password)) {

			mAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener { task ->
					if (task.isSuccessful) {
						Log.d(LoginFragment.TAG, "Login successful")

						_loginResult.value = FormResult(
							success = true, error = null
						)
					} else {
						Log.d(LoginFragment.TAG, "Login failed: ${task.exception?.message}")

						_loginResult.value = FormResult(
							error = R.string.login_failed
						)
					}
				}
		}
	}

	/**
	 * Checks the login form data (email, password).
	 * Sets the [FormResult] to an error message if data is invalid.
	 *
	 * @param email The email address of the user.
	 * @param password The password of the user.
	 * @return true if the data is valid, false otherwise.
	 */
	private fun checkLoginForm(email: String, password: String): Boolean {

		return if (!isEmailValid(email)) {
			Log.d(LoginFragment.TAG, "Login failed: email is invalid")

			_loginResult.value = FormResult(error = R.string.invalid_email)
			false
		} else if (!isPasswordValid(password)) {
			Log.d(LoginFragment.TAG, "Login failed: password is invalid")

			_loginResult.value = FormResult(error = R.string.invalid_password)
			false
		} else {
			true
		}
	}
}
