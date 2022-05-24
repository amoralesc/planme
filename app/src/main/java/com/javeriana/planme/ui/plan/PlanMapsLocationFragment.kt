package com.javeriana.planme.ui.plan

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.javeriana.planme.R
import com.javeriana.planme.data.model.CustomLocation
import com.javeriana.planme.databinding.FragmentPlanMapsLocationBinding
import com.javeriana.planme.util.LOCATION_REQUEST_FAST_INTERVAL
import com.javeriana.planme.util.LOCATION_REQUEST_INTERVAL

class PlanMapsLocationFragment : Fragment(), OnMapReadyCallback {

	companion object {
		const val TAG = "PlanMapsLocationFragment"
		const val ARGUMENT_LOCATION = "location"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPlanMapsLocationBinding? = null
	private val binding get() = _binding!!

	private var mMap: GoogleMap? = null

	private lateinit var location: CustomLocation

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		location = arguments?.getParcelable(ARGUMENT_LOCATION)!!
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		// Inflate the layout and binding for this fragment
		_binding = FragmentPlanMapsLocationBinding.inflate(inflater, container, false)
		return binding.root
	}

	@SuppressLint("MissingPermission")
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val mapFragment = childFragmentManager
			.findFragmentById(R.id.map) as SupportMapFragment
		mapFragment.getMapAsync(this)
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@SuppressLint("MissingPermission")
	override fun onMapReady(googleMap: GoogleMap) {
		mMap = googleMap

		// Add marker at location
		if (location.lat != null && location.lng != null) {
			val latLng = LatLng(location.lat!!, location.lng!!)
			mMap!!.addMarker(
				MarkerOptions()
					.position(latLng)
					.title(location.address_line_1 ?: "")
					.snippet(location.address_line_2 ?: "")
			)
			mMap!!.moveCamera(
				CameraUpdateFactory.newLatLngZoom(latLng, 15f)
			)
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
