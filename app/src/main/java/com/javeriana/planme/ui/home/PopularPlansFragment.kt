package com.javeriana.planme.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.javeriana.planme.R
import com.javeriana.planme.databinding.FragmentPopularPlansBinding
import com.javeriana.planme.ui.adapter.PlanItemAdapter
import com.javeriana.planme.ui.data.SharedViewModel

class PopularPlansFragment : Fragment() {

	companion object {
		const val TAG = "PopularPlansFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPopularPlansBinding? = null
	private val binding get() = _binding!!

	private val sharedViewModel: SharedViewModel by activityViewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setHasOptionsMenu(true)
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentPopularPlansBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupBottomNavigation()

		binding.apply {
			lifecycleOwner = viewLifecycleOwner
			viewModel = sharedViewModel
			listPopularPlans.adapter = PlanItemAdapter { plan ->
				sharedViewModel.setSelectedPlan(plan)
				onPlanSelected()
			}
		}
	}

	private fun onPlanSelected() {
		findNavController().navigate(
			R.id.action_popularPlansFragment_to_planDetailFragment
		)
	}

	private fun onAccountSelected() {

	}

	private fun onLogoutSelected() {
		FirebaseAuth.getInstance().signOut()
		findNavController().navigate(
			R.id.action_popularPlansFragment_to_loginFragment
		)
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.app_bar_menu, menu)
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.option_account -> {
				onAccountSelected()
				return true
			}
			R.id.option_logout -> {
				onLogoutSelected()
				return true
			}
			else -> {}
		}
		return false
	}

	private fun setupBottomNavigation() {
		binding.bottomNavigation.selectedItemId = R.id.page_popular

		binding.bottomNavigation.setOnItemSelectedListener { item ->
			when (item.itemId) {
				R.id.page_popular -> {
					false
				}
				R.id.page_plan_me -> {
					findNavController().navigate(
						R.id.action_popularPlansFragment_to_planMeFragment
					)
					false
				}
				R.id.page_reservations -> {
					findNavController().navigate(
						R.id.action_popularPlansFragment_to_reservationsFragment
					)
					false
				}
				else -> false
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
