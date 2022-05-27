package com.javeriana.planme.ui.plan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.javeriana.planme.R
import com.javeriana.planme.databinding.FragmentPlanDetailBinding
import com.javeriana.planme.ui.adapter.PictureItemAdapter
import com.javeriana.planme.ui.adapter.ProductItemAdapter
import com.javeriana.planme.ui.adapter.ReviewItemAdapter
import com.javeriana.planme.ui.data.SharedViewModel
import com.javeriana.planme.util.formatDateFromMillis
import com.javeriana.planme.util.formatTime
import com.javeriana.planme.util.hideKeyboard
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.ColorFilterTransformation

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

			// Set the cover image
			// This is done using a transformation that
			// blurs the image and adds a color filter (darkens it)
			val multiTransformation = MultiTransformation(
				BlurTransformation(25),
				ColorFilterTransformation(R.color.black)
			)

			Glide.with(requireContext())
				.load(sharedViewModel.selectedPlan.value?.cover_img)
				.placeholder(R.drawable.placeholder_dark_rectangle)
				.error(R.drawable.placeholder_dark_rectangle)
				.apply(bitmapTransform(multiTransformation))
				.into(coverImage)

			// Set the logo image
			Glide.with(requireContext())
				.load(sharedViewModel.selectedPlan.value?.logo_img)
				.placeholder(R.drawable.placeholder_white_circle)
				.error(R.drawable.placeholder_white_circle)
				.into(logoImage)

			pictures.adapter = PictureItemAdapter {
				Log.d(TAG, "Clicked on picture: $it")
			}
			reviews.adapter = ReviewItemAdapter()
			products.adapter = ProductItemAdapter()

			// Set the click listeners
			reviewNumberContainer.setOnClickListener {
				onReviewsClicked()
			}
			allReviews.setOnClickListener {
				onReviewsClicked()
			}
			mapLocation.setOnClickListener {
				onMapLocationClicked()
			}
			reserve.setOnClickListener {
				onReserveClicked()
			}

			// Setup the date picker
			datePicker.addOnPositiveButtonClickListener { dateSelected ->
				// Set the time in the date EditText
				date.setText(
					formatDateFromMillis(dateSelected)
				)
				sharedViewModel.updateNewReservation(
					date = dateSelected
				)
			}

			// Setup the time picker
			timePicker.addOnPositiveButtonClickListener {
				// Set the time in the time EditText
				time.setText(
					formatTime(timePicker.hour, timePicker.minute)
				)
				sharedViewModel.updateNewReservation(
					hour = timePicker.hour,
					minute = timePicker.minute
				)
			}

			date.apply {
				showSoftInputOnFocus = false
				setOnClickListener {
					hideKeyboard()
					showDatePicker()
				}
			}
			time.apply {
				showSoftInputOnFocus = false
				setOnClickListener {
					hideKeyboard()
					showTimePicker()
				}
			}
			size.apply {
				showSoftInputOnFocus = false
				setOnClickListener {
					hideKeyboard()
					showSizePicker()
				}
			}

			sharedViewModel.startNewReservation()
			sharedViewModel.reservationResult.observe(viewLifecycleOwner) { result ->
				result ?: return@observe
				result.message?.let {
					showReservationMessage(it)
				}
				if (result.success) {
					onReservationSuccess()
				}
			}
		}
	}

	private fun onMapLocationClicked() {
		val location = sharedViewModel.selectedPlan.value?.location ?: return
		val action = PlanDetailFragmentDirections
			.actionPlanDetailFragmentToPlanMapsLocationFragment(
				location = location
			)

		findNavController().navigate(action)
	}

	private fun onReviewsClicked() {
		findNavController().navigate(
			R.id.action_planDetailFragment_to_planReviewsFragment
		)
	}

	private fun onReserveClicked() {
		sharedViewModel.reserve()
	}

	private fun showReservationMessage(@StringRes message: Int) {
		Toast.makeText(
			requireContext(), message, Toast.LENGTH_SHORT
		).show()
	}

	private fun onReservationSuccess() {
		sharedViewModel.startNewReservation()
		binding.apply {
			date.setText("")
			time.setText("")
			size.setText("")
		}
	}

	private val datePicker =
		MaterialDatePicker.Builder.datePicker()
			.build()

	private val timePicker =
		MaterialTimePicker.Builder()
			.setTimeFormat(TimeFormat.CLOCK_12H)
			.setHour(7)
			.setMinute(0)
			.build()

	/**
	 * Shows a date picker dialog.
	 */
	private fun showDatePicker() {
		val fragment: Fragment? = childFragmentManager.findFragmentByTag("datePicker")
		if (fragment != null)
			childFragmentManager.beginTransaction().remove(fragment).commit()

		datePicker.show(childFragmentManager, "datePicker")
	}

	/**
	 * Shows a time picker dialog.
	 */
	private fun showTimePicker() {
		val fragment: Fragment? = childFragmentManager.findFragmentByTag("timePicker")
		if (fragment != null)
			childFragmentManager.beginTransaction().remove(fragment).commit()

		timePicker.show(childFragmentManager, "timePicker")
	}

	/**
	 * Shows a number picker dialog to select the size of the reservation.
	 */
	private fun showSizePicker() {
		val inflater = LayoutInflater.from(requireContext())
		val dialogView = inflater.inflate(R.layout.number_picker_dialog, null)
		val numberPicker = dialogView.findViewById<NumberPicker>(R.id.number_picker)
		numberPicker.minValue = 1
		numberPicker.maxValue = 20
		numberPicker.wrapSelectorWheel = false
		if (binding.size.text.isNotEmpty())
			numberPicker.value = binding.size.text.toString().toInt()

		AlertDialog
			.Builder(requireContext())
			.setTitle(R.string.reservation_size)
			.setView(dialogView)
			.setPositiveButton(R.string.ok) { _, _ ->
				val number = numberPicker.value
				binding.size.setText(number.toString())
				sharedViewModel.updateNewReservation(
					size = number
				)
			}
			.setNegativeButton(R.string.cancel) { _, _ ->
				// Do nothing
			}
			.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
