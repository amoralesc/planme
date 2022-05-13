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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.javeriana.planme.R
import com.javeriana.planme.databinding.FragmentPlanMapsLocationBinding
import com.javeriana.planme.util.LOCATION_REQUEST_FAST_INTERVAL
import com.javeriana.planme.util.LOCATION_REQUEST_INTERVAL

class PlanMapsLocationFragment : Fragment(), OnMapReadyCallback {

	companion object {
		const val TAG = "PlanMapsLocationFragment"
	}

	// Binding objects to access the view elements
	private var _binding: FragmentPlanMapsLocationBinding? = null
	private val binding get() = _binding!!

	private var mMap: GoogleMap? = null

	private val hasLocationPermissions = MutableLiveData(false)
	private val isLocationEnabled = MutableLiveData(false)

	private val canAccessLocation: LiveData<Boolean>
		get() {
			return hasLocationPermissions.switchMap { hasLocationPermissions ->
				if (hasLocationPermissions) {
					isLocationEnabled
				} else {
					MutableLiveData(false)
				}
			}
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

		checkLocationPermissions()
		if (hasLocationPermissions.value!!) {
			checkLocationSettings()
		}

		canAccessLocation.observe(viewLifecycleOwner) { canAccess ->
			mMap?.isMyLocationEnabled = canAccess
		}
	}

	/**
	 * Checks the location permissions and requests them if they are not granted.
	 */
	private fun checkLocationPermissions() {
		when (PackageManager.PERMISSION_GRANTED) {
			activity?.checkSelfPermission(
				Manifest.permission.ACCESS_FINE_LOCATION
			) -> {
				onPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
			}
			activity?.checkSelfPermission(
				Manifest.permission.ACCESS_COARSE_LOCATION
			) -> {
				onPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
				requestPermissionsLocation()    // To upgrade to fine location access
			}
			else -> {
				requestPermissionsLocation()
			}
		}
	}

	/**
	 * Requests all location permissions (FINE and COARSE).
	 */
	private fun requestPermissionsLocation() {
		locationPermissionsRequester.launch(
			arrayOf(
				Manifest.permission.ACCESS_FINE_LOCATION,
				Manifest.permission.ACCESS_COARSE_LOCATION
			)
		)
	}

	/**
	 * Launches an implicit intent to request the location access permissions.
	 * - If the permission to access fine location is granted,
	 * [onPermissionGranted] with ACCESS_FINE_LOCATION is called.
	 * - If the permission to access coarse location is granted,
	 * [onPermissionGranted] with ACCESS_COARSE_LOCATION is called.
	 * - If the permissions are denied, it may show the rationale
	 * calling [requestPermissionRationale].
	 * - If the permissions are definitely denied,
	 * [onPermissionsDenied] is called.
	 */
	private val locationPermissionsRequester =
		registerForActivityResult(
			ActivityResultContracts.RequestMultiplePermissions()
		) { map ->

			when {
				map[Manifest.permission.ACCESS_FINE_LOCATION] == true -> {
					onPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
				}

				map[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> {
					onPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
				}

				shouldShowRequestPermissionRationale(
					Manifest.permission.ACCESS_FINE_LOCATION
				) -> {
					if (map.containsKey(Manifest.permission.ACCESS_COARSE_LOCATION))
						requestPermissionRationale(true)
					else
						requestPermissionRationale(false)
				}

				else -> onPermissionsDenied()
			}
		}

	/**
	 * Callback function if any permission to access location is granted.
	 * Sets the granted flags in the view model
	 */
	private fun onPermissionGranted(permission: String) {
		when (permission) {
			Manifest.permission.ACCESS_FINE_LOCATION -> {
				hasLocationPermissions.value = true
			}
			Manifest.permission.ACCESS_COARSE_LOCATION -> {
				hasLocationPermissions.value = true
			}
		}
	}

	/**
	 * Callback function if permissions to access location are denied.
	 * Sets the denied flags in the view model
	 */
	private fun onPermissionsDenied() {}

	/**
	 * Callback function if the user has denied the permission to access location
	 * and the rationale should be shown.
	 * Shows the rationale dialog.
	 */
	private fun requestPermissionRationale(both: Boolean) {
		val title: String
		val message: String
		when {
			both -> {
				title =
					getString(R.string.permissions_location_title)
				message =
					getString(R.string.permissions_location_message)
			}
			else -> {
				title =
					getString(R.string.permission_fine_location_title)
				message =
					getString(R.string.permission_fine_location_message)
			}
		}

		AlertDialog.Builder(requireContext())
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(R.string.request) { _, _ ->
				requestPermissionsLocation()
			}
			.setNegativeButton(R.string.dismiss) { _, _ ->
				onPermissionsDenied()
			}
			.show()
	}

	private val locationRequest = createLocationRequest()

	private fun createLocationRequest(): LocationRequest {
		return LocationRequest.create().apply {
			interval = LOCATION_REQUEST_INTERVAL
			fastestInterval = LOCATION_REQUEST_FAST_INTERVAL
			priority = LocationRequest.PRIORITY_HIGH_ACCURACY
		}
	}

	private fun checkLocationSettings() {
		LocationSettingsRequest.Builder()
			.addLocationRequest(locationRequest)
			.build()
			.let { it ->
				LocationServices.getSettingsClient(requireActivity())
					.checkLocationSettings(it)
					.addOnSuccessListener {
						onLocationSettingsEnabled()
					}
					.addOnFailureListener {
						if (it is ResolvableApiException) {
							val isr = IntentSenderRequest
								.Builder(it.resolution)
								.build()
							getLocationSettings.launch(isr)
						} else {
							onLocationSettingsUnavailable()
						}
					}
			}
	}

	private val getLocationSettings =
		registerForActivityResult(
			ActivityResultContracts.StartIntentSenderForResult()
		) {
			if (it.resultCode == Activity.RESULT_OK) {
				onLocationSettingsEnabled()
			} else {
				onLocationSettingsUnavailable()
			}
		}

	private fun onLocationSettingsEnabled() {
		isLocationEnabled.value = true
	}

	private fun onLocationSettingsUnavailable() {
		isLocationEnabled.value = false
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

		if (canAccessLocation.value == true) {
			mMap!!.isMyLocationEnabled = true
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
