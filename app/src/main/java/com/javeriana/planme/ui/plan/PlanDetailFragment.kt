package com.javeriana.planme.ui.plan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.javeriana.planme.R
import com.javeriana.planme.databinding.FragmentPlanDetailBinding
import com.javeriana.planme.ui.adapter.PictureItemAdapter
import com.javeriana.planme.ui.adapter.ReviewItemAdapter
import com.javeriana.planme.ui.data.SharedViewModel
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

			pictures.adapter = PictureItemAdapter{
				Log.d(TAG, "Clicked on picture: $it")
			}
			reviews.adapter = ReviewItemAdapter()

			// Set the click listeners
			reviewNumberContainer.setOnClickListener{
				onReviewsClicked()
			}
			allReviews.setOnClickListener {
				onReviewsClicked()
			}
			mapLocation.setOnClickListener {
				onMapLocationClicked()
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
