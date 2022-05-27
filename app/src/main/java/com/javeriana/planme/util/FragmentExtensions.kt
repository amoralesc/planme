package com.javeriana.planme.util

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment

/**
 * Extends the [Fragment] to have the ability to hide the
 * keyboard from whatever view is currently focused.
 */
fun Fragment.hideKeyboard() = ViewCompat.getWindowInsetsController(requireView())
	?.hide(WindowInsetsCompat.Type.ime())
